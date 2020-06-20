package com.demo.camunda.bpm.extra;

import com.alibaba.fastjson.JSON;
import com.demo.camunda.bpm.mq.MqMessage;
import com.demo.camunda.helper.BpmnHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.impl.interceptor.CommandInvocationContext;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.springframework.util.CollectionUtils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ExtraContext {

    protected static final ThreadLocal<Deque<MqMessage>> MA_MESSAGE_HOLDER = new ThreadLocal<>();

    protected static final ThreadLocal<List<String>> AUTO_COMPLETE_TASKS = ThreadLocal.withInitial(Lists::newArrayList);

    private static final ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(5);

    public static void close(CommandInvocationContext commandInvocationContext) {

        try {
            if (commandInvocationContext.getThrowable() != null) {
                return;
            }

            handleMessage(message -> {
                log.info("Send message : {}", JSON.toJSONString(message));
            });

            completeTasks();

        } finally {
            MA_MESSAGE_HOLDER.remove();
            AUTO_COMPLETE_TASKS.remove();
        }

    }

    private static void completeTasks() {

        List<String> taskIds = AUTO_COMPLETE_TASKS.get();

        if (CollectionUtils.isEmpty(taskIds)) {
            return;
        }

        for (String taskId : taskIds) {

            EXECUTOR.schedule(() -> {
                        BpmnHelper.execute(commandContext -> {
                            TaskEntity task = commandContext.getTaskManager().findTaskById(taskId);
                            log.debug("AUTO COMPLETE TASK : {}", taskId);
                            task.complete();
                            return null;
                        });
                    }, 5, TimeUnit.SECONDS
            );
        }
    }

    public static void handleMessage(Handler<MqMessage> handler) {
        Deque<MqMessage> stack = getStack(MA_MESSAGE_HOLDER);
        while (!stack.isEmpty()) {
            MqMessage message = stack.pop();
            handler.handle(message);
        }
    }

    public static void setMqMessage(MqMessage mqMessage) {
        getStack(MA_MESSAGE_HOLDER).push(mqMessage);
    }

    protected static void addAutoCompleteTask(String taskId) {
        AUTO_COMPLETE_TASKS.get().add(taskId);
    }

    protected static <T> Deque<T> getStack(ThreadLocal<Deque<T>> threadLocal) {
        Deque<T> stack = threadLocal.get();
        if (stack == null) {
            stack = new ArrayDeque<T>();
            threadLocal.set(stack);
        }
        return stack;
    }

    public interface Handler<T> {
        void handle(T t);
    }
}
