import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class Tree {
    public static void add(String str) throws IOException{
        //initilizes tree file in objects direcotry
        initTree();

        //appends new str to tree file on a new file in objects directory
        Writer output;
        output = new BufferedWriter(new FileWriter("objects/tree", true));
        output.append("\n" + str);
        output.close();

    }

    public static void initTree() throws IOException{
         File tree = new File ("objects/tree");

        //if directory doesn't exist, make a new one
        if (!tree.exists())
        {
            tree.createNewFile();
        }
    }

    public static void main(String[] args) throws IOException{
        add("cum");
    }
}
