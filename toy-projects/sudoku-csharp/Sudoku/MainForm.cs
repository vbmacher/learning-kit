using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Sudoku.SudokuImpl;
using Sudoku.UndoRedoImpl;

namespace Sudoku
{
    public partial class MainForm : Form
    {
        private Sudoku sudoku;
        private Color[,] squareColors;
        private bool saved;
        private String fileName = "";
        private long time = 0; // casovac

        // for implementing datagridview's virtual mode
        private int rowInEdit = -1;
        private int colInEdit = -1;

        // undo/redo
        private UndoRedo undo;

        public MainForm()
        {
            InitializeComponent();
            squareColors = new Color[9,9];
            saved = true;
            undo = new UndoRedo(new Update(setMark),new UpdateState(undoRedoButtons));
            sudoku = new Sudoku(undo);
            
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    squareColors[i,j] = Color.FromArgb(214,230,237);
                }
                for (int j = 3; j < 6; j++) {
                    squareColors[i,j] = Color.FromArgb(208,227,234);
                }
                for (int j = 6; j < 9; j++) {
                    squareColors[i,j] = Color.FromArgb(201,223,232);
                }
            }
            for (int i = 3; i < 6; i++) {
                for (int j = 0; j < 3; j++) {
                    squareColors[i,j] = Color.FromArgb(193,219,229);
                }
                for (int j = 3; j < 6; j++) {
                    squareColors[i,j] = Color.FromArgb(186,215,226);
                }
                for (int j = 6; j < 9; j++) {
                    squareColors[i,j] = Color.FromArgb(179,211,224);
                }
            }
            for (int i = 6; i < 9; i++) {
                for (int j = 0; j < 3; j++) {
                    squareColors[i,j] = Color.FromArgb(171,207,221);
                }
                for (int j = 3; j < 6; j++) {
                    squareColors[i,j] = Color.FromArgb(161,203,218);
                }
                for (int j = 6; j < 9; j++) {
                    squareColors[i,j] = Color.FromArgb(152,198,215);
                }
                undoRedoButtons();
            }

            gridSudoku.RowsDefaultCellStyle.Font = new Font("Courier New", 12, FontStyle.Bold);
            gridSudoku.RowsDefaultCellStyle.Alignment = DataGridViewContentAlignment.MiddleCenter;
            gridSudoku.MultiSelect = false;
            for (int i = 1; i < 10; i++)
            {
                gridSudoku.Rows.Add();
                if (gridSudoku.Rows[i-1].IsNewRow) continue;
                gridSudoku.Rows[i - 1].HeaderCell.Value = i.ToString();
                gridSudoku.Rows[i - 1].Height = 40;
                for (int j=0; j < 9; j++) 
                    gridSudoku.Rows[i - 1].Cells[j].Style.BackColor = squareColors[i-1,j];
            }

