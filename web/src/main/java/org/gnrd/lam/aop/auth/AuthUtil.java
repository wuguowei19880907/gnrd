package org.gnrd.lam.aop.auth;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.gnrd.lam.common.constants.UserStatusEnum;
import org.gnrd.lam.common.tools.Convert;
import org.gnrd.lam.dto.LoginPermissionDTO;
import org.gnrd.lam.dto.LoginUserDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component("authUtil")
public class AuthUtil {


    public boolean hasLogin() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        HttpSession session = request.getSession();
        Object loginUserObj = session.getAttribute("login-user");
        if (loginUserObj != null) {
            LoginUserDTO loginUserDTO = (LoginUserDTO) loginUserObj;
            return loginUserDTO.getState() == UserStatusEnum.ENABLED.getValue();
        }
        return false;
    }

    public boolean hasPermissions(String... permissions) {
        // 要求的权限信息不可为空
        if (permissions.length == 0) {
            return false;
        }
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        HttpSession session = request.getSession();
        Object loginPermissionObj = session.getAttribute("login-permissions");
        if (loginPermissionObj == null) {
            return false;
        }
        List<LoginPermissionDTO> list = Convert.toList(loginPermissionObj, LoginPermissionDTO.class);
        // 获取session中的登录用户的权限信息 loginPermissionSet
        Set<String> loginPermissionSet = list.stream().map(LoginPermissionDTO::getCode).collect(Collectors.toSet());
        // 接口要求的权限 permissionSet
        ImmutableSet<String> permissionSet = ImmutableSet.copyOf(permissions);
        // 接口要求的权限和登录用户的权限求交集，如果交集长度和接口要求的权限相等，表面登录的用户拥有接口要去的权限
        return Sets.intersection(permissionSet, loginPermissionSet).size() == permissionSet.size();
    }
}
