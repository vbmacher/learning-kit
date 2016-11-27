
namespace Sudoku
{
    partial class MainForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(MainForm));
            this.toolStrip1 = new System.Windows.Forms.ToolStrip();
            this.btnNew = new System.Windows.Forms.ToolStripButton();
            this.btnGenerate = new System.Windows.Forms.ToolStripButton();
            this.toolStripSeparator4 = new System.Windows.Forms.ToolStripSeparator();
            this.btnOpen = new System.Windows.Forms.ToolStripButton();
            this.btnSave = new System.Windows.Forms.ToolStripButton();
            this.toolStripSeparator1 = new System.Windows.Forms.ToolStripSeparator();
            this.btnUndo = new System.Windows.Forms.ToolStripButton();
            this.btnRedo = new System.Windows.Forms.ToolStripButton();
            this.toolStripSeparator2 = new System.Windows.Forms.ToolStripSeparator();
            this.btnAdvice = new System.Windows.Forms.ToolStripButton();
            this.btnSolve = new System.Windows.Forms.ToolStripButton();
            this.gridSudoku = new System.Windows.Forms.DataGridView();
            this.col1 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.col2 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.col3 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.col4 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.col5 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.col6 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.col7 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.col8 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.col9 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.menuStrip1 = new System.Windows.Forms.MenuStrip();
            this.súborToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.novýToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.otvoriťToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.uložiťToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.uložiťAkoToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.koniecToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.sudokuToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.späťToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.znovuToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.toolStripSeparator3 = new System.Windows.Forms.ToolStripSeparator();
            this.poradiťToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.vyriešiťToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.pomocToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.oProgrameToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.toolStripSeparator5 = new System.Windows.Forms.ToolStripSeparator();
            this.toolStripSeparator6 = new System.Windows.Forms.ToolStripSeparator();
            this.toolStripSeparator7 = new System.Windows.Forms.ToolStripSeparator();
            this.btnStart = new System.Windows.Forms.ToolStripButton();
            this.btnStop = new System.Windows.Forms.ToolStripButton();
            this.toolStripLabel1 = new System.Windows.Forms.ToolStripLabel();
            this.lblTime = new System.Windows.Forms.ToolStripLabel();
            this.timer1 = new System.Windows.Forms.Timer(this.components);
            this.toolStrip1.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.gridSudoku)).BeginInit();
            this.menuStrip1.SuspendLayout();
            this.SuspendLayout();
            // 
            // toolStrip1
            // 
            this.toolStrip1.ImageScalingSize = new System.Drawing.Size(22, 22);
            this.toolStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.btnNew,
            this.btnGenerate,
            this.toolStripSeparator4,
            this.btnOpen,
            this.btnSave,
            this.toolStripSeparator1,
            this.btnUndo,
            this.btnRedo,
            this.toolStripSeparator2,
            this.btnAdvice,
            this.btnSolve,
            this.toolStripSeparator7,
            this.btnStart,
            this.btnStop,
            this.toolStripLabel1,
            this.lblTime});
            this.toolStrip1.Location = new System.Drawing.Point(0, 24);
            this.toolStrip1.Name = "toolStrip1";
            this.toolStrip1.RenderMode = System.Windows.Forms.ToolStripRenderMode.System;
            this.toolStrip1.Size = new System.Drawing.Size(429, 29);
            this.toolStrip1.TabIndex = 0;
            this.toolStrip1.Text = "toolStrip1";
            // 
            // btnNew
            // 
            this.btnNew.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.btnNew.Image = ((System.Drawing.Image)(resources.GetObject("btnNew.Image")));
            this.btnNew.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.btnNew.Name = "btnNew";
            this.btnNew.Size = new System.Drawing.Size(26, 26);
            this.btnNew.Text = "Nové sudoku";
            this.btnNew.Click += new System.EventHandler(this.toolStripButton1_Click);
            // 
            // btnGenerate
            // 
            this.btnGenerate.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.btnGenerate.Image = ((System.Drawing.Image)(resources.GetObject("btnGenerate.Image")));
            this.btnGenerate.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.btnGenerate.Name = "btnGenerate";
            this.btnGenerate.Size = new System.Drawing.Size(26, 26);
            this.btnGenerate.ToolTipText = "Vymyslieť nové sudoku";
            this.btnGenerate.Click += new System.EventHandler(this.btnGenerate_Click);
            // 
            // toolStripSeparator4
            // 
            this.toolStripSeparator4.Name = "toolStripSeparator4";
            this.toolStripSeparator4.Size = new System.Drawing.Size(6, 29);
            // 
            // btnOpen
            // 
            this.btnOpen.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.btnOpen.Image = ((System.Drawing.Image)(resources.GetObject("btnOpen.Image")));
            this.btnOpen.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.btnOpen.Name = "btnOpen";
            this.btnOpen.Size = new System.Drawing.Size(26, 26);
            this.btnOpen.Text = "Otvoriť sudoku";
            this.btnOpen.Click += new System.EventHandler(this.toolStripButton2_Click);
            // 
            // btnSave
            // 
            this.btnSave.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.btnSave.Image = ((System.Drawing.Image)(resources.GetObject("btnSave.Image")));
            this.btnSave.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.btnSave.Name = "btnSave";
            this.btnSave.Size = new System.Drawing.Size(26, 26);
            this.btnSave.Text = "Uložiť sudoku";
            this.btnSave.Click += new System.EventHandler(this.toolStripButton3_Click);
            // 
            // toolStripSeparator1
            // 
            this.toolStripSeparator1.Name = "toolStripSeparator1";
            this.toolStripSeparator1.Size = new System.Drawing.Size(6, 29);
            // 
            // btnUndo
            // 
            this.btnUndo.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.btnUndo.Image = ((System.Drawing.Image)(resources.GetObject("btnUndo.Image")));
            this.btnUndo.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.btnUndo.Name = "btnUndo";
            this.btnUndo.Size = new System.Drawing.Size(26, 26);
            this.btnUndo.ToolTipText = "Krok späť";
            this.btnUndo.Click += new System.EventHandler(this.btnUndo_Click);
            // 
            // btnRedo
            // 
            this.btnRedo.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.btnRedo.Image = ((System.Drawing.Image)(resources.GetObject("btnRedo.Image")));
            this.btnRedo.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.btnRedo.Name = "btnRedo";
            this.btnRedo.Size = new System.Drawing.Size(26, 26);
            this.btnRedo.ToolTipText = "Krok dopredu";
            this.btnRedo.Click += new System.EventHandler(this.btnRedo_Click);
            // 
            // toolStripSeparator2
            // 
            this.toolStripSeparator2.Name = "toolStripSeparator2";
            this.toolStripSeparator2.Size = new System.Drawing.Size(6, 29);
            // 
            // btnAdvice
            // 
            this.btnAdvice.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.btnAdvice.Image = ((System.Drawing.Image)(resources.GetObject("btnAdvice.Image")));
            this.btnAdvice.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.btnAdvice.Name = "btnAdvice";
            this.btnAdvice.Size = new System.Drawing.Size(26, 26);
            this.btnAdvice.ToolTipText = "Poradiť";
            this.btnAdvice.Click += new System.EventHandler(this.btnHelp_Click);
            // 
            // btnSolve
            // 
            this.btnSolve.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.btnSolve.Image = ((System.Drawing.Image)(resources.GetObject("btnSolve.Image")));
            this.btnSolve.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.btnSolve.Name = "btnSolve";
            this.btnSolve.Size = new System.Drawing.Size(26, 26);
            this.btnSolve.Text = "Vyriešiť";
            this.btnSolve.Click += new System.EventHandler(this.btnSolve_Click);
            // 
            // gridSudoku
            // 
            this.gridSudoku.AllowUserToAddRows = false;
            this.gridSudoku.AllowUserToDeleteRows = false;
            this.gridSudoku.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.gridSudoku.BackgroundColor = System.Drawing.SystemColors.ActiveBorder;
            this.gridSudoku.BorderStyle = System.Windows.Forms.BorderStyle.None;
            this.gridSudoku.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.gridSudoku.Columns.AddRange(new System.Windows.Forms.DataGridViewColumn[] {
            this.col1,
            this.col2,
            this.col3,
            this.col4,
            this.col5,
            this.col6,
            this.col7,
            this.col8,
            this.col9});
            this.gridSudoku.GridColor = System.Drawing.Color.White;
            this.gridSudoku.Location = new System.Drawing.Point(12, 56);
            this.gridSudoku.MultiSelect = false;
            this.gridSudoku.Name = "gridSudoku";
            this.gridSudoku.SelectionMode = System.Windows.Forms.DataGridViewSelectionMode.CellSelect;
            this.gridSudoku.Size = new System.Drawing.Size(405, 378);
            this.gridSudoku.TabIndex = 1;
            this.gridSudoku.VirtualMode = true;
            this.gridSudoku.CellDoubleClick += new System.Windows.Forms.DataGridViewCellEventHandler(this.gridSudoku_CellDoubleClick);
            // 
            // col1
            // 
            this.col1.HeaderText = "1";
            this.col1.Name = "col1";
            this.col1.Width = 40;
            // 
            // col2
            // 
            this.col2.HeaderText = "2";
            this.col2.Name = "col2";
            this.col2.Width = 40;
            // 
            // col3
            // 
            this.col3.HeaderText = "3";
            this.col3.Name = "col3";
            this.col3.Width = 40;
            // 
            // col4
            // 
            this.col4.HeaderText = "4";
            this.col4.Name = "col4";
            this.col4.Width = 40;
            // 
            // col5
            // 
            this.col5.HeaderText = "5";
            this.col5.Name = "col5";
            this.col5.Width = 40;
            // 
            // col6
            // 
            this.col6.HeaderText = "6";
            this.col6.Name = "col6";
            this.col6.Width = 40;
            // 
            // col7
            // 
            this.col7.HeaderText = "7";
            this.col7.Name = "col7";
            this.col7.Width = 40;
            // 
            // col8
            // 
            this.col8.HeaderText = "8";
            this.col8.Name = "col8";
            this.col8.Width = 40;
            // 
            // col9
            // 
            this.col9.HeaderText = "9";
            this.col9.Name = "col9";
            this.col9.Width = 40;
            // 
            // menuStrip1
            // 
            this.menuStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.súborToolStripMenuItem,
            this.sudokuToolStripMenuItem,
            this.pomocToolStripMenuItem});
            this.menuStrip1.Location = new System.Drawing.Point(0, 0);
            this.menuStrip1.Name = "menuStrip1";
            this.menuStrip1.Size = new System.Drawing.Size(429, 24);
            this.menuStrip1.TabIndex = 2;
            this.menuStrip1.Text = "menuStrip1";
            // 
            // súborToolStripMenuItem
            // 
            this.súborToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.novýToolStripMenuItem,
            this.otvoriťToolStripMenuItem,
            this.toolStripSeparator6,
            this.uložiťToolStripMenuItem,
            this.uložiťAkoToolStripMenuItem,
            this.toolStripSeparator5,
            this.koniecToolStripMenuItem});
            this.súborToolStripMenuItem.Name = "súborToolStripMenuItem";
            this.súborToolStripMenuItem.Size = new System.Drawing.Size(47, 20);
            this.súborToolStripMenuItem.Text = "&Súbor";
            // 
            // novýToolStripMenuItem
            // 
            this.novýToolStripMenuItem.Name = "novýToolStripMenuItem";
            this.novýToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.N)));
            this.novýToolStripMenuItem.Size = new System.Drawing.Size(160, 22);
            this.novýToolStripMenuItem.Text = "&Nový";
            this.novýToolStripMenuItem.Click += new System.EventHandler(this.novýToolStripMenuItem_Click);
            // 
            // otvoriťToolStripMenuItem
            // 
            this.otvoriťToolStripMenuItem.Name = "otvoriťToolStripMenuItem";
            this.otvoriťToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.O)));
            this.otvoriťToolStripMenuItem.Size = new System.Drawing.Size(160, 22);
            this.otvoriťToolStripMenuItem.Text = "&Otvoriť";
            this.otvoriťToolStripMenuItem.Click += new System.EventHandler(this.otvoriťToolStripMenuItem_Click);
            // 
            // uložiťToolStripMenuItem
            // 
            this.uložiťToolStripMenuItem.Name = "uložiťToolStripMenuItem";
            this.uložiťToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.S)));
            this.uložiťToolStripMenuItem.Size = new System.Drawing.Size(160, 22);
            this.uložiťToolStripMenuItem.Text = "&Uložiť";
            this.uložiťToolStripMenuItem.Click += new System.EventHandler(this.uložiťToolStripMenuItem_Click);
            // 
            // uložiťAkoToolStripMenuItem
            // 
            this.uložiťAkoToolStripMenuItem.Name = "uložiťAkoToolStripMenuItem";
            this.uložiťAkoToolStripMenuItem.Size = new System.Drawing.Size(160, 22);
            this.uložiťAkoToolStripMenuItem.Text = "Uložiť &Ako";
            this.uložiťAkoToolStripMenuItem.Click += new System.EventHandler(this.uložiťAkoToolStripMenuItem_Click);
            // 
            // koniecToolStripMenuItem
            // 
            this.koniecToolStripMenuItem.Name = "koniecToolStripMenuItem";
            this.koniecToolStripMenuItem.Size = new System.Drawing.Size(160, 22);
            this.koniecToolStripMenuItem.Text = "&Koniec";
            this.koniecToolStripMenuItem.Click += new System.EventHandler(this.koniecToolStripMenuItem_Click);
            // 
            // sudokuToolStripMenuItem
            // 
            this.sudokuToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.späťToolStripMenuItem,
            this.znovuToolStripMenuItem,
            this.toolStripSeparator3,
            this.poradiťToolStripMenuItem,
            this.vyriešiťToolStripMenuItem});
            this.sudokuToolStripMenuItem.Name = "sudokuToolStripMenuItem";
            this.sudokuToolStripMenuItem.Size = new System.Drawing.Size(54, 20);
            this.sudokuToolStripMenuItem.Text = "S&udoku";
            // 
            // späťToolStripMenuItem
            // 
            this.späťToolStripMenuItem.Name = "späťToolStripMenuItem";
            this.späťToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.Z)));
            this.späťToolStripMenuItem.Size = new System.Drawing.Size(159, 22);
            this.späťToolStripMenuItem.Text = "Späť";
            // 
            // znovuToolStripMenuItem
            // 
            this.znovuToolStripMenuItem.Name = "znovuToolStripMenuItem";
            this.znovuToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.R)));
            this.znovuToolStripMenuItem.Size = new System.Drawing.Size(159, 22);
            this.znovuToolStripMenuItem.Text = "Znovu";
            // 
            // toolStripSeparator3
            // 
            this.toolStripSeparator3.Name = "toolStripSeparator3";
            this.toolStripSeparator3.Size = new System.Drawing.Size(156, 6);
            // 
            // poradiťToolStripMenuItem
            // 
            this.poradiťToolStripMenuItem.Name = "poradiťToolStripMenuItem";
            this.poradiťToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.A)));
            this.poradiťToolStripMenuItem.Size = new System.Drawing.Size(159, 22);
            this.poradiťToolStripMenuItem.Text = "Poradiť";
            this.poradiťToolStripMenuItem.Click += new System.EventHandler(this.poradiťToolStripMenuItem_Click);
            // 
            // vyriešiťToolStripMenuItem
            // 
            this.vyriešiťToolStripMenuItem.Name = "vyriešiťToolStripMenuItem";
            this.vyriešiťToolStripMenuItem.Size = new System.Drawing.Size(159, 22);
            this.vyriešiťToolStripMenuItem.Text = "&Vyriešiť";
            // 
            // pomocToolStripMenuItem
            // 
            this.pomocToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.oProgrameToolStripMenuItem});
            this.pomocToolStripMenuItem.Name = "pomocToolStripMenuItem";
            this.pomocToolStripMenuItem.Size = new System.Drawing.Size(50, 20);
            this.pomocToolStripMenuItem.Text = "&Pomoc";
            // 
            // oProgrameToolStripMenuItem
            // 
            this.oProgrameToolStripMenuItem.Name = "oProgrameToolStripMenuItem";
            this.oProgrameToolStripMenuItem.ShortcutKeys = System.Windows.Forms.Keys.F1;
            this.oProgrameToolStripMenuItem.Size = new System.Drawing.Size(161, 22);
            this.oProgrameToolStripMenuItem.Text = "&O programe";
            this.oProgrameToolStripMenuItem.Click += new System.EventHandler(this.oProgrameToolStripMenuItem_Click);
            // 
            // toolStripSeparator5
            // 
            this.toolStripSeparator5.Name = "toolStripSeparator5";
            this.toolStripSeparator5.Size = new System.Drawing.Size(157, 6);
            // 
            // toolStripSeparator6
            // 
            this.toolStripSeparator6.Name = "toolStripSeparator6";
            this.toolStripSeparator6.Size = new System.Drawing.Size(157, 6);
            // 
            // toolStripSeparator7
            // 
            this.toolStripSeparator7.Name = "toolStripSeparator7";
            this.toolStripSeparator7.Size = new System.Drawing.Size(6, 29);
            // 
            // btnStart
            // 
            this.btnStart.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.btnStart.Image = ((System.Drawing.Image)(resources.GetObject("btnStart.Image")));
            this.btnStart.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.btnStart.Name = "btnStart";
            this.btnStart.Size = new System.Drawing.Size(26, 26);
            this.btnStart.ToolTipText = "Spustiť časovač";
            this.btnStart.Click += new System.EventHandler(this.toolStripButton1_Click_1);
            // 
            // btnStop
            // 
            this.btnStop.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.btnStop.Enabled = false;
            this.btnStop.Image = ((System.Drawing.Image)(resources.GetObject("btnStop.Image")));
            this.btnStop.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.btnStop.Name = "btnStop";
            this.btnStop.Size = new System.Drawing.Size(26, 26);
            this.btnStop.ToolTipText = "Zastaviť časovač";
            this.btnStop.Click += new System.EventHandler(this.btnStop_Click);
            // 
            // toolStripLabel1
            // 
            this.toolStripLabel1.Name = "toolStripLabel1";
            this.toolStripLabel1.Size = new System.Drawing.Size(29, 26);
            this.toolStripLabel1.Text = "Čas:";
            // 
            // lblTime
            // 
            this.lblTime.Font = new System.Drawing.Font("Tahoma", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(238)));
            this.lblTime.Name = "lblTime";
            this.lblTime.Size = new System.Drawing.Size(55, 26);
            this.lblTime.Text = "00:00:00";
            // 
            // timer1
            // 
            this.timer1.Interval = 1000;
            this.timer1.Tick += new System.EventHandler(this.timer1_Tick);
            // 
            // MainForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.AutoSize = true;
            this.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
            this.ClientSize = new System.Drawing.Size(429, 446);
            this.Controls.Add(this.gridSudoku);
            this.Controls.Add(this.toolStrip1);
            this.Controls.Add(this.menuStrip1);
            this.DoubleBuffered = true;
            this.MainMenuStrip = this.menuStrip1;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "MainForm";
            this.SizeGripStyle = System.Windows.Forms.SizeGripStyle.Hide;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Sudoku";
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.MainForm_FormClosing);
            this.toolStrip1.ResumeLayout(false);
            this.toolStrip1.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.gridSudoku)).EndInit();
            this.menuStrip1.ResumeLayout(false);
            this.menuStrip1.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.ToolStrip toolStrip1;
        private System.Windows.Forms.ToolStripButton btnNew;
        private System.Windows.Forms.ToolStripButton btnOpen;
        private System.Windows.Forms.ToolStripButton btnSave;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator1;
        private System.Windows.Forms.ToolStripButton btnSolve;
        private System.Windows.Forms.DataGridView gridSudoku;
        private System.Windows.Forms.DataGridViewTextBoxColumn col1;
        private System.Windows.Forms.DataGridViewTextBoxColumn col2;
        private System.Windows.Forms.DataGridViewTextBoxColumn col3;
        private System.Windows.Forms.DataGridViewTextBoxColumn col4;
        private System.Windows.Forms.DataGridViewTextBoxColumn col5;
        private System.Windows.Forms.DataGridViewTextBoxColumn col6;
        private System.Windows.Forms.DataGridViewTextBoxColumn col7;
        private System.Windows.Forms.DataGridViewTextBoxColumn col8;
        private System.Windows.Forms.DataGridViewTextBoxColumn col9;
        private System.Windows.Forms.MenuStrip menuStrip1;
        private System.Windows.Forms.ToolStripMenuItem súborToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem novýToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem otvoriťToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem uložiťToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem uložiťAkoToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem koniecToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem sudokuToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem vyriešiťToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem pomocToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem oProgrameToolStripMenuItem;
        private System.Windows.Forms.ToolStripButton btnAdvice;
        private System.Windows.Forms.ToolStripButton btnUndo;
        private System.Windows.Forms.ToolStripButton btnRedo;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator2;
        private System.Windows.Forms.ToolStripMenuItem späťToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem znovuToolStripMenuItem;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator3;
        private System.Windows.Forms.ToolStripMenuItem poradiťToolStripMenuItem;
        private System.Windows.Forms.ToolStripButton btnGenerate;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator4;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator6;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator5;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator7;
        private System.Windows.Forms.ToolStripButton btnStart;
        private System.Windows.Forms.ToolStripButton btnStop;
        private System.Windows.Forms.ToolStripLabel toolStripLabel1;
        private System.Windows.Forms.ToolStripLabel lblTime;
        private System.Windows.Forms.Timer timer1;
    }
}

