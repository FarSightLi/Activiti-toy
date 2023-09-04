//package com.farsight.activititoy;
//
//
//import org.activiti.engine.*;
//import org.activiti.engine.history.HistoricTaskInstance;
//import org.activiti.engine.history.HistoricVariableInstance;
//import org.activiti.engine.repository.Deployment;
//import org.activiti.engine.task.Task;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//class ActivitiToyApplicationTests {
//
//    @Autowired
//    private RuntimeService runtimeService;
//    @Autowired
//    private TaskService taskService;
//    @Autowired
//    private RepositoryService repositoryService;
////    @Autowired
////    private SecurityUtil securityUtil;
//    @Autowired
//    private HistoryService historyService;
//
//
//    /**
//     * 根据key自动删除所有部署的流程
//     */
//    @Test
//    public void delete1() {
//        List<Deployment> deployment = repositoryService.createDeploymentQuery()
//                .deploymentKey("FormTest2")
//                .list();
//        for (Deployment deployment1 : deployment) {
//            repositoryService.deleteDeployment(deployment1.getId(), true);
//        }
//        System.out.println("成功删除");
//    }
//
//    /**
//     * 根据id删除部署的流程
//     */
//    @Test
//    public void delete2() {
//        repositoryService.deleteDeployment("9ea64610-2ded-11ee-bfec-005056c00001", true);
//    }
//
//    /**
//     * 部署一个流程
//     */
//    @Test
//    public void development() {
//        String fileName = "FormTest2";
//        String resourceName = "processes/" +fileName+ ".bpmn20.xml";
//        //1.获取ProcessEngine对象
//        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
//        //2.获取用于部署的RepositoryService
//        RepositoryService repositoryService = engine.getRepositoryService();
//        //3.执行部署
//        Deployment deploy = repositoryService.createDeployment()
//                .key("FormTest2")
//                //添加资源
//                .addClasspathResource(resourceName)
//                .deploy();
//        System.out.println("流程部署的id:" + deploy.getId() + "\n" + "流程部署的key:" + deploy.getKey() + "\n" + "流程name:" + deploy.getName());
//    }
//
//    /**
//     * FormTest2的测试
//     * 开始一个流程实例,并递交给上级
//     */
//    @Test
//    public void test2Start() {
//        String userNameA = "jack";
//        securityUtil.logInAs(userNameA);
//        Map<String, Object> map = new HashMap<>();
//        map.put("apply", userNameA);
//        map.put("days", "77");
//        map.put("reason", "must gogogo SHANGHAI");
//        map.put("approve", "rose");
//
//        runtimeService.startProcessInstanceByKey("FormTest2", map);
//        System.out.println("流程启动，" + userNameA + "发起了一个申请");
//        // 查询用户A的任务
//        List<Task> tasks = taskService.createTaskQuery()
//                .taskAssignee(userNameA)
//                .list();
//
//        for (Task task : tasks) {
//            System.out.println("formKey :" + task.getFormKey());
//            taskService.complete(task.getId()); // 完成用户A的任务，流程将流转到用户B
//            System.out.println(userNameA + "完成了任务：" + task.getName());
//        }
//    }
//
//    /**
//     * 上级审批
//     */
//    @Test
//    public void approve() {
//        String userNameB = "rose";
//        securityUtil.logInAs(userNameB);
//        List<Task> tasks = taskService.createTaskQuery()
//                .taskAssignee(userNameB)
//                .list();
//        if (tasks != null && !tasks.isEmpty()) {
//            for (Task task : tasks) {
//                Map<String, Object> values = taskService.getVariables(task.getId());
//                System.out.println("------查看申请人的请假表单--------");
//                values.forEach((k, v) -> {
//                    System.out.println(k + ":" + v);
//                });
//                System.out.println("formKey :" + task.getFormKey());
//                Map<String, Object> variables = new HashMap<>();
//                variables.put("isApprove", "true");
//                variables.put("ApproveReason", "完全OK");
//                taskService.complete(task.getId(), variables);
//                System.out.println("申请已通过");
//            }
//        }
//    }
//
//    /**
//     * 查看历史信息(api测试,功能未结合业务)
//     */
//    @Test
//    public void history() {
//        List<HistoricTaskInstance> taskInstances = historyService.createHistoricTaskInstanceQuery()
//                .taskAssignee("rose").finished().list();
//        for (HistoricTaskInstance taskInstance : taskInstances) {
//            String taskId = taskInstance.getId();
//            String processInstanceId = taskInstance.getProcessInstanceId();
//            // 查询任务相关的表单变量
//            List<HistoricVariableInstance> formVariables = historyService.createHistoricVariableInstanceQuery()
//                    .processInstanceId(processInstanceId)
//                    .taskId(taskId)
//                    .list();
//            for (HistoricVariableInstance formVariable : formVariables) {
//                System.out.println("create time:::" + formVariable.getCreateTime());
//                System.out.println("form name:::" + formVariable.getVariableName());
//                System.out.println("form value:::" + formVariable.getValue());
//                System.out.println("-----------");
//            }
//        }
//    }
//
//}
