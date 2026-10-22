package com.example.employee_system.Shared;

import lombok.Getter;

import java.util.List;

@Getter
public class GlobalResponse<T> {
    private final static String SUCCESS = "success";
    private final static String ERROR = "error";

    private String status;

    private T data;

    private List<ErrorItem> errors;

    public GlobalResponse(List<ErrorItem> errors) {
        this.status = ERROR;
        this.data = null;
        this.errors = errors;
    }

    public GlobalResponse(T data) {
        this.status = SUCCESS;
        this.data = data;
        this.errors = null;
    }

    public record ErrorItem(String message) {
    }







    /*
        {
            status: "success";
            data: {}|[]
            error: null
        }




         {
            status: "error";
            data: null
            error: [{},{},{}]
        }
     */
}
