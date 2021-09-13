package com.financemanager.demo.site.controller;

import org.springframework.http.ResponseEntity;

import com.financemanager.demo.site.exception.APIException;

public class ResponseEntityBuilder {

	 public static ResponseEntity<Object> build(APIException apiException) {
         return new ResponseEntity<>(apiException, apiException.getHttpStatus());
   }
}
