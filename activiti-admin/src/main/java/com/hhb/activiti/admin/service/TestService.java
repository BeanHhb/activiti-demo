package com.hhb.activiti.admin.service;

import com.hhb.activiti.admin.utils.SecurityUtil;
import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author huanghebin
 * @date 2020/9/17 15:34
 */
@Service
public class TestService {

	private final Logger log = LogManager.getLogger(getClass());

	@Autowired
	private ProcessRuntime processRuntime;

	@Autowired
	private TaskRuntime taskRuntime;

	@Autowired
	private SecurityUtil securityUtil;

	public void addProcess() {

		Page<ProcessDefinition> processDefinitionPage = processRuntime.processDefinitions(Pageable.of(0, 10));
		log.info("可用的流程定义数量：" + processDefinitionPage.getTotalItems());
		for (ProcessDefinition pd : processDefinitionPage.getContent()) {
			log.info("流程定义：" + pd);
		}

		ProcessInstance pi = processRuntime.start(ProcessPayloadBuilder
				.start()
				.withProcessDefinitionKey("myProcess_1")
				.build());
		log.info("流程实例 ID：" + pi.getId());
	}

	public void finish() {
		securityUtil.logInAs("salaboy");
		Page<Task> taskPage = taskRuntime.tasks(Pageable.of(0, 10));
		if (taskPage.getTotalItems() > 0) {
			for (Task task : taskPage.getContent()) {
				// 拾取任务
				taskRuntime.claim(TaskPayloadBuilder
						.claim()
						.withTaskId(task.getId())
						.build());
				log.info("任务：" + task);
				// 完成任务
				taskRuntime.complete(TaskPayloadBuilder
						.complete()
						.withTaskId(task.getId())
						.build());
			}
		}

		Page<Task> taskPage2 = taskRuntime.tasks(Pageable.of(0, 10));
		log.info("剩余任务数；" + taskPage2.getTotalItems());
	}
}
