package com.moss.client.controller;

import com.moss.client.micro.ServerFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/controller")
public class Controller {


    @Autowired
    ServerFeign serverFeign;

    @RequestMapping(value = "/api")
    public String api(@RequestBody String message){
        System.out.println(message);

        String result = serverFeign.api("from client "+message);
//        String result = "cc";

        return "this is client :"+result;


    }



}
