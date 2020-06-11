public class Main {
    public static void main(String[] args) {
        PlagAlert checker = new DumbAssAlg();
        //checker.parseFile("src/NP3.txt", "src/NP1.txt");
        checker.parseFile("src/C", "src/D");
        double result = checker.calculateScore();
        System.out.println(result);
    }
}
