package com.v02.minback.controller;

import com.v02.minback.model.result.RestMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin")
@RestController
public class AdminController {

    @GetMapping("/access")
    public RestMessage access(){

        return new RestMessage("ROLE_ADMIN");
    }
}
