package com.coursecatalog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ApiDataResponse <T>{
    private boolean success;
    private String message;
    private T data;
    private HttpStatus httpStatus;
}