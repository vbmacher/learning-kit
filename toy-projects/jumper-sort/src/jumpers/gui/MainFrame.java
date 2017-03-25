// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MainFrame.java

package jumpers.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import jumpers.ExcelDataManip;
import jumpers.Genetic;

// Referenced classes of package jumpers.gui:
//            ExcelTableModel, AboutDialog

public class MainFrame extends JFrame
{

    public MainFrame()
    {
        excelFN = "";
        initComponents();
        excelData = new ArrayList();
        etm = new ExcelTableModel(excelData);
        excelTable.setModel(etm);
        setLocationRelativeTo(null);
    }

    private void guiui(boolean enable)
    {
        startButton.setEnabled(enable);
        uploadButton.setEnabled(enable);
    }

    private void guiSS(boolean start)
    {
        startButton.setEnabled(!start);
        stopButton.setEnabled(start);
        startRowText.setEnabled(!start);
        columnsText.setEnabled(!start);
        txtIterations.setEnabled(!start);
        downloadButton.setEnabled(!start);
        uploadButton.setEnabled(!start);
        if(start)
            excelTable.setModel(new ExcelTableModel(new ArrayList()));
    }

    private void geneticMethod()
    {
        t = new Thread() {

            private Genetic findOptimum(ArrayList ed, JLabel lab)
            {
                int optCount = 0;
                Genetic pa = new Genetic(ed);
                Genetic pmin = null;
                double lmin = 1.0000000000000001E+050D;
                int count = 0;
                int niter = Integer.parseInt(txtIterations.getText());
                Random R = new Random();
                runn = true;
                do
                {
                    pa.random(R);
                    pa.localoptimize();
                    if(pa.length() < lmin - 1E-010D)
                    {
                        pmin = (Genetic)pa.clone();
                        lmin = pa.length();
                        count = 0;
                        optCount++;
                    } else
                    {
                        count++;
                    }
                    lab.setText((new StringBuilder()).append("seeking optimum (").append(optCount).append(" found) [").append(count).append("/").append(niter).append("]").toString());
                } while(count < niter && runn);
                return pmin;
            }

            private ArrayList sortJumpers(ArrayList ed, Genetic p)
            {
                int i = 0;
                int j = p.To[i];
                ArrayList ex = new ArrayList();
                ex.add(ed.get(i));
                while(j != 0) 
                {
                    ex.add(ed.get(j));
                    j = p.To[j];
                    i = p.From[j];
                }
                return ex;
            }

            public void run()
            {
                lblStatusT1.setText("parsing data...");
                ArrayList ed1 = ExcelDataManip.parseExcelData(excelData, 1);
                ArrayList ed2 = ExcelDataManip.parseExcelData(excelData, 2);
                lblT1.setText("T=1:");
                Genetic p1 = findOptimum(ed1, lblStatusT1);
                if(runn)
                {
                    lblT2.setText("T=2:");
                    Genetic p2 = findOptimum(ed2, lblStatusT2);
                    if(runn)
                    {
                        lblStatusT1.setText("sorting...");
                        ed1 = sortJumpers(ed1, p1);
                        lblStatusT1.setText("nothing");
                        lblStatusT2.setText("sorting...");
                        ed2 = sortJumpers(ed2, p2);
                        lblStatusT2.setText("nothing");
                    }
                }
                excelData.clear();
                ExcelDataManip.rollDataUntilFive(ed1, true);
                ExcelDataManip.rollDataUntilFive(ed2, false);
                excelData.addAll(ed1);
                excelData.addAll(ed2);
                runn = false;
                lblT1.setText("");
                lblT2.setText("");
                lblStatusT2.setText("");
                etm = new ExcelTableModel(excelData);
                excelTable.setModel(etm);
                excelTable.revalidate();
                excelTable.repaint();
                lblStatusT1.setText("nothing");
                guiSS(false);
            }

            final MainFrame this$0;

            
            {
                this$0 = MainFrame.this;
                super();
            }
        }
;
        t.start();
    }

    private void initComponents()
    {
        ButtonGroup buttonGroup1 = new ButtonGroup();
        JScrollPane jScrollPane1 = new JScrollPane();
        excelTable = new JTable();
        JLabel jLabel1 = new JLabel();
        startRowText = new JTextField();
        downloadButton = new JButton();
        startButton = new JButton();
        uploadButton = new JButton();
        lblStatusT1 = new JLabel();
        JLabel jLabel2 = new JLabel();
        columnsText = new JTextField();
        JLabel jLabel3 = new JLabel();
        txtIterations = new JTextField();
        stopButton = new JButton();
        JButton aboutButton = new JButton();
        lblT2 = new JLabel();
        lblT1 = new JLabel();
        lblStatusT2 = new JLabel();
        setDefaultCloseOperation(3);
        setTitle("Jumpers sorter");
        excelTable.setModel(new DefaultTableModel(new Object[0][], new String[0]));
        jScrollPane1.setViewportView(excelTable);
        jLabel1.setText("Start row:");
        startRowText.setText("15");
        downloadButton.setText("Download Excel data");
        downloadButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                downloadButtonActionPerformed(evt);
            }

