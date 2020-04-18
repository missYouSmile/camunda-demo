package com.demo.camunda.bpm.extra;

import com.demo.camunda.bpm.mq.MqMessage;

import java.util.ArrayDeque;
import java.util.Deque;

public class ExtraContext {

    protected static ThreadLocal<Deque<MqMessage>> mqMessageThreadLocal = new ThreadLocal<>();

    public static void handleMessage(Handler<MqMessage> handler) {
        Deque<MqMessage> stack = getStack(mqMessageThreadLocal);
        while (!stack.isEmpty()) {
            MqMessage message = stack.pop();
            handler.handle(message);
        }
    }

    public static void setMqMessage(MqMessage mqMessage) {
        getStack(mqMessageThreadLocal).push(mqMessage);
    }

    public static void removeMqMessage() {
        getStack(mqMessageThreadLocal).pop();
    }

    public static MqMessage getMqMessage() {
        Deque<MqMessage> stack = getStack(mqMessageThreadLocal);
        if (stack.isEmpty()) {
            return null;
        }
        return stack.peek();
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
