package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomHttpResponse {
    private int statusCode;
    private HttpStatus status;
    private Map<String, Object> data;
    private Map<String, String> error;

}
