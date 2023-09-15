import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utils{
    //Don't run
    public static void addFiles(){
        String command = "osascript -e 'tell app \"System Events\" to shut down'";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", command);

            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();

            System.out.println("Exit Code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}