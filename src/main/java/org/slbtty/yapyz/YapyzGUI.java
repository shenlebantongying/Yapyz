package org.slbtty.yapyz;


import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;


public class YapyzGUI extends JFrame {

    public static IndexSettings indexSettings;
    public  Indexer indexer;

    public void initMainWindow()  {
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

        indexControlMenu.add(rebuildIndexMenu);

        menuBar.add(indexControlMenu);

        setJMenuBar(menuBar);

        var vbox = Box.createVerticalBox();

        // SearchBar
        var searchBar = Box.createHorizontalBox();

        var searchBtn = new JButton("Search");
        var searchTextField = new JTextField();
        searchTextField.setMaximumSize(
            new Dimension(Integer.MAX_VALUE,searchTextField.getPreferredSize().height));



        searchBar.add(searchBtn);
        searchBar.add(searchTextField);

        vbox.add(searchBar);

        // table
        var mainTableModel = new MainTableModel();
        JTable table = new JTable(mainTableModel);
        vbox.add(table.getTableHeader());
        vbox.add(table);

        add(vbox);

        setTitle("Yapyz");
        setSize(600,800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        rebuildIndexMenu.addActionListener((ev -> {
            indexer.setPaths(indexSettings.indexPaths);
            try{
                indexer.rebuildIndex();
            } catch (IOException err){
                throw new RuntimeException(err);
            }
        }));

        Action SearchAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainTableModel.resetResults();
                mainTableModel.setResults(SearchFiles.simpleTermSearch(searchTextField.getText()));
                mainTableModel.fireTableDataChanged();
            }
        };

        searchBtn.addActionListener(SearchAction);
        searchTextField.addActionListener(SearchAction);
    }

    public YapyzGUI() {
        FlatIntelliJLaf.setup();
        initMainWindow();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            var mainWindow = new YapyzGUI();
            mainWindow.setVisible(true);
        });
    }
}