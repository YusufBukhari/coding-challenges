import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Incorrect usage");
            return;
        }

        String flag =  args[0];
        File file = new File(args[1]);

        if(!file.exists() || !file.isFile()) {
            System.out.println("File not found");
            return;
        }

        if ("-c".equals(flag)) {
            System.out.println(countBytes(file) + " " + file.getName());
        } else if ("-l".equals(flag)) {
            System.out.println(countLines(file) + " " + file.getName());
        } else if ("-w".equals(flag)) {
            System.out.println(countWords(file) + " " + file.getName());
        } else {
            System.out.println("Incorrect usage");
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
}