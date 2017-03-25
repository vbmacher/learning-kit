// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Genetic.java

package jumpers;

import java.util.ArrayList;
import java.util.Random;

// Referenced classes of package jumpers:
//            ExcelDataManip

public class Genetic
{
    private class Graph
    {

        void connect(int i, int j, double x)
        {
            M[i][j] = x;
        }

        final double distance(int i, int j)
        {
            return M[i][j];
        }

        final int size()
        {
            return N;
        }

        protected int N;
        double M[][];
        final Genetic this$0;

        public Graph(ArrayList excelData)
        {
            this$0 = Genetic.this;
            super();
            N = excelData.size();
            M = new double[N][N];
            for(int i = 0; i < N; i++)
            {
                for(int j = 0; j < N; j++)
                    if(i != j)
                        connect(i, j, ExcelDataManip.getW_ij(excelData, i, j));
                    else
                        connect(i, j, 0.0D);

            }

        }
    }


    private Genetic(ArrayList excelData, Graph g)
    {
        this.excelData = excelData;
        graph = g;
        N = graph.size();
        From = new int[N];
        To = new int[N];
    }

    public Genetic(ArrayList excelData)
    {
        this.excelData = excelData;
        graph = new Graph(excelData);
        N = graph.size();
        From = new int[N];
        To = new int[N];
    }

    public Object clone()
    {
        Genetic p = new Genetic(excelData, graph);
        p.L = L;
        for(int i = 0; i < N; i++)
        {
            p.From[i] = From[i];
            p.To[i] = To[i];
        }

        return p;
    }

    public void random(Random r)
    {
        int i;
        for(i = 0; i < N; i++)
            To[i] = -1;

        int i0 = i = 0;
        for(; i < N - 1; i++)
        {
            int j = (int)(r.nextLong() % (long)(N - i));
            To[i0] = 0;
            int k;
            int j0 = k = 0;
            for(; k < j; k++)
                for(j0++; To[j0] != -1; j0++);

            for(; To[j0] != -1; j0++);
            To[i0] = j0;
            From[j0] = i0;
            i0 = j0;
        }

        To[i0] = 0;
        From[0] = i0;
        getlength();
    }

    public double length()
    {
        return L;
    }

    public boolean improve()
    {
        double H[] = new double[N];
        for(int i = 0; i < N; i++)
            H[i] = (-graph.distance(From[i], i) - graph.distance(i, To[i])) + graph.distance(From[i], To[i]);

        for(int i = 0; i < N; i++)
        {
            double d1 = -graph.distance(i, To[i]);
            for(int j = To[To[i]]; j != i; j = To[j])
            {
                double d2 = H[j] + graph.distance(i, j) + graph.distance(j, To[i]) + d1;
                if(d2 < -1E-010D)
                {
                    int h = From[j];
                    To[h] = To[j];
                    From[To[j]] = h;
                    h = To[i];
                    To[i] = j;
                    To[j] = h;
                    From[h] = j;
                    From[j] = i;
                    getlength();
                    return true;
                }
            }

        }

        return false;
    }

    public boolean improvecross()
    {
        for(int i = 0; i < N; i++)
        {
            double d1 = -graph.distance(i, To[i]);
            int j = To[To[i]];
            double d2 = 0.0D;
            double d = 0.0D;
            for(; To[j] != i; j = To[j])
            {
                d += graph.distance(j, From[j]) - graph.distance(From[j], j);
                d2 = (d1 + graph.distance(i, j) + d + graph.distance(To[i], To[j])) - graph.distance(j, To[j]);
                if(d2 < -1E-010D)
                {
                    int h = To[i];
                    int h1 = To[j];
                    To[i] = j;
                    To[h] = h1;
                    From[h1] = h;
                    int hj = i;
                    for(; j != h; j = h1)
                    {
                        h1 = From[j];
                        To[j] = h1;
                        From[j] = hj;
                        hj = j;
                    }

                    From[j] = hj;
                    getlength();
                    return true;
                }
            }

        }

        return false;
    }

    void getlength()
    {
        L = 0.0D;
        for(int i = 0; i < N; i++)
            L += graph.distance(i, To[i]);

    }

    public void localoptimize()
    {
        while(improve() || improvecross()) ;
    }

    private ArrayList excelData;
    private Graph graph;
    int N;
    double L;
    public int From[];
    public int To[];
}
