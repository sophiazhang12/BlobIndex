import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;

public class BlobIndex 
{
    public BlobIndex ()
    {
        //some constructor ig?
    }

    //turns file on disk into a blob
    public static void blobify (String fileName) throws IOException
    {
        String sha1 = getSHA1 (fileName); //sha1 will be the name of the new file

        PrintWriter writer = new PrintWriter (sha1);

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
    public static String getSHA1 (String fileName) throws IOException
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
        //System.out.println (getSHA1 ("testingFile.txt"));
        blobify ("testingFile.txt");
    }

}