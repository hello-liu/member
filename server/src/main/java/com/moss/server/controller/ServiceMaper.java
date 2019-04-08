package com.moss.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.moss.common.model.BackModel;
import com.moss.server.model.Flow;
import com.moss.server.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceMaper {

	@Autowired
	UserService userService;

	@Autowired
	MerchantService merchantService;

	@Autowired
	MemberService memberService;

	@Autowired
	BusiService busiService;

	@Autowired
	FlowService flowService;

	@Autowired
	LogService logService;

	public BackModel maper(JSONObject json ) throws Exception {
		String method = json.getString("method");
		switch(method){

			//user
			case "user.login":return userService.login(json);
			case "user.logout":return userService.logout(json);
			case "user.add":return userService.add(json);
			case "user.del":return userService.del(json);
			case "user.update":return userService.update(json);
			case "user.updateFlag":return userService.updateFlag(json);
			case "user.setMerchant":return userService.setMerchant(json);
			case "user.get":return userService.get(json);
			case "user.list":return userService.list(json);

			//merchant
			case "merchant.add":return merchantService.add(json);
			case "merchant.del":return merchantService.del(json);
			case "merchant.update":return merchantService.update(json);
			case "merchant.list":return merchantService.list(json);
			case "merchant.updateFlag":return merchantService.updateFlag(json);
			case "merchant.get":return merchantService.get(json);

			//member
			case "member.add":return memberService.add(json);
			case "member.del":return memberService.del(json);
			case "member.update":return memberService.update(json);
			case "member.list":return memberService.list(json);
			case "member.get":return memberService.get(json);
			case "member.updateFlag":return memberService.updateFlag(json);

			//busi
			case "busi.charge":return busiService.charge(json);
			case "busi.consume":return busiService.consume(json);
			case "busi.refund":return busiService.refund(json);
			case "busi.integralToMoney":return busiService.integralToMoney(json);

			//flow
			case "flow.list":return flowService.list(json);

			//log
			case "log.list":return logService.list(json);


			default: return new BackModel("error","接口不存在");
		}

	}

}


