package com.moss.server.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class Member {

	private Integer id;
	private Integer merchantId;
	private String merchantName;
	private String nickname ;
	private String sex ;
	private Integer age ;
	@JSONField(serialize=false)//非序列化字段
	private String pwd ;
	private String account ;
	private String phone ;
	private String email ;
	private String idnumber ;
	private String address ;
	private Integer money ;
	private Integer integral ;
	private Integer head ;
	private String flag ;
	private Date createTime ;
	private String remark ;

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", merchantId=" + merchantId +
				", nickname='" + nickname + '\'' +
				", sex='" + sex + '\'' +
				", age=" + age +
				", pwd='" + pwd + '\'' +
				", account='" + account + '\'' +
				", phone='" + phone + '\'' +
				", email='" + email + '\'' +
				", idnumber='" + idnumber + '\'' +
				", address='" + address + '\'' +
				", money=" + money +
				", integral=" + integral +
				", head=" + head +
				", flag='" + flag + '\'' +
				", createTime=" + createTime +
				", remark='" + remark + '\'' +
				'}';
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
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

	public String getIdnumber() {
		return idnumber;
	}

	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public Integer getHead() {
		return head;
	}

	public void setHead(Integer head) {
		this.head = head;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
}
