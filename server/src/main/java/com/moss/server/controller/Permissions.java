package com.moss.server.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.moss.common.model.BackModel;
import com.moss.server.common.Constant;
import com.moss.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Service
public class Permissions {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private UserService userService;

	//默认通过的方法
	private static final String[] passs = {"user.login"};


	public BackModel validation(JSONObject json ){

		String method = json.getString("method");
		String token = json.getString("token");

		if(StringUtils.isEmpty(method) ){
			return null;
		}

		//默认通过
		for(String pass : passs){
			if(pass.equals(method)){
				return null;
			}
		}

		if(StringUtils.isEmpty(token) ){
			return new BackModel("error","未登录！");
		}
		//redist 权限检查
		String term = "web";
		String[] ts = token.split("-");
		String userId = ts[ts.length-1];
		String tokenKey = Constant.TOKEN_KEY +":"+term+":"+userId;
		String userTokenData = redisTemplate.opsForValue().get(tokenKey);
		if(userTokenData == null){
			return new BackModel("error","未登录！");
		}else{
			JSONObject userData = JSONObject.parseObject(userTokenData);
			String userToken = userData.getString(Constant.NAME_USER_TOKEN );
			//验证token是否正确
			if(! token.equals(userToken) ){
				return new BackModel("error","在其他地方登录！");
			}
			//验证权限是否存在
			JSONArray ps = userData.getJSONArray(Constant.NAME_USER_PERMIS );

			for(int i = 0; i<ps.size(); i++){
				String permis = ps.getString(i);
				if(method.equals(permis ) ){
					//给json参数中添加用户信息
					json.put(Constant.NAME_USER_DATA, userData);
					//刷新token有效时间
					redisTemplate.expire(tokenKey, userService.getTimeout(term), TimeUnit.SECONDS);
					return null;
				}
			}
		}
		return new BackModel("error","权限验证失败！");
	}

}


