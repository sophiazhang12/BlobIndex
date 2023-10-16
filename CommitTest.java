import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

public class CommitTest {

    @AfterEach
    void tearDown() throws IOException {
        deleteDirectory(Paths.get("objects"));
    }

    @Test
    public void testCommitObject() throws IOException {
        Index ind = new Index();
        ind.init();
        Commit c1 = new Commit("This commit is cool", "BRG", "previousHash");

        // check if contents is right:
        String content = Blob.read("objects/" + c1.getSha());
        System.out.println(content);
        assertEquals(
                "da39a3ee5e6b4b0d3255bfef95601890afd80709\n" + "previousHash\n\n" + "BRG\n" + c1.getDate().toString()
                        + '\n'
                        + "This commit is cool!",
                content);

        // check if hash is right:
        // nvm this is impossible because time is always changing, so just tpray it
        // works

        tearDown();
    }

    @Test
    public void createOneCommit () throws IOException 
    {
        Index ind = new Index();
        ind.init();
        Tree tr = new Tree ();
        File file1 = new File ("objects/File1");
        file1.createNewFile();
        PrintWriter pw = new PrintWriter (file1);
        pw.print ("hello");
        pw.close();
        File file2 = new File ("objects/File2");
        file2.createNewFile();
        PrintWriter pww = new PrintWriter (file2);
        pww.print ("world");
        pww.close();

        tr.add (file1.getName());
        tr.add (file2.getName());

        Commit c1 = new Commit("This commit is cool", "BRG");
        
        //check if tree is correct
        BufferedReader br = new BufferedReader (new FileReader (c1.makeFile()));
        String trSha = br.readLine(); //first line
        assertEquals (trSha, "9fc018a74703bf28e8da54dcd368197d01e256c0");
        
        //check if prev sha is correct
        assertEquals (Commit.prevTrSha, null);

        assertEquals (c1.next, null);

    }

    @Test
    public void createTwoCommit () throws IOException 
    {
        Index ind = new Index();
        ind.init();
        Tree tr = new Tree ();
        File file1 = new File ("objects/File1");
        file1.createNewFile();
        PrintWriter pw = new PrintWriter (file1);
        pw.print ("hello");
        pw.close();
        File file2 = new File ("objects/File2");
        file2.createNewFile();
        PrintWriter pww = new PrintWriter (file2);
        pww.print ("world");
        pww.close();

        tr.add (file1.getName());
        tr.add (file2.getName());

        Commit c1 = new Commit("This commit is cool", "BRG");
        
        String c1Contents = c1.makeFile();
        //check if tree is correct
        BufferedReader br = new BufferedReader (new FileReader (c1Contents));
        String trSha = br.readLine(); //first line
        assertEquals (trSha, "9fc018a74703bf28e8da54dcd368197d01e256c0");
        
        //check if prev sha is correct
        assertEquals (Commit.prevTrSha, null);

        //check if next is correct
        assertEquals (c1.next, null);

        File file3 = new File ("objects/File3");
        file1.createNewFile();
        PrintWriter pi = new PrintWriter (file3);
        pi.print ("hello");
        pi.close();
        File file4 = new File ("objects/File4");
        file2.createNewFile();
        PrintWriter piw = new PrintWriter (file4);
        piw.print ("world");
        piw.close();

        tr.add (file3.getName());
        tr.add (file4.getName());

        Commit c2 = new Commit("This commit is cooler", "BRG");
        
        //check if tree is correct
        
        BufferedReader brr = new BufferedReader (new FileReader (c2.makeFile()));

        String trShaa = brr.readLine(); //first line
        assertEquals (trShaa, "9fc018a74703bf28e8da54dcd368197d01e256c0");
        brr.close();

        //check if prev sha is correct
        assertEquals (Commit.prevTrSha, c1.sha);

        //check if next is correct
        assertEquals (c1.next, c2.sha); //make sure the next pointer updates

    }

    private void deleteDirectory(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }
}
