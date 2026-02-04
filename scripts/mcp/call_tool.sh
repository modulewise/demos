#!/bin/sh

if [ $# -lt 1 ]; then
    echo "Usage: $0 <TOOL_NAME> [key=value] [key=value] ..."
    echo "Example: $0 hello_greet name=World"
    exit 1
fi

SESSION_FILE=".mcp_session"

if [ ! -f "$SESSION_FILE" ]; then
    echo "No session file found. Run ./initialize.sh first."
    exit 1
fi

source "$SESSION_FILE"

if [ -z "$SERVER" ] || [ -z "$SESSION_ID" ] || [ -z "$NEXT_REQUEST_ID" ]; then
    echo "Invalid session file. Run ./initialize.sh to create a new session."
    exit 1
fi

CURRENT_REQUEST_ID=$NEXT_REQUEST_ID
NEXT_REQUEST_ID=$((NEXT_REQUEST_ID + 1))
echo "SERVER=$SERVER" > "$SESSION_FILE"
echo "SESSION_ID=$SESSION_ID" >> "$SESSION_FILE"
echo "NEXT_REQUEST_ID=$NEXT_REQUEST_ID" >> "$SESSION_FILE"

TOOL_NAME="$1"
shift 1

ARGUMENTS="{"
first=true
for arg in "$@"; do
    if [[ $arg == *"="* ]]; then
        key="${arg%%=*}"
        value="${arg#*=}"

        if [ "$first" = true ]; then
            first=false
        else
            ARGUMENTS="$ARGUMENTS,"
        fi

        if [[ $value == \"*\" ]]; then
            # explicit quotes: JSON string
            ARGUMENTS="$ARGUMENTS\"$key\":$value"
        elif [[ $value =~ ^-?[0-9]+\.?[0-9]*$ ]] || [[ $value == "true" ]] || [[ $value == "false" ]] || [[ $value == "null" ]]; then
            # number, boolean, or null: literal JSON
            ARGUMENTS="$ARGUMENTS\"$key\":$value"
        else
            # default: string with quotes
            ARGUMENTS="$ARGUMENTS\"$key\":\"$value\""
        fi
    else
        echo "Warning: Ignoring invalid argument format: $arg (expected key=value)"
    fi
done
ARGUMENTS="$ARGUMENTS}"

HTTP_RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$SERVER" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json, text/event-stream" \
  -H "Mcp-Session-Id: $SESSION_ID" \
  -d "{
    \"jsonrpc\": \"2.0\",
    \"id\": $CURRENT_REQUEST_ID,
    \"method\": \"tools/call\",
    \"params\": {
      \"name\": \"$TOOL_NAME\",
      \"arguments\": $ARGUMENTS
    }
  }")

HTTP_CODE=$(echo "$HTTP_RESPONSE" | tail -n1)
CALL_RESPONSE=$(echo "$HTTP_RESPONSE" | sed '$d')

if [ "$HTTP_CODE" = "401" ] || [ "$HTTP_CODE" = "404" ]; then
    echo "Error: Session expired or not found. Run ./initialize.sh to create a new session." >&2
    exit 1
fi

if [ -z "$CALL_RESPONSE" ]; then
    echo "Error: No response from server." >&2
    exit 1
fi

echo "$CALL_RESPONSE" | grep '^data: {' | sed 's/^data: //' | tr -d '\000-\037' | jq '.result.structuredContent // .'
