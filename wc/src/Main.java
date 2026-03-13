import java.io.File;

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

        if (flag.equals("-c")) {
            System.out.println(countBytes(file) + " " + file.getName());
        } else {
            System.out.println("Incorrect usage");
        }
    }

    private static long countBytes(File file) {
        return file.length();
    }
}