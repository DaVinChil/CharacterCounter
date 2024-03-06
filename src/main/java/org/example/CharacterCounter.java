package org.example;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class CharacterCounter {
    private static final Scanner in = new Scanner(System.in);
    private String fromFileName;
    private String toFileName;

    public void start() {
        do {
            newSession();
        } while (askAgain());
    }

    public void newSession() {
        requestFileNames();
        if (validateFileNames()) {
            var count = count();
            if (count != null && write(count)) {
                System.out.println("Finished");
            }
        }
    }

    private boolean askAgain() {
        System.out.print("Do you want to try again? [y/n] ");
        var ans = in.nextLine();

        return Objects.equals(ans, "y");
    }

    private void requestFileNames() {
        fromFileName = requestFileName("Enter source file");
        toFileName = requestFileName("Enter output file");
    }

    private boolean validateFileNames() {
        if (Objects.equals(fromFileName, toFileName)) {
            System.out.println("Files should not be the same!");
            return false;
        }

        return true;
    }

    private boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    private String requestFileName(String message) {
        String result;

        while (true) {
            System.out.print(message + " -> ");

            result = in.nextLine();

            if (isNullOrEmpty(result)) {
                System.out.println("File's name can not be empty!");
            } else {
                break;
            }
        }

        return result;
    }

    private boolean write(HashMap<Integer, Long> count) {
        try (var bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(toFileName)))) {
            for (var entry : count.entrySet()) {
                bw.write(new StringBuilder()
                        .append((char) (int) entry.getKey())
                        .append(" = ")
                        .append(entry.getValue())
                        .append("\n")
                        .toString());
            }
        } catch (IOException e) {
            System.out.println("Problem with file " + e.getMessage());
            return false;
        }

        return true;
    }

    private HashMap<Integer, Long> count() {
        HashMap<Integer, Long> count = new HashMap<>();

        try (var br = new BufferedReader(new InputStreamReader(new FileInputStream(fromFileName)))) {
            int c;
            while ((c = br.read()) != -1) {
                if (Character.isAlphabetic(c)) {
                    count.put(c, count.getOrDefault(c, 0L) + 1);
                }
            }
        } catch (IOException e) {
            System.out.println("Problem with file " + fromFileName);
            return null;
        }

        return count;
    }
}
