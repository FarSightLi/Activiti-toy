**Activiti7入门指南**

**版本：1.0**

**最后更新时间：2023年7月29日** 

**作者：FarSightLi**

# 前言

欢迎阅读《Activiti7入门指南》！

在学习Activiti框架的过程中，你可能会发现国内一些教程过于肤浅，停留在API的使用和概念的介绍上，对新手不够友好。这不仅耗费时间，也增加了入门的难度。

另外，相较于之前的版本，Activiti7缺乏详细的接口文档，这给对接口功能有疑问的读者带来了困扰。更令人窒息的一点是，Activiti7的formService接口被移除了，而官方也没有提供明确的替代方案。这样的情况在Stack Overflow等社区上也未能找到解决方案。

因此，我决定编写这本《Activiti7入门指南》，旨在为您提供一个快速入门的途径。在本指南中，您将找到更实用、更深入的内容，帮助您快速掌握Activiti7的核心概念和实际应用。通过逐步引导，您将学会如何正确使用Activiti7，避免陷入困境，更好地应用于实际项目中。

在开始之前，我想强调我的水平有限，虽然尽我最大努力保证内容的准确性和完整性，但难免会存在一些可能会导致bug的地方，以及不符合开发规约的情况。在此，我真诚地希望读者能够理解并给予包容，如果您在阅读过程中发现任何问题或错误，还请不吝指正。您的宝贵意见和建议将对指南的完善和改进提供巨大的帮助。

我希望这本指南能够让你轻松入门，从而更好地应用Activiti7框架。

以下是我的入门思路：

1.首先通过画出一个流程图，形象地展示整个审批流程的各个环节和步骤。接着，使用代码来实现整个流程图的运行，体验工作流引擎的强大功能。

2.为了更好地满足实际业务的需求，我会逐步改进代码，并思考如何优化流程，确保它能够更准确、高效地反映您的具体业务场景。

在完成了这两步之后，相信你会对Activiti有更深刻的认识，此时你就可以根据实际业务慢慢探索更多功能（如更多网关、更多Task以及监听器等）

