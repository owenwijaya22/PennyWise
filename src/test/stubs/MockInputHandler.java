/*
 * 
 */
package test.stubs;

import pennywise.ui.handlers.InputHandler;
import java.util.Queue;
import java.util.LinkedList;


/**
 * The Class MockInputHandler.
 */
public class MockInputHandler extends InputHandler {
    
    /** The input queue. */
    private Queue<String> inputQueue = new LinkedList<>();
    
    /** The double queue. */
    private Queue<Double> doubleQueue = new LinkedList<>();
    
    /** The int queue. */
    private Queue<Integer> intQueue = new LinkedList<>();
    
    /** The float queue. */
    private Queue<Float> floatQueue = new LinkedList<>();
    
    /** The boolean queue. */
    private Queue<Boolean> booleanQueue = new LinkedList<>();

    /**
     * Queue input.
     *
     * @param input the input
     */
    public void queueInput(String input) {
        inputQueue.offer(input);
    }

    /**
     * Queue double.
     *
     * @param value the value
     */
    public void queueDouble(double value) {
        doubleQueue.offer(value);
    }

    /**
     * Queue int.
     *
     * @param value the value
     */
    public void queueInt(int value) {
        intQueue.offer(value);
    }

    /**
     * Queue float.
     *
     * @param value the value
     */
    public void queueFloat(float value) {
        floatQueue.offer(value);
    }

    /**
     * Queue boolean.
     *
     * @param value the value
     */
    public void queueBoolean(boolean value) {
        booleanQueue.offer(value);
    }

    /**
     * Queue string.
     *
     * @param input the input
     */
    public void queueString(String input) {
        queueInput(input);
    }

    /**
     * Read line.
     *
     * @return the string
     */
    @Override
    public String readLine() {
        return inputQueue.poll();
    }

    /**
     * Read int.
     *
     * @return the int
     */
    @Override
    public int readInt() {
        Integer value = intQueue.poll();
        return value != null ? value : -1;
    }

    /**
     * Read double.
     *
     * @return the double
     */
    @Override
    public double readDouble() {
        Double value = doubleQueue.poll();
        return value != null ? value : -1.0;
    }

    /**
     * Read float.
     *
     * @return the float
     */
    @Override
    public float readFloat() {
        Float value = floatQueue.poll();
        return value != null ? value : -1.0f;
    }

    /**
     * Ask yes no.
     *
     * @param prompt the prompt
     * @return true, if successful
     */
    @Override
    public boolean askYesNo(String prompt) {
        Boolean value = booleanQueue.poll();
        return value != null ? value : false;
    }
} 