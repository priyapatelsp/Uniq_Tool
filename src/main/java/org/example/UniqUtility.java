package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class UniqUtility {

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

        try {
            // Read input options
            System.out.println("Welcome to uniq tool::");

            System.out.print("Enter options (-c, -d, -u): ");
            String optionsInput = reader.readLine().trim();
            boolean countOption = optionsInput.contains("-c");
            boolean repeatedOption = optionsInput.contains("-d");
            boolean uniqOption = optionsInput.contains("-u");

            // Read input file name
            System.out.print("Enter input file name (or '-' for stdin): ");
            String inputFileName = reader.readLine().trim();

            // Read output file name
            System.out.print("Enter output file name (or leave blank for stdout): ");
            String outputFileName = reader.readLine().trim();

            BufferedReader fileReader = getInputReader(inputFileName);
            PrintWriter fileWriter = getOutputWriter(outputFileName);

            processFile(fileReader, fileWriter, countOption, repeatedOption, uniqOption);

            // Close resources
            fileReader.close();
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void processFile(BufferedReader reader, PrintWriter writer,
                                    boolean countOption, boolean repeatedOption, boolean uniqOption) throws IOException {
        String line;
        String prevLine = null;
        int count = 0;
        boolean isRepeated = false;
        boolean outputLine = false;

        while ((line = reader.readLine()) != null) {
            if (prevLine == null || !line.equals(prevLine)) {
                if (isRepeated && repeatedOption) {
                    if (countOption) {
                        writer.println(count + " " + prevLine);
                    } else {
                        writer.println(prevLine);
                    }
                    outputLine = true;
                } else if (!isRepeated && uniqOption) {
                    if (countOption) {
                        writer.println("1 " + prevLine);
                    } else {
                        writer.println(prevLine);
                    }
                    outputLine = true;
                }
                count = 1;
                isRepeated = false;
            } else {
                count++;
                if (count == 2) {
                    isRepeated = true;
                }
            }
            prevLine = line;
        }

        // Check the last line
        if (prevLine != null) {
            if ((isRepeated && repeatedOption) || (!isRepeated && uniqOption && count == 1)) {
                if (countOption) {
                    writer.println(count + " " + prevLine);
                } else {
                    writer.println(prevLine);
                }
            }
        }
    }

    private static BufferedReader getInputReader(String fileName) throws IOException {
        if (fileName == null || fileName.equals("-")) {
            return new BufferedReader(new InputStreamReader(System.in));
        } else {
            return new BufferedReader(new FileReader(fileName));
        }
    }

    private static PrintWriter getOutputWriter(String fileName) throws IOException {
        if (fileName == null || fileName.isEmpty()) {
            return new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        } else {
            return new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
        }
    }
}
