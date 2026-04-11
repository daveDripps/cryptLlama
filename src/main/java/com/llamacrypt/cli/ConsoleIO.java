package com.llamacrypt.cli;

import java.io.Console;

public final class ConsoleIO {
    private final Console console;

    public ConsoleIO() {
        this.console = System.console();
    }

    public void printInfo(String message) {
        if (console != null) {
            console.format(message).format("%n").flush();
        } else {
            System.out.println(message);
        }
    }

    public void printSuccess(String message) {
        printInfo("[SUCCESS] " + message);
    }

    public void printError(String message) {
        if (console != null) {
            console.format("[ERROR] %s%n", message).flush();
        } else {
            System.err.println("[ERROR] " + message);
        }
    }

    public void printKey(String key) {
        if (console != null) {
            console.format("%n========================================%n");
            console.format("  IMPORTANT: Save this key securely!%n");
            console.format("  It cannot be recovered if lost.%n");
            console.format("========================================%n%n");
            console.format("  Key: %s%n%n", key);
            console.format("========================================%n%n").flush();
        } else {
            System.out.println("\n========================================");
            System.out.println("  IMPORTANT: Save this key securely!");
            System.out.println("  It cannot be recovered if lost.");
            System.out.println("========================================\n");
            System.out.println("  Key: " + key + "\n");
            System.out.println("========================================\n");
        }
    }

    public char[] readPassword(String prompt) {
        if (console != null) {
            return console.readPassword(prompt);
        } else {
            System.out.print(prompt);
            return new java.util.Scanner(System.in).nextLine().toCharArray();
        }
    }
}
