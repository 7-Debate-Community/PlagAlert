import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;

public class Main {

    //TODO: Add input and output file
    // the directory to where all files are stored
    public static final String FOLDER_PATH = "";
    // defined the path to result file
    public static final String RESULT_PATH = "";

    public static void main(String[] args) throws Exception {
        CosSimAlg checker = new CosSimAlg();
        // Declare n array to store all the files in the folders
        File[] fileList = new File(FOLDER_PATH).listFiles();
        assert fileList != null;
        // Declare a variable to write to the output CSV file
        PrintWriter output = new PrintWriter(RESULT_PATH);
        // add csv header
        output.print("file name" + ",");
        for (File file : fileList) {
            output.print(file.getName() + ",");
        }
        output.print("\n");
        // For every file in the duplicate-list
        for (File duplicate : fileList) {
            // If the duplicate is a file
            if (duplicate.isFile()) {
                // Output the file-name to the csv file
                output.print(duplicate.getName() + ",");
                // For every file in the origin-list
                for (File origin : fileList) {
                    // skip checking current file
                    if (origin.equals(duplicate)) {
                        output.print("N/A" + ",");
                        continue;
                    }
                    // If it is a file, then calculate the duplicate's plagiarism against this one and write it to the CSV
                    if (origin.isFile()) {
                        output.print(checker.calculateScore(duplicate.getPath(), origin.getPath()) + ",");
                    }
                }
                // Move to the next line.
                output.print("\n");
            }
        }
        // Close the output file
        output.close();
    }
}
