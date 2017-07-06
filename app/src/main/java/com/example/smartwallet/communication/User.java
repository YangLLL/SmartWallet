package com.example.smartwallet.communication;

import java.io.Serializable;
import java.util.Date;

/** 
 * @ClassName: User 
 * @Description: TODO
 * @author: 龚胜军
 * @date: 2016年10月24日 下午3:27:59  
 * @Fields id : 用户id
 * @Fields name : 用户名
 * @Fields password : 用户密码
 * @Fields phone : 电话号码
 * @Fields email : 邮箱
 * @Fields age : 年龄
 * @fields sax : 性别
 * @fields birth : 生日
 * @Fields avatar : 头像
 * @fields address : 地址
 * @Fields friends : 好友列表
 */
public class User implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private String name;
	
	private String password;
	
	private String phone;
	
	private String email;
	
	private int age;
	
	private String sex;
	
	private Date birth;

	private String address = "";

	private String avatar = "";
	
	private String friends = "";
	
	public User() {
	}

	public User(String name, String password, String phone, String email, int age, String sex, Date birth, String address) {
		super();
		this.name = name;
		this.password = password;
		this.phone = phone;
		this.email = email;
		this.age = age;
		this.sex = sex;
		this.birth = birth;
		this.address = address;
	}
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}
	
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getFriends() {
		return friends;
	}

	public void setFriends(String friends) {
		this.friends = friends;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", password=" + password + ", phone=" + phone + ", email=" + email
				+ ", age=" + age + ", sex=" + sex + ", birth=" + birth + ", address=" + address + ", avatar=" + avatar
				+ ", friends=" + friends + "]";
	}
	
}