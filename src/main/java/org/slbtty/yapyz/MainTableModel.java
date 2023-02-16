package org.slbtty.yapyz;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class MainTableModel extends AbstractTableModel {

    private final String[] columnNames = {"path", "score"};
    private List<ResultEntry> results;

    public MainTableModel(){
        results = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return results.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if (columnIndex == 0) {
            return results.get(rowIndex).getPath();

        } else if (columnIndex == 1) {
            return results.get(rowIndex).getScore();
        }

        return null;
    }

    public void resetResults(){
        results.clear();
    };

    public void addEntry(ResultEntry etr){
        results.add(etr);
    }

    public void setResults(List<ResultEntry> results){
        this.results = results;

        if (this.results.size() ==0){
            results.add(new ResultEntry("No result found", 0));
        }

        fireTableDataChanged();
    }

}
