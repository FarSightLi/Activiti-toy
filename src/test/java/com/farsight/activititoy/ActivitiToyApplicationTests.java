package com.farsight.activititoy;

import com.farsight.activititoy.uitl.SnowflakeIdGenerator;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
class ActivitiToyApplicationTests {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;

    /**
     * 启动一个流程,并添加参数
     */
    @Test
    public void test1() {
        Map<String, Object> map = new HashMap<>();
        //申请人
        map.put("formKey", SnowflakeIdGenerator.generateStringId());
        map.put("user", "user");
        map.put("numberOfDays", 60);
        map.put("startDate", "2022-1-1");
        map.put("vacationMotivation", "must gogogo");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("FormTest", map);
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getProcessDefinitionKey());

    }

    @Test
    public void test2() {
        List<Task> tasks = taskService//与任务相关的Service
                .createTaskQuery()//创建一个任务查询对象
                .taskAssignee("user")
                .list();
        if (tasks != null && !tasks.isEmpty()) {
            for (Task task : tasks) {
                Map<String, Object> variables = taskService.getVariables(task.getId());
                variables.forEach(System.out::printf);
                System.out.println(task);
                System.out.println("formKey:" + task.getFormKey());
                BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
                bpmnModel.getFlowElement(task.getFormKey());
            }
        }
    }

    @Test
    public void delete1() {
        Deployment deployment = repositoryService.createDeploymentQuery()
                .deploymentKey("FormTest")
                .singleResult();
        repositoryService.deleteDeployment(deployment.getId(), true);
        System.out.println("成功删除");
    }

    @Test
    public void delete2() {
        repositoryService.deleteDeployment("9ea64610-2ded-11ee-bfec-005056c00001", true);
    }

    @Test
    public void development() {
        String resourceName = "processes/" + "FormTest" + ".bpmn20.xml";
        //1.获取ProcessEngine对象
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        //2.获取用于部署的RepositoryService
        RepositoryService repositoryService = engine.getRepositoryService();
        //3.执行部署
        Deployment deploy = repositoryService.createDeployment()
                .key("FormTest")
                //添加资源
                .addClasspathResource(resourceName)
                .deploy();
        System.out.println("流程部署的id:" + deploy.getId() + "\n" + "流程部署的key:" + deploy.getKey() + "\n" + "流程name:" + deploy.getName());
    }

    public void demo() {
    }
}