本指南的所有代码都可以在我的GitHub上找到，所有最新的改动都会更新到GitHub（[FarSightLi/activiti-toy: activiti的学习及项目应用 (github.com)](https://github.com/FarSightLi/activiti-toy)）



# 入门流程

## 前期准备

### 相关依赖引入

```xml
<dependency>
    <groupId>org.activiti</groupId>
    <artifactId>activiti-spring-boot-starter</artifactId>
    <version>7.1.0.M6</version>
    <exclusions>
        <exclusion>
            <artifactId>mybatis</artifactId>
            <groupId>org.mybatis</groupId>
        </exclusion>
    </exclusions>
</dependency>
<!-- 生成流程图-->
<dependency>
    <groupId>org.activiti</groupId>
    <artifactId>activiti-image-generator</artifactId>
    <version>7.1.0.M6</version>
    <exclusions>
        <exclusion>
            <artifactId>commons-io</artifactId>
            <groupId>commons-io</groupId>
        </exclusion>
    </exclusions>
</dependency>
```

### 工具类、配置类编写

```java
package com.farsight.activititoy.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class ActivitiConfig {
    private final Logger logger = LoggerFactory.getLogger(ActivitiConfig.class);

    @Bean
    public UserDetailsService myUserDetailsService() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        //这里添加用户，后面处理流程时用到的任务负责人，需要添加在这里
        String[][] usersGroupsAndRoles = {
                {"jack", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
                {"rose", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
                {"tom", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
                {"other", "password", "ROLE_ACTIVITI_USER", "GROUP_otherTeam"},
                {"system", "password", "ROLE_ACTIVITI_USER"},
                {"lisi", "password", "ROLE_ACTIVITI_USER"},
                {"admin", "password", "ROLE_ACTIVITI_ADMIN"},
        };

        for (String[] user : usersGroupsAndRoles) {
            List<String> authoritiesStrings = Arrays.asList(Arrays.copyOfRange(user, 2, user.length));
            logger.info("> Registering new user: " + user[0] + " with the following Authorities[" + authoritiesStrings + "]");
            inMemoryUserDetailsManager.createUser(new User(user[0], passwordEncoder().encode(user[1]),
                    authoritiesStrings.stream().map(s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList())));
        }

        return inMemoryUserDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
```

```java
package com.farsight.activititoy.uitl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SecurityUtil {
    private final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    @Autowired
    @Qualifier("myUserDetailsService")
    private UserDetailsService userDetailsService;

    public void logInAs(String username) {
        UserDetails user = userDetailsService.loadUserByUsername(username);

        if (user == null) {
            throw new IllegalStateException("User " + username + " doesn't exist, please provide a valid user");
        }
        logger.info("> Logged in as: " + username);

        SecurityContextHolder.setContext(
                new SecurityContextImpl(
                        new Authentication() {
                            @Override
                            public Collection<? extends GrantedAuthority> getAuthorities() {
                                return user.getAuthorities();
                            }

                            @Override
                            public Object getCredentials() {
                                return user.getPassword();
                            }

                            @Override
                            public Object getDetails() {
                                return user;
                            }

                            @Override
                            public Object getPrincipal() {
                                return user;
                            }

                            @Override
                            public boolean isAuthenticated() {
                                return true;
                            }

                            @Override
                            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
                            }

                            @Override
                            public String getName() {
                                return user.getUsername();
                            }
                        }));
        org.activiti.engine.impl.identity.Authentication.setAuthenticatedUserId(username);
    }
}
```



## 工作流初步使用

### 流程图创建

流程图就是用一定规范描述的业务流程是如何流转的一种文件，在本例中，为了简化演示，只会出现USerTask、Exclusive Gateway（互斥网关）、普通的StartEvent（标志着工作流的开始）和EndEvent（结束工作流）

![image-20230722141737866](/./md-img/image-20230722141737866.png)

![image-20230722141943250](/./md-img/image-20230722141943250.png)

在空白处右键点击即可添加组件

![image-20230722142049771](/./md-img/image-20230722142049771.png)

可以在下面的框里填写id、name、assignee（指定该任务的负责人）

使用${}占位符的原因是：在业务流程中动态指定负责人

![image-20230722142333788](/./md-img/image-20230722142333788.png)

![image-20230722142400695](/./md-img/image-20230722142400695.png)

![image-20230722142913431](/./md-img/image-20230722142913431.png)

点击右上角可以拖动箭头将各个任务关联起来

![image-20230722143007225](/./md-img/image-20230722143007225.png)

![image-20230722143222106](/./md-img/image-20230722143222106.png)

点击gateway可以编辑Condition expression，里面是一个布尔表达式，决定了流程如何流转

![image-20230722144014327](/./md-img/image-20230722144014327.png)

![image-20230722144616381](/./md-img/image-20230722144616381.png)

对应的BPMN.XML文件如下

```xml
<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:activiti="http://activiti.org/bpmn" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" typeLanguage="http://www.w3.org/2001/XMLSchema" expressionLanguage="http://www.w3.org/1999/XPath" targetNamespace="http://www.activiti.org/processdef">
  <process id="toy" name="toy" isExecutable="true">
    <startEvent id="sid-10268166-f4ae-40ab-bb95-324e4a807c2e"/>
    <userTask id="application" name="提交请假申请" activiti:assignee="${apply}"/>
    <userTask id="examine" name="审核请假申请" activiti:assignee="${approve}"/>
    <userTask id="modify" name="修改请假申请" activiti:assignee="${apply}"/>
    <endEvent id="end"/>
    <sequenceFlow id="sid-e6d32532-f1d9-466f-8827-b9c8a49d3cfc" sourceRef="sid-10268166-f4ae-40ab-bb95-324e4a807c2e" targetRef="application"/>
    <sequenceFlow id="sid-7732ea48-2cc2-456e-a667-5de072e3a2ca" sourceRef="application" targetRef="examine"/>
    <sequenceFlow id="sid-faea8a95-a0b2-4139-bb93-bcc5ac7e3166" sourceRef="examine" targetRef="gateway"/>
    <sequenceFlow id="sid-dd005591-a794-45e1-a0e7-6d07af5303b2" sourceRef="modify" targetRef="examine"/>
    <sequenceFlow id="sid-90a9f383-7aa7-49ca-811b-80a4554eedb6" sourceRef="gateway" targetRef="modify" name="不同意，打回重写">
      <conditionExpression xsi:type="tFormalExpression"><![CDATA[${!pass}]]></conditionExpression>
    </sequenceFlow>
    <exclusiveGateway id="gateway" name="ExclusiveGateway"/>
    <sequenceFlow id="sid-9a3a6081-ee34-41b2-bcf5-6714eea597a3" sourceRef="gateway" targetRef="end" name="同意">
      <conditionExpression xsi:type="tFormalExpression"><![CDATA[${pass}]]></conditionExpression>
    </sequenceFlow>
  </process>
  <bpmndi:BPMNDiagram id="BPMNDiagram_toy">
    <bpmndi:BPMNPlane bpmnElement="toy" id="BPMNPlane_toy">
      <bpmndi:BPMNShape id="shape-ac2e6f8f-ce26-4577-a3bc-84be92ef452f" bpmnElement="sid-10268166-f4ae-40ab-bb95-324e4a807c2e">
        <omgdc:Bounds x="55.0" y="70.0" width="30.0" height="30.0"/>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape id="shape-b565e144-df28-4426-aa88-adc77d544855" bpmnElement="application">
        <omgdc:Bounds x="140.0" y="45.0" width="100.0" height="80.0"/>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape id="shape-01642c7d-3cd5-4c9d-b2d8-0fbc08c5d214" bpmnElement="examine">
        <omgdc:Bounds x="285.00006" y="45.0" width="100.0" height="80.0"/>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape id="shape-a744f880-b15d-4cb6-8ff3-521b12283ba4" bpmnElement="modify">
        <omgdc:Bounds x="285.00003" y="180.0" width="100.0" height="80.0"/>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape id="shape-01d0e239-6b8c-4372-a528-14755f7c569d" bpmnElement="end">
        <omgdc:Bounds x="550.0" y="70.0" width="30.0" height="30.0"/>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNEdge id="edge-54c43b22-e57a-4914-8023-bfa1ed957de1" bpmnElement="sid-e6d32532-f1d9-466f-8827-b9c8a49d3cfc">
        <omgdi:waypoint x="85.0" y="85.00001"/>
        <omgdi:waypoint x="140.0" y="85.0"/>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge id="edge-1a8f3cb9-b9c3-4060-9be3-c888aeb7042d" bpmnElement="sid-7732ea48-2cc2-456e-a667-5de072e3a2ca">
        <omgdi:waypoint x="240.0" y="85.0"/>
        <omgdi:waypoint x="262.5" y="85.0"/>
        <omgdi:waypoint x="285.00006" y="85.0"/>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge id="edge-b2f623e3-ac1d-45d2-9220-a778afc7a027" bpmnElement="sid-faea8a95-a0b2-4139-bb93-bcc5ac7e3166">
        <omgdi:waypoint x="385.00006" y="85.0"/>
        <omgdi:waypoint x="415.00003" y="85.0"/>
        <omgdi:waypoint x="435.0" y="85.0"/>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge id="edge-bffe7bc2-c5aa-460e-a409-4c7ad8d69f1e" bpmnElement="sid-dd005591-a794-45e1-a0e7-6d07af5303b2">
        <omgdi:waypoint x="335.00003" y="180.0"/>
        <omgdi:waypoint x="335.00006" y="125.0"/>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge id="edge-0ce7c582-2b5a-4bfa-a667-5a0988959de7" bpmnElement="sid-90a9f383-7aa7-49ca-811b-80a4554eedb6">
        <omgdi:waypoint x="455.0" y="104.99999"/>
        <omgdi:waypoint x="455.0" y="220.0"/>
        <omgdi:waypoint x="385.00003" y="220.0"/>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNShape id="shape-2fc64d64-b856-42b2-8288-6d32a3b63ead" bpmnElement="sid-41b85451-8383-4a70-8602-52ff0dab60ef">
        <omgdc:Bounds x="435.0" y="65.0" width="40.0" height="40.0"/>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape id="shape-5c56b2a1-f85c-4a9b-8516-5fe480bcd7a8" bpmnElement="gateway">
        <omgdc:Bounds x="435.0" y="65.0" width="40.0" height="40.0"/>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNEdge id="edge-1986c3dc-f843-4381-8969-b25b99ed0946" bpmnElement="sid-9a3a6081-ee34-41b2-bcf5-6714eea597a3">
        <omgdi:waypoint x="475.0" y="85.0"/>
        <omgdi:waypoint x="550.0" y="85.0"/>
      </bpmndi:BPMNEdge>
    </bpmndi:BPMNPlane>
  </bpmndi:BPMNDiagram>
</definitions>
```

这个流程图想要表达的是：

请假人提交一个请假申请，交给上级审批，如果审批通过，则事件结束，如果审批没通过，则打回申请人修改，修改完成后再提交给上级审批

### 部署流程

现在编写好了流程图，那么activiti是如何知道我们是怎么定义的流程呢？就需要部署流程。

```java
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
```

![image-20230722171159438](/./md-img/image-20230722171159438.png)





### 启动实例（申请人提交申请）

流程部署好以后，要开始使用这个流程，就要启动一个流程实例。一个流程可以对应多个流程实例，就像类和对象的关系一样

在这个例子中，启动流程时将指定申请人和审批人，同时将请假天数、请假原因等业务内容存入数据库（用MyBatisPlus）并与用businessKey的方式该实例进行绑定。

```java
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
    //完成任务，将任务提交给审批人
    taskService.complete(task.getId());
    return "任务创建成功，已经成功提交给" + approve;
}
```

![image-20230722171225140](/./md-img/image-20230722171225140.png)

### 上级审批

```java
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
```

![image-20230722204720701](/./md-img/image-20230722204720701.png)

### 修改请假申请

```java
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
```

### 该流程存在的问题

自始至终该流程的请假理由、请假天数等业务信息都只与businessKey关联，如果后续需要增加一个”证明附件“的字段，需要对数据库进行操作已经大量的代码修改。显然是不符合开发规范的。同时，如果要对审批任务需要添加”审批意见“，则也只能耦合进businessKey对应的business里。耦合度太高显然也是不行的。

这仅仅只有2个Task，就已经如此复杂。如果在实际业务中有很多Task，那么将几乎不可能维护，因此需要更好的解决方案。

## 更合理的工作流使用方式

实际上，Activiti提供了Form这一功能。将Form与task绑定，即可由activiti维护这些业务信息

以请假流程为例，忽略了审批不通过的情况，专注对form的操作，给出以下流程和测试代码

### 流程图

由于idea插件的问题，建议只使用插件绘制大致流程，详细信息手动在xml文件里编写（如form相关内容）

![image-20230730162157957](/./md-img/image-20230730162157957.png)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:activiti="http://activiti.org/bpmn" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" typeLanguage="http://www.w3.org/2001/XMLSchema" expressionLanguage="http://www.w3.org/1999/XPath" targetNamespace="http://www.activiti.org/processdef">
  <process id="FormTest2" name="FormTest2" isExecutable="true">
    <startEvent id="sid-4eacf38a-db07-4d6c-b720-9a1122ce6d04"/>
    <userTask id="apply" name="apply" activiti:assignee="${apply}" activiti:formKey="applyForm">
      <extensionElements>
        <activiti:formField id="reason" label="请假理由" type="string" required="true"/>
        <activiti:formField id="days" label="请假天数" type="string" required="true"/>
      </extensionElements>
    </userTask>
    <sequenceFlow id="sid-95047ec2-2738-4f62-9c65-ff0d81896bb0" sourceRef="sid-4eacf38a-db07-4d6c-b720-9a1122ce6d04" targetRef="apply"/>
    <userTask id="approve" name="approve" activiti:assignee="${approve}" activiti:formKey="approveForm">
      <extensionElements>
        <activiti:formField id="isApprove" label="是否同意" type="string" required="true"/>
        <activiti:formField id="reason" label="理由" type="string" required="true"/>
      </extensionElements>
    </userTask>
    <sequenceFlow id="sid-4749a664-ff34-4d9a-bc5c-98e4dd5baab4" sourceRef="apply" targetRef="approve"/>
    <endEvent id="sid-e13715c1-5fb3-4198-920f-c7491367f2fe"/>
    <sequenceFlow id="sid-4f4ce7f8-5786-49f9-a58c-e4e31ebf244e" sourceRef="approve" targetRef="sid-e13715c1-5fb3-4198-920f-c7491367f2fe"/>
  </process>
  <bpmndi:BPMNDiagram id="BPMNDiagram_FormTest2">
    <bpmndi:BPMNPlane bpmnElement="FormTest2" id="BPMNPlane_FormTest2">
      <bpmndi:BPMNShape id="shape-2df686c9-830a-444a-9946-a9713ea59f64" bpmnElement="sid-4eacf38a-db07-4d6c-b720-9a1122ce6d04">
        <omgdc:Bounds x="-135.0" y="-20.0" width="30.0" height="30.0"/>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape id="shape-24e76973-5321-4272-aa08-acae17aa88f7" bpmnElement="apply">
        <omgdc:Bounds x="-50.0" y="-45.0" width="100.0" height="80.0"/>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNEdge id="edge-cb5255ea-8cd6-4ed0-b540-236594188e3d" bpmnElement="sid-95047ec2-2738-4f62-9c65-ff0d81896bb0">
        <omgdi:waypoint x="-105.0" y="-5.0"/>
        <omgdi:waypoint x="-50.0" y="-5.0"/>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNShape id="shape-e68e3707-f47f-4da1-b861-b5f9429fc21b" bpmnElement="approve">
        <omgdc:Bounds x="140.0" y="-45.0" width="100.0" height="80.0"/>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNEdge id="edge-3212ed86-80c5-4f07-a37e-a3b355034f6c" bpmnElement="sid-4749a664-ff34-4d9a-bc5c-98e4dd5baab4">
        <omgdi:waypoint x="50.0" y="-5.0"/>
        <omgdi:waypoint x="140.0" y="-5.0"/>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNShape id="shape-5c4adade-9898-4a03-95d1-2d315ad156c1" bpmnElement="sid-e13715c1-5fb3-4198-920f-c7491367f2fe">
        <omgdc:Bounds x="380.0" y="-20.0" width="30.0" height="30.0"/>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNEdge id="edge-b024d5a2-3a3c-4302-95a9-779ddfeeffe9" bpmnElement="sid-4f4ce7f8-5786-49f9-a58c-e4e31ebf244e">
        <omgdi:waypoint x="240.0" y="-5.0"/>
        <omgdi:waypoint x="380.0" y="-5.0"/>
      </bpmndi:BPMNEdge>
    </bpmndi:BPMNPlane>
  </bpmndi:BPMNDiagram>
</definitions>
```

### 部署流程

一样的，通过指定bpmn文件，将流程部署

```java
/**
 * 部署一个流程
 */
@Test
public void development() {
    String fileName = "FormTest2";
    String resourceName = "processes/" +fileName+ ".bpmn20.xml";
    //1.获取ProcessEngine对象
    ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
    //2.获取用于部署的RepositoryService
    RepositoryService repositoryService = engine.getRepositoryService();
    //3.执行部署
    Deployment deploy = repositoryService.createDeployment()
            .key("FormTest2")
            //添加资源
            .addClasspathResource(resourceName)
            .deploy();
    System.out.println("流程部署的id:" + deploy.getId() + "\n" + "流程部署的key:" + deploy.getKey() + "\n" + "流程name:" + deploy.getName());
}
```

## 启动实例

与上面的例子不同的是，在这里没有定义自己的business实例，而是直接使用form，由activiti维护

启动流程时，制定了申请人、审批人，同时写入了请假天数和请假理由，再将该任务完成，请假申请流转到上级

```java
/**
 * FormTest2的测试
 * 开始一个流程实例,并递交给上级
 */
@Test
public void test2Start() {
    String userNameA = "jack";
    securityUtil.logInAs(userNameA);
    Map<String, Object> map = new HashMap<>();
    map.put("apply", userNameA);
    //请假天数
    map.put("days", "77");
    //请假理由
    map.put("reason", "must gogogo SHANGHAI");
    map.put("approve", "rose");

    runtimeService.startProcessInstanceByKey("FormTest2", map);
    System.out.println("流程启动，" + userNameA + "发起了一个申请");
    // 查询用户A的任务
    List<Task> tasks = taskService.createTaskQuery()
            .taskAssignee(userNameA)
            .list();

    for (Task task : tasks) {
        System.out.println("formKey :" + task.getFormKey());
        taskService.complete(task.getId()); // 完成用户A的任务，流程将流转到用户B
        System.out.println(userNameA + "完成了任务：" + task.getName());
    }
}
```

### 上级审批

申请人递交申请后，上级可以查看申请人的请假表单，同时对请假进行审批

```java
/**
 * 上级审批
 */
@Test
public void approve() {
    String userNameB = "rose";
    securityUtil.logInAs(userNameB);
    List<Task> tasks = taskService.createTaskQuery()
            .taskAssignee(userNameB)
            .list();
    if (tasks != null && !tasks.isEmpty()) {
        for (Task task : tasks) {
            Map<String, Object> values = taskService.getVariables(task.getId());
            System.out.println("------查看申请人的请假表单--------");
            values.forEach((k, v) -> {
                System.out.println(k + ":" + v);
            });
            System.out.println("formKey :" + task.getFormKey());
            Map<String, Object> variables = new HashMap<>();
            //是否同意
            variables.put("isApprove", "true");
            //审批原因
            variables.put("ApproveReason", "完全OK");
            taskService.complete(task.getId(), variables);
            System.out.println("申请已通过");
        }
    }
}
```

# 使用过程中的一些坑

## idea插件

2019版本后插件可以使用Activiti BPMN visualizer（我用的是2021.2.2）

## activiti相关接口bean无法注入

使用springboot3.1.x时，activiti的taskRuntime等无法注入bean

降级到2.7.x后即可解决（同时java也从17降到了8）

## 相关表不会自动创建

1.手动创建

2.数据库url添加nullCatalogMeansCurrent=true，activiti的database-schema-update:改为true

```yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/activiti_toy?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true
activiti:
  database-schema-update: true
```

## arc_re_deployment自动添加数据

![image-20230718183517756](/./md-img/image-20230718183517756.png)

每次启动都会添加SpringAutoDeployment

解决方案：

```yaml
spring:
	activiti:
  		deployment-mode: never-fail
```

## 启动时报错

### processDeployedEventProducer bean创建失败

```bash
org.springframework.context.ApplicationContextException: Failed to start bean 'processDeployedEventProducer'; nested exception is java.lang.NullPointerException
	at org.springframework.context.support.DefaultLifecycleProcessor.doStart(DefaultLifecycleProcessor.java:181) ~[spring-context-5.3.26.jar:5.3.26]
	at org.springframework.context.support.DefaultLifecycleProcessor.access$200(DefaultLifecycleProcessor.java:54) ~[spring-context-5.3.26.jar:5.3.26]
	at org.springframework.context.support.DefaultLifecycleProcessor$LifecycleGroup.start(DefaultLifecycleProcessor.java:356) ~[spring-context-5.3.26.jar:5.3.26]
	at java.lang.Iterable.forEach(Iterable.java:75) ~[na:1.8.0_181]
	at org.springframework.context.support.DefaultLifecycleProcessor.startBeans(DefaultLifecycleProcessor.java:155) ~[spring-context-5.3.26.jar:5.3.26]
	at org.springframework.context.support.DefaultLifecycleProcessor.onRefresh(DefaultLifecycleProcessor.java:123) ~[spring-context-5.3.26.jar:5.3.26]
	at org.springframework.context.support.AbstractApplicationContext.finishRefresh(AbstractApplicationContext.java:937) ~[spring-context-5.3.26.jar:5.3.26]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:586) ~[spring-context-5.3.26.jar:5.3.26]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:147) ~[spring-boot-2.7.10.jar:2.7.10]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:731) [spring-boot-2.7.10.jar:2.7.10]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:408) [spring-boot-2.7.10.jar:2.7.10]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:307) [spring-boot-2.7.10.jar:2.7.10]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1303) [spring-boot-2.7.10.jar:2.7.10]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1292) [spring-boot-2.7.10.jar:2.7.10]
	at com.farsight.quickstudy.QuickStudyApplication.main(QuickStudyApplication.java:10) [classes/:na]
