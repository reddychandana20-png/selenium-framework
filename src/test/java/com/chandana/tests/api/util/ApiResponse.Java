package com.chandana.tests.api.util;

import io.restassured.response.Response;

public class ApiResponse<T> {

    private final Response response;
    private final T body;

    public ApiResponse(Response response, T body) {
        this.response = response;
        this.body = body;
    }

    public Response getResponse() {
        return response;
    }

    public T getBody() {
        return body;
    }
}