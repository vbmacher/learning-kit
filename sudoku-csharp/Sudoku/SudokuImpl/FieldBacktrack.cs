using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Sudoku.SudokuImpl
{
    class FieldBacktrack
    {
        private int row;
        private int col;
        private int index;

        public FieldBacktrack(int row, int col, int index)
        {
            this.row = row;
            this.col = col;
            this.index = index;
        }

        public int Row
        {
            get
            {
                if (row > 8)
                    throw new IndexOutOfRangeException();
                return row;
            }
        }

        public int Col
        {
            get
            {
                if (col > 8)
                    throw new IndexOutOfRangeException();
                return col;
            }
        }

        // metoda vrati tah, na ktory ukazuje tato instancia (od 1-9)
        public int getPointingMark(SudokuField[,] sudoku)
        {
            int i = 0;
            // najdem fyzicke poradie v poli moznych tahov
            // (index ukazuje logicke poradie - poradie mozneho tahu v zozname
            //  moznych tahov, fyzicke pole na ulozenie moznych tahov
            //  obsahuje vsetky cisla 0-8 s tym ze ak je tah mozny tak je
            //  hodnota prvku 1 inak 0)
            int k = 0;
            System.Console.Write("point ["+row +"," + col+ "]: ");
            for (k = 0; k < 9; k++)
            {
            //    System.Console.WriteLine(" " + k + " >" + sudoku[row, col].getMove(k));
                System.Console.Write((k + 1) + "[" + sudoku[row, col].getMove(k) + "] ");
                if (sudoku[row, col].getMove(k) == 0)
                    continue;
                if (i == index) 
                    break;
                i++;
            }
            System.Console.WriteLine();
            if (i == index)
                return k+1;
            else
                return 0;
        }

        public void increment(SudokuField[,] sudoku)
        {
            int i = 0;
            // najdem fyzicke poradie v poli moznych tahov
            // (index ukazuje logicke poradie - poradie mozneho tahu v zozname
            //  moznych tahov, fyzicke pole na ulozenie moznych tahov
            //  obsahuje vsetky cisla 0-8 s tym ze ak je tah mozny tak je
            //  hodnota prvku 1 inak 0)
            int k = 0;
            for (k = 0; k < 9; k++)
            {
                if (sudoku[row, col].getMove(k) == 0)
                    continue;
                if (i == index)
                    break;
                i++;
            }
            // "k" ukazuje na fyzicke poradie. Teraz treba k zvacsit na
            // nasledujuci mozny tah (ak je mozny). Ak nie je mozny,
            // najst nasledujuci
            bool found = false;
            for (; k < 9; k++)
                if (sudoku[row, col].getMove(k) != 0)
                {
                    found = true;
                    break;
                }
            if (found)
                index++;
            else
            {
                found = false;

                while (true)
                {
                    // inak inkrementujem suradnice a hladam novy volny tah
                    col++;
                    if (col > 8)
                    {
                        col = 0;
                        row++;
                    }
                    if (row > 8 || col > 8) break;
                    if (sudoku[row,col].Mark != 0) continue;
                    index = 0;
                    break;
                }
            }

        }

    }
}
