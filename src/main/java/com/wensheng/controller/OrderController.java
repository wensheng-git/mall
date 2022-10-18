package com.wensheng.controller;


import com.github.pagehelper.PageInfo;
import com.wensheng.consts.MallConst;
import com.wensheng.entity.MallUser;
import com.wensheng.entity.formEntity.OrderCreateForm;
import com.wensheng.resposeVo.OrderVo;
import com.wensheng.resposeVo.ResponseVo;
import com.wensheng.service.MallOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
public class OrderController {

	@Autowired
	private MallOrderService orderService;

	@PostMapping("/orders")
	public ResponseVo<OrderVo> create(@Valid @RequestBody OrderCreateForm form,
									  HttpSession session) {
		MallUser user = (MallUser) session.getAttribute(MallConst.CURRENT_USER);
		return orderService.create(user.getId(), form.getShippingId());
	}

	@GetMapping("/orders")
	public ResponseVo<PageInfo> list(@RequestParam Integer pageNum,
									 @RequestParam Integer pageSize,
									 HttpSession session) {
		MallUser user = (MallUser) session.getAttribute(MallConst.CURRENT_USER);
		return orderService.list(user.getId(), pageNum, pageSize);
	}

	@GetMapping("/orders/{orderNo}")
	public ResponseVo<OrderVo> detail(@PathVariable Long orderNo,
									  HttpSession session) {
		MallUser user = (MallUser) session.getAttribute(MallConst.CURRENT_USER);
		return orderService.detail(user.getId(), orderNo);
	}

	@PutMapping("/orders/{orderNo}")
	public ResponseVo cancel(@PathVariable Long orderNo,
							 HttpSession session) {
		MallUser user = (MallUser) session.getAttribute(MallConst.CURRENT_USER);
		return orderService.cancel(user.getId(), orderNo);
	}
}
