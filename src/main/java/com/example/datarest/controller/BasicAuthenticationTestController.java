package com.example.datarest.controller;

import com.example.datarest.annotation.BasicAuthentication;
import com.example.datarest.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/test")
public class BasicAuthenticationTestController {

   public static final String BASIC_AUTH_SUCCESS = "Basic Authentication Success";

   /**
    * Rest endpoint using 'basic authentication' with @BasicAuthentication
    *
    * @param authentication
    * @return
    * @throws Exception
    */
   @RequestMapping("/basic-auth")
   public ResponseEntity<String> start(@BasicAuthentication BasicAuthentication authentication)
      throws Exception {

      return ResponseEntity.ok().body( BASIC_AUTH_SUCCESS );
   }

   @RequestMapping(value= {"/welcome","/welcome/{name}"}, method= RequestMethod.GET)
   public String welcome(@BasicAuthentication BasicAuthentication authentication,
         @PathVariable(value="name",required = false) String name) {
      return "Hello " + name;
   }
}
