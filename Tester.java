import static org.junit.Assert.*;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Tester {
    @AfterAll
    //deletes all files after test
    static void deleteEverything(){
        File test = new File("test.txt"); 
        File test1 = new File("test1.txt");
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
    @DisplayName("[8] Test if initialize and objects are created correctly")
    void testInitialize() throws Exception {

        // Run the person's code
        Index cool = new Index();
        cool.init();

        // check if the file exists
        File index = new File("index");
        File objects = new File("objects");

        assertTrue(index.exists());
        assertTrue(objects.exists());
    }

    @Test
    @DisplayName("[15] Test if adding a blob works.  5 for sha, 5 for file contents, 5 for correct location")
    void testCreateBlob() throws Exception {
        PrintWriter test = new PrintWriter("test.txt");
        test.print("some content");
        test.close();

        Index testBlob = new Index ();
        testBlob.init ();
        testBlob.addBlob ("test.txt");

        //test Sha
        String fileSha = Blob.getSHA1("test.txt");
        String realSha = "94e66df8cd09d410c62d9e0dc59d3a884e458e05";

        assertEquals(realSha, fileSha);

        //test location
        File testLocation = new File("objects/" + fileSha);

        assertTrue(testLocation.exists());

        //test contents
        String fileContentsReal = Blob.read("test.txt");
        String fielContentsToTest = Blob.read("objects/" + fileSha);

        assertEquals(fileContentsReal, fielContentsToTest);
    }

    @Test
    @DisplayName("[15] Test if Tree works.  5 for add, 5 for remove, 5 for write")
    void testTree() throws Exception{
        PrintWriter test = new PrintWriter("test.txt");
        test.print("some content");
        test.close();

        PrintWriter test1 = new PrintWriter("test1.txt");
        test1.print("this is different content :");
        test1.close();

        Tree toTest = new Tree();

        //Test add
        String add1 = "blob : 94e66df8cd09d410c62d9e0dc59d3a884e458e05 : test.txt";
        String add2 = "blob : 5419b650baae81cd8c5f92a9ad7923fbc43edd2e : test1.txt";

        toTest.add(add1);
        toTest.add(add2);

        ArrayList<String> added = toTest.getLocal();

        String added1 = added.get(0);
        String added2 = added.get(1);

        assertEquals(add1, added1);
        assertEquals(add2, added2);

        //test remove
        toTest.remove("94e66df8cd09d410c62d9e0dc59d3a884e458e05");

        ArrayList<String> removed = toTest.getLocal();
        assertEquals(add2, removed.get(0));

        //test Write
        Index needToInit = new Index();
        needToInit.init();

        toTest.write();

        File writeTest = new File("objects/82a6c4f07204fb95c79650c91d5376d5860ca4e2");

        assertTrue(writeTest.exists());
    }   
}
