public class DumbAssAlg implements PlagAlert{
    private TxtFile file1;
    private TxtFile file2;
    public void parseFile(String relativePath1, String relativePath2) {
        file1 = new TxtFile(relativePath1);
        file2 = new TxtFile(relativePath2);
        file1.readFile();
        file2.readFile();
    }

    public double calculateScore() {
        String first = file1.content;
        String second = file2.content;
        int maxLength = Math.max(first.length(), second.length());
        //Can't divide by 0
        if (maxLength == 0) return 1.0d;
        return ((double) (maxLength - checkPlagiarism(first, second))) / (double) maxLength;
    }

    public int checkPlagiarism(String first, String second) {
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
