package com.moss.server.service;

import com.alibaba.fastjson.JSONObject;
import com.moss.common.model.BackModel;
import com.moss.common.model.CheckParamsModel;
import com.moss.server.common.Constant;
import com.moss.server.dao.MemberDao;
import com.moss.server.model.Flow;
import com.moss.server.model.Member;
import com.moss.server.model.User;
import com.moss.server.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BusiService {

	@Autowired
	private MemberService memberService;

	@Autowired
	private FlowService flowService;

	@Autowired
	private UserService userService;

	@Autowired
	MemberDao memberDao;

	//充值
	@Transactional
	public BackModel charge(JSONObject json ) {

		//验证请求参数
		List<CheckParamsModel> cps = new ArrayList<CheckParamsModel>();
		cps.add(new CheckParamsModel("memberId", 0,50,"" ) );
		cps.add(new CheckParamsModel("money", 0,255,"[0-9]+" ) );
		cps.add(new CheckParamsModel("payway", 1,20,"" ) );
		cps.add(new CheckParamsModel("content", 1,20,"" ) );

		BackModel backModel = Util.checkParams(json, cps);
		if(backModel != null){
			return backModel;
		}

		//获取操作员信息
		User user = userService.getLoginUser(json);
		Integer merchantId = user.getMerchantId();
		Integer userId = user.getId();

		//设置操作员和商户信息
		Flow flow = JSONObject.toJavaObject(json, Flow.class);
		flow.setOperatorId(userId);
		flow.setMerchantId(merchantId);

		//充值
		flow.setType("1");
		String no = System.currentTimeMillis()+"";
		flow.setNo(no);

		//修改会员金额
		//充值不怎加积分
		memberService.updateMoney(flow.getMemberId(), flow.getMoney(), 0);

		//记录流水
		flowService.add(flow);

		return new BackModel("ok","充值成功！");

	}

	//消费
	@Transactional
	public BackModel consume(JSONObject json ) throws Exception {

		//验证请求参数
		List<CheckParamsModel> cps = new ArrayList<CheckParamsModel>();
		cps.add(new CheckParamsModel("memberId", 0,50,"[0-9]+" ) );
		cps.add(new CheckParamsModel("money", 0,20,"-[0-9]+" ) );
		cps.add(new CheckParamsModel("payway", 1,20,"" ) );
		cps.add(new CheckParamsModel("content", 1,255,"" ) );
		cps.add(new CheckParamsModel("pwd", 1,50,"" ) );

		BackModel backModel = Util.checkParams(json, cps);
		if(backModel != null){
			return backModel;
		}

		//验证会员密码
		Integer memberId = json.getInteger("memberId");
		Member member = memberDao.getById(memberId);
		String pwd = json.getString("pwd");

		if(Util.checkPwd(pwd, member.getPwd() ) == false){
			return new BackModel("error","密码错误！");
		}

		//获取操作员信息
		User user = userService.getLoginUser(json);
		Integer merchantId = user.getMerchantId();
		Integer userId = user.getId();

		//设置操作员和商户信息
		Flow flow = JSONObject.toJavaObject(json, Flow.class);
		flow.setOperatorId(userId);
		flow.setMerchantId(merchantId);

		//消费
		flow.setType("2");
		String no = System.currentTimeMillis()+"";
		flow.setNo(no);

		//修改会员金额
		//加积分
		Integer integral = 0-flow.getMoney();
		//其他消费方式，不减扣余额
		Integer money = flow.getMoney();
		if( ! "1".equals(flow.getPayway() ) ){
			money = 0;
		}
		memberService.updateMoney(flow.getMemberId(), money, integral);

		//记录流水
		flowService.add(flow);

		return new BackModel("ok","消费成功！");

	}

	//退款
	@Transactional
	public BackModel refund(JSONObject json ) throws Exception {

		//验证请求参数
		List<CheckParamsModel> cps = new ArrayList<CheckParamsModel>();
		cps.add(new CheckParamsModel("memberId", 0,50,"" ) );
		cps.add(new CheckParamsModel("money", 0,255,"-[0-9]+" ) );
		cps.add(new CheckParamsModel("payway", 1,20,"" ) );
		cps.add(new CheckParamsModel("content", 1,20,"" ) );
		cps.add(new CheckParamsModel("pwd", 1,50,"" ) );

		BackModel backModel = Util.checkParams(json, cps);
		if(backModel != null){
			return backModel;
		}

		//验证会员密码
		Integer memberId = json.getInteger("memberId");
		Member member = memberDao.getById(memberId);
		String pwd = json.getString("pwd");

		if(Util.checkPwd(pwd, member.getPwd() ) == false){
			return new BackModel("error","密码错误！");
		}

		//获取操作员信息
		User user = userService.getLoginUser(json);
		Integer merchantId = user.getMerchantId();
		Integer userId = user.getId();

		//设置操作员和商户信息
		Flow flow = JSONObject.toJavaObject(json, Flow.class);
		flow.setOperatorId(userId);
		flow.setMerchantId(merchantId);

		//退款
		flow.setType("3");
		String no = System.currentTimeMillis()+"";
		flow.setNo(no);

		//修改会员金额
		//积分不变
		memberService.updateMoney(flow.getMemberId(), flow.getMoney(), 0);

		//记录流水
		flowService.add(flow);

		return new BackModel("ok","退款成功！");

	}

	//积分兑换
	@Transactional
	public BackModel integralToMoney(JSONObject json ) throws Exception {

		//验证请求参数
		List<CheckParamsModel> cps = new ArrayList<CheckParamsModel>();
		cps.add(new CheckParamsModel("memberId", 0,50,"" ) );
		cps.add(new CheckParamsModel("money", 0,255,"[0-9]+" ) );
		cps.add(new CheckParamsModel("payway", 1,20,"" ) );
		cps.add(new CheckParamsModel("content", 1,20,"" ) );
		cps.add(new CheckParamsModel("pwd", 1,50,"" ) );

		BackModel backModel = Util.checkParams(json, cps);
		if(backModel != null){
			return backModel;
		}

		//验证会员密码
		Integer memberId = json.getInteger("memberId");
		Member member = memberDao.getById(memberId);
		String pwd = json.getString("pwd");

		if(Util.checkPwd(pwd, member.getPwd() ) == false){
			return new BackModel("error","密码错误！");
		}

		//获取操作员信息
		User user = userService.getLoginUser(json);
		Integer merchantId = user.getMerchantId();
		Integer userId = user.getId();

		//设置操作员和商户信息
		Flow flow = JSONObject.toJavaObject(json, Flow.class);
		flow.setOperatorId(userId);
		flow.setMerchantId(merchantId);

		//退款
		flow.setType("3");
		String no = System.currentTimeMillis()+"";
		flow.setNo(no);

		//修改会员金额
		//积分兑换规则： 10 积分 = 1 块钱
		int integral = 0 - flow.getMoney() * 10;

		//修改余额
		memberService.updateMoney(flow.getMemberId(), flow.getMoney(), integral);

		//记录流水
		flowService.add(flow);

		return new BackModel("ok","兑换成功！");

	}


}