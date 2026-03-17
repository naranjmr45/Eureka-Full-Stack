package com.tcs.fullstack.users.fullstackusers.shared;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 400 -> {
            }
            case 404 -> {
                if (methodKey.contains("getItems")) {
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()), "Users Items are not found.");
                }
            }
            default -> {
                return new Exception(response.reason());
            }
        }

        return null;
    }

}
