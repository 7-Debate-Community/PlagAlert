import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
public class TxtFile {
    private final String path;
    private File file;
    private String content = "";
    private String[] rawSentences;
    private List<String> sentences;
    private String[] words;
    public TxtFile(String relPath) {
        path = relPath;
    }

    /**
     * Read file with given path.
     * @return true if read succeeded
     */
    public boolean readFile() {
        try {
            file = new File(path);
            Scanner reader = new Scanner(file);
            //read each line of the file
            while (reader.hasNextLine()) {
                content = content.concat(reader.nextLine());
            }
            reader.close();
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }

    /**
     * Parse the file into sentences.
     * for future algorithm support
     */
    public void parseSentence() {
        rawSentences = content.split("[.?!]");
        for (String sentence : rawSentences) {
            sentences.add(sentence.trim());
        }
    }

    /**
     * Parse the file into list of words.
     */
    public void parseWords() {
        //split by space
        words = content.split("\\s+");

        //remove punctuation and special char
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].replaceAll("[^\\w]", "").toLowerCase();
        }
    }

    public String[] getWords() {
        return words;
    }

    public String getContent() {
        return content;
    }
}
