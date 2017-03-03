// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ExcelTableModel.java

package jumpers.gui;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class ExcelTableModel extends AbstractTableModel
{

    public ExcelTableModel(ArrayList excelData)
    {
        this.excelData = excelData;
    }

    public int getRowCount()
    {
        return excelData.size();
    }

    public int getColumnCount()
    {
        if(excelData.size() > 0)
            return ((ArrayList)excelData.get(0)).size();
        else
            return 0;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return ((ArrayList)excelData.get(rowIndex)).get(columnIndex);
    }

    private ArrayList excelData;
}
