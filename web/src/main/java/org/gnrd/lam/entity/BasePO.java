package org.gnrd.lam.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Setter
@Getter
@MappedSuperclass
public class BasePO implements Serializable {

    /**
     * id
     */
    @Id
    @Column(updatable = false)
    @GenericGenerator(name = "uid",
            strategy = "org.gnrd.uid.SnowflakeId")
    @GeneratedValue(generator = "uid")
    protected Long id;
}
