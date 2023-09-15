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
    static void createFilesForTest() throws IOException{
        Utils.addFiles();
        PrintWriter test1 = new PrintWriter("test1.txt");
        test1.print("some content");
        test1.close();

        PrintWriter test2 = new PrintWriter("test2.txt");
        test2.print("some more content");
        test2.close();
    }

    @AfterAll
    //deletes all files after test
    static void deleteEverything(){
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

        ArrayList<String> localTree= testTree.getLocal();

        assertEquals(test1, localTree.get(0));
        assertEquals(test2, localTree.get(1));
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

        assertEquals(test2, localTree.get(0));
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
        assertTrue(testingFile.exists());
    }
}
