// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ExcelDataManip.java

package jumpers;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class ExcelDataManip
{

    public ExcelDataManip()
    {
    }

    private static double getNum_i(ArrayList excelData, int i)
    {
        String nStr = (String)((ArrayList)excelData.get(i)).get(2);
        return Double.parseDouble(nStr.substring(8));
    }

    public static double getW_ij(ArrayList excelData, int i, int j)
    {
        long x1 = (long)Double.parseDouble(((ArrayList)excelData.get(i)).get(6).toString());
        long x2 = (long)Double.parseDouble(((ArrayList)excelData.get(j)).get(6).toString());
        long y1 = (long)Double.parseDouble(((ArrayList)excelData.get(i)).get(7).toString());
        long y2 = (long)Double.parseDouble(((ArrayList)excelData.get(j)).get(7).toString());
        long c = (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
        double dist = Math.sqrt(c);
        double num = Math.abs(getNum_i(excelData, i) - getNum_i(excelData, j));
        return dist + num * 1000D;
    }

    public static ArrayList parseExcelData(ArrayList ed, int Tcol)
    {
        if(ed == null)
            return null;
        ArrayList a = new ArrayList();
        for(int i = 0; i < ed.size(); i++)
        {
            ArrayList row = (ArrayList)ed.get(i);
            String tc = "";
            if(row.get(5) instanceof Double)
                tc = String.valueOf(((Double)row.get(5)).intValue());
            else
                tc = String.valueOf(row.get(5));
            if(tc.equals(String.valueOf(Tcol)))
                a.add(row);
        }

        return a;
    }

    public static void rollDataUntilFive(ArrayList ed, boolean start)
    {
        if(ed == null || ed.size() == 0)
            return;
        int c = start ? 0 : ed.size() - 1;
        for(int i = 0; i < ed.size(); i++)
        {
            if(getNum_i(ed, c) == 5D)
                continue;
            Object row = ed.get(0);
            for(int j = 0; j < ed.size() - 1; j++)
                ed.set(j, ed.get(j + 1));

            ed.set(ed.size() - 1, row);
        }

    }

    public static ArrayList readExcelData(String fileName, int startRow, int colsCount)
    {
        if(fileName == null || fileName.equals(""))
            return null;
        ArrayList excelData = new ArrayList();
        try
        {
            InputStream input = new FileInputStream(fileName);
            POIFSFileSystem fs = new POIFSFileSystem(input);
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);
            Iterator rows = sheet.rowIterator();
            excelData.clear();
            boolean ee = false;
            do
            {
                if(!rows.hasNext())
                    break;
                HSSFRow row = (HSSFRow)rows.next();
                if(row.getRowNum() < startRow - 1)
                    continue;
                ArrayList rr = new ArrayList();
                int currentCol = 0;
                Iterator cells = row.cellIterator();
                do
                {
                    if(!cells.hasNext() || currentCol >= colsCount)
                        break;
                    HSSFCell cell = (HSSFCell)cells.next();
                    currentCol++;
                    switch(cell.getCellType())
                    {
                    case 0: // '\0'
                        Double vv = Double.valueOf(cell.getNumericCellValue());
                        String ss = String.valueOf(vv.longValue());
                        rr.add(ss);
                        break;

                    default:
                        String s = cell.getRichStringCellValue().getString();
                        if(currentCol == 1 && s.equals(""))
                            ee = true;
                        else
                            rr.add(s);
                        break;
                    }
                } while(!ee);
                if(ee)
                    break;
                excelData.add(rr);
            } while(true);
            input.close();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        return excelData;
    }

    public static void writeExcelData(ArrayList ed, String fn, int startRow)
    {
        if(ed == null)
            return;
        if(fn == null || fn.equals(""))
            return;
        try
        {
            HSSFWorkbook wb;
            HSSFSheet sheet;
            try
            {
                POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fn));
                wb = new HSSFWorkbook(fs);
                sheet = wb.getSheetAt(0);
            }
            catch(Exception e)
            {
                wb = new HSSFWorkbook();
                sheet = wb.createSheet();
            }
            for(int i = 0; i < ed.size(); i++)
            {
                HSSFRow row = null;
                try
                {
                    row = sheet.getRow((i + startRow) - 1);
                }
                catch(Exception e) { }
                if(row == null)
                    row = sheet.createRow((i + startRow) - 1);
                ArrayList aRow = (ArrayList)ed.get(i);
                for(short j = 0; j < aRow.size(); j++)
                {
                    HSSFCell cell = null;
                    try
                    {
                        cell = row.getCell(j);
                    }
                    catch(Exception e) { }
                    if(cell == null)
                        cell = row.createCell(j);
                    Object val = aRow.get(j);
                    if(val != null)
                        cell.setCellValue(new HSSFRichTextString((String)val));
                }

            }

            FileOutputStream output = new FileOutputStream(fn);
            wb.write(output);
            output.close();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private static final int XCOL = 6;
    private static final int YCOL = 7;
    private static final int nameCOL = 2;
    private static final int TCOL = 5;
}
