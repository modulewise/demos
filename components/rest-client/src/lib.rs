#![no_main]

use wasi::http::types::{Headers, OutgoingBody, OutgoingRequest, Scheme};

wit_bindgen::generate!({
    path: "../wit",
    world: "rest-client-world",
    generate_all
});

struct RestClient;

impl RestClient {
    fn request(
        url: &str,
        headers: Vec<(String, String)>,
        method: &wasi::http::types::Method,
        body: Option<String>,
    ) -> Result<String, String> {
        let request_headers = Headers::new();
        for (name, value) in headers {
            let _ = request_headers.append(&name, value.as_bytes());
        }

        let request = OutgoingRequest::new(request_headers);
        let _ = request.set_method(method);

        let (scheme, path) = url.split_once("://").unwrap_or(("https", url));
        let _ = request.set_scheme(match scheme {
            "http" => Some(&Scheme::Http),
            "https" => Some(&Scheme::Https),
            _ => None,
        });

        let (host, path) = path.split_once('/').unwrap_or((path, ""));
        let _ = request.set_authority(Some(host));
        let path_with_query = if path.is_empty() {
            "/".to_string()
        } else if path.starts_with('/') {
            path.to_string()
        } else {
            format!("/{path}")
        };
        let _ = request.set_path_with_query(Some(&path_with_query));

        if let Some(body_data) = body {
            let outgoing_body = request
                .body()
                .map_err(|()| "Failed to get request body".to_string())?;
            let output_stream = outgoing_body
                .write()
                .map_err(|()| "Failed to get output stream".to_string())?;
            let _ = output_stream.blocking_write_and_flush(body_data.as_bytes());
            drop(output_stream);
            OutgoingBody::finish(outgoing_body, None)
                .map_err(|_| "Failed to finish body".to_string())?;
        }

        let response = wasi::http::outgoing_handler::handle(request, None)
            .map_err(|_| "Failed to send HTTP request".to_string())?;
        response.subscribe().block();
        let response = response
            .get()
            .ok_or("Failed to get response".to_string())?
            .map_err(|()| "Request failed".to_string())?
            .map_err(|e| format!("HTTP error: {e:?}").replace("ErrorCode::", ""))?;
        let body = response
            .consume()
            .map_err(|()| "Failed to consume response body".to_string())?;
        let stream = body
            .stream()
            .map_err(|()| "Failed to get response stream".to_string())?;
        let mut result = String::new();
        while let Ok(v) = stream.blocking_read(1024) {
            if v.is_empty() {
                break;
            }
            result.push_str(
                &String::from_utf8(v).map_err(|_| "Invalid UTF-8 in response".to_string())?,
            );
        }
        drop(stream);
        drop(body);
        Ok(result)
    }
}

impl exports::modulewise::demo::rest_client::Guest for RestClient {
    fn get(url: String, headers: Vec<(String, String)>) -> Result<String, String> {
        Self::request(&url, headers, &wasi::http::types::Method::Get, None)
    }

    fn post(url: String, headers: Vec<(String, String)>, body: String) -> Result<String, String> {
        Self::request(&url, headers, &wasi::http::types::Method::Post, Some(body))
    }
}

export!(RestClient);
