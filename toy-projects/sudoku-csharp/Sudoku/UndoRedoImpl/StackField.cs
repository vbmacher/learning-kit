using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Sudoku.SudokuImpl;

namespace Sudoku.UndoRedoImpl
{
    public class StackField
    {
        private FieldPosition pos;
        private int old_mark;
        private int new_mark;

        public StackField(FieldPosition pos, int old_mark, int new_mark)
        {
            this.pos = pos;
            this.old_mark = old_mark;
            this.new_mark = new_mark;
        }

        // undo/redo
        public void switchMarks()
        {
            int m = old_mark;
            old_mark = new_mark;
            new_mark = m;
        }

        public FieldPosition Position
        {
            get
            {
                return pos;
            }
        }

        public int Mark
        {
            get
            {
                return old_mark;
            }
        }

        public override String ToString()
        {
            return pos.ToString() + " " + new_mark + " -> " + old_mark;
        }

    }
}
