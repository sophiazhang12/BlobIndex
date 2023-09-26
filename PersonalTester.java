import java.io.IOException;

public class PersonalTester {
    public static void main (String [] args) throws IOException
    {
        // Index ind = new Index ();
        // ind.init();
        // ind.addBlob ("example.txt");

        Tree tr = new Tree ();
        tr.add ("blob : 0208719e828e84e65fc78ada1a42d6fd98dda797 : example.txt");
        tr.write();

        tr.addDirectory ("documents/doc1");
    }

}
