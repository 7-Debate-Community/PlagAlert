public interface PlagAlert {

    //parse the file into txt
    void parseFile(String relativePath1, String relativePath2);

    //Check if plagiarism exist
    //Return similarity
    int checkPlagiarism(String first, String second);

    double calculateScore();
}
