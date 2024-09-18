package org.gnrd.examples.service;

import org.gnrd.examples.vo.StuVO;
import org.gnrd.lam.common.result.ParamPager;
import org.gnrd.lam.common.result.ResultPager;

public interface StuService {

    ResultPager<StuVO> list(ParamPager pager);
}