Caused by: java.lang.NullPointerException: null
	at org.activiti.engine.impl.persistence.deploy.DeploymentManager.resolveProcessDefinition(DeploymentManager.java:122) ~[activiti-engine-7.1.0.M6.jar:na]
	at org.activiti.engine.impl.persistence.deploy.DeploymentManager.findDeployedProcessDefinitionById(DeploymentManager.java:76) ~[activiti-engine-7.1.0.M6.jar:na]
	at org.activiti.engine.impl.util.ProcessDefinitionUtil.getBpmnModel(ProcessDefinitionUtil.java:65) ~[activiti-engine-7.1.0.M6.jar:na]
	at org.activiti.engine.impl.cmd.GetBpmnModelCmd.execute(GetBpmnModelCmd.java:41) ~[activiti-engine-7.1.0.M6.jar:na]
	at org.activiti.engine.impl.cmd.GetBpmnModelCmd.execute(GetBpmnModelCmd.java:26) ~[activiti-engine-7.1.0.M6.jar:na]
	at org.activiti.engine.impl.interceptor.CommandInvoker$1.run(CommandInvoker.java:37) ~[activiti-engine-7.1.0.M6.jar:na]
	at org.activiti.engine.impl.interceptor.CommandInvoker.executeOperation(CommandInvoker.java:78) ~[activiti-engine-7.1.0.M6.jar:na]
	at org.activiti.engine.impl.interceptor.CommandInvoker.executeOperations(CommandInvoker.java:57) ~[activiti-engine-7.1.0.M6.jar:na]
	at org.activiti.engine.impl.interceptor.CommandInvoker.execute(CommandInvoker.java:42) ~[activiti-engine-7.1.0.M6.jar:na]
	at org.activiti.engine.impl.interceptor.TransactionContextInterceptor.execute(TransactionContextInterceptor.java:48) ~[activiti-engine-7.1.0.M6.jar:na]
	at org.activiti.engine.impl.interceptor.CommandContextInterceptor.execute(CommandContextInterceptor.java:59) ~[activiti-engine-7.1.0.M6.jar:na]
	at org.activiti.spring.SpringTransactionInterceptor$1.doInTransaction(SpringTransactionInterceptor.java:47) ~[activiti-spring-7.1.0.M6.jar:na]
	at org.springframework.transaction.support.TransactionTemplate.execute(TransactionTemplate.java:140) ~[spring-tx-5.3.26.jar:5.3.26]
	at org.activiti.spring.SpringTransactionInterceptor.execute(SpringTransactionInterceptor.java:45) ~[activiti-spring-7.1.0.M6.jar:na]
	at org.activiti.engine.impl.interceptor.LogInterceptor.execute(LogInterceptor.java:29) ~[activiti-engine-7.1.0.M6.jar:na]
	at org.activiti.engine.impl.cfg.CommandExecutorImpl.execute(CommandExecutorImpl.java:44) ~[activiti-engine-7.1.0.M6.jar:na]
	at org.activiti.engine.impl.cfg.CommandExecutorImpl.execute(CommandExecutorImpl.java:39) ~[activiti-engine-7.1.0.M6.jar:na]
	at org.activiti.engine.impl.RepositoryServiceImpl.getBpmnModel(RepositoryServiceImpl.java:142) ~[activiti-engine-7.1.0.M6.jar:na]
	at org.activiti.runtime.api.model.impl.APIProcessDefinitionConverter.from(APIProcessDefinitionConverter.java:43) ~[activiti-api-process-runtime-impl-7.1.0.M6.jar:na]
	at org.activiti.runtime.api.model.impl.APIProcessDefinitionConverter.from(APIProcessDefinitionConverter.java:26) ~[activiti-api-process-runtime-impl-7.1.0.M6.jar:na]
	at org.activiti.runtime.api.model.impl.ListConverter.from(ListConverter.java:28) ~[activiti-api-runtime-shared-impl-7.1.0.M6.jar:na]
	at org.activiti.spring.ProcessDeployedEventProducer.doStart(ProcessDeployedEventProducer.java:55) ~[activiti-spring-boot-starter-7.1.0.M6.jar:na]
	at org.activiti.spring.AbstractActivitiSmartLifeCycle.start(AbstractActivitiSmartLifeCycle.java:68) ~[activiti-spring-boot-starter-7.1.0.M6.jar:na]
	at org.springframework.context.support.DefaultLifecycleProcessor.doStart(DefaultLifecycleProcessor.java:178) ~[spring-context-5.3.26.jar:5.3.26]
	... 14 common frames omitted

