package com.joy.entity;

import java.io.Serializable;

/**
 * @Description:
 * @Author: Joy
 * @Date: 2019-05-20 17:09
 */
public class User implements Serializable {

    private final static long serialVersionUID = 1L;

    private String name;
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
