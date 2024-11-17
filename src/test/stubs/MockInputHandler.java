package test.stubs;

import pennywise.ui.handlers.InputHandler;
import java.util.Queue;
import java.util.LinkedList;

public class MockInputHandler extends InputHandler {
    private Queue<String> inputQueue = new LinkedList<>();
    private Queue<Double> doubleQueue = new LinkedList<>();
    private Queue<Integer> intQueue = new LinkedList<>();
    private Queue<Float> floatQueue = new LinkedList<>();
    private Queue<Boolean> booleanQueue = new LinkedList<>();

    public void queueInput(String input) {
        inputQueue.offer(input);
    }

    public void queueDouble(double value) {
        doubleQueue.offer(value);
    }

    public void queueInt(int value) {
        intQueue.offer(value);
    }

    public void queueFloat(float value) {
        floatQueue.offer(value);
    }

    public void queueBoolean(boolean value) {
        booleanQueue.offer(value);
    }

    public void queueString(String input) {
        queueInput(input);
    }

    @Override
    public String readLine() {
        return inputQueue.poll();
    }

    @Override
    public int readInt() {
        Integer value = intQueue.poll();
        return value != null ? value : -1;
    }

    @Override
    public double readDouble() {
        Double value = doubleQueue.poll();
        return value != null ? value : -1.0;
    }

    @Override
    public float readFloat() {
        Float value = floatQueue.poll();
        return value != null ? value : -1.0f;
    }

    @Override
    public boolean askYesNo(String prompt) {
        Boolean value = booleanQueue.poll();
        return value != null ? value : false;
    }
} 