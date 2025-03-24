import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        String pathToFiles = selectFolder();
        if (pathToFiles == null) {
            JOptionPane.showMessageDialog(null, "No folder selected. Exiting...");
            System.exit(0);
        }

        JFrame frame = new JFrame("File Search Engine");
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        Color backgroundColor = new Color(240, 170, 230);
        Color searchBarColor = Color.WHITE;
        Color textColor = new Color(50, 50, 50);
        Color resultBackground = new Color(240, 200, 230);

        JLabel titleLabel = new JLabel("File Search Engine", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(240, 200, 250));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(500, 30));
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setBackground(searchBarColor);
        searchField.setForeground(textColor);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Arial", Font.PLAIN, 14));
        resultArea.setBackground(resultBackground);
        resultArea.setForeground(textColor);
        resultArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel titleAndSearchPanel = new JPanel(new BorderLayout());
        titleAndSearchPanel.setBackground(backgroundColor);
        titleAndSearchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        titleAndSearchPanel.add(titleLabel, BorderLayout.NORTH);
        titleAndSearchPanel.add(searchField, BorderLayout.SOUTH);

        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultScrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        frame.add(titleAndSearchPanel, BorderLayout.NORTH);
        frame.add(resultScrollPane, BorderLayout.CENTER);
        frame.getContentPane().setBackground(backgroundColor);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { performSearch(); }
            public void removeUpdate(DocumentEvent e) { performSearch(); }
            public void changedUpdate(DocumentEvent e) { performSearch(); }

            private void performSearch() {
                String query = searchField.getText().trim();
                if (query.isEmpty()) {
                    resultArea.setText("");
                    return;
                }
                SwingWorker<String, Void> worker = new SwingWorker<>() {
                    @Override
                    protected String doInBackground() {
                        return FileSearch.getResult(query);
                    }
                    @Override
                    protected void done() {
                        try {
                            resultArea.setText(get());
                        } catch (Exception ignored) {}
                    }
                };
                worker.execute();
            }
        });

        frame.setVisible(true);
        FileIndexer.clearPreviousRecords();
        FileIndexer findFiles = new FileIndexer();
        findFiles.indexFile(new File(pathToFiles));
        System.out.println("Indexed files in: " + pathToFiles);
        System.out.println(findFiles);
    }

    private static String selectFolder() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a Folder to Index");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }
}
