package org.searchengine;

import org.searchengine.indexing.FileIndexer;
import org.searchengine.ui.SearchFrame;
import org.searchengine.util.FileChooserUtil;

import javax.swing.*;
import java.io.File;

public class FileSearchApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            File directory = FileChooserUtil.selectDirectory("Select a Folder to Index");
            if (directory == null) {
                JOptionPane.showMessageDialog(null, "No folder selected. Exiting...");
                System.exit(0);
            }

            SearchFrame frame = new SearchFrame();
            frame.setVisible(true);

            FileIndexer.clearPreviousRecords();
            FileIndexer.indexDirectory(directory);
            System.out.println("Indexed files in: " + directory.getAbsolutePath());
        });
    }
}
