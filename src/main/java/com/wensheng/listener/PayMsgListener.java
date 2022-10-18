package com.wensheng.listener;

import com.google.gson.Gson;
import com.wensheng.entity.MallPayInfo;
import com.wensheng.service.MallOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "payNotify")
@Slf4j
public class PayMsgListener {

	@Autowired
	private MallOrderService orderService;

	@RabbitHandler
	public void process(String msg) {
		log.info("【接收到消息】=> {}", msg);

		MallPayInfo payInfo = new Gson().fromJson(msg, MallPayInfo.class);
		if (payInfo.getPlatformStatus().equals("SUCCESS")) {
			//修改订单里的状态
			orderService.paid(payInfo.getOrderNo());
		}
	}
}
