package com.financemanager.controller;

import com.financemanager.exception.APIException;

import org.springframework.http.ResponseEntity;

public class ResponseEntityBuilder {

	 public static ResponseEntity<Object> build(APIException apiException) {
         return new ResponseEntity<>(apiException, apiException.getHttpStatus());
   }
}
