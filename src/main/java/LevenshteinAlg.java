public class LevenshteinAlg {
    private TxtFile file1;
    private TxtFile file2;
    //content in first file
    private String first;
    //content in second file
    private String second;

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
        first = file1.getContent().toLowerCase();
        second = file2.getContent().toLowerCase();
    }

    /**
     * Calculate the similarity between two files.
     * @return Levenshtein similarity
     */
    public double calculateScore() {
        int maxLength = Math.max(first.length(), second.length());
        //Can't divide by 0
        if (maxLength == 0) return 1.0d;
        return ((double) (maxLength - checkPlagiarism())) / (double) maxLength;
    }

    /**
     * Check for plagiarism using Levenshtein
     * @return ~
     */
    public int checkPlagiarism() {
        first = first.toLowerCase();
        second = second.toLowerCase();

        int[] costs = new int[second.length() + 1];
        for (int i = 0; i <= first.length(); i++) {
            int previousValue = i;
            for (int j = 0; j <= second.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                }
                else if (j > 0) {
                    int useValue = costs[j - 1];
                    if (first.charAt(i - 1) != second.charAt(j - 1)) {
                        useValue = Math.min(Math.min(useValue, previousValue), costs[j]) + 1;
                    }
                    costs[j - 1] = previousValue;
                    previousValue = useValue;

                }
            }
            if (i > 0) {
                costs[second.length()] = previousValue;
            }
        }
        return costs[second.length()];
    }
}
