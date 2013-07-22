using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Sudoku.SudokuImpl
{
    public class FieldPosition
    {
        int row;
        int col;

        public FieldPosition(int row, int col)
        {
            this.row = row;
            this.col = col;
        }

        public int Row
        {
            get
            {
                return row;
            }
        }

        public int Col
        {
            get
            {
                return col;
            }
        }

        public override string ToString()
        {
            return "[" + (row+1) + "," + (col+1) + "]:";
        }
    }
}
