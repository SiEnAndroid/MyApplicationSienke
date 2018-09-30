package com.example.administrator.thinker_soft.meter_code.sk.http;

public class Response {

    private final int code;
    private final String message;
    private final String method;
    private final String contentType;
    private final int contentLength;
    private final String body;

    public Response(Builder builder) {
        this.code = builder.code;
        this.message = builder.message;
        this.method = builder.method;
        this.contentType = builder.contentType;
        this.contentLength = builder.contentLength;
        this.body = builder.body;
    }

    public Response newResponse(Response response) {
        return new Builder()
                .code(response.getCode())
                .message(response.getMessage())
                .method(response.getMethod())
                .contentType(response.getContentType())
                .contentLength(response.getContentLength())
                .body(response.getBody())
                .build();
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getMethod() {
        return method;
    }

    public String getContentType() {
        return contentType;
    }

    public int getContentLength() {
        return contentLength;
    }

    public boolean isSuccessful() {
        return this.code >= 200 && this.code < 300;
    }

    public String getBody() {
        return body;
    }

    public static class Builder {
        private int code;
        private String message;
        private String method;
        private String contentType;
        private int contentLength;
        private String body;

        public Builder code(int code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder contentLength(int contentLength) {
            this.contentLength = contentLength;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Response build() {
            return new Response(this);
        }

    }

}
