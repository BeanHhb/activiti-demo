package com.hhb.activiti.admin;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author huanghebin
 * @date 2020/9/17 10:42
 */
@SpringBootTest
public class ActivitiTest {

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private HistoryService historyService;

	/**
	 * 部署bpmn文件
	 */
	@Test
	public void initDeployment() {
		String fileName = "bpmn/Test2.bpmn";
		Deployment deployment = repositoryService.createDeployment()
				.addClasspathResource(fileName)
				.name("流程候选人测试")
				.deploy();
		System.out.println(deployment.getName());
	}

	/**
	 * 查询流程部署
	 */
	@Test
	public void getDeployment() {
		List<Deployment> list = repositoryService.createDeploymentQuery().list();
		list.forEach(deployment -> {
			System.out.println(deployment.getName());
			System.out.println(deployment.getCategory());
			System.out.println(deployment.getId());
			System.out.println(deployment.getDeploymentTime());
			System.out.println("------------");
		});
	}

	/**
	 * 查询流程定义
	 */
	@Test
	public void getDefinitions() {
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
		list.forEach(definition -> {
			System.out.println(definition.getName());
			System.out.println(definition.getId());
			System.out.println(definition.getDeploymentId());
			System.out.println(definition.getResourceName());
			System.out.println(definition.getKey());
		});
	}

	/**
	 * 删除部署
	 */
	@Test
	public void deleteDeployment() {
		String pdID = "32f7f4cb-fe49-11ea-afef-105bad8fe669";
		// true：删除所有历史记录，false：只删除部署
		repositoryService.deleteDeployment(pdID, true);
	}

	/**
	 * 初始化流程实例
	 */
	@Test
	public void initProcessInstance() {
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("myProcess_2", "businessKey_2");
		System.out.println(pi.getProcessDefinitionId());
		System.out.println(pi.getName());
	}

	/**
	 * 获取流程实例列表
	 */
	@Test
	public void getProcessInstance() {
		List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().list();
		list.forEach(pi -> {
			System.out.println(pi.getProcessInstanceId());
			System.out.println(pi.getProcessDefinitionId());
			System.out.println(pi.isEnded());
			System.out.println(pi.isSuspended());
		});
	}

	/**
	 * 挂起、激活流程实例
	 */
	@Test
	public void activeProcessInstance() {
		// 挂起
//		runtimeService.suspendProcessInstanceById("73a834dd-fecf-11ea-b7bd-105bad8fe669");
		// 激活
		runtimeService.activateProcessInstanceById("73a834dd-fecf-11ea-b7bd-105bad8fe669");
	}

	/**
	 * 删除流程实例
	 */
	@Test
	public void deleteProcessInstance() {
		runtimeService.deleteProcessInstance("73a834dd-fecf-11ea-b7bd-105bad8fe669", "爱删不删");
	}

	/**
	 * 查询任务列表
	 */
	@Test
	public void getTasks() {
		List<Task> list = taskService.createTaskQuery().list();
		list.forEach(task -> {
			System.out.println(task.getId());
			System.out.println(task.getName());
			System.out.println(task.getAssignee());
		});
	}

	@Test
	public void getTaskByAssignee() {
		List<Task> list = taskService.createTaskQuery().taskAssignee("wukong").list();
		list.forEach(task -> {
			System.out.println(task.getId());
			System.out.println(task.getName());
			System.out.println(task.getAssignee());
		});
	}

	/**
	 * 执行任务
	 */
	@Test
	public void completeTask() {
		taskService.complete("fa718cdf-ff0b-11ea-8177-105bad8fe669");
	}

	/**
	 * 拾取任务
	 */
	@Test
	public void claimTask() {
		Task task = taskService.createTaskQuery().taskId("fa718cdf-ff0b-11ea-8177-105bad8fe669").singleResult();
		taskService.claim(task.getId(), "wukong");
	}

	/**
	 * 归还、指派任务
	 */
	@Test
	public void returnTask() {
//		taskService.setAssignee("fa718cdf-ff0b-11ea-8177-105bad8fe669", null);
		taskService.setAssignee("fa718cdf-ff0b-11ea-8177-105bad8fe669", "wukong");
	}

	/**
	 * 查看历史
	 */
	@Test
	public void history() {
		List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
				.orderByHistoricTaskInstanceEndTime().asc()
//				.taskAssignee("bajie")
				.processInstanceId("0cf2f91b-ff09-11ea-8ff3-105bad8fe669")
				.list();
		list.forEach(instance -> {
			System.out.println(instance.getId());
			System.out.println(instance.getProcessInstanceId());
			System.out.println(instance.getProcessDefinitionId());
			System.out.println(instance.getName());
		});
	}

	/**
	 * 并行 Part1
	 */
	@Test
	public void parallel() {
		String fileName = "bpmn/Parallel.bpmn";
		Deployment deployment = repositoryService.createDeployment()
				.addClasspathResource(fileName)
				.name("并行网关测试")
				.deploy();
		System.out.println(deployment.getName());
		System.out.println("--------------------");
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("myProcess_parallel", "businessKey_parallel");
		System.out.println(pi.getProcessDefinitionId());
		System.out.println(pi.getName());
		System.out.println("--------------------");
		List<Task> list = taskService.createTaskQuery().taskAssignee("bajie").list();
		list.forEach(task -> {
			System.out.println(task.getId());
			System.out.println(task.getName());
			System.out.println(task.getAssignee());
			System.out.println("--------------------");
			taskService.complete(task.getId());
		});
	}

	@Test
	public void parallel2() {
		List<Task> list1 = taskService.createTaskQuery().taskAssignee("wukong").list();
		List<Task> list2 = taskService.createTaskQuery().taskAssignee("tangseng").list();
		list1.forEach(task -> {
			System.out.println(task.getProcessInstanceId());
			System.out.println(task.getId());
			System.out.println(task.getName());
			System.out.println(task.getAssignee());
//			taskService.complete(task.getId());
		});
		System.out.println("-----------");
		list2.forEach(task -> {
			System.out.println(task.getProcessInstanceId());
			System.out.println(task.getId());
			System.out.println(task.getName());
			System.out.println(task.getAssignee());
			taskService.complete(task.getId());
		});
	}
}
