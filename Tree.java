import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Scanner;

public class Tree {

    private ArrayList<String> local;
    private String sha;

    public Tree() {
        local = new ArrayList<String>();
    }

    public String getSha() {
        return sha;
    }

    public ArrayList<String> getLocal() {
        return local;
    }

    public void add(String str) {
        if (!local.contains(str)) {
            local.add(str);
        } else {
            System.out.println("Error: Already in tree");
        }
    }

    public void remove(String str) throws IOException {
        // checks if input is a file or a sha
        // if it is a file it converts it to a sha
        String sha = "";
        if (str.contains(".")) {
            sha = Blob.getSHA1(str);
        } else {
            sha = str;
        }

        for (int i = 0; i < local.size(); i++) {
            if (local.get(i).contains(sha)) {
                local.remove(i);
            }
        }
    }

    // Writes contents of local ArrayList to Tree file with sha;
    public void write() throws IOException {
        FileWriter tree = new FileWriter("objects/tree");
        BufferedWriter writeToTree = new BufferedWriter(tree);

        for (int i = 0; i < local.size() - 1; i++) {
            writeToTree.append(local.get(i) + "\n");
        }

        writeToTree.write(local.get(local.size() - 1));

        writeToTree.close();

        String sha = Blob.getSHA1("objects/tree");
        this.sha = sha;

        File tree1 = new File("objects/tree");
        File renamed = new File("objects/" + sha);

        boolean flag = tree1.renameTo(renamed);
    }

    //copied from blob class, written by someone other than me (sophia)
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
    public String getSHA1(String f) throws IOException 
    {
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



    public void addDirectoryHelper (String directoryPath) throws IOException
    {
        //Tree tr = new Tree ();
        File dir = new File (directoryPath);
        dir.mkdirs();

        //recursion part
        for (final File fileEntry : dir.listFiles()) {
            if (fileEntry.isDirectory()) 
            {
                Tree childTree = new Tree ();
                addDirectoryHelper (fileEntry.getPath());
                
                StringBuilder childContents = new StringBuilder ("");
                for (int i = 0; i < childTree.getLocal().size(); i++)
                {
                    childContents.append (local.get(i));
                }
                String value = childContents.toString();
                String childSha1 = "";
                try {
                    MessageDigest digest = MessageDigest.getInstance("SHA-1");
                    digest.reset();
                    digest.update(value.getBytes("utf8"));
                    childSha1 = String.format("%040x", new BigInteger(1, digest.digest()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Blob childTBlob = new Blob (fileEntry.getName()); //blob it?? //recently commented out
                //tr.add ("tree : " + childSha + " : " + fileEntry.getName());
                add ("tree : " + childSha1 + " : " + fileEntry.getName());
                //yo how does recursion work
            } 
            else 
            {
                //String fileContents = read (fileEntry.getName());
                String sha = getSHA1 (fileEntry.getPath());
                Blob bob = new Blob (fileEntry.getName()); //blob it??
                //tr.add ("blob : " + sha + " : " + fileEntry.getName());
                add ("blob : " + sha + " : " + fileEntry.getName());
            }
        }


        
    }
    public String addDirectory (String directoryPath) throws IOException
    {
        
        addDirectoryHelper (directoryPath);
        //add directory into tree
        StringBuilder treeContents = new StringBuilder ("");
        int x = local.size();
        for (int i = 0; i < local.size(); i++)
        {
            treeContents.append (local.get(i) + "\n");
            String str = treeContents.toString();
        }
        
        String value = treeContents.toString();
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
        //sha1 now has the sha1

        PrintWriter pw = new PrintWriter ("objects/" + directoryPath);
        pw.print (treeContents);
        pw.close();

        return sha1; //does this count as a getter??
    }
}
