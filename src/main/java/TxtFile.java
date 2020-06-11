import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
public class TxtFile {
    private final String path;
    private File file;
    public  String content = "";
    private String[] rawSentences;
    private List<String> sentences;
    public TxtFile(String relPath) {
        path = relPath;
    }

    public boolean readFile() {
        try {
            file = new File(path);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                content = content.concat(reader.nextLine());
            }
            reader.close();
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }

    private void parseTxt() {
        rawSentences = content.split("[.?!]");
        for (String sentence : rawSentences) {
            sentences.add(sentence.trim());
        }
    }
}
