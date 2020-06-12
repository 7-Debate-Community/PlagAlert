public class Main {
    public static void main(String[] args) {
        PlagAlert checker = new DumbAssAlg();
        checker.parseFile("src/NP1.txt", "src/NP2.txt");
        checker.checkPlagiarism("", "");
        //checker.parseFile("src/C", "src/D");
        double result = checker.calculateScore();
        System.out.println(result);
    }
}
