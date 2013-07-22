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
    public partial class SelectNumberForm : Form
    {
        private bool selected;
        private int number;

        public SelectNumberForm(Point location, int alreadySelected)
        {
            selected = false;
            InitializeComponent();
            this.Location = location;
            switch (alreadySelected)
            {
                case 1: 
                    btn1.Font = new Font(btn1.Font,FontStyle.Bold);
                    break;
                case 2:
                    btn2.Font = new Font(btn2.Font, FontStyle.Bold);
                    break;
                case 3:
                    btn3.Font = new Font(btn3.Font, FontStyle.Bold);
                    break;
                case 4:
                    btn4.Font = new Font(btn4.Font, FontStyle.Bold);
                    break;
                case 5:
                    btn5.Font = new Font(btn5.Font, FontStyle.Bold);
                    break;
                case 6:
                    btn6.Font = new Font(btn6.Font, FontStyle.Bold);
                    break;
                case 7:
                    btn7.Font = new Font(btn7.Font, FontStyle.Bold);
                    break;
                case 8:
                    btn8.Font = new Font(btn8.Font, FontStyle.Bold);
                    break;
                case 9:
                    btn9.Font = new Font(btn9.Font, FontStyle.Bold);
                    break;
            }
        }

        public bool isSelected()
        {
            return selected;
        }

        public int getNumber()
        {
            return number;
        }

        private void SelectNumberForm_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == 27)
            {
                selected = false;
                this.Close();
            }
        }

        private void btnCancel_Click(object sender, EventArgs e)
        {
            selected = false;
            this.Close();
        }

        private void btn1_Click(object sender, EventArgs e)
        {
            number = 1;
            selected = true;
            this.Close();
        }

        private void btn2_Click(object sender, EventArgs e)
        {
            number = 2;
            selected = true;
            this.Close();
        }

        private void btn3_Click(object sender, EventArgs e)
        {
            number = 3;
            selected = true;
            this.Close();
        }

        private void btn4_Click(object sender, EventArgs e)
        {
            number = 4;
            selected = true;
            this.Close();
        }

        private void btn5_Click(object sender, EventArgs e)
        {
            number = 5;
            selected = true;
            this.Close();
        }

        private void btn6_Click(object sender, EventArgs e)
        {
            number = 6;
            selected = true;
            this.Close();
        }

        private void btn7_Click(object sender, EventArgs e)
        {
            number = 7;
            selected = true;
            this.Close();
        }

        private void btn8_Click(object sender, EventArgs e)
        {
            number = 8;
            selected = true;
            this.Close();
        }

        private void btn9_Click(object sender, EventArgs e)
        {
            number = 9;
            selected = true;
            this.Close();
        }
    }
}
