import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CosSimAlg implements PlagAlert {

    TxtFile file1;
    TxtFile file2;
    //Synonyms checker
    Synonym synonym = new Synonym();
    //Vector representing a doc
    HashMap<String, Integer> file1Vec = new HashMap<>();
    HashMap<String, Integer> file2Vec = new HashMap<>();
    //common words
    String[] common = {"the","of","and","a","to","in","is","you","that","it","he","was","for","on","are","as","with",
            "his","they","I","at","be","this","have","from","or","one","had","by","word","but","not","what","all",
            "were","we","when","your","can","said","there","use","an","each","which","she","do","how","their","if",
            "will","up","other","about","out","many","then","them","these","so","some","her","would","make","like",
            "him","into","time","has","look","two","more","write","go","see","number","no","way","could","people",
            "my","than","first","water","been","call","who","oil","its","now","find","long","down","day","did","get",
            "come","made","may","part"};

    /**
     * Parse the file given and extract content from path.
     * @param relativePath1 relative path of the first file
     * @param relativePath2 relative path of the second file
     */
    public void parseFile(String relativePath1, String relativePath2) {
        file1 = new TxtFile(relativePath1);
        file2 = new TxtFile(relativePath2);
        file1.readFile();
        file2.readFile();
        file1.parseWords();
        file2.parseWords();
    }

    /**
     * represent file by a vector
     * @return 0
     */
    public int checkPlagiarism() {
        constructVector();
        file1Vec.remove("");
        file2Vec.remove("");
        return 0;
    }

    /**
     * Calculates the cosine similarity between two doc.
     * @return cosine similarity.
     * @throws IOException exception
     * @throws URISyntaxException exception
     */
    public double calculateScore() throws IOException, URISyntaxException {

        if (file1Vec == null || file2Vec == null) {
            throw new IllegalArgumentException("Vectors must not be null");
        }

        //get the intersection between two vectors
        final Set<String> intersection = getIntersection();
        final double dotProduct = dot(file1Vec, file2Vec, intersection);
        double d1 = 0.0d;
        for (final Integer value : file1Vec.values()) {
            d1 += Math.pow(value, 2);
        }
        double d2 = 0.0d;
        for (final Integer value : file2Vec.values()) {
            d2 += Math.pow(value, 2);
        }
        double cosSimilarity;
        if (d1 <= 0 || d2 <= 0) {
            return 0;
        } else {
            cosSimilarity = dotProduct / (Math.sqrt(d1) * Math.sqrt(d2));
        }
        return cosSimilarity;
    }

    /**
     * Parse two files into vectors and eliminate common words.
     */
    private void constructVector() {
        String[] doc1 = file1.getWords();
        String[] doc2 = file2.getWords();
        for(int i = 0; i < doc1.length; i++) {
            if(!file1Vec.containsKey(doc1[i]) || file1Vec.get(doc1[i]) == 0) { //word is not in dict1 yet
                file1Vec.put(doc1[i], 1);
            }
            else if(file1Vec.containsKey(doc1[i])) { //word is in dict1
                file1Vec.put(doc1[i], file1Vec.get(doc1[i]) + 1);
            }
        }

        //update counts for doc1 both dictionaries
        for(int i = 0; i < doc2.length; i++) {
            if(!file2Vec.containsKey(doc2[i]) || file2Vec.get(doc2[i]) == 0) { //word is not in dict2 yet
                file2Vec.put(doc2[i], 1);
            }
            else if(file2Vec.containsKey(doc2[i])) { //word is in dict2
                file2Vec.put(doc2[i], file2Vec.get(doc2[i]) + 1);
            }
        }

        //Eliminate common words in both vectors
        eliminateCommonWord();
    }

    /**
     * Finds the intersection between two given vectors.
     * @return a set with strings common to the two given maps.
     * @throws IOException exception
     * @throws URISyntaxException exception
     */
    private Set<String> getIntersection()
            throws IOException, URISyntaxException {

        final Set<String> intersection = new HashSet<>(file1Vec.keySet());
        HashMap<String, Integer> similarWord = new HashMap<>();
        intersection.retainAll(file2Vec.keySet());
        HashMap<String, List<String>> wordSynList = synonym.getSynonyms(file1Vec.keySet());
        //Loop thru words in leftVec
        for (String word : wordSynList.keySet()) {
            //Loop thru synonyms of words in leftVec
            for (String syn : wordSynList.get(word)) {
                //Loop thru words in rightVec
                for (String otherDocWord : file2Vec.keySet()) {
                    //if those were the same then add to intersection
                    if (syn.equalsIgnoreCase(otherDocWord) && !intersection.contains(word)) {
                        int tempVal = file2Vec.get(otherDocWord);
                        similarWord.put(word, tempVal);
                        intersection.add(word);
                    }
                }
            }
        }
        file2Vec.putAll(similarWord);
        return intersection;
    }

    //Compute dot product of two vectors
    private double dot(final HashMap<String, Integer> leftVec,
                       final HashMap<String, Integer> rightVec, Set<String> intersection) {
        long dotProduct = 0;
        for (final String key : intersection) {
            dotProduct += leftVec.get(key) * rightVec.get(key);
        }
        System.out.println("Dot product: " + dotProduct);
        return dotProduct;
    }

    //eliminate common words from two vectors.
    private void eliminateCommonWord() {
        for (String com : common) {
            file1Vec.remove(com);
            file2Vec.remove(com);
        }
    }
}
