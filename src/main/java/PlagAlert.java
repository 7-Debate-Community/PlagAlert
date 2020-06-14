import java.io.IOException;
import java.net.URISyntaxException;

public interface PlagAlert {

    //parse the file into txt
    void parseFile(String relativePath1, String relativePath2);

    //Check if plagiarism exist
    int checkPlagiarism();
    //Calculate and return similarity
    double calculateScore() throws IOException, URISyntaxException;
}
