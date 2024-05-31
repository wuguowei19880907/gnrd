package org.gnrd.lam.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class IndexController {

    @GetMapping(value = "index")
    public String index() {
        return "ok";
    }
}
