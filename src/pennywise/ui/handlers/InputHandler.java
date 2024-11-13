package pennywise.ui.handlers;

import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner;

    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    public int readInt() {
        try {
            return Integer.parseInt(readLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public double readDouble() {
        try {
            return Double.parseDouble(readLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public float readFloat() {
        try {
            return Float.parseFloat(readLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public String readLine() {
        return scanner.nextLine();
    }

    public boolean askYesNo(String question) {
        System.out.printf("%s (Y/N): ", question);
        return readLine().trim().toUpperCase().equals("Y");
    }

    public void close() {
        scanner.close();
    }
}