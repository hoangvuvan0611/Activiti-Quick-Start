package org.vvh;

import org.activiti.engine.*;
import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.LongFormType;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Main {
    public static void main(String[] args) throws ParseException {
        // Tao va cau hinh mot Process Engine su dung h2 database
        /**
         * - ProcessEngineConfiguration: lop cau hinh trong activiti de thiet lap cac cau hinh can thiet cho Process Engine
         * - StandaloneInMemProcessEngineConfiguration: mot instance cua ProcessEngineConfiguration khoi tao mot ProcessEngine
         * doc lap (Standalone) su dung bo nho trong in-memory
         *
         */
        ProcessEngineConfiguration processEngineConfiguration = new StandaloneInMemProcessEngineConfiguration()
                .setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1") // Cau hinh URL ket noi JDBC toi H2 database, DB_CLOSE_DELAY=-1 giu cho csdl khong bi dong ngay ca khi cac ket noi voi no da dong lai
                .setJdbcUsername("hoang")
                .setJdbcPassword("hoang")
                .setJdbcDriver("org.h2.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        /**
         * - Sau khi cau hinh xong nhu tren, processEngineConfiguration.buildProcessEngine() duoc goi
         * de tao moi ProcessEngine
         * - ProcessEngine: thanh phan trung tam cua activiti, giup quan ly va thuc thi cac quy trinh nghiep vu
         */
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

        // Hien thi cau hinh cua ProcessEngine va phien ban cua activiti
        String engineName = processEngine.getName();
        String ver = ProcessEngine.VERSION;
        System.out.println("ProcessEngine [" + engineName + "] Version: [" + ver + "]");

        /**
         * - RepositoryService: la mot trong nhung dich vu cot loi cua Activiti, no dung de luu tru quy trinh
         * va cac dinh nghia quy trinh
         * - No lay cac thong tin cau hinh tu ProcessEngine
         */
        RepositoryService repositoryService = processEngine.getRepositoryService();

        /**
         * - DeploymentBuilder: duoc tao tu repositoryService.createDeployment(), cho phep bat dau trien khai
         * quy trinh duoc dinh nghia (file BPMN) vao ProcessEngine
         * - addClasspathResource("onboarding.bpmn20.xml").deploy(): Trien khai quy trinh BPMN da khai bao trong
         * tep 'onboarding.bpmn20.xml'
         */
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("onboarding.bpmn20.xml").deploy();

        /**
         * ProcessDefinition: luu tru dinh nghia cac quy trinh duoc lay ra tu co so du lieu
         * - repositoryService.createProcessDefinitionQuery(): Tao doi tuong dung de truy van, tim kiem cac dinh
         * nghia quy trinh da trien khai trong he thong.
         * - deploymentId(deployment.getId()): Gioi han ket qua truy van, chi cho cac dinh nghia
         * quy trinh thuoc ve deployment voi id da duoc trien khai o tren.
         * - singleResult(): Tra ve duy nhat mot ket qua truy van la doi tuong ProcessDefination dai dien cho dinh
         * nghia cua quy trinh da trien khai
         */
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();
        System.out.println("Found process definition [" + processDefinition.getName() + "] with id [" + processDefinition.getId() + "]");

        /**
         * RuntimeSerive: su dung de quan ly va dieu khien cac phien ban quy trinh dang chay. No cung cap cac phuong thuc
         * de khoi dong, truy van va quan ly qua trinh thuc thi cua cac quy trinh.
         */
        RuntimeService runtimeService = processEngine.getRuntimeService();

        /**
         * ProcessInstance: Khi mot quy trinh duoc khoi dong, mot doi tuong ProcessInstance duoc tra ve. Doi tuong nay
         * dai dien cho phien ban quy trinh cu the dang chay.
         * - ProcessInstance chua thong tin ve trang thai hien tai cua quy trinh, cac buoc da thuc hien, du lieu cua
         * quy trinh va cac nhiem vu dang cho.
         * - runtimeService.startProcessInstanceByKey("onboarding"): Phuong thuc duoc su dung de khoi dong mot phien
         * ban quy trinh dua tren mot dinh danh khoa cua quy trinh (process id trong bpmn)
         */
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("onboarding");
        System.out.println("Onboarding process started with process instance id ["
                + processInstance.getProcessInstanceId()
                + "] key [" + processInstance.getProcessDefinitionKey() + "]");

        TaskService taskService = processEngine.getTaskService();
        FormService formService = processEngine.getFormService();
        HistoryService historyService = processEngine.getHistoryService();

        Scanner scanner = new Scanner(System.in);
        while (processInstance != null && !processInstance.isEnded()) {
            List<Task> tasks = taskService.createTaskQuery()
                    .taskCandidateGroup("managers").list();
            System.out.println("Active outstanding tasks: [" + tasks.size() + "]");
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                System.out.println("Processing Task [" + task.getName() + "]");
                Map<String, Object> variables = new HashMap<String, Object>();
                FormData formData = formService.getTaskFormData(task.getId());
                formData.getFormProperties().forEach(formProperty -> {
                    System.out.println(formProperty.getId());
                    System.out.println(formProperty.getName());
                    System.out.println(formProperty.getType());
                    System.out.println(formProperty.getValue());
                });
                for (FormProperty formProperty : formData.getFormProperties()) {
                    if (StringFormType.class.isInstance(formProperty.getType())) {
                        System.out.println(formProperty.getName() + "?");
                        String value = scanner.nextLine();
                        variables.put(formProperty.getId(), value);
                    } else if (LongFormType.class.isInstance(formProperty.getType())) {
                        System.out.println(formProperty.getName() + "? (Must be a whole number)");
                        Long value = Long.valueOf(scanner.nextLine());
                        variables.put(formProperty.getId(), value);
                    } else if (DateFormType.class.isInstance(formProperty.getType())) {
                        System.out.println(formProperty.getName() + "? (Must be a date m/d/yy)");
                        DateFormat dateFormat = new SimpleDateFormat("m/d/yy");
                        Date value = dateFormat.parse(scanner.nextLine());
                        variables.put(formProperty.getId(), value);
                    } else {
                        System.out.println("<form type not supported>");
                    }
                }
                taskService.complete(task.getId(), variables);

                HistoricActivityInstance endActivity = null;
                List<HistoricActivityInstance> activities =
                        historyService.createHistoricActivityInstanceQuery()
                                .processInstanceId(processInstance.getId()).finished()
                                .orderByHistoricActivityInstanceEndTime().asc()
                                .list();
                for (HistoricActivityInstance activity : activities) {
                    if (activity.getActivityType() == "startEvent") {
                        System.out.println("BEGIN " + processDefinition.getName()
                                + " [" + processInstance.getProcessDefinitionKey()
                                + "] " + activity.getStartTime());
                    }
                    if (activity.getActivityType() == "endEvent") {
                        // Handle edge case where end step happens so fast that the end step
                        // and previous step(s) are sorted the same. So, cache the end step
                        //and display it last to represent the logical sequence.
                        endActivity = activity;
                    } else {
                        System.out.println("-- " + activity.getActivityName()
                                + " [" + activity.getActivityId() + "] "
                                + activity.getDurationInMillis() + " ms");
                    }
                }
                if (endActivity != null) {
                    System.out.println("-- " + endActivity.getActivityName()
                            + " [" + endActivity.getActivityId() + "] "
                            + endActivity.getDurationInMillis() + " ms");
                    System.out.println("COMPLETE " + processDefinition.getName() + " ["
                            + processInstance.getProcessDefinitionKey() + "] "
                            + endActivity.getEndTime());
                }
            }
            // Re-query the process instance, making sure the latest state is available
            processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstance.getId()).singleResult();
        }
        scanner.close();
    }
}