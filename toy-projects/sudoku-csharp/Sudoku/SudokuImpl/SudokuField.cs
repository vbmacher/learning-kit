using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Sudoku.SudokuImpl
{
    class SudokuField
    {
        private int mark;
        private int[] moves;
        private bool dirty = false;
        private int square; // cislo stvorca, v ktorom sa policko nachadza

        public SudokuField(int square)
        {
            moves = new int[9];
            this.square = square;
        }

        public SudokuField Clone()
        {
            SudokuField sufi = new SudokuField(this.square);
            sufi.Mark = this.mark;
            sufi.Dirty = this.dirty;
            sufi.moves = (int[])this.moves.Clone();
            return sufi;
        }

        public int Mark
        {
            get
            {
                return mark;
            }
            set
            {
                this.mark = value;
                if (value == 0)
                    dirty = false;
            }
        }

        public bool Dirty
        {
            get
            {
                return dirty;
            }
            set
            {
                dirty = value;
            }
        }

        public void clear()
        {
            dirty = false;
            mark = 0;
            for (int i = 0; i < 9; i++)
            {
                moves[i] = 0;
            }
        }

        public void clearMoves()
        {
            for (int i = 0; i < 9; i++)
            {
                moves[i] = 0;
            }
        }

        public int getMove(int index)
        {
            return moves[index];
        }

        public void setMove(int index, int value)
        {
            if (value >= 0 && value < 10)
                moves[index] = value;
        }

        public int Square
        {
            get
            {
                return square;
            }
        }

    }
}
