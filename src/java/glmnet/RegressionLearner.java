package glmnet;

import cern.colt.matrix.tdouble.*;
import cern.colt.matrix.tdouble.impl.*;

/**
 * Linear regression learner.
 *
 * This is a Java-eseque wrapper around The elnet and spelnet functions from
 * the glmnet package.
 *
 * @author Thomas Down
 */

public class RegressionLearner {
    private int standardize = 1;
    private int covUpdating = 2;
    private double alpha = 1.0;
    private double convThreshold = 0.000001;
    private double minLambdaRatio = -1; // Set automatically.
    private int numLambdas = 100;
    private int maxIterations = 100000;
    
    public RegressionModelSet learn(DoubleMatrix1D y, DoubleMatrix2D x) {
	int rows = (int) y.size(); // Matrix1Ds can can be >MAX_INT?  What is the world coming to?
	int cols = x.columns();
	if (x.rows() != rows) {
	    throw new IllegalArgumentException("Outputs don't match rows of predictor matrix");
	}

	int maxFinalFeatures = cols + 1;
	int maxPathFeatures = Math.min(maxFinalFeatures * 2, cols);

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

	double[] weights = new double[rows];
	for (int i = 0; i < rows; ++i) {
	    weights[i] = 1.0;
	}

	SparseCCDoubleMatrix2D sccd = new SparseCCDoubleMatrix2D(x.rows(), x.columns());
	sccd.assign(x);
	int[] xi = sccd.getRowIndexes();
	for (int i = 0; i < xi.length; ++i) {
	    xi[i] += 1;
	}
	int[] xp = sccd.getColumnPointers();
	for (int i = 0; i < xp.length; ++i) {
	    xp[i] += 1;
	}
	double[] xx = sccd.getValues();

	DenseDoubleMatrix1D yc = new DenseDoubleMatrix1D(rows);
	yc.assign(y);

	double _mlr = minLambdaRatio;
	if (_mlr < 0) {
	    _mlr = rows < cols ? 0.01 : 0.0001;
	}
	
	int err = new GLMNet().spelnet(
	   covUpdating,
	   alpha,
	   yc.elements(),
	   weights,
	   xx,
	   xi,
	   xp,
	   mFlags,
	   penalties,
	   maxFinalFeatures,
	   maxPathFeatures,
	   numLambdas,
	   _mlr,
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
	

	return new RegressionModelSet(outNumPasses[0], outNumFits[0], outRsq, outIntercepts, outCoeffs, outCoeffPtrs, outCoeffCnts);
    }
}