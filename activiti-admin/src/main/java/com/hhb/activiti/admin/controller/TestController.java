package com.hhb.activiti.admin.controller;

import com.hhb.activiti.admin.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huanghebin
 * @date 2020/9/17 15:32
 */
@RestController
@RequestMapping("test")
public class TestController {

	@Autowired
	private TestService testService;

	@PostMapping("new")
	public void addProcess() {
		testService.addProcess();
	}

	@PostMapping("finish")
	public void finish() {
		testService.finish();
	}
}