            final MainFrame this$0;

            
            {
                this$0 = MainFrame.this;
                super();
            }
        }
);
        startButton.setText("Start");
        startButton.setEnabled(false);
        startButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                startButtonActionPerformed(evt);
            }

            final MainFrame this$0;

            
            {
                this$0 = MainFrame.this;
                super();
            }
        }
);
        uploadButton.setText("Upload Excel data");
        uploadButton.setEnabled(false);
        uploadButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                uploadButtonActionPerformed(evt);
            }

            final MainFrame this$0;

            
            {
                this$0 = MainFrame.this;
                super();
            }
        }
);
        lblStatusT1.setFont(new Font("DejaVu Sans", 1, 14));
        lblStatusT1.setText("nothing");
        jLabel2.setText("Columns:");
        columnsText.setText("24");
        jLabel3.setText("Iterations:");
        txtIterations.setText("30");
        stopButton.setText("Stop");
        stopButton.setEnabled(false);
        stopButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                stopButtonActionPerformed(evt);
            }

            final MainFrame this$0;

            
            {
                this$0 = MainFrame.this;
                super();
            }
        }
);
        aboutButton.setText("About...");
        aboutButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                aboutButtonActionPerformed(evt);
            }

            final MainFrame this$0;

            
            {
                this$0 = MainFrame.this;
                super();
            }
        }
);
        lblStatusT2.setFont(new Font("DejaVu Sans", 1, 14));
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, -1, 679, 32767).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(layout.createSequentialGroup().addComponent(jLabel3).addGap(8, 8, 8).addComponent(txtIterations)).addGroup(layout.createSequentialGroup().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(columnsText)).addGroup(layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(startRowText, -2, 138, -2))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(uploadButton, -1, -1, 32767).addComponent(downloadButton)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(startButton, -2, 56, -2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(stopButton, -2, 60, -2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(aboutButton)).addGroup(layout.createSequentialGroup().addComponent(lblT1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lblStatusT1)).addGroup(layout.createSequentialGroup().addComponent(lblT2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lblStatusT2))).addContainerGap(93, 32767)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(startRowText, -2, -1, -2).addComponent(downloadButton).addComponent(startButton).addComponent(stopButton).addComponent(aboutButton)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(columnsText, -2, -1, -2).addComponent(uploadButton)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(txtIterations, -2, -1, -2))).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lblStatusT1).addComponent(lblT1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lblT2).addComponent(lblStatusT2)))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, -1, 249, 32767)));
        pack();
    }

    private void downloadButtonActionPerformed(ActionEvent evt)
    {
        FileDialog fd = new FileDialog(this);
        fd.setMode(0);
        fd.setVisible(true);
        fd.setFilenameFilter(new FilenameFilter() {

            public boolean accept(File dir, String name)
            {
                return name.endsWith(".xls");
            }

            final MainFrame this$0;

            
            {
                this$0 = MainFrame.this;
                super();
            }
        }
);
        if(fd.getFile() == null)
            return;
        lblStatusT1.setText("loading data...");
        excelFN = (new StringBuilder()).append(fd.getDirectory()).append(fd.getFile()).toString();
        if((excelData = ExcelDataManip.readExcelData(excelFN, Integer.parseInt(startRowText.getText()), Integer.parseInt(columnsText.getText()))) == null)
            guiui(false);
        else
            guiui(true);
        etm = new ExcelTableModel(excelData);
        excelTable.setModel(etm);
        excelTable.revalidate();
        excelTable.repaint();
        lblStatusT1.setText("nothing");
    }

    private void startButtonActionPerformed(ActionEvent evt)
    {
        runn = true;
        guiSS(true);
        geneticMethod();
    }

    private void stopButtonActionPerformed(ActionEvent evt)
    {
        runn = false;
    }

    private void aboutButtonActionPerformed(ActionEvent evt)
    {
        (new AboutDialog(this, true)).setVisible(true);
    }

    private void uploadButtonActionPerformed(ActionEvent evt)
    {
        FileDialog fd = new FileDialog(this);
        fd.setMode(1);
        fd.setVisible(true);
        fd.setFilenameFilter(new FilenameFilter() {

            public boolean accept(File dir, String name)
            {
                return name.endsWith(".xls");
            }

            final MainFrame this$0;

            
            {
                this$0 = MainFrame.this;
                super();
            }
        }
);
        if(fd.getFile() == null)
        {
            return;
        } else
        {
            lblStatusT1.setText("writing data...");
            excelFN = (new StringBuilder()).append(fd.getDirectory()).append(fd.getFile()).toString();
            ExcelDataManip.writeExcelData(excelData, excelFN, Integer.parseInt(startRowText.getText()));
            lblStatusT1.setText("nothing");
            return;
        }
    }

    public static void main(String args[])
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(UnsupportedLookAndFeelException e) { }
        catch(ClassNotFoundException e) { }
        catch(InstantiationException e) { }
        catch(IllegalAccessException e) { }
        (new MainFrame()).setVisible(true);
    }

    private ArrayList excelData;
    private ExcelTableModel etm;
    private String excelFN;
    private Thread t;
    private boolean runn;
    JTextField columnsText;
    JButton downloadButton;
    JTable excelTable;
    JLabel lblStatusT1;
    JLabel lblStatusT2;
    JLabel lblT1;
    JLabel lblT2;
    JButton startButton;
    JTextField startRowText;
    JButton stopButton;
    JTextField txtIterations;
    JButton uploadButton;











}
