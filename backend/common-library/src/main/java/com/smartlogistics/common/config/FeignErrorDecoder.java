package com.smartlogistics.common.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus responseStatus = HttpStatus.valueOf(response.status());
        
        if (responseStatus.is4xxClientError()) {
            return new ResponseStatusException(responseStatus, "Client Error: " + response.reason());
        } else if (responseStatus.is5xxServerError()) {
            // Feign will retry on RetryableException automatically if a Retryer is configured.
            return new feign.RetryableException(
                    response.status(),
                    "Server Error: " + response.reason(),
                    response.request().httpMethod(),
                    (Long) null,
                    response.request()
            );
        }

        return defaultErrorDecoder.decode(methodKey, response);
        
    }
}
