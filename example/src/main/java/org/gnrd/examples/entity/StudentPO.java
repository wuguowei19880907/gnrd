package org.gnrd.examples.entity;

import lombok.Getter;
import lombok.Setter;
import org.gnrd.lam.entity.BasePO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "student")
@Setter
@Getter
public class StudentPO extends BasePO {

    /**
     * 学生名称
     */
    @Column(name = "stu_name")
    private String name;

    /**
     * 学生地址
     */
    @Column(name = "stu_addr")
    private String addr;
}
