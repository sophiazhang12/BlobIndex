import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IndexTest {
    @BeforeAll
    static void createFiles() throws IOException{
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
    void testAddBlob() throws IOException {
        Index testBlob = new Index ();
        testBlob.init ();
        testBlob.addBlob ("test.txt");

        File toTest = new File("objects/94e66df8cd09d410c62d9e0dc59d3a884e458e05");
        assertTrue(toTest.exists()); //tests if blob is added in the correct location
    }

    @Test
    void testInit() throws IOException {
        Index testBlob = new Index();
        testBlob.init();

        // check if the file exists
        File index = new File("index");
        File objects = new File("objects");

        assertTrue(index.exists()); //tests if index is created
        assertTrue(objects.exists()); //tests if objects is created
    }

    @Test
    void testRemoveBlob() throws IOException {
        Index testBlob = new Index();
        testBlob.init();

        testBlob.addBlob("test.txt");

        testBlob.removeBlob("test.txt");

        boolean found = false;

        String lineToLook = "test.txt : 94e66df8cd09d410c62d9e0dc59d3a884e458e05";

        BufferedReader reader = new BufferedReader(new FileReader("index"));
			String line = reader.readLine();

			while (line != null) {
				if (line.equals(lineToLook)){
                    found = true;
                    break;
                }
			}

			reader.close();
        
        assertTrue(!found); //checks to see if removed string is found in index
    }
}
