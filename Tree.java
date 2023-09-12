import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.Writer;

public class Tree {
    //Instead of using a local hashmap I use the tree file
    //itself as a hashmap. This makes it easier because 
    //a tree input can either have 2 or 3 pieces of information

    public static void add(String str) throws IOException{
        //initilizes tree file in objects direcotry
        initTree();

        //appends new str to tree file on a new file in objects directory

        try (FileWriter file = new FileWriter("objects/tree", true);
            BufferedWriter b = new BufferedWriter(file);
            PrintWriter p = new PrintWriter(b);){
                p.println(str);
        }
    }

    public static void initTree() throws IOException{
         File tree = new File ("objects/tree");

        //if directory doesn't exist, make a new one
        if (!tree.exists())
        {
            tree.createNewFile();
        }
    }

    public static void remove(String str) throws IOException{
        //checks if input is a file or a sha
        //if it is a file it converts it to a sha
        String sha = "";
        if (str.contains("s")){
            sha = Blob.getSHA1(str);
        }
        sha = str;

        //copied the code below from: https://stackoverflow.com/questions/1377279/find-a-line-in-a-file-and-remove-it
        //Made changes in criteria for removal.

        File inputFile = new File("objects/tree");
        File tempFile = new File("objects/TempFile");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String currentLine;

        while((currentLine = reader.readLine()) != null) {
            //Creates substing of just the sha hash in the line
            //Checks saved sha variable against the sha in the file line
            String toCheck = currentLine.substring(currentLine.indexOf(":") + 2, currentLine.indexOf(":") + 42);
            if(toCheck.equals(sha)) continue;
            writer.write(currentLine + System.getProperty("line.separator"));
        }
        writer.close(); 
        reader.close(); 
        boolean successful = tempFile.renameTo(inputFile);
    }

    public static void main(String[] args) throws IOException{
        add("a : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
        remove("bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
    }
}
