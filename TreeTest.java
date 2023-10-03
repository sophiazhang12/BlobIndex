import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TreeTest {
    @BeforeAll
    static void createFilesForTest() throws IOException {
        // Utils.addFiles(); //Mac users, run at your own risk
        PrintWriter test1 = new PrintWriter("test1.txt");
        test1.print("some content");
        test1.close();

        PrintWriter test2 = new PrintWriter("test2.txt");
        test2.print("some more content");
        test2.close();
    }

    @AfterAll
    // deletes all files after test
    static void deleteEverything() {
        File test = new File("test1.txt");
        File test1 = new File("test2.txt");
        File index = new File("index");
        File objects = new File("objects");
        test.delete();
        test1.delete();
        index.delete();
        for (File subfile : objects.listFiles()) {
            subfile.delete();
        }
        objects.delete();
    }

    @Test
    void testAdd() {
        Tree testTree = new Tree();
        String test1 = "blob : 94e66df8cd09d410c62d9e0dc59d3a884e458e05 : test1.txt";
        String test2 = "blob : 4ca8deacbe9ea18450248727171dae4fd03a1e50 : test2.txt";

        testTree.add(test1);
        testTree.add(test2);

        ArrayList<String> localTree = testTree.getLocal();

        assertEquals(test1, localTree.get(0)); // tests if correct string is created in local ArrayList at position 0
        assertEquals(test2, localTree.get(1)); // tests if correct string is created in local ArrayList at position 1
    }

    @Test
    void testRemove() throws IOException {
        Tree testTree = new Tree();
        String test1 = "blob : 94e66df8cd09d410c62d9e0dc59d3a884e458e05 : test1.txt";
        String test2 = "blob : 4ca8deacbe9ea18450248727171dae4fd03a1e50 : test2.txt";

        testTree.add(test1);
        testTree.add(test2);

        testTree.remove("test1.txt");

        ArrayList<String> localTree = testTree.getLocal();

        assertEquals(test2, localTree.get(0)); // tests if correct string is in position 0 of local ArrayList
    }

    @Test
    void testWrite() throws IOException {
        Tree testTree = new Tree();
        String test1 = "blob : 94e66df8cd09d410c62d9e0dc59d3a884e458e05 : test1.txt";
        String test2 = "blob : 4ca8deacbe9ea18450248727171dae4fd03a1e50 : test2.txt";

        testTree.add(test1);
        testTree.add(test2);

        Index coolio = new Index();

        coolio.init();

        testTree.write();

        File testingFile = new File("objects/768a9bdce939a78f02eb69d90aba7c61babb7ff6");
        assertTrue(testingFile.exists()); // tests if correct Tree is made in correct location
    }

    
    //tests addDirectory to make sure that it can add one folder with three files
    @Test
    void testAddDirectoryFiles () throws IOException {
        Tree testTree = new Tree ();
        Index coolio = new Index ();
        coolio.init();
        
        File documents = new File ("documents");
        documents.mkdirs();
        documents.createNewFile();

        File doc1 = new File ("documents/doc1");
        doc1.createNewFile();
        PrintWriter p1 = new PrintWriter ("documents/doc1");
        p1.print ("abra");
        p1.close();

        File doc2 = new File ("documents/doc2");
        doc2.createNewFile();
        PrintWriter p2 = new PrintWriter ("documents/doc2");
        p2.print ("ca");
        p2.close();

        File doc3 = new File ("documents/doc3");
        doc3.createNewFile();
        PrintWriter p3 = new PrintWriter ("documents/doc3");
        p3.print ("dabra");
        p3.close();
        
        String s = testTree.addDirectory ("documents");

        String expected = "cf36151709f2dbd98213cf78e59d838755fcfbb4"; //expected sha of the tree
        assertEquals (expected, s);

    }

    //tests addDirectory to make sure that it can add one folder with three files and two folder
    @Test
    void testAddDirectoryFolders () throws IOException {
        Tree testTree = new Tree ();
        Index coolio = new Index ();
        coolio.init();
        
        // File documents1 = new File ("documents1");
        // documents1.mkdirs();
        // documents1.createNewFile();

        // File doc1 = new File ("documents1/doc1");
        // doc1.createNewFile();
        // PrintWriter p1 = new PrintWriter ("documents1/doc1");
        // p1.print ("abra");
        // p1.close();

        // File doc2 = new File ("documents1/doc2");
        // doc2.createNewFile();
        // PrintWriter p2 = new PrintWriter ("documents1/doc2");
        // p2.print ("ca");
        // p2.close();

        // File doc3 = new File ("documents1/doc3");
        // doc3.createNewFile();
        // PrintWriter p3 = new PrintWriter ("documents1/doc3");
        // p3.print ("dabra");
        // p3.close();

        File dir4 = new File ("documents1/dir4");
        dir4.mkdirs();
        dir4.createNewFile();
            // File doc5 = new File ("documents1/dir4/doc5");
            // doc5.createNewFile();
            // PrintWriter p5 = new PrintWriter ("documents1/dir4/doc5");
            // p5.print ("hello");
            // p5.close();
            
        // File doc6 = new File ("documents1/doc6");
        // doc6.createNewFile ();
        // PrintWriter p6 = new PrintWriter ("documents1/doc6");
        // p6.print ("world");
        // p6.close();


        String s = testTree.addDirectory ("documents1");

        testTree.write ();

        String expected = "1844da3dbb15e4024d044a35c448776bd3fd0740"; //expected sha of the tree
        assertEquals (expected, s);
    }
}
