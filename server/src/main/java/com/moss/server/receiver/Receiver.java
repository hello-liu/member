package com.moss.server.receiver;

import com.alibaba.fastjson.JSONObject;
import com.moss.server.dao.LogDao;
import com.moss.server.model.Log;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

	@Autowired
	LogDao logDao;

	@RabbitListener(queues = "moss.queue.log")
	public void processLog(byte[] msg) {
		//log.#，记录所有日志信息
		String logStr = new String(msg) ;
		JSONObject json = JSONObject.parseObject (logStr);
		Log log = new Log();
		log.setToken(json.getString("token") );
		log.setUserIp(json.getString("userIp") );
		log.setJson(json.getString("json") );
		log.setResult(json.getString("result") );
		log.setUseTime(json.getInteger("useTime") );
		log.setMethod(json.getString("method") );
		logDao.add(log);
	}
}