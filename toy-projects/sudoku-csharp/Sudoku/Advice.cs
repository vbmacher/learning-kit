using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace Sudoku
{
    public partial class Advice : Form
    {
        private DataGridViewCell cell; // for changing a color
        private Color old_back;

        public Advice(Point location, String toolTip, DataGridViewCell cell)
        {
            InitializeComponent();
            lblAdvice.Text = toolTip;
            this.Location = location;
            this.cell = cell;

            old_back = cell.Style.BackColor;
            cell.Style.BackColor = Color.FromArgb(old_back.R | 100,
                old_back.G | 100, old_back.B | 100);
        }

        private void Advice_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == 27)
            {
                this.Close();
            }
        }

        private void Advice_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void lblAdvice_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void Advice_FormClosing(object sender, FormClosingEventArgs e)
        {
            cell.Style.BackColor = old_back;
        }
    }
}
