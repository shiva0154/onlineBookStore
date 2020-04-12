package com.netent.demo.controllers;


import com.netent.demo.utils.Constants;
import com.netent.demo.utils.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.BOOKSTORE)
public class HealthController {

  @RequestMapping(value = Constants.PING, method = RequestMethod.GET)
  public ResponseEntity<String> ping() {
    return new ResponseEntity<>(Messages.PONG, HttpStatus.OK);
  }

}
