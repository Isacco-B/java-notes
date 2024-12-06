package com.isaccobertoli.dto;

public class GenericResponse<T> {
    private String message;
    private String status;
    private T data;

    public GenericResponse() {}

    public void setAccessToken(T data, String message, String status) {
        this.message = message;
        this.data = data;
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
