#!/bin/sh

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

TOOLS_RESPONSE=$(curl -s -X POST "$SERVER" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json, text/event-stream" \
  -H "Mcp-Session-Id: $SESSION_ID" \
  -d "{
    \"jsonrpc\": \"2.0\",
    \"id\": $CURRENT_REQUEST_ID,
    \"method\": \"tools/list\",
    \"params\": {}
  }")

echo "$TOOLS_RESPONSE" | grep "^data:" | sed 's/^data: //' | jq .
