package com.anosi.asset.service.impl;

import com.anosi.asset.component.QLExpressComponent;
import com.anosi.asset.component.WebSocketComponent;
import com.anosi.asset.exception.CustomRunTimeException;
import com.anosi.asset.i18n.I18nComponent;
import com.anosi.asset.model.jpa.*;
import com.anosi.asset.model.jpa.BaseProcess.FinishType;
import com.anosi.asset.model.jpa.ProcessRecord.HandleType;
import com.anosi.asset.service.*;
import com.google.common.collect.ImmutableMap;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/***
 * 所有流程service集成的抽象类，实现了上层接口的部分方法,剩余方法在具体流程中实现
 *
 * @author jinyao
 *
 * @param <T>
 */
@Transactional
public abstract class BaseProcessServiceImpl<T extends BaseProcess> extends BaseJPAServiceImpl<T>
        implements BaseProcessService<T> {

    private static final Logger logger = LoggerFactory.getLogger(BaseProcessServiceImpl.class);

    @Autowired
    protected TaskService taskService;
    @Autowired
    protected RuntimeService runtimeService;
    @Autowired
    protected HistoryService historyService;
    @Autowired
    protected IdentityService identityService;
    @Autowired
    protected RepositoryService repositoryService;
    @Autowired
    protected MessageInfoService messageInfoService;
    @Autowired
    protected WebSocketComponent webSocketComponent;
    @Autowired
    protected I18nComponent i18nComponent;
    @Autowired
    protected ProcessRecordService processRecordService;
    @Autowired
    protected AccountService accountService;
    @Autowired
    protected IntegralRuleService integralRuleService;
    @Autowired
    protected QLExpressComponent qlExpressComponent;


    protected String quene = "/queue/private/message/";

    protected String detailUrl;

    protected String definitionKey;// 由于每个子类的definitionKey都是一样的，所以不会有线程安全问题

    protected static ThreadLocal<String> threadLocal = new ThreadLocal<String>();

    @Override
    public Page<T> findHistoricProcessInstance(Pageable pageable,
                                               HistoricProcessInstanceQuery historicProcessInstanceQuery) {
        int firstResult = pageable.getPageNumber() * pageable.getPageSize();
        int maxResults = pageable.getPageSize();

        List<HistoricProcessInstance> instances = historicProcessInstanceQuery.listPage(firstResult, maxResults);
        long total = historicProcessInstanceQuery.count(); // 总数
        logger.debug("the total for historicProcessInstance:{}", total);

        List<T> list = instances.stream().map(instance -> findAndSetInstanceValue(instance))
                .collect(Collectors.toList());

        return new PageImpl<>(list, pageable, total);
    }

    @Override
    public Page<T> findRuntimeTasks(Pageable pageable, TaskQuery taskQuery) {
        int firstResult = pageable.getPageNumber() * pageable.getPageSize();
        int maxResults = pageable.getPageSize();

        List<Task> tasks = taskQuery.listPage(firstResult, maxResults); // 分页task
        long total = taskQuery.count(); // task总数
        logger.debug("the total for runtimeTask:{}", total);

        // lambda表达式，用task查出CustomerServiceProcess，设置相关属性后，汇聚到一个list中
        List<T> list = tasks.stream().map(task -> findAndSetRunTimeValue(task)).collect(Collectors.toList());

        return new PageImpl<>(list, pageable, total);
    }

    @Override
    public Page<T> findHistoricTasks(Pageable pageable, HistoricTaskInstanceQuery historicTaskInstanceQuery) {
        int firstResult = pageable.getPageNumber() * pageable.getPageSize();
        int maxResults = pageable.getPageSize();

        List<HistoricTaskInstance> historicTaskInstances = historicTaskInstanceQuery.listPage(firstResult, maxResults);
        long total = historicTaskInstanceQuery.count(); // task总数
        logger.debug("the total for historicTaskInstance:{}", total);

        List<T> list = historicTaskInstances.stream().map(task -> findAndSetHistoricValue(task))
                .collect(Collectors.toList());

        return new PageImpl<>(list, pageable, total);
    }

    @Override
    public T setRunTimeValueForProcess(T t, Task task) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId()).singleResult();
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId()).singleResult();
        t.setTask(task);
        t.setProcessInstance(processInstance);
        t.setHistoricProcessInstance(historicProcessInstance);
        return t;
    }

    @Override
    public T setHistoricValueForProcess(T t, HistoricTaskInstance historicTaskInstance) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(historicTaskInstance.getProcessInstanceId()).singleResult();
        t.setHistoricProcessInstance(historicProcessInstance);
        t.setHistoricTaskInstance(historicTaskInstance);
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(historicTaskInstance.getProcessInstanceId())
                .list();
        if (!CollectionUtils.isEmpty(tasks)) {
            t.setTask(tasks.get(0));
        }
        return t;
    }

    @Override
    public T setHistoricValueForProcess(T t) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(t.getProcessInstanceId()).singleResult();
        t.setHistoricProcessInstance(historicProcessInstance);
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(t.getProcessInstanceId())
                .list();
        if (!CollectionUtils.isEmpty(tasks)) {
            t.setTask(tasks.get(0));
        }
        return t;
    }

    @Override
    public T findAndSetInstanceValue(HistoricProcessInstance instance) {
        T process = findByProcessInstanceId(instance.getId());
        process.setHistoricProcessInstance(instance);
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(instance.getId()).list();
        if (!CollectionUtils.isEmpty(tasks)) {
            process.setTask(tasks.get(0));
        }
        return process;
    }

    @Override
    public T findAndSetRunTimeValue(Task task) {
        return setRunTimeValueForProcess(findByProcessInstanceId(task.getProcessInstanceId()), task);
    }

    @Override
    public T findAndSetHistoricValue(HistoricTaskInstance historicTaskInstance) {
        return setHistoricValueForProcess(findByProcessInstanceId(historicTaskInstance.getProcessInstanceId()),
                historicTaskInstance);
    }

    @Override
    public void completeTask(String taskId, DoInComplete doInComplete, List<MessageInfo> messageInfos)
            throws Exception {
        completeTask(taskId, doInComplete, HandleType.PASS, null, messageInfos, null);
    }

    @Override
    public void completeTask(String taskId, DoInComplete doInComplete, List<MessageInfo> messageInfos, String remain)
            throws Exception {
        completeTask(taskId, doInComplete, HandleType.PASS, null, messageInfos, remain);
    }

    @Override
    public void completeTask(String taskId, DoInComplete doInComplete, HandleType type, String reason,
                             List<MessageInfo> messageInfos) throws Exception {
        completeTask(taskId, doInComplete, type, reason, messageInfos, null);
    }

    @Override
    public void completeTask(String taskId, DoInComplete doInComplete, HandleType type, String reason,
                             List<MessageInfo> messageInfos, String remain) throws Exception {
        T t = findBytaskId(taskId);

        // 找出这个任务的记录,设置任务的完成时间和完成类型
        ProcessRecord processRecord = processRecordService.findByTaskIdNotEnd(taskId);

        processRecord.setType(type);
        processRecord.setEndTime(new Date());
        processRecord.setReason(reason);
        processRecord.setAssignee(sessionComponent.getCurrentUser());
        String processInstanceId = processRecord.getProcessInstanceId();

        if (!Objects.equals((String) taskService.getVariable(taskId, "applicant"),
                sessionComponent.getCurrentUser().getLoginId())) {
            messageInfoForApplicant(t, taskId,
                    accountService.findByLoginId((String) taskService.getVariable(taskId, "applicant")), messageInfos,
                    reason);// 生成发送给发起人的信息
        }
        doInComplete.excute();// 办理任务

        // 积分操作
        HistoricTaskInstance hTask = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        IntegralRule integralRule = integralRuleService.findByKey(hTask.getTaskDefinitionKey());
        if (integralRule != null) {
            qlExpressComponent.excuteExpress(ImmutableMap.of("startTime", hTask.getStartTime(), "endTime", hTask.getEndTime()), integralRule);
        }

        // 这里只发送个人任务的信息，由于组任务相对较少，且封装复杂
        // 所以如果是组任务，将发送信息的代码自行写到DoInComplete中
        // 然后由此模板方法完成发送及创建记录
        /*-----注意以下三步的顺序，要先发送消息，再创建下一步的记录，不然不会发送--*/
        searchNextTaskAndSend(t, processInstanceId, messageInfos);// 生成发给下一步办理人的信息
        saveMessageInfoAndSend(messageInfos);// 发送
        createNewProcessRecord(processInstanceId, remain);// 生成新的待办任务记录
    }

    @Override
    public void createNewProcessRecord(String processInstanceId, String remain) {
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        for (Task task : tasks) {
            if (processRecordService.findByTaskIdNotEnd(task.getId()) == null) {
                ProcessRecord processRecord = new ProcessRecord();
                processRecord.setProcessInstanceId(processInstanceId);
                processRecord.setTaskId(task.getId());
                processRecord.setTaskName(task.getName());
                processRecord.setStartTime(new Date());
                processRecord.setType(HandleType.REMAIN_TO_DO);
                processRecord.setRemain(StringUtils.isBlank(task.getAssignee()) ? remain : task.getAssignee());
                processRecordService.save(processRecord);
            }
        }
    }

    @Override
    public void saveMessageInfoAndSend(List<MessageInfo> messageInfos) {
        for (MessageInfo messageInfo : messageInfos) {
            messageInfoService.save(messageInfo);
            try {
                // 利用websocket向浏览器发送消息
                webSocketComponent.sendByQuene(messageInfo.getTo().getLoginId(), quene, MessageFormat
                        .format(i18nComponent.getMessage("message.template.simple"), messageInfo.getFrom().getName()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getDefinitionKey() {
        return Objects.requireNonNull(definitionKey, "definitionKey can not be null");
    }

    @Override
    public Page<T> findStartedProcess(Pageable pageable, String searchContent, String timeType, Date beginTime,
                                      Date endTime, FinishType finishType) {
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(getDefinitionKey()).startedBy(sessionComponent.getCurrentUser().getLoginId())
                .orderByProcessInstanceStartTime().desc();
        if (beginTime != null) {
            if ("start".equals(timeType)) {
                historicProcessInstanceQuery.startedAfter(beginTime);
            } else if ("end".equals(timeType)) {
                historicProcessInstanceQuery.finishedAfter(beginTime);
            } else {
                throw new CustomRunTimeException("timeType illegal");
            }
        }
        if (endTime != null) {
            if ("start".equals(timeType)) {
                historicProcessInstanceQuery.startedBefore(endTime);
            } else if ("end".equals(timeType)) {
                historicProcessInstanceQuery.finishedBefore(endTime);
            } else {
                throw new CustomRunTimeException("timeType illegal");
            }
        }
        if (finishType != null) {
            switch (finishType) {
                case FINISHED:
                    historicProcessInstanceQuery.finished();
                    break;
                case REMAIN:
                    historicProcessInstanceQuery.unfinished();
                    break;
                default:
                    break;
            }
        }
        if (StringUtils.isNoneBlank(searchContent)) {
            List<String> processInstanceIdsBySearchContent = getProcessInstanceIdsBySearchContent(searchContent);
            if (!CollectionUtils.isEmpty(processInstanceIdsBySearchContent)) {
                historicProcessInstanceQuery
                        .processInstanceIds(new HashSet<>(getProcessInstanceIdsBySearchContent(searchContent)));
            } else {
                // 如果查询结果为null,返回空的page
                return new PageImpl<>(new ArrayList<>(), pageable, 0);
            }
        }
        return findHistoricProcessInstance(pageable, historicProcessInstanceQuery);
    }

    @Override
    public Page<T> findTasksToDo(Pageable pageable, String searchContent, Date beginTime, Date endTime) {
        TaskQuery taskQuery = taskService.createTaskQuery().processDefinitionKey(getDefinitionKey())
                .orderByTaskCreateTime().desc().taskCandidateOrAssigned(sessionComponent.getCurrentUser().getLoginId());
        if (beginTime != null) {
            taskQuery.taskCreatedAfter(beginTime);
        }
        if (endTime != null) {
            taskQuery.taskCreatedBefore(endTime);
        }
        if (StringUtils.isNoneBlank(searchContent)) {
            List<String> processInstanceIdsBySearchContent = getProcessInstanceIdsBySearchContent(searchContent);
            if (!CollectionUtils.isEmpty(processInstanceIdsBySearchContent)) {
                taskQuery.processInstanceIdIn(getProcessInstanceIdsBySearchContent(searchContent));
            } else {
                // 如果查询结果为null,返回空的page
                return new PageImpl<>(new ArrayList<>(), pageable, 0);
            }
        }
        return findRuntimeTasks(pageable, taskQuery);
    }

    @Override
    public Page<T> findHistoricTasks(Pageable pageable, String searchContent, String timeType, Date beginTime,
                                     Date endTime, FinishType finishType) {
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionKey(getDefinitionKey()).orderByTaskCreateTime().desc()
                .taskAssignee(sessionComponent.getCurrentUser().getLoginId()).finished();
        if (beginTime != null) {
            if ("start".equals(timeType)) {
                historicTaskInstanceQuery.taskCreatedAfter(beginTime);
            } else if ("end".equals(timeType)) {
                historicTaskInstanceQuery.taskCompletedAfter(beginTime);
            } else {
                throw new CustomRunTimeException("timeType illegal");
            }
        }
        if (endTime != null) {
            if ("start".equals(timeType)) {
                historicTaskInstanceQuery.taskCreatedBefore(endTime);
            } else if ("end".equals(timeType)) {
                historicTaskInstanceQuery.taskCompletedBefore(endTime);
            } else {
                throw new CustomRunTimeException("timeType illegal");
            }
        }
        if (finishType != null) {
            switch (finishType) {
                case FINISHED:
                    historicTaskInstanceQuery.processFinished();
                    break;
                case REMAIN:
                    historicTaskInstanceQuery.processUnfinished();
                    break;
                default:
                    break;
            }
        }
        if (StringUtils.isNoneBlank(searchContent)) {
            List<String> processInstanceIdsBySearchContent = getProcessInstanceIdsBySearchContent(searchContent);
            if (!CollectionUtils.isEmpty(processInstanceIdsBySearchContent)) {
                historicTaskInstanceQuery.processInstanceIdIn(getProcessInstanceIdsBySearchContent(searchContent));
            } else {
                // 如果查询结果为null,返回空的page
                return new PageImpl<>(new ArrayList<>(), pageable, 0);
            }
        }
        return findHistoricTasks(pageable, historicTaskInstanceQuery);
    }

    @Override
    public Page<T> findAllProcesses(Pageable pageable, String searchContent, String timeType, Date beginTime,
                                    Date endTime, FinishType finishType) {
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(getDefinitionKey()).orderByProcessInstanceStartTime().desc();
        if (beginTime != null) {
            if ("start".equals(timeType)) {
                historicProcessInstanceQuery.startedAfter(beginTime);
            } else if ("end".equals(timeType)) {
                historicProcessInstanceQuery.finishedAfter(beginTime);
            } else {
                throw new CustomRunTimeException("timeType illegal");
            }
        }
        if (endTime != null) {
            if ("start".equals(timeType)) {
                historicProcessInstanceQuery.startedBefore(endTime);
            } else if ("end".equals(timeType)) {
                historicProcessInstanceQuery.finishedBefore(endTime);
            } else {
                throw new CustomRunTimeException("timeType illegal");
            }
        }
        if (finishType != null) {
            switch (finishType) {
                case FINISHED:
                    historicProcessInstanceQuery.finished();
                    break;
                case REMAIN:
                    historicProcessInstanceQuery.unfinished();
                    break;
                default:
                    break;
            }
        }
        if (StringUtils.isNoneBlank(searchContent)) {
            List<String> processInstanceIdsBySearchContent = getProcessInstanceIdsBySearchContent(searchContent);
            if (!CollectionUtils.isEmpty(processInstanceIdsBySearchContent)) {
                historicProcessInstanceQuery
                        .processInstanceIds(new HashSet<>(getProcessInstanceIdsBySearchContent(searchContent)));
            } else {
                // 如果查询结果为null,返回空的page
                return new PageImpl<>(new ArrayList<>(), pageable, 0);
            }
        }
        return findHistoricProcessInstance(pageable, historicProcessInstanceQuery);
    }

    @Override
    public T findBytaskId(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return findByProcessInstanceId(task.getProcessInstanceId());
    }

    @Override
    public void searchNextTaskAndSend(T t, String processInstanceId, List<MessageInfo> messageInfos) {
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        for (Task task : tasks) {
            // 如果是新生成的任务
            if (processRecordService.findByTaskIdNotEnd(task.getId()) == null) {
                if (StringUtils.isNoneBlank(task.getAssignee())) {
                    logger.debug("next task name:{}", task.getName());
                    logger.debug("next task assignee:{}", task.getAssignee());
                    messageInfoForAssignee(t, task.getId(), accountService.findByLoginId(task.getAssignee()),
                            messageInfos);
                }
            }
        }
    }

    @Override
    public void messageInfoForAssignee(T t, String taskId, Account nextAssignee, List<MessageInfo> messageInfos) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId()).singleResult();

        MessageInfo messageInfo = new MessageInfo();
        logger.debug("current task assignee:{}", sessionComponent.getCurrentUser().getLoginId());
        messageInfo.setFrom(sessionComponent.getCurrentUser());// 当前任务办理人
        logger.debug("next task assignee:{}", nextAssignee.getLoginId());
        messageInfo.setTo(nextAssignee);// 下一步办理人
        messageInfo.setSendTime(new Date());
        // 从i18n中读取信息
        messageInfo.setTitle(MessageFormat.format(i18nComponent.getMessage("message.titile.taskToDo"),
                processInstance.getProcessDefinitionName(), task.getName()));// {0}等待办理,任务:{1}
        messageInfo.setContent(MessageFormat.format(i18nComponent.getMessage("message.content.taskToDo"), t.getName(),
                task.getName()));// 流程编号为{0},任务名称为{1},等待办理
        messageInfo.setUrl(detailUrl + t.getId());

        messageInfos.add(messageInfo);
    }

    @Override
    public void messageInfoForApplicant(T t, String taskId, Account applicant, List<MessageInfo> messageInfos,
                                        String reason) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId()).singleResult();

        MessageInfo messageInfo = new MessageInfo();
        logger.debug("current task assignee:{}", sessionComponent.getCurrentUser().getLoginId());
        messageInfo.setFrom(sessionComponent.getCurrentUser());// 当前任务办理人
        logger.debug("applicant:{}", applicant.getLoginId());
        messageInfo.setTo(applicant); // 流程发起人
        messageInfo.setSendTime(new Date());
        // 从i18n中读取信息
        messageInfo.setTitle(MessageFormat.format(i18nComponent.getMessage("message.titile.taskComplete"),
                processInstance.getProcessDefinitionName()));// {0}被办理
        messageInfo.setContent(MessageFormat.format(i18nComponent.getMessage("message.content.taskComplete"),
                t.getName(), task.getName(), messageInfo.getFrom().getName(), reason));// 你发起的流程{0},{1}已经被办理,办理人为{2},办理说明:{3}
        messageInfo.setUrl(detailUrl + t.getId());

        messageInfos.add(messageInfo);
    }

    @Override
    public T getDetail(T t) {
        String processInstanceId = t.getProcessInstanceId();
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        if (historicProcessInstance.getEndTime() == null) {
            Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).list().get(0);
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId).singleResult();
            t.setTask(task);
            t.setProcessInstance(processInstance);
        }
        t.setHistoricProcessInstance(historicProcessInstance);
        return t;
    }

    @Override
    public List<HistoricTaskInstance> getTaskDatas(T t) {
        List<HistoricTaskInstance> historicTaskInstances = new ArrayList<>();
        String processInstanceId = t.getProcessInstanceId();
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        // 先获取所有任务节点的定义
        BpmnModel model = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
        if (model != null) {
            Collection<FlowElement> flowElements = model.getMainProcess().getFlowElements();
            for (FlowElement e : flowElements) {
                if (e instanceof UserTask) {
                    // 获取最近的一个任务
                    List<HistoricTaskInstance> hTaskInstances = historyService.createHistoricTaskInstanceQuery()
                            .taskDefinitionKey(e.getId()).processInstanceId(processInstanceId).orderByTaskCreateTime()
                            .desc().list();
                    if (!CollectionUtils.isEmpty(hTaskInstances)) {
                        historicTaskInstances.add(hTaskInstances.get(0));
                    }
                }
            }
        }
        return historicTaskInstances;
    }

    public List<String> getTaskDefinitionKeys() {
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().processDefinitionKey(definitionKey).orderByProcessDefinitionVersion().desc().listPage(0, 1);
        BpmnModel model = repositoryService.getBpmnModel(processDefinitions.get(0).getId());
        if (model != null) {
            Collection<FlowElement> flowElements = model.getMainProcess().getFlowElements();
            return flowElements.stream().map(f -> {
                if (f instanceof UserTask) {
                    return f.getId();
                } else {
                    return null;
                }
            }).filter(f -> f != null).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 流程转向操作
     *
     * @param taskId     当前任务ID
     * @param activityId 目标节点任务ID
     * @param variables  流程变量
     * @throws Exception
     */
    protected void turnTransition(String taskId, String activityId, Map<String, Object> variables) throws Exception {
        // 当前节点
        ActivityImpl currActivity = findActivitiImpl(taskId, null);
        // 清空当前流向
        List<PvmTransition> oriPvmTransitionList = clearTransition(currActivity);

        // 创建新流向
        TransitionImpl newTransition = currActivity.createOutgoingTransition();
        // 目标节点
        ActivityImpl pointActivity = findActivitiImpl(taskId, activityId);
        // 设置新流向的目标节点
        newTransition.setDestination(pointActivity);

        // 执行转向任务
        taskService.complete(taskId, variables);
        // 删除目标节点新流入
        pointActivity.getIncomingTransitions().remove(newTransition);

        // 还原以前流向
        restoreTransition(currActivity, oriPvmTransitionList);
    }

    /**
     * 根据任务ID和节点ID获取活动节点 <br>
     *
     * @param taskId     任务ID
     * @param activityId 活动节点ID <br>
     *                   如果为null或""，则默认查询当前活动节点 <br>
     *                   如果为"end"，则查询结束节点 <br>
     * @return
     * @throws Exception
     */
    private ActivityImpl findActivitiImpl(String taskId, String activityId) throws Exception {
        // 取得流程定义
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(
                        taskService.createTaskQuery().taskId(taskId).singleResult().getProcessDefinitionId());

        // 获取当前活动节点ID
        if (StringUtils.isBlank(activityId)) {
            activityId = taskService.createTaskQuery().taskId(taskId).singleResult().getTaskDefinitionKey();
        }

        // 根据流程定义，获取该流程实例的结束节点
        if (activityId.toUpperCase().equals("END")) {
            for (ActivityImpl activityImpl : processDefinition.getActivities()) {
                List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
                if (pvmTransitionList.isEmpty()) {
                    return activityImpl;
                }
            }
        }

        // 根据节点ID，获取对应的活动节点
        ActivityImpl activityImpl = ((ProcessDefinitionImpl) processDefinition).findActivity(activityId);

        return activityImpl;
    }

    /**
     * 清空指定活动节点流向
     *
     * @param activityImpl 活动节点
     * @return 节点流向集合
     */
    private List<PvmTransition> clearTransition(ActivityImpl activityImpl) {
        // 存储当前节点所有流向临时变量
        List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
        // 获取当前节点所有流向，存储到临时变量，然后清空
        List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
        for (PvmTransition pvmTransition : pvmTransitionList) {
            oriPvmTransitionList.add(pvmTransition);
        }
        pvmTransitionList.clear();

        return oriPvmTransitionList;
    }

    /**
     * 还原指定活动节点流向
     *
     * @param activityImpl         活动节点
     * @param oriPvmTransitionList 原有节点流向集合
     */
    private void restoreTransition(ActivityImpl activityImpl, List<PvmTransition> oriPvmTransitionList) {
        // 清空现有流向
        List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
        pvmTransitionList.clear();
        // 还原以前流向
        for (PvmTransition pvmTransition : oriPvmTransitionList) {
            pvmTransitionList.add(pvmTransition);
        }
    }

    /***
     * 内部方法接口,用来执行completeTask中的具体操作
     *
     * @author jinyao
     *
     */
    @FunctionalInterface
    public interface DoInComplete {
        public void excute();
    }

}
