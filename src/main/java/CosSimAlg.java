import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CosSimAlg implements PlagAlert {

    TxtFile file1;
    TxtFile file2;
    //Vector representing a doc
    HashMap<String, Integer> file1Vec = new HashMap<String, Integer>();
    HashMap<String, Integer> file2Vec = new HashMap<String, Integer>();
    //common words
    String[] common = {"the","of","and","a","to","in","is","you","that","it","he","was","for","on","are","as","with","his","they","I","at","be","this","have","from","or","one","had","by","word","but","not","what","all","were","we","when","your","can","said","there","use","an","each","which","she","do","how","their","if","will","up","other","about","out","many","then","them","these","so","some","her","would","make","like","him","into","time","has","look","two","more","write","go","see","number","no","way","could","people","my","than","first","water","been","call","who","oil","its","now","find","long","down","day","did","get","come","made","may","part"};


    public void parseFile(String relativePath1, String relativePath2) {
        file1 = new TxtFile(relativePath1);
        file2 = new TxtFile(relativePath2);
        file1.readFile();
        file2.readFile();
        file1.parseWords();
        file2.parseWords();
    }

    public int checkPlagiarism(String first, String second) {
        constructVector();
        constructVector();
        file1Vec.remove("");
        file2Vec.remove("");
        if (file1Vec.keySet().size() != file2Vec.keySet().size())
            return 0;
        return 1;
    }

    //Calculates the cosine similarity between two doc
    public double calculateScore() {
        if (file1Vec == null || file2Vec == null) {
            throw new IllegalArgumentException("Vectors must not be null");
        }

        final Set<String> intersection = getIntersection(file1Vec, file2Vec);
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

//            if(!file2Vec.containsKey(doc1[i])) { //word is not in dict2 yet
//                file2Vec.put(doc1[i], 0);
//            }
        }

        //update counts for doc1 both dictionaries
        for(int i = 0; i < doc2.length; i++) {
            if(!file2Vec.containsKey(doc2[i]) || file2Vec.get(doc2[i]) == 0) { //word is not in dict2 yet
                file2Vec.put(doc2[i], 1);
            }
            else if(file2Vec.containsKey(doc2[i])) { //word is in dict2
                file2Vec.put(doc2[i], file2Vec.get(doc2[i]) + 1);
            }

//            if(!file1Vec.containsKey(doc2[i])) { //word is not in dict1
//                file1Vec.put(doc2[i], 0);
//            }
        }
    }

    //Returns a set with strings common to the two given maps
    private Set<String> getIntersection(final HashMap<String, Integer> leftVec,
                                        final HashMap<String, Integer> rightVec) {
        final Set<String> intersection = new HashSet<String>(leftVec.keySet());
        intersection.retainAll(rightVec.keySet());
        for (String com : common) {
            if (intersection.contains(com)) {
                intersection.remove(com);
            }
        }
        return intersection;
    }

    //Compute doc product of two vectors
    private double dot(final HashMap<String, Integer> leftVec,
                       final HashMap<String, Integer> rightVec, Set<String> intersection) {
        long dotProduct = 0;
        for (final String key: intersection) {
            dotProduct += leftVec.get(key) * rightVec.get(key);
        }
        System.out.println("Dot product: " + dotProduct);
        return dotProduct;
    }
}
