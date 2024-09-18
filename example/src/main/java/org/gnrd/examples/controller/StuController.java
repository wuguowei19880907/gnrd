package org.gnrd.examples.controller;

import org.gnrd.examples.service.StuService;
import org.gnrd.examples.vo.StuVO;
import org.gnrd.lam.common.result.CommonResult;
import org.gnrd.lam.common.result.ParamPager;
import org.gnrd.lam.common.result.ResultPager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("students")
public class StuController {

    @Resource
    private StuService stuService;

    @RequestMapping(name = "获取学生列表", value = "", method = RequestMethod.GET)
    public CommonResult<ResultPager<StuVO>> list(ParamPager pager) throws Exception {
        ResultPager<StuVO> list = stuService.list(pager);
        return new CommonResult<>(list);
    }
}
