import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Scanner;

public class Blob {
    private static String fileName; // the original name of the file (unhashed)

    public Blob(String fName) {
        // some constructor ig?
        fileName = fName;
    }

    public String getName() {
        return fileName;
    }

    // turns file on disk into a blob and writes to objects
    public void blobify() throws IOException {
        // initlizes before adding
        Index cool = new Index();
        cool.init();

        // All code below is to write to objects file

        String sha1 = getSHA1(fileName);

        File actualFile = new File("objects/" + sha1);

        PrintWriter writer = new PrintWriter(actualFile);

        BufferedReader reader = new BufferedReader(new FileReader(getName()));
        StringBuilder sb = new StringBuilder("");

        while (reader.ready()) {
            sb.append((char) reader.read());
        }
        reader.close();

        writer.println(sb.toString());
        writer.close();
    }

    // reads in a file's content's and returns the sha1 of it
    // imma be honest i don't really know why this works, but it runs, soooooo
    public static String getSHA1(String f) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        StringBuilder sb = new StringBuilder("");

        while (reader.ready()) {
            sb.append((char) reader.read());
        }
        reader.close();

        String value = sb.toString();

        String sha1 = "";

        // With the java libraries
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(value.getBytes("utf8"));
            sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sha1;
    }

    // NEW METHOD!!!!!!
    // Reads a file and returns it as a String
    public static String read(String txt) {
        String content = "";
        try {
            File myObj = new File(txt);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                content = content + data;
                if (myReader.hasNextLine()) {
                    content += '\n';
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return content;
    }
}