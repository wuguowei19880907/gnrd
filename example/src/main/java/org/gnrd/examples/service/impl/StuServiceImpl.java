package org.gnrd.examples.service.impl;

import org.gnrd.examples.dao.StudentDao;
import org.gnrd.examples.entity.StudentPO;
import org.gnrd.examples.service.StuService;
import org.gnrd.examples.vo.StuVO;
import org.gnrd.lam.common.result.ParamPager;
import org.gnrd.lam.common.result.ResultPager;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("stuService")
public class StuServiceImpl implements StuService {

    @Resource
    private StudentDao studentDao;
    @Resource
    private ModelMapper modelMapper;

    @Override
    public ResultPager<StuVO> list(ParamPager pager) {
        Pageable pageable = PageRequest.of(pager.getNumber(), pager.getSize());
        Page<StudentPO> studentPOS = studentDao.findAll(pageable);
        Page<StuVO> map = studentPOS.map(studentPO -> modelMapper.map(studentPO, StuVO.class));
        return ResultPager.of(map);
    }
}
