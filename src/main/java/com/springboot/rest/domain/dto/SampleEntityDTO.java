package com.springboot.rest.domain.dto;

import java.util.List;

import com.springboot.rest.infrastructure.entity.SampleEntity;
import com.springboot.rest.infrastructure.entity.SampleEntityAuxiliary;

public class SampleEntityDTO {

    private Long id;

    private String name;

    private String password;

    private Integer age;

    private Integer phone;
    
//    private List<SampleEntityAuxiliary> seAuxList;

    public SampleEntityDTO() {
        // Empty constructor needed for Jackson.
    }

    public SampleEntityDTO(SampleEntity sampleEntity) {
        this.id = sampleEntity.getId();
        this.name = sampleEntity.getName();
        this.password = sampleEntity.getPassword();
        this.age = sampleEntity.getAge();
        this.phone = sampleEntity.getPhone();
    }

    /*
    public SampleEntityDTO(Long id, String name, String password, Integer age, Integer phone,
			List<SampleEntityAuxiliary> seAuxList) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.age = age;
		this.phone = phone;
		this.seAuxList = seAuxList;
	}
	*/

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

	/*
	 * public List<SampleEntityAuxiliary> getSeAuxList() { return seAuxList; }
	 * 
	 * public void setSeAuxList(List<SampleEntityAuxiliary> seAuxList) {
	 * this.seAuxList = seAuxList; }
	 */

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
