import java.io.*;
import java.nio.charset.StandardCharsets;

public class Grep {
    public static void main(String[] args) {

        if (args.length == 0) {
            System.err.println("Incorrect usage");
            System.exit(2);
        }

        try (BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(System.out, StandardCharsets.UTF_8))) {

            boolean found;

            if (args[0].equals("-r")) {

                if (args.length < 3) {
                    System.err.println("Incorrect usage");
                    System.exit(2);
                }

                String expr = args[1];
                found = false;

                for (int i = 2; i < args.length; i++) {
                    File file = new File(args[i]);
                    found |= processPath(file, expr, out);
                }

            } else {

                String expr = args[0];

                if (args.length == 2) {
                    File file = new File(args[1]);
                    found = processPath(file, expr, out);
                } else {
                    found = matchStream(System.in, expr, out);
                }
            }

            out.flush();
            System.exit(found ? 0 : 1);

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(2);
        }
    }

    private static boolean matchFile(File file, String expr, BufferedWriter out) {
        boolean found = false;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(expr)) {
                    out.write(file.getPath() + ":" + line);
                    out.newLine();
                    found = true;
                }
            }

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

        return found;
    }

    private static boolean matchStream(InputStream input, String expr, BufferedWriter out) {
        boolean found = false;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(input, StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(expr)) {
                    out.write(line);
                    out.newLine();
                    found = true;
                }
            }

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

        return found;
    }

    private static boolean processPath(File file, String expr, BufferedWriter out) {
        if (file.isFile()) {
            return matchFile(file, expr, out);
        }

        if (file.isDirectory()) {
            boolean found = false;

            File[] children = file.listFiles();
            if (children == null) return false;

            for (File child : children) {
                found |= processPath(child, expr, out);
            }

            return found;
        }

        return false;
    }
}