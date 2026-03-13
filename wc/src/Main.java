import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        String flag = null;
        File file;

        if (args.length == 1) {
            file = new File(args[0]);
        } else if (args.length == 2){
            flag =  args[0];
            file = new File(args[1]);
        } else {
            System.out.println("Incorrect usage");
            return;
        }

        if(!file.exists() || !file.isFile()) {
            System.out.println("File not found");
            return;
        }

        switch (flag) {
            case "-c" -> System.out.println(countBytes(file) + " " + file.getName());
            case "-l" -> System.out.println(countLines(file) + " " + file.getName());
            case "-w" -> System.out.println(countWords(file) + " " + file.getName());
            case "-m" -> System.out.println(countChars(file) + " " + file.getName());
            case null, default -> System.out.println(
                    countLines(file) + " " +
                    countWords(file) + " " +
                    countBytes(file) + " " +
                    file.getName()
            );
        }
    }

    private static long countBytes(File file) {
        return file.length();
    }

    private static long countLines(File file) {
        long lines = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int c;
            while ((c = br.read()) != -1) {
                if (c == '\n') {
                    lines++;
                }
            }
            return lines;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static long countWords(File file) {
        long wordCount = 0;
        boolean previousWasWhitespace = true;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int c;
            while ((c = br.read()) != -1) {
                boolean whiteSpace = Character.isWhitespace(c);
                if (!whiteSpace && previousWasWhitespace) {
                    wordCount++;
                }

                previousWasWhitespace = whiteSpace;
            }
            return wordCount;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static long countChars(File file) {
        long charCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((br.read()) != -1) {
                charCount++;
            }
            return charCount;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}