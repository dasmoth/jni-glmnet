package glmnet;


import cern.colt.matrix.tdouble.*;
import cern.colt.matrix.tdouble.impl.*;

/**
 * Set of models produced by a glmnet learner run.
 *
 * @author Thomas Down
 */

public class ClassificationModelSet {
    private final int numPasses;
    private final int numFits;
    private final double[] intercepts;
    private final double[] coeffs;
    private final int[] coeffPtrs;
    private final int[] coeffCnts;
    private final int columns;
    private final int maxPathFeatures;

    ClassificationModelSet(int numPasses, int numFits,
		       double[] intercepts, double[] coeffs,int[] coeffPtrs, int[] coeffCnts,
		       int columns, int maxPathFeatures) 
    {
	this.numPasses = numPasses;
	this.numFits = numFits;
	this.intercepts = intercepts;
	this.coeffs = coeffs;
	this.coeffPtrs = coeffPtrs;
	this.coeffCnts = coeffCnts;

	this.columns = columns;
	this.maxPathFeatures = maxPathFeatures;
    }

    public int getNumPasses() {
	return numPasses;
    }

    public int getNumFits() {
	return numFits;
    }

    public ClassificationModel getModel(int i) {
	if (i < 0 || i >= numFits) {
	    throw new IllegalArgumentException(String.format("No model %d, allowed range 0-%d", i, numFits - 1));
	}

	DoubleMatrix1D weights = new SparseDoubleMatrix1D(columns);
	for (int j = 0; j < coeffCnts[i]; ++j) {
	    weights.set(coeffPtrs[j] - 1, coeffs[i*maxPathFeatures + j]);
	}
	return new ClassificationModel(intercepts[i], weights);
    }
}
