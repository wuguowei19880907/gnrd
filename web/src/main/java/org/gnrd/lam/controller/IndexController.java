package org.gnrd.lam.controller;

import org.gnrd.lam.common.result.CommonResult;
import org.gnrd.lam.ro.LoginRO;
import org.gnrd.lam.service.IndexService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("api")
public class IndexController {

    @Resource
    private IndexService indexService;

    @GetMapping(value = "index")
    public String index() {
        return "ok";
    }

    @PostMapping(value = "auth/login")
    @ResponseBody
    public CommonResult<String> auth(@RequestBody LoginRO loginRO, HttpServletRequest request) throws Exception {
        indexService.login(loginRO.getUsername(), loginRO.getPassword());
        HttpSession session = request.getSession();
        session.setAttribute("username", loginRO.getUsername());
        return new CommonResult<>("ok");
    }

    @GetMapping(value = "me")
    @ResponseBody
    public CommonResult<String> getInfo(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        Object username = session.getAttribute("username");
        return new CommonResult<>(username.toString());
    }

    @PostMapping(value = "auth/logout")
    @ResponseBody
    public CommonResult<String> logout(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        session.invalidate();
        return new CommonResult<>("logout ok");
    }
}