            // Connect the virtual-mode events to event handlers. 
            gridSudoku.CellValueNeeded += new
                DataGridViewCellValueEventHandler(gridSudoku_CellValueNeeded);
            gridSudoku.CellValuePushed += new
                DataGridViewCellValueEventHandler(gridSudkou_CellValuePushed);
            gridSudoku.RowValidated += new
                DataGridViewCellEventHandler(gridSudoku_RowValidated);
            gridSudoku.RowDirtyStateNeeded += new
                QuestionEventHandler(gridSudoku_RowDirtyStateNeeded);
            gridSudoku.CancelRowEdit += new
                QuestionEventHandler(gridSudoku_CancelRowEdit);            
        }

        public void setMark(StackField fi)
        {
            setMark(fi.Position.Row, fi.Position.Col, fi.Mark,false);
        }

        public void undoRedoButtons()
        {
            if (undo.canRedo())
            {
                btnRedo.Enabled = true;
                btnRedo.ToolTipText = "Krok dopredu (" + undo.RedoString + ")";
            }
            else
            {
                btnRedo.Enabled = false;
                btnRedo.ToolTipText = "Krok dopredu";
            }
            if (undo.canUndo())
            {
                btnUndo.Enabled = true;
                btnUndo.ToolTipText = "Krok späť (" + undo.UndoString + ")";
            }
            else
            {
                btnUndo.Enabled = false;
                btnUndo.ToolTipText = "Krok späť";
            }
        }

        private void setMark(int row, int col, int mark, bool passUndo)
        {
            sudoku.setMark(row, col, mark, passUndo);
            gridSudoku.Invalidate();
            gridSudoku.Refresh();
            if (sudoku.IsSolved)
            {
                String s = "";
                if (timer1.Tag != null)
                {
                    timer1.Stop();
                    timer1.Tag = null;
                    s = " za čas: " + lblTime.Text;
                }
                MessageBox.Show("Gratulujem! Sudoku je vyriešené" + s);
                return;
            }
        }


        private void gridSudoku_CellValueNeeded(object sender,
            DataGridViewCellValueEventArgs e)
        {
            int m = sudoku.getMark(e.RowIndex, e.ColumnIndex);
            if (m == 0) e.Value = "";
            else e.Value = m.ToString();
            if (sudoku.isDirty(e.RowIndex, e.ColumnIndex))
            {
                gridSudoku.Rows[e.RowIndex].Cells[e.ColumnIndex].Style.ForeColor = Color.Red;
            }
            else
            {
                gridSudoku.Rows[e.RowIndex].Cells[e.ColumnIndex].Style.ForeColor = Color.Black;
            }
        }

        private void gridSudkou_CellValuePushed(object sender,
            DataGridViewCellValueEventArgs e)
        {
            this.rowInEdit = e.RowIndex;
            this.colInEdit = e.ColumnIndex;

            // Set the appropriate SudokuField property to the cell value entered.
            String newValue = e.Value as String;
            int mark = 0;

            try {
                if (newValue.Trim().Equals("")) mark = 0;
                else mark = Int32.Parse(newValue); 
            }
            catch (Exception)
            {
                //System.Windows.Forms.MessageBox.Show("Chybná hodnota!");
                //return;
                mark = 0;
            }
            if (mark < 0 || mark > 9)
            {
                System.Windows.Forms.MessageBox.Show("Chybná hodnota (číslo musí byť od 1-9)!");
                return;
            }
            setMark(rowInEdit, colInEdit, mark,true);
        }

        private void gridSudoku_RowValidated(object sender,
            DataGridViewCellEventArgs e)
        {
            if (rowInEdit != -1 || colInEdit != -1) 
            {
                this.rowInEdit = -1;
                this.colInEdit = -1;
            }
            else if (gridSudoku.ContainsFocus)
            {
                this.rowInEdit = -1;
                this.colInEdit = -1;
            }
        }

        private void gridSudoku_RowDirtyStateNeeded(object sender,
            QuestionEventArgs e)
        {
            e.Response = gridSudoku.IsCurrentCellDirty;
        }

        private void gridSudoku_CancelRowEdit(object sender,
            QuestionEventArgs e)
        {
            this.rowInEdit = -1;
            this.colInEdit = -1;
        }

        private void toolStripButton1_Click(object sender, EventArgs e)
        {
            if (saved == false)
            {
                DialogResult res = System.Windows.Forms.MessageBox.Show(this, "Sudoku nebolo uložené. Chcete ho najprv uložiť?",
                    "Sudoku", MessageBoxButtons.YesNoCancel);
                if (res == DialogResult.Cancel) return;
                if (res == DialogResult.Yes)
                {
                    toolStripButton3_Click(sender, e);
                }
            }
            sudoku.clear();
            gridSudoku.Invalidate();
            saved = true;
            fileName = "";
        }

        private void toolStripButton2_Click(object sender, EventArgs e)
        {
            OpenFileDialog op = new OpenFileDialog();
            op.AddExtension = true;
            op.Multiselect = false;
            op.FileName = fileName;
            op.Filter = "Textové súbory (*.txt)|*.txt|Všetky súbory (*.*)|*.*";
            DialogResult res = op.ShowDialog();
            if (res == DialogResult.OK)
            {
                saved = true;
                fileName = op.FileName;
                sudoku.load(op.FileName);
                gridSudoku.Invalidate();
                gridSudoku.Refresh();
            }
        }

        private void toolStripButton3_Click(object sender, EventArgs e)
        {
            if (fileName != "")
            {
                sudoku.save(fileName);
                saved = true;
                return;
            }
            else
            {
                SaveFileDialog dia = new SaveFileDialog();
                dia.AddExtension = true;
                dia.Filter = "Textové súbory (*.txt)|*.txt|Všetky súbory (*.*)|*.*";
                DialogResult res = dia.ShowDialog();
                if (res == DialogResult.OK)
                {
                    sudoku.save(dia.FileName);
                    fileName = dia.FileName;
                    saved = true;
                }
            }
        }

        private void novýToolStripMenuItem_Click(object sender, EventArgs e)
        {
            toolStripButton1_Click(sender, e);
        }

        private void otvoriťToolStripMenuItem_Click(object sender, EventArgs e)
        {
            toolStripButton2_Click(sender, e);
        }

        private void uložiťToolStripMenuItem_Click(object sender, EventArgs e)
        {
            toolStripButton3_Click(sender, e);
        }

        private void uložiťAkoToolStripMenuItem_Click(object sender, EventArgs e)
        {
            SaveFileDialog dia = new SaveFileDialog();
            dia.AddExtension = true;
            dia.Filter = "Textové súbory (*.txt)|*.txt|Všetky súbory (*.*)|*.*";
            DialogResult res = dia.ShowDialog();
            if (res == DialogResult.OK)
            {
                sudoku.save(dia.FileName);
                fileName = dia.FileName;
                saved = true;
            }
        }

        private void koniecToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if (saved == false)
            {
                DialogResult res = System.Windows.Forms.MessageBox.Show(this, "Sudoku nebolo uložené. Chcete ho najprv uložiť?",
                    "Sudoku", MessageBoxButtons.YesNoCancel);
                if (res == DialogResult.Cancel)
                {
                    return;
                }
                if (res == DialogResult.Yes)
                {
                    toolStripButton3_Click(sender, e);
                }
            }
            Application.Exit();
        }

        private void MainForm_FormClosing(object sender, FormClosingEventArgs e)
        {
            if (saved == false)
            {
                DialogResult res = System.Windows.Forms.MessageBox.Show(this, "Sudoku nebolo uložené. Chcete ho najprv uložiť?",
                    "Sudoku", MessageBoxButtons.YesNoCancel);
                if (res == DialogResult.Cancel)
                {
                    e.Cancel = true;
                    return;
                }
                if (res == DialogResult.Yes)
                {
                    toolStripButton3_Click(sender, e);
                }
            }
        }

        private void oProgrameToolStripMenuItem_Click(object sender, EventArgs e)
        {
            new AboutBox().ShowDialog();
        }

        private void btnSolve_Click(object sender, EventArgs e)
        {
            timer1.Stop();
            timer1.Tag = null;
            btnStart.Enabled = true;
            btnStop.Enabled = false;
            sudoku.solve();
            gridSudoku.Refresh();
        }

        private void gridSudoku_CellDoubleClick(object sender, DataGridViewCellEventArgs e)
        {
            Point pos = gridSudoku.GetCellDisplayRectangle(e.ColumnIndex,e.RowIndex,false).Location;
            Point position = new Point(this.Location.X + pos.X, this.Location.Y + pos.Y);
                
            SelectNumberForm f = new SelectNumberForm(position,sudoku.getMark(e.RowIndex,e.ColumnIndex));
            f.ShowDialog();
            if (f.isSelected())
                setMark(e.RowIndex, e.ColumnIndex, f.getNumber(),true);
        }

        private void btnHelp_Click(object sender, EventArgs e)
        {
            if (gridSudoku.CurrentCell != null)
            {
                int row = gridSudoku.CurrentCell.RowIndex;
                int col = gridSudoku.CurrentCell.ColumnIndex;
                if (sudoku.getMark(row, col) != 0) return;
                String s = sudoku.getMovesString(row, col);

                Point cellloc = gridSudoku.GetCellDisplayRectangle(col, row, false).Location;
                Point loc = new Point(this.Location.X + gridSudoku.Location.X + cellloc.X,
                    this.Location.Y + gridSudoku.Location.Y + cellloc.Y);
                Advice a = new Advice(loc, s, gridSudoku.CurrentCell);
                a.Show(this);
            }
        }

        private void poradiťToolStripMenuItem_Click(object sender, EventArgs e)
        {
            btnHelp_Click(sender, e);
        }

        private void btnUndo_Click(object sender, EventArgs e)
        {
            if (undo.canUndo())
                undo.undo();
        }

        private void btnRedo_Click(object sender, EventArgs e)
        {
            if (undo.canRedo())
                undo.redo();
        }

        private void toolStripButton1_Click_1(object sender, EventArgs e)
        {
            lblTime.Text = "00:00:00";
            time = 0;
            timer1.Start();
            timer1.Tag = "1";
            btnStop.Enabled = true;
            btnStart.Enabled = false;
        }

        private void btnStop_Click(object sender, EventArgs e)
        {
            timer1.Stop();
            timer1.Tag = null;
            btnStart.Enabled = true;
            btnStop.Enabled = false;
        }

        private void timer1_Tick(object sender, EventArgs e)
        {
            time++;
            int hs = (int)(time / 3600);
            int min = (int)((time-hs*3600) / 60);
            int s = (int)(time % 60);
            lblTime.Text = String.Format("{0,2:D2}:{1,2:D2}:{2,2:D2}", hs, min, s);
        }

        private void btnGenerate_Click(object sender, EventArgs e)
        {
            timer1.Stop();
            btnStop.Enabled = false;
            btnStart.Enabled = true;
            sudoku.generate(0);
            gridSudoku.Invalidate();
            gridSudoku.Refresh();
        }
    }
}
