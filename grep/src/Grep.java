import java.io.*;
import java.nio.charset.StandardCharsets;

public class Grep {
    public static void main(String[] args) {

        if (args.length < 1 || args.length > 2) {
            System.out.println("Incorrect usage");
            System.exit(2);
        }

        String expr = args[0];
        InputStream input;

        if (args.length == 2) {
            try {
                input = new FileInputStream(args[1]);
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
                System.exit(2);
                return;
            }
        } else {
            input = System.in;
        }

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

        System.exit(found ? 0 : 1);
    }
}
