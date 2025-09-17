package com.example.zerowaste_api.common;

import org.springframework.http.HttpStatus;

public abstract class BaseController {

  protected <T> ResponseDTO<T> createResponse(HttpStatus status) {
    return new ResponseDTO<>(status);
  }

  protected <T> ResponseDTO<T> createResponse(HttpStatus status, T body) {
    return new ResponseDTO<>(body, status);
  }
}
