package com.moss.server.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.moss.common.model.BackModel;
import com.moss.common.model.CheckParamsModel;
import com.moss.common.model.PageModel;
import com.moss.server.dao.MerchantDao;
import com.moss.server.model.Merchant;
import com.moss.server.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MerchantService {

	@Autowired
	private MerchantDao merchantDao;

	@Transactional
	public BackModel add(JSONObject json ) {

		//验证请求参数
		List<CheckParamsModel> cps = new ArrayList<CheckParamsModel>();
		cps.add(new CheckParamsModel("name", 0,50,"" ) );
		cps.add(new CheckParamsModel("address", 0,255,"" ) );
		cps.add(new CheckParamsModel("phone", 1,20,"" ) );
		cps.add(new CheckParamsModel("linkman", 1,20,"" ) );

		BackModel backModel = Util.checkParams(json, cps);
		if(backModel != null){
			return backModel;
		}

		Merchant merchant = JSONObject.toJavaObject(json, Merchant.class);
		int result = merchantDao.add(merchant);
		return new BackModel("ok","添加成功！");

	}

	@Transactional
	public BackModel del(JSONObject json ) {

		Integer id = json.getInteger("id");
		int result = merchantDao.del(id);
		return new BackModel("ok","删除成功！");

	}

	@Transactional
	public BackModel update(JSONObject json ) {

		//验证请求参数
		List<CheckParamsModel> cps = new ArrayList<CheckParamsModel>();
		cps.add(new CheckParamsModel("id", 0,20,"[0-9]+" ) );
		cps.add(new CheckParamsModel("name", 0,50,"" ) );
		cps.add(new CheckParamsModel("address", 0,255,"" ) );
		cps.add(new CheckParamsModel("phone", 1,20,"" ) );
		cps.add(new CheckParamsModel("linkman", 1,20,"" ) );

		BackModel backModel = Util.checkParams(json, cps);
		if(backModel != null){
			return backModel;
		}

		Merchant merchant = JSONObject.toJavaObject(json, Merchant.class);
		int result = merchantDao.update(merchant);
		return new BackModel("ok","修改成功！");

	}

	@Transactional
	public BackModel updateFlag(JSONObject json ) {

		//验证请求参数
		List<CheckParamsModel> cps = new ArrayList<CheckParamsModel>();
		cps.add(new CheckParamsModel("id", 0,20,"[0-9]+" ) );
		cps.add(new CheckParamsModel("flag", 0,8,"" ) );

		BackModel backModel = Util.checkParams(json, cps);
		if(backModel != null){
			return backModel;
		}

		Merchant merchant = JSONObject.toJavaObject(json, Merchant.class);
		int result = merchantDao.updateFlag(merchant);
		return new BackModel("ok","修改成功！");

	}

	@Transactional
	public BackModel get(JSONObject json) {

		Integer id = json.getInteger("id");
		Merchant merchant = merchantDao.get(id );
		return new BackModel("ok","查询成功！", merchant );

	}

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
		Merchant merchant = JSONObject.toJavaObject(json, Merchant.class);

		//分页查询
		PageHelper.startPage(pageNo, pageNum);
		List<Merchant> ms = merchantDao.list(merchant);
		PageInfo page= new PageInfo<>(ms);

		return new BackModel("ok","查询成功！", new PageModel(page.getTotal(),ms) );

	}


}