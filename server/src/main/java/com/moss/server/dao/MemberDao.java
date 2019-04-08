package com.moss.server.dao;

import com.moss.server.model.Member;

import java.util.HashMap;
import java.util.List;

public interface MemberDao {

	int add(Member member);
	int del(Integer id);
	int update(Member member);
	int updateFlag(Member member);
	Member get(Member member);
	Member getById(Integer id);
	List<Member> list(Member member);
	int updateMoney(HashMap map);

}