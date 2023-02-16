package org.slbtty.yapyz;


import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.io.IOException;


public class YapyzGUI extends JFrame {

    // platforms
    final static Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    final static UrlHandler urlHandler = new UrlHandler();
    public static IndexSettings indexSettings;
    private static JDialog pathDialog;
    public Indexer indexer;
    // temp variable to hold table's selected row path
    private String p_selected_row_path;

    public YapyzGUI() {

        // macOS things
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        setTitle("Yapyz");
        setSize(600, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        FlatLightLaf.setup();

        Font font = UIManager.getFont( "defaultFont" );
        Font newFont = StyleContext.getDefaultStyleContext().getFont(font.getFamily(), font.getStyle(), 14);
        UIManager.put( "defaultFont", newFont );

        initMainWindow();
        initSettingDialog();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            var mainWindow = new YapyzGUI();
            mainWindow.setVisible(true);
        });
    }

    private void initSettingDialog() {
        pathDialog = new JDialog(this, "Index Paths", true);
        pathDialog.setSize(new Dimension(300, 500));
        pathDialog.setLocationRelativeTo(this);

        var addRemoveBar = Box.createHorizontalBox();
        var mainbox = pathDialog.getContentPane();

        var addBtn = new JButton("add");
        var removeBtn = new JButton("remove");
        var pathModel = new DefaultListModel<String>();
        var pathList = new JList<String>(pathModel);
        var okBtn = new JButton("ok");

        addRemoveBar.add(addBtn);
        addRemoveBar.add(removeBtn);

        mainbox.add(addRemoveBar, BorderLayout.NORTH);
        mainbox.add(pathList, BorderLayout.CENTER);
        mainbox.add(okBtn, BorderLayout.SOUTH);

        // Populate the list from user's current settings
        // todo: this can be write better
        for (var x : indexSettings.indexPaths) {
            pathModel.addElement(x);
        }
        //

        // Event binding
        final JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        addBtn.addActionListener(e -> {
            int ret = fc.showOpenDialog(addBtn);
            if (ret == JFileChooser.APPROVE_OPTION) {
                String selected_dir = fc.getSelectedFile().getAbsolutePath();
                pathModel.addElement(selected_dir);
                indexSettings.addPath(selected_dir);
            }
        });

        removeBtn.addActionListener(e -> {

            indexSettings.removePath(pathList.getSelectedValue());

            // Removal of the selected must be the last step, or you will get invalid values
            pathModel.remove(pathList.getSelectedIndex());
        });

        okBtn.addActionListener(e -> {
            indexSettings.saveToDisk();
            pathDialog.setVisible(false);
        });

    }

    public void initMainWindow() {
        indexSettings = new IndexSettings();
        try {
            indexer = new Indexer();
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
        // Menu

        var menuBar = new JMenuBar();

        var indexControlMenu = new JMenu("Index Control");
        var rebuildIndexMenu = new JMenuItem("Rebuild Index");
        var configIndexMenu = new JMenuItem("Index Config");

        indexControlMenu.add(rebuildIndexMenu);
        indexControlMenu.add(configIndexMenu);

        configIndexMenu.addActionListener(e -> {
            pathDialog.setVisible(true);
        });

        menuBar.add(indexControlMenu);

        setJMenuBar(menuBar);

        var vbox = Box.createVerticalBox();

        // SearchBar
        var searchBar = Box.createHorizontalBox();

        var searchBtn = new JButton("Search");
        var searchTextField = new JTextField();
        searchTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchTextField.getPreferredSize().height));

        searchBar.add(searchBtn);
        searchBar.add(searchTextField);

        vbox.add(searchBar);

        // table
        var mainTableModel = new MainTableModel();
        JTable table = new JTable(mainTableModel);
        vbox.add(table.getTableHeader());
        vbox.add(table);

        add(vbox);

        // tablePopup

        var tableMenu = new JPopupMenu();
        var table_copy_to_clipboard = new JMenuItem("Copy path to clipboard");
        var table_open_file = new JMenuItem("Open file");
        tableMenu.add(table_copy_to_clipboard);
        tableMenu.add(table_open_file);

        // Even register

        table_copy_to_clipboard.addActionListener((ev) -> {
            clipboard.setContents(new StringSelection(p_selected_row_path), null);
        });

        table_open_file.addActionListener((ev) -> {
            urlHandler.open(p_selected_row_path);
        });

        rebuildIndexMenu.addActionListener((ev -> {
            indexer.setPaths(indexSettings.indexPaths);
            try {
                indexer.rebuildIndex();
            } catch (IOException err) {
                throw new RuntimeException(err);
            }
        }));

        Action SearchAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainTableModel.resetResults();
                var result = SearchFiles.simpleTermSearch(searchTextField.getText());
                mainTableModel.setResults(result);
            }
        };

        searchBtn.addActionListener(SearchAction);
        searchTextField.addActionListener(SearchAction);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent ev) {
                tableMenu.show(ev.getComponent(), ev.getX(), ev.getY());
                int row = table.rowAtPoint(ev.getPoint());
                if (row >= 0) {
                    p_selected_row_path = (String) mainTableModel.getValueAt(row, 0);
                }
            }
        });
    }
}