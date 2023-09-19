import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class BlobTest {
    @BeforeAll
    static void createFile() throws FileNotFoundException{
        // Utils.addFiles(); //Mac users, run at your own risk
        PrintWriter test = new PrintWriter("test.txt");
        test.print("some content");
        test.close();
    }

    @AfterAll
    //deletes all files after test
    static void deleteEverything(){
        File test = new File("test.txt"); 
        File index = new File("index");
        File objects = new File("objects");
        test.delete();
        index.delete();
        for (File subfile : objects.listFiles()) {
            subfile.delete();
        }
        objects.delete();
    }

    @Test
    void testBlobify() throws IOException {
        Blob toTest = new Blob("test.txt");
        toTest.blobify();

        File testFile = new File("objects/94e66df8cd09d410c62d9e0dc59d3a884e458e05");

        assertTrue(testFile.exists()); //tests if blob with correct file name exists in correct lcoation
    }

    @Test
    void testGetSha1() throws IOException {
        String hash = Blob.getSHA1("test.txt");
        assertEquals("94e66df8cd09d410c62d9e0dc59d3a884e458e05", hash); //tests if generated hash is correct
    }
}