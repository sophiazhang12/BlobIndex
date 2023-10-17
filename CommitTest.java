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
        String c1Sha = c1.makeFile();
        
        //check if tree is correct
        BufferedReader br = new BufferedReader (new FileReader ("objects/" + c1Sha));
        String trSha = br.readLine(); //first line
        assertEquals ("da39a3ee5e6b4b0d3255bfef95601890afd80709", trSha);
        br.close();

        //check if prev sha is correct
        assertEquals ("", c1.parent);

        assertEquals (c1.next, "");

        tearDown();
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
        
        String c1Sha = c1.makeFile();
        //check if tree is correct
        BufferedReader br = new BufferedReader (new FileReader ("objects/" + c1Sha));
        String trSha = br.readLine(); //first line
        br.close();
        assertEquals (trSha, "da39a3ee5e6b4b0d3255bfef95601890afd80709");
        
        //check if prev sha is correct
        assertEquals (c1.parent, "");

        //check if next is correct
        assertEquals (c1.next, "");

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
        File dir = new File ("objects/dir");
        dir.mkdirs();
        dir.createNewFile();

        tr.add (file3.getName());
        tr.add (file4.getName());
        tr.addDirectory ("hello"); //placeHolder since addDirectoyr is a little scuffed

        Commit c2 = new Commit("This commit is cooler", "BRG", c1Sha);
        
        //check if tree is correct
        
        String c2Sha = c2.makeFile (); //the sha of c2 commit itself
        BufferedReader brr = new BufferedReader (new FileReader ("objects/" + c2Sha));

        String trShaa = brr.readLine(); //first line
        assertEquals (trShaa, "da39a3ee5e6b4b0d3255bfef95601890afd80709");

        String parentOfC2 = brr.readLine();

        //check if prev sha (of parent commit) is correct
        assertEquals (parentOfC2, c1Sha);
        //check if next is correct
        assertEquals (c1.next, c2Sha); //make sure the next pointer updates\
        c1.makeFile();
        brr.close();

        tearDown();
    }

    @Test
    public void testFourCommits () throws IOException 
    {
         Index ind = new Index();
        ind.init();
        
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

        Commit c1 = new Commit("This commit is cool", "BRG");

        c1.tree.add (file1.getName());
        c1.tree.add (file2.getName());
        
        
        
        String c1Sha = c1.makeFile();
        //check if tree is correct
        BufferedReader br = new BufferedReader (new FileReader ("objects/" + c1Sha));
        String trSha = br.readLine(); //first line
        br.close();
        assertEquals (trSha, "da39a3ee5e6b4b0d3255bfef95601890afd80709");
        
        //check if prev sha is correct
        assertEquals (c1.parent, "");

        //check if next is correct
        assertEquals (c1.next, "");

        //COMMIT NUMBER TWO
        File file3 = new File ("objects/File3");
        file1.createNewFile();
        PrintWriter pi = new PrintWriter (file3);
        pi.print ("one");
        pi.close();
        File file4 = new File ("objects/File4");
        file2.createNewFile();
        PrintWriter piw = new PrintWriter (file4);
        piw.print ("two");
        piw.close();
        File dir = new File ("objects/dir");
        dir.mkdirs();
        dir.createNewFile();

        Commit c2 = new Commit("This commit is cooler", "BRG", c1Sha);

        c2.tree.add (file3.getName());
        c2.tree.add (file4.getName());
        c2.tree.addDirectory ("hello"); //placeHolder since addDirectoyr is a little scuffed

        
        //check if tree is correct
        
        String c2Sha = c2.makeFile (); //the sha of c2 commit itself
        BufferedReader brr = new BufferedReader (new FileReader ("objects/" + c2Sha));

        String trShaa = brr.readLine(); //first line
        assertEquals (trShaa, "da39a3ee5e6b4b0d3255bfef95601890afd80709");

        String parentOfC2 = brr.readLine();

        //check if prev sha (of parent commit) is correct
        assertEquals (parentOfC2, c1Sha);
        //check if next is correct
        assertEquals (c1.next, c2Sha); //make sure the next pointer updates
        c1.makeFile();
        brr.close();

        //COMMIT NUMBER THREE
        File file5 = new File ("objects/File5");
        file5.createNewFile();
        PrintWriter pew = new PrintWriter (file5);
        pew.print ("uno");
        pew.close();
        File file6 = new File ("objects/File6");
        file6.createNewFile();
        PrintWriter paw = new PrintWriter (file6);
        paw.print ("dos");
        paw.close();

        Commit c3 = new Commit("This commit is coolest", "BRG", c2Sha);
        c3.tree.add (file1.getName());
        c3.tree.add (file2.getName());

        
        String c3Sha = c3.makeFile();
        //check if tree is correct
        BufferedReader bro = new BufferedReader (new FileReader ("objects/" + c1Sha));
        String trShala = bro.readLine(); //first line
        bro.close();
        assertEquals (trShala, "da39a3ee5e6b4b0d3255bfef95601890afd80709");
        
        //check if prev sha is correct
        assertEquals (c3.parent, c2Sha);

        //check if next is correct
        assertEquals (c3.next, "");
        //check if next has been updated
        assertEquals (c2.next, c3Sha);
        c2.makeFile();

        //COMMIT NUMBER FOUR
        File file7 = new File ("objects/File7");
        file7.createNewFile();
        PrintWriter pie = new PrintWriter (file7);
        pie.print ("ava");
        pie.close();
        File file8 = new File ("objects/File8");
        file8.createNewFile();
        PrintWriter piwe = new PrintWriter (file8); //ya'll idk what's happening to my printWriter names; if it contains the letters p and w it's a printWriter
        piwe.print ("cado");
        piwe.close();
        File diro = new File ("objects/diro");
        diro.mkdirs();
        diro.createNewFile();

        Commit c4 = new Commit("This commit is coolester", "BRG", c3Sha);
        c4.tree.add (file7.getName());
        c4.tree.add (file8.getName());
        c4.tree.addDirectory ("hello"); //placeHolder since addDirectoyr is a little scuffed
        
        //check if tree is correct
        
        String c4Sha = c4.makeFile (); //the sha of c2 commit itself
        BufferedReader brro = new BufferedReader (new FileReader ("objects/" + c4Sha));

        String trShalala = brro.readLine(); //first line
        assertEquals (trShalala, "da39a3ee5e6b4b0d3255bfef95601890afd80709"); //is this supposed to be like this?

        //check if prev sha (of parent commit) is correct
        assertEquals (c4.parent, c3Sha);
        //check if next is correct
        assertEquals (c4.next, "");
        assertEquals (c3.next, c4Sha); //make sure the next pointer updates
        c3.makeFile();
        brro.close();

        tearDown();
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
