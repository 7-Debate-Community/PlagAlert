import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        PlagAlert checker = new CosSimAlg();
        checker.parseFile("src/test1", "src/test2");
        checker.checkPlagiarism();
        double result = checker.calculateScore();
        System.out.println(result);
    }
}
