import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Tree {

    private static ArrayList<String> local;
    private static String sha;

    public Tree(){
        local = new ArrayList<String>();
    }

    public ArrayList<String> getLocal(){
        return local;
    }

    public void add(String str){
        if (!local.contains(str)){
            local.add(str);
        } else {
            System.out.println("Error: Already in tree");
        }
    }

    public void remove(String str) throws IOException{
        //checks if input is a file or a sha
        //if it is a file it converts it to a sha
        String sha = "";
        if (str.contains(".")){
            sha = Blob.getSHA1(str);
        } else {
            sha = str;
        }

        for (int i = 0; i < local.size(); i++){
            if (local.get(i).contains(sha)){
                local.remove(i);
            }
        }
    }

    //Writes contents of local ArrayList to Tree file with sha;
    public void write() throws IOException{
        FileWriter tree = new FileWriter("objects/tree");
        BufferedWriter writeToTree = new BufferedWriter(tree);

        for (int i = 0; i < local.size() - 1; i++){
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
}

