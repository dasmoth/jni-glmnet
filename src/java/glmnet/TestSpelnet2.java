package glmnet;

import cern.colt.matrix.tdouble.*;
import cern.colt.matrix.tdouble.impl.*;

public class TestSpelnet2 {
    public static void main(String[] args) {
	double[] y = {
	    -0.5656782,
	    -1.462403,
	    -0.6176524,
       	    -1.630873,
	    -0.4510800
	};

	double[] xx = {
	    -0.876767,
	    -1.027823,
	    -0.9359603,
	    -2.070100,
	    0.5701036,
	    -2.345153,
	    0.01993692,
	    1.099917
	};

    	int[] xi = {1, 4, 1, 3, 2, 1, 2, 3};
	int[] xp = {1, 2, 2, 3, 3, 3, 3, 4, 5, 6, 6, 9};

	int rows = y.length;
	int cols = xp.length - 1;

	//	double[][] matrix = new double[rows][cols];
	DoubleMatrix2D matrix = new SparseDoubleMatrix2D(rows, cols);
	for (int c = 0; c < cols; ++c) {
	    for (int dp = xp[c]; dp < xp[c + 1]; ++dp) {
		matrix.set(xi[dp-1]-1, c, xx[dp-1]);
	    }
	}

	RegressionLearner r = new RegressionLearner();
	RegressionModelSet rms = r.learn(new DenseDoubleMatrix1D(y), matrix);

	for (int i = 0; i < rms.getNumFits(); ++i) {
	    System.out.print(i);
	    RegressionModel m = rms.getModel(i);
	    for (int rr = 0; rr < rows; ++rr) {
		System.out.print("\t" + m.estimate(matrix.viewRow(rr)));
	    }
	    System.out.println();
	}

    }
}