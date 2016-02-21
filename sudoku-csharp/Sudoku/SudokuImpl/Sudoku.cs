using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Sudoku.SudokuImpl;
using System.IO;
using System.Windows.Forms;
using Sudoku.UndoRedoImpl;

namespace Sudoku
{
    class Sudoku
    {
        private SudokuField[,] fields;
        private UndoRedo undo;

        public Sudoku(UndoRedo undo)
        {
            this.undo = undo;
            createFields();
        }

        // metoda vytvori policka sudoku
        // pouziva ju konstruktor a generator noveho sudoku
        private void createFields()
        {
            fields = new SudokuField[9, 9];

            int sq = 2;

            for (int i = 0; i < 9; i++)
            {
                if ((i != 0) && ((i % 3) == 0))
                    sq++;
                else
                    sq -= 2;
                for (int j = 0; j < 9; j++)
                {
                    if ((j != 0) && ((j % 3) == 0))
                        sq++;
                    fields[i, j] = new SudokuField(sq);
                }
            }
        }

        // property zisti, ci je sudoku vyriesene alebo nie
        public Boolean IsSolved
        {
            get
            {
                for (int i = 0; i < 9; i++)
                    for (int j = 0; j < 9; j++)
                        if (fields[i, j].Dirty == true ||
                                fields[i, j].Mark <= 0)
                            return false;
                return true;
            }
        }

        public int getMark(int row, int col)
        {
            if (row > 8 || col > 8 || row < 0 || col < 0)
                return 0;
            return fields[row, col].Mark;
        }

        // mark je od 1-9
        public void setMark(int row, int col, int mark, bool passUndo)
        {
            if (row > 8 || col > 8 || row < 0 || col < 0)
                return;
            FieldPosition pos;
            int old_mark = fields[row, col].Mark;
            if (fields[row, col].Dirty)
            {
                // tym ze prepiseme staru dirty hodnotu,
                // mozme tym vycistit niektore ine hodnoty
                fields[row, col].Mark = 0; // vycisti sa toto pole
                setDirtyFlags(row, col, old_mark);
            }
            fields[row, col].Mark = mark;
            fields[row, col].clearMoves();
            // ak ma byt zmena zaratana do unda
            if (passUndo)
            {
                undo.addChange(row, col, old_mark, mark);
            }
            // zistim, ci je prave oznacena pozicia v konflikte
            pos = canMark(col, row, mark);
            if (pos != null)
            {
                // ak ano, oznacim tuto hodnotu ako "dirty"
                fields[row, col].Dirty = true;
                fields[pos.Row, pos.Col].Dirty = true;
                setDirtyFlags(row, col, mark);
            }
            if (isDirty())
            {
                System.Console.WriteLine("[" + row + "," + col + "]:" + mark + " > DIRTY");
            }
        }

        public bool isDirty(int row, int col)
        {
            return fields[row, col].Dirty;
        }

        private bool isDirty()
        {
            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++)
                    if (isDirty(i, j))
                        return true;
            return false;
        }

