package org.searchengine.util;

import javax.swing.*;
import java.io.File;

public class FileChooserUtil {

  public static File selectDirectory(String title) {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle(title);
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    int result = fileChooser.showOpenDialog(null);
    if (result == JFileChooser.APPROVE_OPTION) {
      return fileChooser.getSelectedFile();
    }
    return null;
  }
}
