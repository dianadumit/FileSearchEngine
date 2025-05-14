package org.searchengine;

import org.searchengine.ui.SearchFrame;
import javax.swing.*;


public class FileSearchApp {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      SearchFrame frame = new SearchFrame();
      frame.setVisible(true);
    });
  }
}
