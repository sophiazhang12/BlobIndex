
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class Index
{
    private HashMap <String, String> ind;
    File indexFile;

    public Index ()
    {
        //some constructor ig
        ind = new HashMap <String, String> ();
        indexFile = new File ("index");
    }

    //initializes a project
    public void init (String pName) //pName = project name
    {
        
    }

    public void addBlob (String fileName) throws IOException
    {
        Blob b = new Blob (fileName);
        String ogName = b.getName ();
        String sha1 = b.getSHA1 ();

        ind.put (ogName, sha1);

        PrintWriter pw = new PrintWriter (indexFile);

        //updates the entire index... a little excessive, but it works
        for (HashMap.Entry <String, String> entry : ind.entrySet ())
            {
                pw.println (entry.getKey () + " : " + entry.getValue ());
            }

        //pw.println (ogName + " : " + sha1);

        //reader.close();
        pw.close();

    }

    public void removeBlob (String fileName) throws FileNotFoundException
    {
        ind.remove (fileName);

        PrintWriter pw = new PrintWriter (indexFile);
        //updates the entire index... a little excessive, but it works
        for (HashMap.Entry <String, String> entry : ind.entrySet ())
            {
                pw.println (entry.getKey () + " : " + entry.getValue ());
            }
        pw.close ();

    }

    public static void main (String [] args) throws IOException
    {
        Index testList = new Index ();
        testList.addBlob ("testingFile.txt");
        testList.addBlob ("test2.txt");
        testList.removeBlob ("testingFile.txt");
        //testList.removeBlob ("test2.txt");
    }

}