```

问题是如何出现的：

直接在数据库里删除某个数据后，突然无法启动springboot了，推测是数据库中数据有问题，所以清除数据

解决过程：

删除了bpmn文件，依然无法启动，再清空act_re_deployment表数据，依然无法启动，再清空act_re_procdef表数据，即可正常启动

解决方案：

清空了表act_re_deployment和act_re_procdef中的数据即可正常启动



## 网关的问题

如果找错为：org.activiti.engine.ActivitiException: No outgoing sequence flow of the exclusive gateway 'gateway' could be selected for continuing the process

![image-20230722195014149](/./md-img/image-20230722195014149.png)

要用转义符CDATA

```xml
<sequenceFlow id="sid-90a9f383-7aa7-49ca-811b-80a4554eedb6" sourceRef="gateway" targetRef="modify" name="不同意，打回重写">
  <conditionExpression xsi:type="tFormalExpression"><![CDATA[${!pass}]]></conditionExpression>
</sequenceFlow>
```

## Activiti7与Activiti6的差异

1.Activiti7移除了FormService，要想获得Form表单的值，要使用taskService.getVariables(task.getId())方法



2.在BPMN文件中定义Form表单值的时候，如果使用插件，添加Properties的话，则在bpmn文件中为<activiti:formProperty id="Property 1"/>

![image-20230730135800540](/./md-img/image-20230730135800540.png)

![image-20230730154550068](/./md-img/image-20230730154550068.png)

然而，formProperties是activiti5.x或6.x的写法，在activiti7中应使用formField
