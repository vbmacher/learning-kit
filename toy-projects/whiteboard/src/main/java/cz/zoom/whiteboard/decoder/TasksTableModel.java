package cz.zoom.whiteboard.decoder;

import cz.zoom.whiteboard.task.Task;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TasksTableModel extends AbstractTableModel {
    private final static String[] COLUMNS = {
        "TODO", "In Progress", "Waiting", "DONE"
    };
    private final List<Task> tasks;
    
    public TasksTableModel(List<Task> tasks) {
        this.tasks = tasks;
    }
    
    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }

    public int getRowCount() {
        return tasks.size();
    }

    public int getColumnCount() {
        return COLUMNS.length;
    }

    public Object getValueAt(int row, int column) {
        Task task = tasks.get(row);
        
        switch (column) {
            case 0:
                return task.get("summary");
        }
        return "";
    }
    
}
