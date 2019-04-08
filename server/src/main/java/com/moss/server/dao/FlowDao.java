package com.moss.server.dao;

import com.moss.server.model.Flow;
import com.moss.server.model.FlowInfo;

import java.util.List;

public interface FlowDao {

	int add(Flow flow);
	List<FlowInfo> list(FlowInfo flow);



}