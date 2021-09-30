package com.springboot.rest.domain.dto;

import com.springboot.rest.infrastructure.entity.A;

public class ADTO {

    private Long id;

    private String name;

    private String password;

    private Integer age;

    private Integer phone;

    public ADTO() {
        // Empty constructor needed for Jackson.
    }

    public ADTO(A a) {
        this.id = a.getId();
        this.name = a.getName();
        this.password = a.getPassword();
        this.age = a.getAge();
        this.phone = a.getPhone();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "ADTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", phone=" + phone +
                '}';
    }

}
