import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

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

    public void addDirectoryHelper (String directoryPath) throws IOException
    {
        Tree tr = new Tree ();
        File dir = new File (directoryPath);
        dir.mkdirs();

        //recursion part
        for (final File fileEntry : dir.listFiles()) {
            if (fileEntry.isDirectory()) 
            {
                Tree childTree = new Tree ();
                addDirectoryHelper (fileEntry.getAbsolutePath());
                
                StringBuilder childContents = new StringBuilder ("");
                for (int i = 0; i < childTree.getLocal().size(); i++)
                {
                    childContents.append (local.get(i));
                }
                String childSha = Blob.getSHA1(childContents.toString());
                Blob childTBlob = new Blob (fileEntry.getName()); //blob it??
                tr.add ("tree : " + childSha + " : " + fileEntry.getName());
            } 
            else 
            {
                String fileContents = Blob.read (fileEntry.getAbsolutePath());
                String sha = Blob.getSHA1 (fileContents);
                Blob bob = new Blob (fileEntry.getName()); //blob it??
                tr.add ("blob: " + sha + " : " + fileEntry.getName());
            }
        }
        
    }
    public String addDirectory (String directoryPath) throws IOException
    {
        addDirectoryHelper (directoryPath);
        //add directory into tree
        StringBuilder treeContents = new StringBuilder ("");
        for (int i = 0; i < local.size(); i++)
        {
            treeContents.append (local.get(i));
        }
        String SHA1 = Blob.getSHA1(treeContents.toString());
        
        PrintWriter pw = new PrintWriter ("objects/" + directoryPath);
        pw.print (SHA1);
        pw.close();

        return SHA1; //does this count as a getter??
    }
}
