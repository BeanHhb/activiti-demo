package com.hhb.activiti.admin;

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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author huanghebin
 * @date 2020/9/17 10:42
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiTest {

	@Autowired
	private ProcessRuntime processRuntime;
	@Autowired
	private TaskRuntime taskRuntime;
	@Autowired
	private SecurityUtil securityUtil;

	@Test
	public void contextLoads() {
		securityUtil.logInAs("salaboy");
		Page<ProcessDefinition> processDefinitionPage = processRuntime.processDefinitions(Pageable.of(0, 10));
		System.out.println("可用的流程定义数量：" + processDefinitionPage.getTotalItems());
		for (ProcessDefinition pd : processDefinitionPage.getContent()) {
			System.out.println("流程定义：" + pd);
		}
	}

	@Test
	public void testStartProcess() {
//		securityUtil.logInAs("salaboy");
		ProcessInstance pi = processRuntime
				.start(ProcessPayloadBuilder
						.start()
						.withProcessDefinitionKey("myProcess_1")
						.build());
		System.out.println("流程实例 ID：" + pi.getId());
	}

	@Test
	public void testTask() {
		securityUtil.logInAs("ryandawsonuk");
		Page<Task> taskPage = taskRuntime.tasks(Pageable.of(0, 10));
		if (taskPage.getTotalItems() > 0) {
			for (Task task : taskPage.getContent()) {
//				// 拾取任务
//				taskRuntime.claim(TaskPayloadBuilder
//						.claim()
//						.withTaskId(task.getId())
//						.build());
//				System.out.println("任务：" + task);

				// 完成任务
				taskRuntime.complete(TaskPayloadBuilder
						.complete()
						.withTaskId(task.getId())
						.build());
			}
		}

		Page<Task> taskPage2 = taskRuntime.tasks(Pageable.of(0, 10));
		System.out.println(taskPage2.getTotalItems());
	}
}
