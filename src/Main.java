import java.io.*;

public class Main {
    public static void main(String[] args) {
        FindFiles findFiles = new FindFiles();
        String pathToFiles = "C://Users//dumit//Desktop//myFiles";
        File file = new File(pathToFiles);
        findFiles.findFiles(file);
        System.out.println(findFiles);
    }
}