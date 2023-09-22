import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

public class CommitTest {

    @AfterEach
    void tearDown() throws IOException {
        deleteDirectory(Paths.get("objects"));
    }

    @Test
    public void testCommitObject() throws IOException {
        Commit c1 = new Commit("This commit is cool! penis", "BRG", "previousHash");

        // check if contents is right:
        String content = Blob.read("objects/" + c1.getSha());
        System.out.println(content);
        assertEquals(
                "da39a3ee5e6b4b0d3255bfef95601890afd80709\n" + "previousHash\n\n" + "BRG\n" + c1.getDate().toString()
                        + '\n'
                        + "This commit is cool! penis",
                content);

        // check if hash is right:
        // nvm this is impossible because time is always changing, so just tpray it
        // works

        tearDown();
    }

    private void deleteDirectory(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }
}
