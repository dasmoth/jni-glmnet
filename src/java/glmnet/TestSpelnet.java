package glmnet;

public class TestSpelnet {
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

	double[][] matrix = new double[rows][cols];
	for (int c = 0; c < cols; ++c) {
	    for (int dp = xp[c]; dp < xp[c + 1]; ++dp) {
		matrix[xi[dp-1]-1][c] = xx[dp-1];
	    }
	}

	for (int r = 0; r < rows; ++r) {
	    for (int c = 0; c < cols; ++c) {
		System.out.printf("%.3g\t", matrix[r][c]);
	    }
	    System.out.println();
	}
	
	double[] w = new double[rows];
	for (int i = 0; i < rows; ++i) {
	    w[i] = 1;
	}
	
	double alpha = 1.0;
	int standardize = 1;
	double convThreshold = 0.0000001;
	int maxFinalFeatures = cols + 1;
	int maxPathFeatures = Math.min(maxFinalFeatures * 2, cols);
	double minLambdaRatio = rows < cols ? 0.01 : 0.0001;
	int numLambdas = 100;
	int covUpdating = 2;
	int maxIterations = 100000;

	double[] outIntercepts = new double[numLambdas];
	double[] outCoeffs = new double[maxPathFeatures * numLambdas];
	int[] outCoeffPtrs = new int[maxPathFeatures];
	int[] outCoeffCnts = new int[numLambdas];
	double[] outRsq = new double[numLambdas];
	double[] outLambdas = new double[numLambdas];
	int[] outNumPasses = new int[1];
	int[] outNumFits = new int[1];
      

	int[] mFlags = new int[1];
	double[] penalties = new double[cols];
	for (int i = 0; i < cols; ++i) {
	    penalties[i] = 1.0;
	}
	
	

	int err = new GLMNet().spelnet(
	   covUpdating,
	   alpha,
	   y,
	   w,
	   xx,
	   xi,
	   xp,
	   mFlags,
	   penalties,
	   maxFinalFeatures,
	   maxPathFeatures,
	   numLambdas,
	   minLambdaRatio,
	   new double[100],
	   convThreshold,
	   standardize,
	   maxIterations,
	   outNumFits,
	   outIntercepts,
	   outCoeffs,
	   outCoeffPtrs,
	   outCoeffCnts,
	   outRsq,
	   outLambdas,
	   outNumPasses);

	System.err.printf("Back in Java, err=%d%n", err);
	System.err.printf("numFits=%d, numPasses=%d%n", outNumFits[0], outNumPasses[0]);

	for (int i = 0; i < outNumFits[0]; ++i) {
	    double[] c = new double[cols];
	    double inter = outIntercepts[i];
	    for (int j = 0; j < outCoeffCnts[i]; ++j) {
		c[outCoeffPtrs[j] - 1] = outCoeffs[i*maxPathFeatures + j];
	    }

	    
	    for (int x = 0; x < rows; ++x) {
		double est = inter;
		for (int cc = 0; cc < cols; ++cc) {
		    est += c[cc] * matrix[x][cc];
		}
		System.out.printf("%.3g\t", est);
	    }


	    // for (int cc = 0; cc < c.length; ++cc) {
	    //	System.out.printf("\t%.3g", c[cc]);
	    // }
	    System.out.println();
	}	
    }
}