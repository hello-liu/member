package com.moss.server.dao;

import com.moss.server.model.Log;

import java.util.List;

public interface LogDao {

	int add(Log log);
	List<Log> list(Log log);



}