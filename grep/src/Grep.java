import java.io.*;
import java.nio.charset.StandardCharsets;

public class Grep {
    public static void main(String[] args) {

        if (args.length < 1 || args.length > 2) {
            System.err.println("Incorrect usage");
            System.exit(2);
        }

        String expr = args[0];
        boolean found;

        if (args.length == 2) {
            File file = new File(args[1]);
            found = processPath(file, expr);
        } else {
            InputStream input = System.in;
            found = matchStream(input, expr);
        }

        System.exit(found ? 0 : 1);
    }


    private static boolean matchFile(File file, String expr) {
        boolean found = false;
        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8))
        ) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.contains(expr)) {
                    out.write(line);
                    out.newLine();
                    found = true;
                }
            }
            out.flush();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return found;
    }

    private static boolean matchStream(InputStream input, String expr) {
        boolean found = false;
        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8))
        ) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.contains(expr)) {
                    out.write(line);
                    out.newLine();
                    found = true;
                }
            }
            out.flush();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return found;
    }

    private static boolean processPath(File file, String expr) {
        if (file.isFile()) {
            return matchFile(file, expr);
        }

        if (file.isDirectory()) {
            boolean found = false;

            File[] children = file.listFiles();
            if (children == null) return false;

            for (File child: children) {
                found |= processPath(child, expr);
            }
            return found;
        }
        return false;
    }
}
