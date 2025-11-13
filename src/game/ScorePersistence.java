package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * The type Score persistence.
 */
public class ScorePersistence {

    private String outputDir = new File("").getAbsolutePath();
    private String fileName = "highscores.txt";
    private String absolutePath = outputDir + File.separator + fileName;

    private int fileScore;

    /**
     * Instantiates a new Score persistence.
     *
     * @throws Exception the exception
     */
    public ScorePersistence() throws Exception {
        setFileScore();
    }

    /**
     * Update file if higher.
     *
     * @param currentHighScore the current high score
     */
    public void updateFileIfHigher(int currentHighScore) {
        if (shouldUpdate(currentHighScore)) {
//            System.out.println("Updating new high score: " + currentHighScore);
            writeNewHighScoreToFile(currentHighScore);
//        } else {
////            System.out.println(
////                    "File score is: " + fileScore + ", currentScore is: "
// + currentHighScore + " no need to update");
        }
    }

    /**
     * updates the file and writes the new high score.
     *
     * @param currentHighScore the current high score.
     */
    private void writeNewHighScoreToFile(int currentHighScore) {
        try {
            FileWriter myWriter = new FileWriter(absolutePath);
//            System.out.println("Writing to file 'The highest score so far is: " + currentHighScore + "'");
            myWriter.write("The highest score so far is: " + currentHighScore);
            myWriter.close();
//            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * checks whether the score in the file isn't higher than the current given score.
     *
     * @param currentHighScore the current score
     * @return true of false
     */
    private boolean shouldUpdate(int currentHighScore) {
        return this.fileScore < currentHighScore;
    }

    /**
     * creates a new high score file if there is no file.
     *
     * @throws Exception checks if there is no error by creating the file.
     */
    private void setFileScore() throws Exception {
        if (isDirEmpty(Paths.get(outputDir))) {
//            System.out.println("Directory is empty, Creating new file.");
            createNewFile();
        }
        this.fileScore = readFromFile();
    }

    /**
     * checks if there is a folder called HighScores, and creates one if not.
     *
     * @param directory the directory of the project
     * @return true or false
     */
    private boolean isDirEmpty(final Path directory) {
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
            return !dirStream.iterator().hasNext();
        } catch (Exception e) {
            try {
                // if there is no directory called HighScores at the project, create one
                Files.createDirectories(directory);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return true;
        }
    }

    /**
     * creates a new file.
     *
     * @throws IOException an error if there is a problem.
     */
    private void createNewFile() throws IOException {
        new File(absolutePath).createNewFile();
    }

    /**
     * Read from file int.
     *
     * @return the int
     * @throws IOException the io exception
     */
    public int readFromFile() throws IOException {
        String data = "";
        try {
            File myObj = new File(absolutePath);
            if (!myObj.exists()) {
//                System.out.println("HighScore.txt doesn't exist yet, score is 0.");
                return 0;
            }
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        if (data == "") {
//            System.out.println("Reading that file is empty, score is 0.");
            return 0;
        } else {
//            System.out.println("Reading score of " + getDigitsFromString(data) + " from file.");
            return getDigitsFromString(data);
        }
    }

    /**
     * gets the score from the entire line in the text file.
     *
     * @param str the line
     * @return the score
     */
    private int getDigitsFromString(String str) {
        return Integer.parseInt(str.replaceAll("[^0-9]", ""));
    }
}
