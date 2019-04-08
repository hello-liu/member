package com.moss.server.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.moss.common.model.BackModel;
import com.moss.common.model.CheckParamsModel;
import com.moss.common.model.PageModel;
import com.moss.server.common.Constant;
import com.moss.server.dao.UserDao;
import com.moss.server.model.User;
import com.moss.server.sender.Sender;
import com.moss.server.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

	@Autowired
	private Sender sender;

	@Autowired
	UserDao userDao;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Value("${user.token.timeout.web}")
	int timeoutWeb;

	@Value("${user.token.timeout.app}")
	int timeoutApp;

	@Value("${user.token.timeout.pc}")
	int timeoutPc;

	@Transactional
	public BackModel login(JSONObject json ) throws Exception {
		String account = json.getString("account");
		String pwd = json.getString("pwd");
		String term = "web";
		//请求参数验证
		if(StringUtils.isEmpty(account)){
			return new BackModel("error","请输入账号!");
		}
		if(StringUtils.isEmpty(pwd)){
			return new BackModel("error","请输入密码!");
		}
		User user = userDao.login(account);

		if(user == null){
			//用户不存在
			return new BackModel("error","用户不存在!");
		}else if(!"0".equals(user.getFlag() ) ){
			//用户已经注销
			return new BackModel("error","用户已注销!");
		}else if( Util.checkPwd(pwd, user.getPwd() )  ){
			//登录成功
			String userId = user.getId().toString();
			String token = Util.getUuid()+"-"+userId;
//			redisTemplate.opsForValue.get(TOKEN_KEY+":web:"+token,);
			int timeout =  this.getTimeout(term);

			//获取用户的权限
			List<String> ps = userDao.getUserPermis(user.getId() );

			JSONObject tokenData = new JSONObject();
			tokenData.put(Constant.NAME_USER_PERMIS,ps);
			tokenData.put(Constant.NAME_USER_TOKEN,token);
			tokenData.put(Constant.NAME_USER_INFO,user);

			String tokenDataStr = tokenData.toJSONString();

			redisTemplate.opsForValue().set( Constant.TOKEN_KEY +":"+term+":"+userId, tokenDataStr,  timeout, TimeUnit.SECONDS);

			sender.send("user.login","user:"+user.getNickname()+",login success");

			HashMap map = new HashMap();
			map.put("token",token);
			map.put("user",user);
			return new BackModel("ok","登录成功！",map);
		}else {
			//密码错误
			return new BackModel("error","密码错误!");
		}

	}

	@Transactional
	public BackModel logout(JSONObject json ) {

		String term = "web";
		User user = this.getLoginUser(json);
		redisTemplate.delete(Constant.TOKEN_KEY +":"+term+":"+user.getId() );
		return new BackModel("ok","退出成功！");
	}

	//获取不同终端的token超时时间
	public int getTimeout(String term){
		if("app".equals(term)){
			return timeoutApp;
		}else if ("web".equals(term)){
			return  timeoutApp;
		}else if ("pc".equals(term)){
			return  timeoutPc;
		}else{
			return 1800;
		}
	}

	//获取json中的用户信息
	public User getLoginUser(JSONObject json){
		JSONObject userData = json.getJSONObject(Constant.NAME_USER_DATA);
		JSONObject userInfo =userData.getJSONObject(Constant.NAME_USER_INFO);
		User user = JSONObject.toJavaObject(userInfo, User.class);
		return user;
	}

	@Transactional
	public BackModel add(JSONObject json ) throws Exception{

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

		User user  = JSONObject.toJavaObject(json, User.class);
		//密码再加密
		String pwd = Util.getPwd(user.getPwd());
		user.setPwd(pwd );
		int result = userDao.add(user);
		return new BackModel("ok","添加成功！");

	}

	@Transactional
	public BackModel del(JSONObject json ) {

		Integer id = json.getInteger("id");
		int result = userDao.del(id);
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

		User user = JSONObject.toJavaObject(json, User.class);
		int result = userDao.update(user);
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

		User user  = JSONObject.toJavaObject(json, User.class);
		int result = userDao.updateFlag(user);
		return new BackModel("ok","修改成功！");

	}

	@Transactional
	public BackModel setMerchant(JSONObject json ) {

		//验证请求参数
		List<CheckParamsModel> cps = new ArrayList<CheckParamsModel>();
		cps.add(new CheckParamsModel("id", 0,20,"[0-9]+" ) );
		cps.add(new CheckParamsModel("merchantId", 0,20,"[0-9]+" ) );

		BackModel backModel = Util.checkParams(json, cps);
		if(backModel != null){
			return backModel;
		}

		User user  = JSONObject.toJavaObject(json, User.class);
		int result = userDao.setMerchant(user);
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
		User user = JSONObject.toJavaObject(json, User.class);
		user.setMerchantId(merchantId );
		user = userDao.get(user );

		return new BackModel("ok","查询成功！", user );

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
		User user = JSONObject.toJavaObject(json, User.class);

		//分页查询
		PageHelper.startPage(pageNo, pageNum);
		List<User> ms = userDao.list(user);
		PageInfo page= new PageInfo<>(ms);

		return new BackModel("ok","查询成功！", new PageModel(page.getTotal(),ms) );

	}

	@Transactional
	public BackModel updateMoney(Integer id, Integer money, Integer integral) {

		HashMap map = new HashMap();
		map.put("id", id);
		map.put("money", money);
		map.put("integral", integral);
		int result = userDao.updateMoney(map );

		if(result <=0){
			return new BackModel("error","失败！",  null);
		}
		return new BackModel("ok","成功！",null );

	}

}