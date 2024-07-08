package org.gnrd.lam.service;

import org.gnrd.lam.vo.LoginVO;

public interface IndexService {

    LoginVO login(String username, String password) throws Exception;
}
