package glmnet;

public class GLMNet {
    static {
	System.loadLibrary("glmnet");
    }

    /*
c call spelnet(ka,parm,no,ni,x,ix,jx,y,w,jd,vp,ne,nx,nlam,flmin,ulam,thr,
c             isd,maxit,lmu,a0,ca,ia,nin,rsq,alm,nlp,jerr)
    */

    public native int spelnet(
       int covUpdating,
       double alpha,
       double[] y, 
       double[] w, 
       double[] xx, 
       int[] xi, 
       int [] xp, 
       int[] mFlags, 
       double[] penalties, 
       int maxFinal, 
       int maxPath, 
       int numLambdas, 
       double lambdaMinRatio, 
       double[] userLambdas, 
       double convThreshold, 
       int standardize, 
       int maxit, 
       int[] outNumFits, 
       double[] outIntercepts, 
       double[] outCoeffs, 
       int[] outCoeffPtrs, 
       int [] outCoeffCnts, 
       double[] outRsq, 
       double[] outLambdas, 
       int[] outNumPasses);

    /*
c call splognet (parm,no,ni,nc,x,ix,jx,y,o,jd,vp,ne,nx,nlam,flmin,
c             ulam,thr,isd,maxit,kopt,lmu,a0,ca,ia,nin,dev0,fdev,alm,nlp,jerr)
c
    */

    public native int splognet(
       double alpha,
       int nc,
       double[] y,
       double[] offsets,
       double[] xx, 
       int[] xi, 
       int [] xp, 
       int[] mFlags, 
       double[] penalties, 
       int maxFinal, 
       int maxPath, 
       int numLambdas, 
       double lambdaMinRatio, 
       double[] userLambdas, 
       double convThreshold, 
       int standardize, 
       int maxit, 
       int kopt,
       int[] outNumFits, 
       double[] outIntercepts, 
       double[] outCoeffs, 
       int[] outCoeffPtrs, 
       int [] outCoeffCnts, 
       double[] dev0,
       double[] fdev,
       double[] outLambdas, 
       int[] outNumPasses);

}
