import java.io.*;

public class FindFiles {
    private int numberOfDirectories;
    private int numberOfFiles;

    @Override
    public String toString() {
        return "FindFiles{" +
                "numberOfDirectories=" + numberOfDirectories +
                ", numberOfFiles=" + numberOfFiles +
                '}';
    }

    void findFiles(File file) {
        if (file.isFile()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (
                    FileNotFoundException e) {
                System.out.println("Cannot find the file:");
                System.out.println(file.getAbsolutePath());
            } catch (
                    IOException e) {
                System.out.println("There was an error during the process of locating the file");
            }
            numberOfFiles++;
        } else {
            numberOfDirectories++;
            File[] files = file.listFiles();
            for (File file1 : files) {
                findFiles(file1);

            }
        }
    }
}
