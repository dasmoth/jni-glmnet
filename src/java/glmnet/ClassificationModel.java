package glmnet;


import cern.colt.matrix.tdouble.*;
import cern.colt.matrix.tdouble.impl.*;

/**
 * Linear model
 *
 * @author Thomas Down
 */

public class ClassificationModel {
    private final double intercept;
    private final DoubleMatrix1D weights;

    ClassificationModel(double intercept, DoubleMatrix1D weights) {
	this.weights = weights;
	this.intercept = intercept;
    }

    public double estimate(DoubleMatrix1D features) {
	return logit(intercept + weights.zDotProduct(features));
    }

    public double logit(double x) {
	return 1.0 / (1.0 + Math.exp(-x));
    }
}