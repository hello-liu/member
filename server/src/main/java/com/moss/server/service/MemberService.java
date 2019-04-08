package com.moss.server.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.moss.common.model.BackModel;
import com.moss.common.model.CheckParamsModel;
import com.moss.common.model.PageModel;
import com.moss.server.common.Constant;
import com.moss.server.dao.MemberDao;
import com.moss.server.model.Member;
import com.moss.server.model.User;
import com.moss.server.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class MemberService {

	@Autowired
	private MemberDao memberDao;

	@Autowired
	private UserService userService;


	@Transactional
	public BackModel add(JSONObject json ) throws Exception{

		User user = userService.getLoginUser(json);

		json.put("merchantId",user.getMerchantId());
		//验证请求参数
		List<CheckParamsModel> cps = new ArrayList<CheckParamsModel>();
		cps.add(new CheckParamsModel("merchantId", 0,20,"[0-9]+" ) );
		cps.add(new CheckParamsModel("nickname", 0,50,"" ) );
		cps.add(new CheckParamsModel("account", 1,60,"" ) );
		cps.add(new CheckParamsModel("phone", 1,20,"" ) );
		cps.add(new CheckParamsModel("pwd", 1,50,"" ) );

		BackModel backModel = Util.checkParams(json, cps);
		if(backModel != null){
			return backModel;
		}

		Member member = JSONObject.toJavaObject(json, Member.class);
		//密码再加密
		String pwd = Util.getPwd(member.getPwd());
		member.setPwd(pwd );
		int result = memberDao.add(member);
		return new BackModel("ok","添加成功！");

	}

	@Transactional
	public BackModel del(JSONObject json ) {

		Integer id = json.getInteger("id");
		int result = memberDao.del(id);
		return new BackModel("ok","删除成功！");

	}

	@Transactional
	public BackModel update(JSONObject json ) {

		//验证请求参数
		List<CheckParamsModel> cps = new ArrayList<CheckParamsModel>();
		cps.add(new CheckParamsModel("id", 0,20,"[0-9]+" ) );
		cps.add(new CheckParamsModel("nickname", 0,50,"" ) );
		cps.add(new CheckParamsModel("account", 1,60,"" ) );
		cps.add(new CheckParamsModel("phone", 1,20,"" ) );

		BackModel backModel = Util.checkParams(json, cps);
		if(backModel != null){
			return backModel;
		}

		Member member = JSONObject.toJavaObject(json, Member.class);
		int result = memberDao.update(member);
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

		Member member = JSONObject.toJavaObject(json, Member.class);
		int result = memberDao.updateFlag(member);
		return new BackModel("ok","修改成功！");

	}

	@Transactional
	public BackModel get(JSONObject json ) {

		//验证请求参数
		List<CheckParamsModel> cps = new ArrayList<CheckParamsModel>();
		cps.add(new CheckParamsModel("account", 0,60,"" ) );
		BackModel backModel = Util.checkParams(json, cps);

		cps.clear();
		cps.add(new CheckParamsModel("phone", 0,20,"" ) );
		BackModel backModel2 = Util.checkParams(json, cps);

		if(backModel != null && backModel2 !=null){
			return new BackModel("error","参数错误！", "need phone or account" );
		}

		JSONObject userData = json.getJSONObject(Constant.NAME_USER_DATA);
		JSONObject userInfo =userData.getJSONObject(Constant.NAME_USER_INFO);
		Integer merchantId = userInfo.getInteger("merchantId");

		//获取请求参数
		Member member = JSONObject.toJavaObject(json, Member.class);
		member.setMerchantId(merchantId );
		member = memberDao.get(member );

		return new BackModel("ok","查询成功！", member );

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
		Member member = JSONObject.toJavaObject(json, Member.class);

		//分页查询
		PageHelper.startPage(pageNo, pageNum);
		List<Member> ms = memberDao.list(member);
		PageInfo page= new PageInfo<>(ms);

		return new BackModel("ok","查询成功！", new PageModel(page.getTotal(),ms) );

	}

	@Transactional
	public BackModel updateMoney(Integer id, Integer money, Integer integral) {

		HashMap map = new HashMap();
		map.put("id", id);
		map.put("money", money);
		map.put("integral", integral);
		int result = memberDao.updateMoney(map );

		if(result <=0){
			return new BackModel("error","失败！",  null);
		}
		return new BackModel("ok","成功！",null );

	}

}