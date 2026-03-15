import java.io.*;
import java.nio.charset.StandardCharsets;

public class Wc {
    public static void main(String[] args) {

        String flag = null;
        File file = null;
        InputStream inputStream;

        if (args.length == 1) {
            if (args[0].startsWith("-")) {
                flag =  args[0];
                inputStream = System.in;
            } else {
                file = new File(args[0]);
                inputStream =  openInput(file);
                if (inputStream == null) return;
            }

        } else if (args.length == 2) {
            if (args[0].startsWith("-")) {
                flag = args[0];
            } else {
                System.out.println("Incorrect usage");
                return;
            }
            file = new File(args[1]);
            inputStream = openInput(file);
            if (inputStream == null) return;

        } else {
            System.out.println("Incorrect usage");
            return;
        }

        Counts textCounts = analyseText(inputStream);
        String suffix = (file != null) ? " " + file.getName() : "";
        printCounts(flag, textCounts, suffix);
    }

    private static void printCounts(String flag, Counts c, String suffix) {
        switch (flag) {
            case "-c" -> System.out.println(c.bytes + suffix);
            case "-l" -> System.out.println(c.lines + suffix);
            case "-w" -> System.out.println(c.words + suffix);
            case "-m" -> System.out.println(c.chars + suffix);
            case null -> System.out.println(
                    c.lines + " " + c.words + " " + c.bytes + suffix
            );
            default -> System.out.println("Incorrect usage");
        }
    }

    private static InputStream getInputStream(File file) {
        if (!file.exists() || !file.isFile()) {
            throw new RuntimeException("File not found: " + file.getName());
        }
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + file.getName());
        }
    }

    private static Counts analyseText(InputStream inputStream) {
        Counts counts = new Counts();
        boolean previousWasWhitespace = true;

        try {

            byte[] data = readAllBytes(inputStream);
            counts.bytes = data.length;

            String text = new String(data, StandardCharsets.UTF_8);
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                counts.chars++;
                if (c == '\n') {
                    counts.lines++;
                }

                boolean whitespace = Character.isWhitespace(c);
                if (!whitespace && previousWasWhitespace) {
                    counts.words++;
                }
                previousWasWhitespace = whitespace;

            }
        } catch ( IOException e) {
            throw new RuntimeException(e);
        }
        return counts;
    }

    private static byte[] readAllBytes(InputStream inputStream) throws IOException {
        try (inputStream; ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            byte[] chunk  = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(chunk)) != -1) {
                buffer.write(chunk, 0, bytesRead);
            }
            return buffer.toByteArray();
        }
    }

    private static InputStream openInput(File file) {
        try {
            return getInputStream(file);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    static class Counts {
        long bytes;
        long lines;
        long words;
        long chars;
    }
}