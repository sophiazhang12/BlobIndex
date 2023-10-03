
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class Index
{
    private HashMap <String, String> ind;

    public Index ()
    {
        //some constructor ig
        ind = new HashMap <String, String> ();

    }

    //initializes a project
    public void init () throws IOException
    {
        //new code
        File f = new File ("objects");

        //if directory doesn't exist, make a new one
        if (!f.exists())
        {
            File dir = new File ("objects");
            dir.mkdirs();
        }

        File in = new File ("index");

        //if index doesn't exist, make a new one
        if (!in.exists())
        {
            //in = new File ("objects", "index");

            in = new File ("index");
            in.createNewFile();
        }
    }

    public void addBlob (String fileName) throws IOException
    {
        Blob b = new Blob (fileName);
        //blobify writes the blob to objects
        b.blobify ();
        String ogName = b.getName ();
        String sha1 = "blob : " + Blob.getSHA1(fileName);

        ind.put (ogName, sha1);

        PrintWriter pw = new PrintWriter ("index");

        //updates the entire index... a little excessive, but it works
        for (HashMap.Entry <String, String> entry : ind.entrySet ())
            {
                pw.println (entry.getValue () + " : " + entry.getKey ());
            }

        //pw.println (ogName + " : " + sha1);

        //reader.close();
        pw.close();

    }
    public void addTree (String fileName) throws IOException
    {
        Tree tr = new Tree ();
        String treeSha = "tree : " + tr.addDirectory (fileName);

        ind.put (fileName, treeSha);

        PrintWriter pw = new PrintWriter ("index");

        //updates the entire index... a little excessive, but it works
        for (HashMap.Entry <String, String> entry : ind.entrySet ())
            {
                pw.println (entry.getValue () + " : " + entry.getKey ());
            }

        pw.close();

    }

    public void removeBlob (String fileName) throws FileNotFoundException
    {
        ind.remove (fileName);

        PrintWriter pw = new PrintWriter ("index");
        //updates the entire index... a little excessive, but it works
        for (HashMap.Entry <String, String> entry : ind.entrySet ())
            {
                pw.println (entry.getKey () + " : " + entry.getValue ());
            }
        pw.close ();

    }
}