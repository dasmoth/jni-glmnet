package glmnet;

import cern.colt.matrix.tdouble.*;
import cern.colt.matrix.tdouble.impl.*;

/**
 * Logistic/multinomial regression learner.
 *
 * This is a Java-eseque wrapper around The lognet and splognet functions from
 * the glmnet package.
 *
 * @author Thomas Down
 */

public class ClassificationLearner {
    private boolean standardize = true;
    private int covUpdating = 2;
    private double alpha = 1.0;
    private double convThreshold = 0.000001;
    private double minLambdaRatio = -1; // Set automatically.
    private int numLambdas = 100;
    private int maxIterations = 100000;
    
    public void setStandardize(boolean b) {
	this.standardize = b;
    }
    
    public boolean getStandardize() {
	return this.standardize;
    }

    public void setCovUpdate(int i) {
	if (i < 1 || i > 2) {
	    throw new IllegalArgumentException("Covariance update parameter must be 1 or 2");
	}
	this.covUpdating = i;
    }

    public int getCovUpdate() {
	return this.covUpdating;
    }

    public void setAlpha(double d) {
	if (d < 0.0 || d > 1.0) {
	    throw new IllegalArgumentException(String.format("Bad alpha parameter %g, allowed range 0.0-1.0", d));
	}
	this.alpha = d;
    }

    public double getAlpha() {
	return this.alpha;
    }

    public void setConvThreshold(double d) {
	this.convThreshold = d;
    }

    public void setNumLambdas(int i) {
	this.numLambdas = i;
    }

    public int getNumLambdas() {
	return this.numLambdas;
    }

    public void setMaxIterations(int i) {
	this.maxIterations = i;
    }

    public int getMaxIterations() {
	return this.maxIterations;
    }

    public ClassificationModelSet learn(boolean[] labels, DoubleMatrix2D x) {
	int rows = labels.length;
	int cols = x.columns();
	if (x.rows() != rows) {
	    throw new IllegalArgumentException("Outputs don't match rows of predictor matrix");
	}

	DenseColumnDoubleMatrix2D y = new DenseColumnDoubleMatrix2D(rows, 2);
	for (int i = 0; i < labels.length; ++i) {
	    y.set(i, labels[i] ? 0 : 1, 1.0);
	}

	int maxFinalFeatures = cols + 1;
	int maxPathFeatures = Math.min(maxFinalFeatures * 2, cols);

	double[] outIntercepts = new double[numLambdas];
	double[] outCoeffs = new double[maxPathFeatures * numLambdas];
	int[] outCoeffPtrs = new int[maxPathFeatures];
	int[] outCoeffCnts = new int[numLambdas];
	double[] outDev0 = new double[numLambdas];
	double[] outFdev = new double[numLambdas];
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

	double _mlr = minLambdaRatio;
	if (_mlr < 0) {
	    _mlr = rows < cols ? 0.01 : 0.0001;
	}

	int err;

	if ((x instanceof SparseDoubleMatrix2D) || 
	    (x instanceof SparseCCDoubleMatrix2D) ||
	    (x instanceof SparseRCDoubleMatrix2D))
	{
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

	    err = new GLMNet().splognet(
		alpha,
		1,
		y.elements(),
		new double[rows],
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
		standardize ? 1 : 0,
		maxIterations,
		0,
		outNumFits,
		outIntercepts,
		outCoeffs,
		outCoeffPtrs,
		outCoeffCnts,
		outDev0,
		outFdev,
		outLambdas,
		outNumPasses);
	} else {
	    DenseColumnDoubleMatrix2D dcdm = new DenseColumnDoubleMatrix2D(x.rows(), x.columns());
	    dcdm.assign(x);

	    err = new GLMNet().lognet(
		alpha,
		1,
		y.elements(),
		new double[rows],
		dcdm.elements(),
		mFlags,
		penalties,
		maxFinalFeatures,
		maxPathFeatures,
		numLambdas,
		_mlr,
		new double[100],
		convThreshold,
		standardize ? 1 : 0,
		maxIterations,
		0,
		outNumFits,
		outIntercepts,
		outCoeffs,
		outCoeffPtrs,
		outCoeffCnts,
		outDev0,
		outFdev,
		outLambdas,
		outNumPasses);
	}

	if (err != 0) {
	    throw new LearnerException(err);
	}

	return new ClassificationModelSet(outNumPasses[0], outNumFits[0], outIntercepts, outCoeffs, outCoeffPtrs, outCoeffCnts, cols, maxPathFeatures);
    }
}