package com.moss.server.dao;

import com.moss.server.model.Merchant;

import java.util.List;

public interface MerchantDao {

	int add(Merchant merchant);
	int del(Integer id);
	int update(Merchant merchant);
	int updateFlag(Merchant merchant);
	Merchant get(Integer id);
	List<Merchant> list(Merchant merchant);


}