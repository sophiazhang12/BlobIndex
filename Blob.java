import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;


public class Blob 
{
    private static String fileName; //the original name of the file (unhashed)

    public Blob (String fName)
    {
        //some constructor ig?
        fileName = fName;
    }

    public String getName ()
    {
        return fileName;
    }

    //turns file on disk into a blob
    public void blobify () throws IOException
    {
        String sha1 = getSHA1 (); //sha1 will be the name of the new file

        String dirName = "objects"; /* something to pull specified dir from input */;
        //String fName = sha1;
        File dir = new File (dirName);
        File actualFile = new File (dirName, sha1); //file you write to
            //dirName should become directory, sha1 should become file under directory
        dir.mkdirs();

        PrintWriter writer = new PrintWriter (actualFile.getName());

        BufferedReader reader = new BufferedReader (new FileReader (fileName));
        StringBuilder sb = new StringBuilder ("");

        while (reader.ready())
        {
            sb.append ((char) reader.read());
        }
        reader.close();

        writer.println (sb.toString());
        writer.close();

    }

    //reads in a file's content's and returns the sha1 of it
    //imma be honest i don't really know why this works, but it runs, soooooo
    public String getSHA1 () throws IOException
    {
        BufferedReader reader = new BufferedReader (new FileReader (fileName));
        StringBuilder sb = new StringBuilder ("");

        while (reader.ready())
        {
            sb.append ((char) reader.read());
        }
        reader.close();

        String value = sb.toString ();

		String sha1 = "";
		
		// With the java libraries
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
	        digest.reset();
	        digest.update(value.getBytes("utf8"));
	        sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
		} catch (Exception e){
			e.printStackTrace();
		}

		return sha1;

    }

    public static void main (String [] args) throws IOException
    {
        Blob bob = new Blob ("testingFile.txt");
        //System.out.println (getSHA1 ("testingFile.txt"));
        bob.blobify ();
    }

}