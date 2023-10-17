import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
    String sha; //sha of the tree
    String next;
    static Commit prevCommit;
    static String prevTrSha;


    public Commit(String summary, String author, String parent) throws IOException {
        createTree();
        time = (new Date()).toString();
        this.summary = summary;
        this.author = author;
        this.parent = parent;
        this.next = "";
        generateSha();
        makeFile();
        prevCommit.next = this.makeFile(); //sets next pointer for previous commit
        prevCommit = this; //so that when you make another commit, this keeps track of the prev one
    }

    //first commit
    public Commit(String summary, String author) throws IOException {
        createTree();
        time = (new Date()).toString();
        this.summary = summary;
        this.author = author;
        this.parent = "";
        this.next = "";
        generateSha();
        makeFile();
        prevCommit = this;
        prevTrSha = ""; //initialize it to empty
    }

    public String makeFile() throws FileNotFoundException { //return sha of commit

        StringBuilder sb = new StringBuilder ("");
        sb.append (tree.getSha() + "\n" + parent + "\n" + next + "\n" + author + "\n" + time + "\n" + summary);
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

        File actualFile = new File("objects/" + sha1);
        PrintWriter writer = new PrintWriter(actualFile);
        writer.println(tree.getSha());
        writer.println(parent);
        // this is where next will go
        writer.println(next);
        writer.println(author);
        writer.println(time);
        writer.print(summary);
        writer.close();

        return sha1;

    }

    private String createTree() throws IOException 
    {
        tree = new Tree();
        BufferedReader reader = new BufferedReader (new FileReader ("index"));
        
        while (reader.ready())
        {
            String line = reader.readLine();
            
            tree.add (line); //just copy index over into the tree
        }
        reader.close();
        
        File theDir = new File("./objects");
        if (!theDir.exists()) 
        {
            theDir.mkdirs();
        }
        tree.write();
        
        PrintWriter pw = new PrintWriter ("index");
        pw.write (""); //empties out the index file
        pw.close();

        tree.add ("tree : " + prevTrSha);

        prevTrSha = tree.getSha(); //moves backwards pointer for future ref
        return tree.getSha();
    }

    private void generateSha() throws IOException {
        String value = (tree.getSha() + "\n" + parent + "\n" + author + "\n" + getDate() + "\n" + summary);
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

    public String givenCommitGetTree (String commitSha) throws IOException
    {
        File commFile = new File ("./objects/" + commitSha);
        if (commFile.exists())
        {
            BufferedReader reader = new BufferedReader (new FileReader (commFile.getPath()));
            String trHash = reader.readLine();
            reader.close();
            return trHash;

        }
        return "No tree found";
    }

    public void updatePrevComm () throws IOException
    {
        prevCommit.next = this.sha;

    }

}
