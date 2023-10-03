import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;

public class Commit {
    Tree tree;
    String summary;
    String author;
    String time;
    String parent;
    String sha;
    String next;

    public Commit(String summary, String author, String parent) throws IOException {
        createTree();
        time = (new Date()).toString();
        this.summary = summary;
        this.author = author;
        this.parent = parent;
        this.next = "";
        generateSha();
        makeFile();
    }

    public Commit(String summary, String author) throws IOException {
        createTree();
        time = (new Date()).toString();
        this.summary = summary;
        this.author = author;
        this.parent = "";
        this.next = "";
        generateSha();
        makeFile();
    }

    private void makeFile() throws FileNotFoundException {
        File actualFile = new File("objects/" + getSha());
        PrintWriter writer = new PrintWriter(actualFile);
        writer.println(tree.getSha());
        writer.println(parent);
        // this is where next will go
        writer.println(next);
        writer.println(author);
        writer.println(time);
        writer.print(summary);
        writer.close();
    }

    private String createTree() throws IOException {
        tree = new Tree();
        BufferedReader reader = new BufferedReader ("index");
        
        while (reader.ready())
        {
            String line = reader.readLine();
            String namePart = line.substring (line.indexOf (":") + 1);
            namePart = namePart.substring (0, namePart.indexOf (":") - 1);

            tree.addDirectory (namePart); //maybe this is how it works?}
        }
        reader.close();
        
        File theDir = new File("./objects");
        if (!theDir.exists()) 
        {
            theDir.mkdirs();
        }
        tree.write();
        return tree.getSha();
    }

    private void generateSha() throws IOException {
        String value = (tree.getSha() + parent + author + getDate() + summary);
        System.out.println(value);
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

        sha = sha1;
    }

    public String getSha() {
        return sha;
    }

    public String getDate() {
        return time;
    }
}
