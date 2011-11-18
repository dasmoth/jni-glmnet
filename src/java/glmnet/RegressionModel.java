package glmnet;


import cern.colt.matrix.tdouble.*;
import cern.colt.matrix.tdouble.impl.*;

/**
 * Linear model
 *
 * @author Thomas Down
 */

public class RegressionModel {
    private final double intercept;
    private final DoubleMatrix1D weights;

    RegressionModel(double intercept, DoubleMatrix1D weights) {
	this.weights = weights;
	this.intercept = intercept;
    }

    public double estimate(DoubleMatrix1D features) {
	return intercept + weights.zDotProduct(features);
    }
}