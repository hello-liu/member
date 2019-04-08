package com.moss.server.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.moss.common.model.BackModel;
import com.moss.common.model.CheckParamsModel;
import com.moss.common.model.PageModel;
import com.moss.server.dao.FlowDao;
import com.moss.server.dao.LogDao;
import com.moss.server.model.Flow;
import com.moss.server.model.FlowInfo;
import com.moss.server.model.Log;
import com.moss.server.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LogService {

	@Autowired
	private LogDao logDao;

	@Transactional
	public BackModel list(JSONObject json ) {

		//验证请求参数
		List<CheckParamsModel> cps = new ArrayList<CheckParamsModel>();
		cps.add(new CheckParamsModel("pageNo", 0,99,"[0-9]+" ) );
		cps.add(new CheckParamsModel("pageNum", 0,99,"[0-9]+" ) );

		BackModel backModel = Util.checkParams(json, cps);
		if(backModel != null){
			return backModel;
		}

		//获取请求参数
		Integer pageNo = json.getInteger("pageNo");
		Integer pageNum = json.getInteger("pageNum");
		Log log = JSONObject.toJavaObject(json, Log.class);

		//分页查询
		PageHelper.startPage(pageNo, pageNum);
		List<Log> ms = logDao.list(log);
		PageInfo page= new PageInfo<>(ms);

		return new BackModel("ok","查询成功！", new PageModel(page.getTotal(),ms) );

	}

}