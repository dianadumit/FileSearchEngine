import java.io.*;

public class Main {
    public static void main(String[] args) {
        FileIndexer.clearPreviousRecords();
        FileIndexer findFiles = new FileIndexer();
        String pathToFiles = "C://Users//dumit//Desktop//myFiles";
        File file = new File(pathToFiles);
        findFiles.indexFile(file);
        System.out.println(findFiles);
    }
}