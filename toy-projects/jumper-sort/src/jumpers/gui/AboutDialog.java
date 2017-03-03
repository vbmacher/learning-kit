// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AboutDialog.java

package jumpers.gui;

import java.awt.*;
import javax.swing.*;

public class AboutDialog extends JDialog
{

    public AboutDialog(Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents()
    {
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jLabel5 = new JLabel();
        jLabel6 = new JLabel();
        setDefaultCloseOperation(2);
        setTitle("About");
        jLabel1.setIcon(new ImageIcon(getClass().getResource("/resources/chicken.gif")));
        jLabel2.setFont(jLabel2.getFont().deriveFont(jLabel2.getFont().getStyle() | 1, 16F));
        jLabel2.setText("Jumpers sorter");
        jLabel3.setText("\251 Copyright 2008, Peter Jakub\u010Do");
        jLabel4.setText("version 0.2b1");
        jLabel5.setFont(jLabel5.getFont().deriveFont(12F));
        jLabel5.setText("<html><strong>Licence agreement</strong><br />\nFor full version of licence see <em>licence.txt</em> file. This software was created for private use by my brother, <em>Tom\341\u0161 Jakub\u010Do</em>. Author grants to him these rights:\n<ul>\n<li>free use of the software</li>\n<li>free copying and installing the software for his private use</li>\n</ul>\n\nAll rights not expressly granted are reserved. Mentioned rights are not granted to any other person with no exceptions.<br/>\n<strong>Generally is forbidden:</strong>reproducing, modifying, decompiling, reverse engineering, disassembling or making derivative products of the software or decoding it in any other way.");
        jLabel6.setFont(new Font("DejaVu Sans", 1, 14));
        jLabel6.setText("All rights reserved.");
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel1).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(24, 24, 24).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)).addGroup(layout.createSequentialGroup().addComponent(jLabel3).addGap(200, 200, 200)).addComponent(jLabel6).addComponent(jLabel5, -1, 352, 32767))).addGroup(layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel2))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel1).addContainerGap()).addGroup(layout.createSequentialGroup().addComponent(jLabel2).addGap(18, 18, 18).addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel5, -1, 244, 32767)))));
        pack();
    }

    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
}
