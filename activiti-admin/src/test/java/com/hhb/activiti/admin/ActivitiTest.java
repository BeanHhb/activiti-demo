package com.hhb.activiti.admin;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
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

	/**
	 * 部署bpmn文件
	 */
	@Test
	public void initDeployment() {
		String fileName = "bpmn/Test.bpmn";
		Deployment deployment = repositoryService.createDeployment()
				.addClasspathResource(fileName)
				.name("流程部署测试1")
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
		String pdID = "7077e788-fe7b-11ea-b082-5c3a4551df3d";
		// true：删除所有历史记录，false：只删除部署
		repositoryService.deleteDeployment(pdID, true);
	}
}
