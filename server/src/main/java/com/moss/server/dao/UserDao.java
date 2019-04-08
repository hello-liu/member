package com.moss.server.dao;

import com.moss.server.model.User;

import java.util.HashMap;
import java.util.List;

public interface UserDao {

	User login(String account);

	List<String> getUserPermis(Integer id);

	int add(User user);
	int del(Integer id);
	int update(User user);
	int updateFlag(User userr);
	int setMerchant(User userr);
	User get(User user);
	List<User> list(User user);
	int updateMoney(HashMap map);

}