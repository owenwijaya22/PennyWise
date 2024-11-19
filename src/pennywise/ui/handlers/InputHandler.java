/*
 * 
 */
package pennywise.ui.handlers;

import java.util.Scanner;


/**
 * The Class InputHandler.
 */
public class InputHandler {
    
    /** The scanner. */
    private final Scanner scanner;

    /**
     * Instantiates a new input handler.
     */
    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Read int.
     *
     * @return the int
     */
    public int readInt() {
        try {
            return Integer.parseInt(readLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Read double.
     *
     * @return the double
     */
    public double readDouble() {
        try {
            return Double.parseDouble(readLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Read float.
     *
     * @return the float
     */
    public float readFloat() {
        try {
            return Float.parseFloat(readLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Read line.
     *
     * @return the string
     */
    public String readLine() {
        return scanner.nextLine();
    }

    /**
     * Ask yes no.
     *
     * @param question the question
     * @return true, if successful
     */
    public boolean askYesNo(String question) {
        System.out.printf("%s (Y/N): ", question);
        return readLine().trim().toUpperCase().equals("Y");
    }

    /**
     * Close.
     */
    public void close() {
        scanner.close();
    }
}