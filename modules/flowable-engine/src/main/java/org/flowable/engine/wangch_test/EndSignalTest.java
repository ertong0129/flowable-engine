package org.flowable.engine.wangch_test;


import org.flowable.engine.*;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;

import java.util.HashMap;
import java.util.Map;

/**
 * @author he.ai aihehe123@gmail.com
 * 使用场景：
 * 功能描述：
 */
public class EndSignalTest {

    public static void main(String[] args) throws InterruptedException {

        ProcessEngineConfiguration cfg = (ProcessEngineConfiguration) new StandaloneProcessEngineConfiguration()
                .setJdbcPassword("")
                .setJdbcUrl("jdbc:mysql://127.0.0.1:3306/flowable_engine_study?autoReconnect=true&useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8")
                .setJdbcUsername("root")
                .setJdbcPassword("123456")
                .setJdbcDriver("com.mysql.jdbc.Driver")
                .setAsyncExecutorActivate(true);
        //.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        ProcessEngine processEngine = cfg.buildProcessEngine();

        // 部署流程
        RepositoryService repositoryService = processEngine.getRepositoryService();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ManagementService managementService = processEngine.getManagementService();
        TaskService taskService = processEngine.getTaskService();

        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("test_signal_end_event.bpmn20.xml")
                .deploy();

        String deploymentId = deployment.getId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploymentId)
                .singleResult();
        System.out.println("Found process definition : " + processDefinition.getName());

        Map<String, Object> variables = new HashMap<>();

        ProcessInstance processInstance =
                runtimeService.startProcessInstanceById(processDefinition.getId(), variables);

    }

}