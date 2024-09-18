package org.gnrd.examples.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StuVO {

    /**
     * 学生id
     */
    private String id;

    /**
     * 学生名称
     */
    private String name;

    /**
     * 学生地址
     */
    private String addr;
}