        public void clear()
        {
            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++)
                    fields[i, j].clear();
        }

        public void load(String fileName)
        {
            clear();
            String s = "";
            try {
                StreamReader fs = File.OpenText(fileName);
                s = fs.ReadToEnd();
                fs.Close();
            } catch(IOException) {
                MessageBox.Show("Chyba: Nepodarilo sa otvoriť súbor!");
                return;
            }
            String[] tmp = s.Split('\n');

            //sq = 2;
            int tLen = tmp.Length;
            int tmpIndex = 0;

            for (int i = 0; i < 9 && tmpIndex < tLen; i++, tmpIndex++)
            {
                //if (i && !(i % 3)) sq++;
                //else sq -= 2;
                while ((tmpIndex < tLen) && tmp[tmpIndex].Trim().Equals(""))
                    tmpIndex++;
                if (tmpIndex >= tLen)
                {
                    MessageBox.Show("Chyba: nedostatočný počet čísel v súbore");
                    return;
                }
                String[] line = tmp[tmpIndex].Split(' ');
                int lLen = line.Length;
                int k = 0;

                for (int j = 0; j < 9; j++)
                {
                    while ((k < lLen) && (line[k].Equals(' ') 
                        || line[k].Equals('\n')
                        || line[k].Equals('\r')
                        || line[k].Equals('\t')
                        || line[k].Equals(""))) 
                        k++;
                    if (k >= lLen)
                    {
                        MessageBox.Show("Chyba: nedostatočný počet čísel v súbore");
                        return;
                    }
                    try
                    {
                        int m = Int16.Parse(line[k++]);
                        if (m < 0 || m > 9)
                        {
                            MessageBox.Show("Chybná hodnota (" + m + ") - číslo musí byť od 1-9!");
                            return;
                        }
                        this.setMark(i,j,m,false);
                    }
                    catch (Exception)
                    {
                        MessageBox.Show("Chybná hodnota (" + line[k-1] + ")!");
                        return;
                    }
                   // if (j && !(j % 3)) sq++;
                    //sudoku[i][j].square = sq;
                }
            }
        }

        public void save(String fileName)
        {
            try
            {
                StreamWriter fs = new StreamWriter(fileName);
                for (int i = 0; i < 9; i++)
                {
                    for (int j = 0; j < 9; j++)
                    {
                        fs.Write(fields[i, j].Mark);
                        if (j < 8)
                            fs.Write(' ');
                        if (((j+1) % 3) == 0)
                            fs.Write(' ');
                    }
                    fs.Write('\n');
                }
                fs.Flush();
                fs.Close();
            }
            catch (IOException)
            {
                MessageBox.Show("Chyba: Nepodarilo sa uložiť súbor!");
                return;
            }
        }

        public String getMovesString(int row, int col)
        {
            String s = "";
            this.findMoves();
            for (int i = 0; i < 9; i++)
                if (fields[row, col].getMove(i) != 0)
                {
                    if (s.Equals(""))
                        s = (i + 1).ToString();
                    else
                        s += "," + (i + 1).ToString();
                }
            return s;
        }

        // sudoku automation and checking

        /// Funkcia vracia null, ak je tah (x,y,mark) mozny
        /// ak nie je, vracia ohrozujucu poziciu
        private FieldPosition canMark(int x, int y, int mark)
        {
            int i, j, istart, jstart, ipodm, jpodm;

            /* najprv sa pozrie na cely riadok */
            for (j = 0; j < 9; j++)
                if ((j != x) && (fields[y, j].Mark == mark))
                    return new FieldPosition(y, j);

            /* potom sa pozrie na cely stlpec */
            for (i = 0; i < 9; i++)
                if ((i != y) && (fields[i, x].Mark == mark))
                    return new FieldPosition(i, x);

            /* potom sa pozrie v malom stvorceku */
            istart = y - (y % 3);
            jstart = x - (x % 3);
            ipodm = istart + 3;
            jpodm = jstart + 3;

            for (i = istart; i < ipodm; i++)
                for (j = jstart; j < jpodm; j++)
                    if (fields[i, j].Mark == mark)
                        if ((j != x) || (i != y))
                            return new FieldPosition(i, j);
            return null;
        }

        // metoda nastavi niektore pozicie ako "dirty" vtedy,
        // ak tieto su (resp. boli by) v konflikte s poziciou 
        // danou parametrami (row,col,mark)
        private void setDirtyFlags(int row, int col, int mark)
        {
            int i, j, istart, jstart, ipodm, jpodm;

            /* najprv sa pozrie na cely riadok */
            for (j = 0; j < 9; j++)
                if ((j != col) && (fields[row, j].Mark == mark))
                {
                    FieldPosition pos = canMark(j,row, mark);
                    if (pos == null)
                        fields[row, j].Dirty = false;
                    else
                    {
                        fields[row, j].Dirty = true;
                    }
                }

            /* potom sa pozrie na cely stlpec */
            for (i = 0; i < 9; i++)
                if ((i != row) && (fields[i, col].Mark == mark))
                {
                    FieldPosition pos = canMark(col, i, mark);
                    if (pos == null)
                        fields[i, col].Dirty = false;
                    else
                        fields[i, col].Dirty = true;
                }

            /* potom sa pozrie v malom stvorceku */
            istart = row - (row % 3);
            jstart = col - (col % 3);
            ipodm = istart + 3;
            jpodm = jstart + 3;

            for (i = istart; i < ipodm; i++)
                for (j = jstart; j < jpodm; j++)
                    if (fields[i, j].Mark == mark)
                        if ((j != col) || (i != row))
                        {
                            FieldPosition pos = canMark(j,i, mark);
                            if (pos == null)
                                fields[i, j].Dirty = false;
                            else
                                fields[i, j].Dirty = true;
                        }
        }

        private void detectWeakMoves()
        {
            int i, j, k;

            for (i = 0; i < 9; i++)
                for (j = 0; j < 9; j++)
                    if (fields[i, j].Mark == 0)
                    {
                        fields[i, j].clearMoves();
                        for (k = 0; k < 9; k++)
                            if (canMark(j, i, k + 1) == null)
                                fields[i, j].setMove(k, 1);
                    }
        }

        // metoda najde mozne tahy pre danu poziciu
        public void findMoves()
        {
            detectWeakMoves();
            removeBadMoves();
            removeCrossMoves();
        }

        /* funkcia vrati 1 ak je tah mozny, 0 ak nie */
        /* cinnost: skontroluje aktualny stvorec okrem oblasti, ktoru
                oznacenie zasiahne */
        private int analyzesquare(int istart, int jstart, int mark, int attack, int horizontal)
        {
            int ipodm, jpodm;
            int i, j;

            ipodm = istart + 3;
            jpodm = jstart + 3;
            /*
               Funkcia sa pouziva na hlbkovu analyzu uz najdenych moznych tahov pomocou
               funkcie removebadmoves. Najde tah mark v stvorci sq mimo jeho riadka s
               y-suradnicou attack_y ak vertical != 0 resp. mimo jeho stlpca s
               x-suradnicou attack_x ak vertical == 0.
               Ak bol tah najdeny (v tomto stvorci sq) znamena to, ze oznacenie sa musi
               ponechat.
               V opacnom pripade to znamena ze stvorec sq obsahuje hladany tah mark
               iba v "zasiahnutej" oblasti a teda sa krizi s povodnym oznacenim, ktore
               musi ustupit.
            */
            for (i = istart; i < ipodm; i++)
                for (j = jstart; j < jpodm; j++)
                    if (((horizontal!=0) && (i != attack)) ||
                        ((horizontal==0) && (j != attack)))
                    {
                        if ((fields[i,j].Mark == 0) 
                                && (fields[i,j].getMove(mark - 1) != 0))
                            return 1;
                        else if (fields[i,j].Mark == mark) return 1;
                    }
            return 0;
        }

        /* funkcia odstrani nemozne tahy */
        /* vrati 1 ak nastala v sudoku zmena, 0 ak nie */
        private int removeBadMoves()
        {
            int i, j, k, res, w = 0, istart, jstart;

            for (i = 0; i < 9; i++)
                for (j = 0; j < 9; j++)
                    if (fields[i,j].Mark == 0)
                    {
                        istart = i - (i % 3);
                        jstart = j - (j % 3);
                        for (k = 0; k < 9; k++)
                            if (fields[i,j].getMove(k) != 0)
                            {
                                /* analyza stvorcov */
                                res = 1;
                                switch (fields[i,j].Square)
                                {
                                    case 0:
                                        res &= analyzesquare(0, 3, k + 1, i, 1); /* doprava */
                                        res &= analyzesquare(0, 6, k + 1, i, 1); /* doprava */
                                        res &= analyzesquare(3, 0, k + 1, j, 0); /* dole */
                                        res &= analyzesquare(6, 0, k + 1, j, 0); /* dole */
                                        break;
                                    case 1:
                                        res &= analyzesquare(0, 0, k + 1, i, 1); /* dolava */
                                        res &= analyzesquare(0, 6, k + 1, i, 1); /* doprava */
                                        res &= analyzesquare(3, 3, k + 1, j, 0); /* dole */
                                        res &= analyzesquare(6, 3, k + 1, j, 0); /* dole */
                                        break;
                                    case 2:
                                        res &= analyzesquare(0, 3, k + 1, i, 1); /* dolava */
                                        res &= analyzesquare(0, 0, k + 1, i, 1); /* dolava */
                                        res &= analyzesquare(3, 6, k + 1, j, 0); /* dole */
                                        res &= analyzesquare(6, 6, k + 1, j, 0); /* dole */
                                        break;
                                    case 3:
                                        res &= analyzesquare(0, 0, k + 1, j, 0); /* hore */
                                        res &= analyzesquare(3, 3, k + 1, i, 1); /* doprava */
                                        res &= analyzesquare(3, 6, k + 1, i, 1); /* doprava */
                                        res &= analyzesquare(6, 0, k + 1, j, 0); /* dole */
                                        break;
                                    case 4:
                                        res &= analyzesquare(0, 3, k + 1, j, 0); /* hore */
                                        res &= analyzesquare(3, 0, k + 1, i, 1); /* dolava */
                                        res &= analyzesquare(3, 6, k + 1, i, 1); /* doprava */
                                        res &= analyzesquare(6, 3, k + 1, j, 0); /* dole */
                                        break;
                                    case 5:
                                        res &= analyzesquare(3, 0, k + 1, i, 1); /* dolava */
                                        res &= analyzesquare(3, 3, k + 1, i, 1); /* dolava */
                                        res &= analyzesquare(0, 6, k + 1, j, 0); /* hore */
                                        res &= analyzesquare(6, 6, k + 1, j, 0); /* dole */
                                        break;
                                    case 6:
                                        res &= analyzesquare(0, 0, k + 1, j, 0); /* hore */
                                        res &= analyzesquare(3, 0, k + 1, j, 0); /* hore */
                                        res &= analyzesquare(6, 3, k + 1, i, 1); /* doprava */
                                        res &= analyzesquare(6, 6, k + 1, i, 1); /* doprava */
                                        break;
                                    case 7:
                                        res &= analyzesquare(0, 3, k + 1, j, 0); /* hore */
                                        res &= analyzesquare(3, 3, k + 1, j, 0); /* hore */
                                        res &= analyzesquare(6, 0, k + 1, i, 1); /* dolava */
                                        res &= analyzesquare(6, 6, k + 1, i, 1); /* doprava */
                                        break;
                                    case 8:
                                        res &= analyzesquare(0, 6, k + 1, j, 0); /* hore */
                                        res &= analyzesquare(3, 6, k + 1, j, 0); /* hore */
                                        res &= analyzesquare(6, 0, k + 1, i, 1); /* dolava */
                                        res &= analyzesquare(6, 3, k + 1, i, 1); /* dolava */
                                        break;
                                }
                                fields[i,j].setMove(k,res);
                                w |= ~res;
                            }
                    }
            return w;
        }

        private void removeCrossMoves()
        {
            int i, j, k;

            for (i = 0; i < 9; i++)
                for (j = 0; j < 9; j++)
                    if (fields[i, j].Mark == 0)
                        for (k = 0; k < 9; k++)
                            if (fields[i, j].getMove(k) != 0)
                            {
                                if (canMark(j, i, k + 1) == null)
                                    fields[i, j].setMove(k, 1);
                                else {
                                    fields[i, j].setMove(k, 0);
                                }
                            }
        }

        //////// RIESENIE //////////

        // funkcia spocita mozne tahy
        private int countmoves(int x, int y)
        {
            int k, cnt = 0;
            for (k = 0; k < 9; k++)
                if (fields[y,x].getMove(k) != 0)
                    cnt++;
            return cnt;
        }

        /* Metoda oznaci policka, ktore maju len jednu moznost na oznacenie */
        /* funkcia vracia 1 ak bola zmena v sudoku, 0 ak nie */
        private bool markonly()
        {
            int i, j, k;
            bool w = false;

            for (i = 0; i < 9; i++)
                for (j = 0; j < 9; j++)
                    if (countmoves(j, i) == 1)
                    {
                        for (k = 0; k < 9; k++)
                            if (fields[i,j].getMove(k) != 0) break;
                        setMark(i, j, k + 1, false);
                        w = true;
                    }
            return w;
        }

        /* funkcia vracia 1 ak tah je mozny, 0 ak nie */
        /* funkcia zistuje tah podla uz zistenych povolenych tahov */
        private int canmove(int x, int y, int mark)
        {
            // TODO preco mark-1 ??
            // JASNE: preto lebo parameter je mark 1-9
            return fields[y,x].getMove(mark - 1);
        }

        /* funkcia vracia 1 ak nastala v sudoku zmena, 0 ak nie */
        // UNTESTED
        private bool markonlycol(int y) {
            int j,k;
            int[] moves = new int[10]; /* tahy, ktore v stlpci su */

            for (j = 0; j < 9; j++)
                moves[fields[y,j].Mark]++;
            /* v moves[0] bude pocet volnych policok */
            if ((moves[0] == 0) || (moves[0] > 1))
                return false;

            /* treba najst volne policko */
            for (j = 0; j < 9; j++)
                if (fields[y,j].Mark == 0) break;
            for (k = 1; k < 10; k++)
                if ((moves[k] == 0) && (canmove(j,y,k) != 0)) {
                    setMark(y, j, k, false);
                    return true;
                }
            return false;
        }

        /* funkcia vracia 1 ak nastala v sudoku zmena, 0 ak nie */
        // UNTESTED
        private bool markonlyrow(int x)
        {
            int i, k;
            int[] moves = new int[10]; /* tahy, ktore v riadku su */

            for (i = 0; i < 9; i++)
                moves[fields[i, x].Mark]++;
            /* v moves[0] bude pocet volnych policok */
            if ((moves[0] == 0) || (moves[0] > 1))
                return false;

            /* treba najst volne policko */
            for (i = 0; i < 9; i++)
                if (fields[i, x].Mark == 0) break;
            for (k = 1; k < 10; k++)
                if ((moves[k] == 0) && (canmove(x, i, k) != 0))
                {
                    setMark(i, x, k, false);
                    return true;
                }
            return false;
        }

        /* funkcia vracia 1 ak nastala v sudoku zmena, 0 ak nie */
        // UNTESTED
        private bool markonlysq(int sq)
        {
            int i, j=0, k;
            int istart=0, jstart=0, ipodm=0, jpodm=0;
            int[] moves = new int[10]; /* tahy, ktore v stvorci su */

            switch (sq)
            {
                case 0: istart = jstart = 0; break;
                case 1: istart = 0; jstart = 3; break;
                case 2: istart = 0; jstart = 6; break;
                case 3: istart = 3; jstart = 0; break;
                case 4: istart = jstart = 3; break;
                case 5: istart = 3; jstart = 6; break;
                case 6: istart = 6; jstart = 0; break;
                case 7: istart = 6; jstart = 3; break;
                case 8: istart = jstart = 6; break;
            }
            ipodm = istart + 3;
            jpodm = jstart + 3;

            for (i = istart; i < ipodm; i++)
                for (j = jstart; j < jpodm; j++)
                    moves[fields[i, j].Mark]++;
            /* v moves[0] bude pocet volnych policok */
            if ((moves[0] == 0) || (moves[0] > 1))
                return false;

            /* treba najst volne policko */
            for (i = istart; i < ipodm; i++)
                for (j = jstart; j < jpodm; j++)
                    if (fields[i, j].Mark == 0) goto sem;
        sem:
            for (k = 1; k < 10; k++)
                if ((moves[k] == 0) && (canmove(j, i, k) != 0))
                {
                    setMark(i, j, k, false);
                    return true;
                }

            return false;
        }

        /* funkcia vracia 1 ak nastala v sudoku zmena, 0 ak nie */
        /* cinnost: oznaci policko, ktoreho jeden z moznych tahov
                je z celeho stlpca len na tomto policku */
        // UNTESTED
        private bool markfreerow(int x)
        {
            int[] moves = new int[10];
            int i, k;

            /* spocitanie vsetkych tahov v stlpci */
            for (i = 0; i < 9; i++)
                if (fields[i, x].Mark == 0)
                    for (k = 0; k < 9; k++)
                        moves[k] += fields[i, x].getMove(k);

            /* najde tahy, ktore su len samotne v celom stlpci */
            for (i = 0; i < 9; i++)
                if (fields[i, x].Mark == 0)
                    for (k = 0; k < 9; k++)
                        if ((moves[k] == 1) && (canmove(x, i, k + 1) != 0))
                        {
                            setMark(i, x, k+1, false);
                            return true;
                        }
            return false;
        }

        /* funkcia vracia 1 ak nastala v sudoku zmena, 0 ak nie */
        /* cinnost: oznaci policko, ktoreho jeden z moznych tahov
                je z celeho riadka len na tomto policku */
        // TESTING
        private bool markfreecol(int y)
        {
            int[] moves = new int[10];
            int j, k;

            /* spocitanie vsetkych tahov v riadku */
            for (j = 0; j < 9; j++)
                if (fields[y, j].Mark == 0)
                    for (k = 0; k < 9; k++)
                        moves[k] += fields[y, j].getMove(k);

            /* najde tahy, ktore su len samotne v celom stlpci */
            for (j = 1; j < 2; j++)
                if (fields[y, j].Mark == 0)
                    for (k = 0; k < 9; k++)
                        if ((moves[k] == 1) && (canmove(j, y, k + 1) != 0))
                        {
                            setMark(y, j, k+1, false);
                            return true;
                        }
            return false;
        }

        /* funkcia vracia 1 ak nastala v sudoku zmena, 0 ak nie */
        /* cinnost: oznaci policko, ktoreho jeden z moznych tahov
                je z celeho stvorceka len na tomto policku */
        // UNTESTED
        private bool markfreesq(int sq)
        {
            int i, j, k;
            int istart=0, jstart=0, ipodm=0, jpodm=0;
            int[] moves = new int[10]; /* tahy, ktore v stvorci su */


            /* najdenie hranic stvorceka */
            switch (sq)
            {
                case 0: istart = jstart = 0; break;
                case 1: istart = 0; jstart = 3; break;
                case 2: istart = 0; jstart = 6; break;
                case 3: istart = 3; jstart = 0; break;
                case 4: istart = jstart = 3; break;
                case 5: istart = 3; jstart = 6; break;
                case 6: istart = 6; jstart = 0; break;
                case 7: istart = 6; jstart = 3; break;
                case 8: istart = jstart = 6; break;
            }
            ipodm = istart + 3;
            jpodm = jstart + 3;

            /* spocitanie vsetkych tahov v stvorceku */
            for (i = istart; i < ipodm; i++)
                for (j = jstart; j < jpodm; j++)
                    if (fields[i, j].Mark == 0)
                        for (k = 0; k < 9; k++)
                            moves[k + 1] += fields[i, j].getMove(k);

            /* najde tahy, ktore su len samotne v celom stvorci */
            for (i = istart; i < ipodm; i++)
                for (j = jstart; j < jpodm; j++)
                    if (fields[i, j].Mark == 0)
                        for (k = 1; k < 10; k++)
                            if ((moves[k] == 1) && (canmove(j, i, k) != 0))
                            {
                                setMark(i, j, k, false);
                                return true;
                            }
            return false;
        }

        private SudokuField[,] deepCopy()
        {
            SudokuField[,] fi = new SudokuField[9,9];
            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++)
                    fi[i, j] = fields[i, j].Clone();
            return fi;
        }

        private void restoreState(SudokuField[,] state)
        {
            this.fields = state;
        }

        private FieldBacktrack findFirstPosition()
        {
            int row=0, col=0;
            while (true)
            {
                if (fields[row, col].Mark == 0)
                    return new FieldBacktrack(row, col, 0);
                col++;
                if (col > 8)
                {
                    col = 0;
                    row++;
                }
                if (row > 8 || col > 8) break;
            }
            return null;
        }

        // BACKTRACKING - posledna moznost
        // otazka - treba ho? urcite mam zachytene vsetky moznosti
        // riesenia sudoku??
        private void backtracking()
        {
            // najde prvy volny tah a vyberie prvu moznost
            // a riesi sudoku normalne
            Stack<SudokuField[,]> state = new Stack<SudokuField[,]>();
            Stack<FieldBacktrack> positions = new Stack<FieldBacktrack>();
            FieldBacktrack pos = findFirstPosition();

            while (IsSolved == false)
            {
                SudokuField[,] saved = deepCopy();
                state.Push(saved);
                positions.Push(pos);

                // nastavi ziadanu moznost vyberu
                try
                {
                    setMark(pos.Row, pos.Col, pos.getPointingMark(fields), false);
                }
                catch (IndexOutOfRangeException)
                {
                    break;
                }
                sudoku();

                // sudoku musi byt ciste
                if (isDirty())
                {
                    restoreState(state.Pop());
                    pos = positions.Pop();
                    pos.increment(fields);
                }
            }
            positions.Clear();
            state.Clear();
        }


        private void sudoku()
        {
            int i, j;
            System.Console.WriteLine("SUDOKUUUUUUUUUUUU");

            detectWeakMoves();
        zac:
            removeBadMoves();
            removeCrossMoves();
            if (markonly())
                goto zac;
            for (i = 0; i < 9; i++)
            {
                if (markonlycol(i)) goto zac;
                if (markfreecol(i)) goto zac;
            }
            for (j = 0; j < 9; j++)
            {
                if (markonlyrow(j)) goto zac;
                if (markfreerow(j)) goto zac;
            }
            for (i = 0; i < 9; i++)
            {
                if (markonlysq(i)) goto zac;
                if (markfreesq(i)) goto zac;
            }
            System.Console.WriteLine("EXITING SUDOKUUUUUUUUUUUU");

        }

        // metoda vyriesi sudoku
        public void solve()
        {
            sudoku();
            if (this.IsSolved == false)
                backtracking();
        }

        // GENEROVANIE SUDOKU

        // level: 0 - easy
        //        1 - normal
        //        2 - hard
        public void generate(int level)
        {
            // save sudoku
            // mozno by stacilo jednoduche priradenie
            SudokuField[,] fi = this.deepCopy();

            // vytvorenie noveho vyrieseneho sudoku
            createFields();
            generateSolved();

            // postupne odoberanie cisel

        }


        private void generateSolved()
        {
            Random rnd = new System.Random((int)DateTime.Now.Ticks);

            detectWeakMoves();
            while (IsSolved == false && isDirty() == false)
            {
                int x = rnd.Next(0, 9);
                int y = rnd.Next(0, 9);

                while (fields[y, x].Mark != 0)
                {
                    x = rnd.Next(0, 9);
                    y = rnd.Next(0, 9);
                    System.Console.WriteLine("trying [" + y + "," + x + "]");
                }
                rnd = new Random((int)DateTime.Now.Ticks);

                int cm = countmoves(x,y);
                if (cm == 0) continue;

                int index = rnd.Next(0, cm-1);
                System.Console.WriteLine("index: " + index);

                FieldBacktrack pos = new FieldBacktrack(y, x, index);
                System.Console.Write("Moves [" + y + "," + x + "] = {");
                for (int r = 0; r < 9; r++)
                {
                    if (fields[y, x].getMove(r) != 0)
                        System.Console.Write((r + 1) + " ");
                }
                System.Console.WriteLine("}");

                System.Console.WriteLine("set: [" + y + "," + x + "]: " + pos.getPointingMark(fields));
                setMark(y, x, pos.getPointingMark(fields), false);
                sudoku();
                detectWeakMoves();  // ?

                System.Console.Write("Moves [" + y + "," + x + "] = {");
                for (int r = 0; r < 9; r++)
                {
                    if (fields[y, x].getMove(r) != 0)
                        System.Console.Write((r + 1) + " ");
                }
                System.Console.WriteLine("}");

            }
        }

    }
}
