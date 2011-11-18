package glmnet;

import java.util.*;

import cern.colt.matrix.tdouble.*;
import cern.colt.matrix.tdouble.impl.*;

public class TestSparseMatrix {
    public static void main(String[] args)
	throws Exception
    {
	DoubleMatrix2D m = new DenseDoubleMatrix2D(3,2);
	m.set(0, 0, 1.0);
	m.set(1, 1, 2.0);
	m.set(2, 0, 3.0);

	SparseCCDoubleMatrix2D mc = new SparseCCDoubleMatrix2D(3,2);
	mc.assign(m);
	mc.sortRowIndexes();
	mc.trimToSize();

	System.out.println(mc);

	System.out.println("Values " + toString(mc.getValues()));
	System.out.println("Indices " + toString(mc.getRowIndexes()));
	System.out.println("Col pointers " + toString(mc.getColumnPointers()));
    }

    private static String toString(int[] i) {
	StringBuilder sb = new StringBuilder("[");
	sb.append(i[0]);
	for (int x = 1; x < i.length; ++x) {
	    sb.append(", ");
	    sb.append(i[x]);
	}
	sb.append(']');
	return sb.toString();
    }

    private static String toString(double[] i) {
	StringBuilder sb = new StringBuilder("[");
	sb.append(i[0]);
	for (int x = 1; x < i.length; ++x) {
	    sb.append(", ");
	    sb.append(i[x]);
	}
	sb.append(']');
	return sb.toString();
    }
}
