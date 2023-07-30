package com.farsight.activititoy.controller;

import com.farsight.activititoy.entity.ToyBusinessEntity;
import com.farsight.activititoy.service.impl.ToyBusinessServiceImp;
import com.farsight.activititoy.uitl.SnowflakeIdGenerator;
import org.activiti.bpmn.model.FormProperty;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.*;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.TaskServiceImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ToyBusinessServiceImp toyBusinessService;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    /**
     * 将一个文件部署为流程
     *
     * @param fileName
     */
    @GetMapping("/development")
    public String development(String fileName, String key) {
        String resourceName = "processes/" + fileName + ".bpmn20.xml";
        //1.获取ProcessEngine对象
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        //2.获取用于部署的RepositoryService
        RepositoryService repositoryService = engine.getRepositoryService();
        //3.执行部署
        Deployment deploy = repositoryService.createDeployment()
                .key(key)
                //添加资源
                .addClasspathResource(resourceName)
                .deploy();
        return "流程部署的id:" + deploy.getId() + "\n" + "流程部署的key:" + deploy.getKey() + "\n" + "流程name:" + deploy.getName();
    }

    /**
     * 启动一个流程，在第一个节点创建一个任务,并将任务流转到下一个节点
     *
     * @param apply
     * @return
     */
    @GetMapping("/create")
    public String createMask(String apply, String reason, Integer days, String approve) {
        Map<String, Object> map = new HashMap<>();
        //申请人
        map.put("apply", apply);
        //审批人
        map.put("approve", approve);
        ToyBusinessEntity toyBusiness = new ToyBusinessEntity();
        toyBusiness.setDays(days);
        toyBusiness.setReason(reason);
        // 生成唯一ID
        String businessId = SnowflakeIdGenerator.generateStringId();
        toyBusiness.setId(businessId);
        //将请假申请存入数据库
        toyBusinessService.save(toyBusiness);
        //流程启动,任务创建
        runtimeService.startProcessInstanceByKey("toy", businessId, map);
        //当前任务办理人
        String assignee = apply;
        //查询申请人的任务
        Task task = taskService
                .createTaskQuery()
                .taskAssignee(assignee)
                .singleResult();
        task.getFormKey();
        //将任务提交给审批人
        taskService.complete(task.getId());
        return "任务创建成功，已经成功提交给" + approve;
    }

    /**
     * 审核
     *
     * @param approve
     * @param opinion
     * @return
     */
    @GetMapping("/examine")
    public String examine(String approve, Boolean opinion) {
        Map<String, Object> map = new HashMap<>();
        map.put("pass", opinion);
        String assignee = approve;//当前任务办理人
        List<Task> tasks = taskService//与任务相关的Service
                .createTaskQuery()//创建一个任务查询对象
                .taskAssignee(assignee)
                .list();
        if (tasks != null && !tasks.isEmpty()) {
            for (org.activiti.engine.task.Task task : tasks) {
                System.out.println("-----接下来开始完成任务-----");
                System.out.println("fromkey is:" + task.getFormKey());
                taskService.complete(task.getId(), map);
                System.out.println("-----已经完成任务：" + task.getId() + "-----");
            }
        }
        return approve + "创建的任务已完成";
    }


    @GetMapping("/modify")
    public String modify(String apply, String reason, Integer days) {
        runtimeService.startProcessInstanceByKey("test");
        Task task = taskService.createTaskQuery().taskAssignee(apply).taskDefinitionKey("modify").singleResult();
        //得到业务主键
        String businessKey = task.getBusinessKey();
        //更新请假业务
        ToyBusinessEntity toyBusinessEntity = toyBusinessService.getById(businessKey);
        toyBusinessEntity.setReason(reason);
        toyBusinessEntity.setDays(days);
        toyBusinessService.updateById(toyBusinessEntity);
        taskService.complete(task.getId());
        return apply + "已修改任务并提交给审批人";
    }

    @GetMapping("/form1")
    public String testForm(String name, String id, String form) {
        Map<String, Object> map = new HashMap<>();
        //申请人
        map.put("apply", name);
        //审批人
        map.put("examination", "examination");
        map.put("form", form);
        runtimeService.startProcessInstanceByKey("test", map);
        List<Task> list = taskService.createTaskQuery().taskAssignee(name).list();
        for (Task task : list) {
            System.out.println("from key is :" + task.getFormKey());
            UserTask userTask = (UserTask) repositoryService.getBpmnModel(task.getProcessDefinitionId()).getFlowElement(task.getFormKey());
            if (userTask != null) {
                List<FormProperty> formProperties = userTask.getFormProperties();
                formProperties.forEach(System.out::println);
            }
        }
        return "OK";
    }
}
