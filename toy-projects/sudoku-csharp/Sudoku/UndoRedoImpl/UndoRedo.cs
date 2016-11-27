using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Sudoku.SudokuImpl;

namespace Sudoku.UndoRedoImpl
{
    public delegate void Update(StackField fi);
    public delegate void UpdateState();

    class UndoRedo
    {
        private Stack<StackField> undoList;
        private Stack<StackField> redoList;
        private Update upMethod;
        private UpdateState upStateMethod;

        public UndoRedo(Update upMethod, UpdateState upStateMethod)
        {
            undoList = new Stack<StackField>();
            redoList = new Stack<StackField>();
            this.upMethod = upMethod;
            this.upStateMethod = upStateMethod;
        }

        public void addChange(int row, int col, int mark, int new_mark)
        {
            undoList.Push(new StackField(new global::Sudoku.SudokuImpl.FieldPosition(row, col), mark, new_mark));
            if (upStateMethod != null)
                upStateMethod();
        }

        public StackField undo()
        {
            try
            {
                StackField fi = undoList.Pop();
                if (upMethod != null)
                    upMethod(fi);
                fi.switchMarks();
                redoList.Push(fi);
                if (upStateMethod != null)
                    upStateMethod();
                return fi;
            }
            catch (InvalidOperationException)
            {
                return null;
            }
        }

        public StackField redo()
        {
            try
            {
                StackField fi = redoList.Pop();
                if (upMethod != null)
                    upMethod(fi);
                fi.switchMarks();
                undoList.Push(fi);
                if (upStateMethod != null)
                    upStateMethod();
                return fi;
            }
            catch (InvalidOperationException)
            {
                return null;
            }
        }

        public bool canUndo()
        {
            return !(undoList.Count == 0);
        }

        public bool canRedo()
        {
            return !(redoList.Count == 0);
        }

        public void clear()
        {
            undoList.Clear();
            redoList.Clear();
            if (upStateMethod != null)
                upStateMethod();
        }

        public String UndoString
        {
            get
            {
                try
                {
                    StackField fi = undoList.Peek();
                    return fi.ToString();
                }
                catch (InvalidOperationException)
                {
                    return "";
                }
            }
        }

        public String RedoString
        {
            get
            {
                try
                {
                    StackField fi = redoList.Peek();
                    return fi.ToString();
                }
                catch (InvalidOperationException)
                {
                    return "";
                }
            }
        }

    }
}
