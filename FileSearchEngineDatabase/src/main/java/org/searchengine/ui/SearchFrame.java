package org.searchengine.ui;

import org.searchengine.search.FileSearchService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class SearchFrame extends JFrame {

    private final JTextField searchField;
    private final JTextArea resultArea;

    public SearchFrame() {
        setTitle("File Search Engine");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center the window

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 170, 230));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("File Search Engine", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(240, 200, 250));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(500, 30));
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setBackground(Color.WHITE);
        searchField.setForeground(new Color(50, 50, 50));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Arial", Font.PLAIN, 14));
        resultArea.setBackground(new Color(240, 200, 230));
        resultArea.setForeground(new Color(50, 50, 50));
        resultArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultScrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JPanel titleAndSearchPanel = new JPanel(new BorderLayout());
        titleAndSearchPanel.setBackground(new Color(240, 170, 230));
        titleAndSearchPanel.add(titleLabel, BorderLayout.NORTH);
        titleAndSearchPanel.add(searchField, BorderLayout.SOUTH);

        mainPanel.add(titleAndSearchPanel, BorderLayout.NORTH);
        mainPanel.add(resultScrollPane, BorderLayout.CENTER);
        add(mainPanel);

        addSearchListener();
    }

    private void addSearchListener() {
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                performSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                performSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                performSearch();
            }

            private void performSearch() {
                String query = searchField.getText().trim();
                if (query.isEmpty()) {
                    resultArea.setText("");
                    return;
                }
                SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
                    @Override
                    protected String doInBackground() {
                        return FileSearchService.searchFiles(query);
                    }

                    @Override
                    protected void done() {
                        try {
                            resultArea.setText(get());
                        } catch (Exception e) {
                            resultArea.setText("Error retrieving results.");
                        }
                    }
                };
                worker.execute();
            }
        });
    }
}
