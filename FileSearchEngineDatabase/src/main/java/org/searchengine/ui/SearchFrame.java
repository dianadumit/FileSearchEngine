package org.searchengine.ui;

import org.searchengine.indexing.FileIndexer;
import org.searchengine.search.FileSearchService;
import org.searchengine.util.FileChooserUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import java.awt.*;
import java.io.*;
import java.time.Instant;

public class SearchFrame extends JFrame {
    private final JTextField searchField;
    private final JTextArea resultArea;
    private final JComboBox<String> formatBox;
    private final JButton indexButton;

    public SearchFrame() {
        setTitle("File Search Engine");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        controlPanel.setBackground(new Color(240, 170, 230));
        controlPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        formatBox = new JComboBox<>(new String[]{"Text", "JSON"});
        formatBox.setSelectedItem("Text");
        controlPanel.add(new JLabel("Report Format:"));
        controlPanel.add(formatBox);

        indexButton = new JButton("Start Indexing");
        controlPanel.add(indexButton);

        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        searchPanel.setBackground(new Color(240, 170, 230));

        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setEnabled(false);
        searchPanel.add(searchField, BorderLayout.NORTH);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scroller = new JScrollPane(resultArea);
        searchPanel.add(scroller, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(controlPanel, BorderLayout.NORTH);
        add(searchPanel, BorderLayout.CENTER);

        indexButton();
        searchField();
    }

    private void indexButton() {
        indexButton.addActionListener(evt -> {
            String format = ((String) formatBox.getSelectedItem()).toLowerCase();
            FileIndexer.setReportFormat(format);

            File dir = FileChooserUtil.selectDirectory("Select folder to index");
            if (dir == null) return;

            indexButton.setEnabled(false);
            formatBox.setEnabled(false);
            searchField.setEnabled(false);
            resultArea.setText("Indexing… please wait.\n");
            SwingWorker<String, Void> worker = new SwingWorker<>() {
                @Override
                protected String doInBackground() {
                    try {
                        FileIndexer.resetMetrics();
                        FileIndexer.clearPreviousRecords();
                        FileIndexer.setStartTime(Instant.now());
                        FileIndexer.indexDirectory(dir);
                        FileIndexer.setEndTime(Instant.now());

                        return FileIndexer.writeReportToFile();
                    } catch (IOException e) {
                        return null;
                    }
                }

                @Override
                protected void done() {
                    String reportPath;
                    try {
                        reportPath = get();
                    } catch (Exception ex) {
                        reportPath = null;
                    }
                    if (reportPath != null) {
                        JOptionPane.showMessageDialog(
                                SearchFrame.this,
                                "Index complete!\nReport written to:\n" + reportPath,
                                "Done",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(
                                SearchFrame.this,
                                "Indexing finished but report failed to write.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    searchField.setEnabled(true);
                    indexButton.setEnabled(true);
                    formatBox.setEnabled(true);
                    resultArea.setText("");
                }
            };
            worker.execute();
        });
    }

    private void searchField() {
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                search();
            }

            public void removeUpdate(DocumentEvent e) {
                search();
            }

            public void changedUpdate(DocumentEvent e) {
                search();
            }

            private void search() {
                String q = searchField.getText().trim();
                if (q.isEmpty()) {
                    resultArea.setText("");
                } else {
                    new SwingWorker<String, Void>() {
                        protected String doInBackground() {
                            return FileSearchService.searchFiles(q);
                        }

                        protected void done() {
                            try {
                                resultArea.setText(get());
                            } catch (Exception ex) {
                                resultArea.setText("Search error.");
                            }
                        }
                    }.execute();
                }
            }
        });
    }
}