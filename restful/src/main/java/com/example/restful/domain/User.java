package com.example.restful.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * @author: shudj
 * @time: 2019/2/15 16:24
 * @description:
 */

@GenericGenerator(name="jpa-uuid", strategy = "uuid")
@Entity
public class User {

    @Id
    @NotNull
    @GeneratedValue(generator = "jpa-uuid")
    @JsonIgnoreProperties(value={"hibernateLazyInitializer"})
    private String id;
    @NotNull
    private String userName;
    private int age;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
