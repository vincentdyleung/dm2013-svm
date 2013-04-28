package org.ethz.las;

import java.util.*;

public class SVM {

  private final double COMPARE_MARGIN = 0.00001;
  // Hyperplane weights.
  RealVector weights;

  public SVM(RealVector weights) {
    this.weights = weights;
  }

  /**
   * Instantiates an SVM from a list of training instances, for a given
   * learning rate 'eta' and regularization parameter 'lambda'.
   */
  public SVM(List<TrainingInstance> trainingSet, double lambda, double eta) {
    // TODO: Implement me!
    int dimension = trainingSet.get(0).getFeatures().getDimension();
    RealVector w = new RealVector(dimension);
    for (TrainingInstance instance : trainingSet) {
      RealVector gradientStep = gradient(w, instance).scaleThis(eta * -1);
      w.add(gradientStep);
      projection(w, lambda);
    }
    this.weights = w;
  }

  /** 
  * Helper functions for the SVM
  */

  /** 
  * Project a vector w into the feasible set with radius lambda
  */
  private void projection(RealVector w, double lambda) {
    if (w.getNorm() > Math.sqrt(lambda)) {
      double scaleFactor = lambda * w.getNorm();
      w.scaleThis(scaleFactor);
    } 
    return;
  }

  /**
  * Calculate gradient
  */

  private RealVector gradient(RealVector w, TrainingInstance instance) {
    double value = instance.getLabel() * w.dotProduct(instance.getFeatures());
    int dimension = w.getDimension();
    if (value >= 1) {
      return new RealVector(dimension);
    } else {
      return instance.getFeatures().scale(instance.getLabel() * -1);
    }
  }

  /**
   * Instantiates SVM from weights given as a string.
   */
  public SVM(String w) {
    List<Double> ll = new LinkedList<Double>();
    Scanner sc = new Scanner(w);
    while(sc.hasNext()) {
      double coef = sc.nextDouble();
      ll.add(coef);
    }

    double[] weights = new double[ll.size()];
    int cnt = 0;
    for (Double coef : ll)
      weights[cnt++] = coef;

    this.weights = new RealVector(weights);
  }

  /**
   * Instantiates the SVM model as the average model of the input SVMs.
   */
  public SVM(List<SVM> svmList) {
    int dim = svmList.get(0).getWeights().getDimension();
    RealVector weights = new RealVector(dim);
    for (SVM svm : svmList)
      weights.add(svm.getWeights());

    this.weights = weights.scaleThis(1.0/svmList.size());
  }

  /**
   * Given a training instance it returns the result of sign(weights'instanceFeatures).
   */
  public int classify(TrainingInstance ti) {
    RealVector features = ti.getFeatures();
    double result = ti.getFeatures().dotProduct(this.weights);
    if (result >= 0) return 1;
    else return -1;
  }

  public RealVector getWeights() {
    return this.weights;
  }

  @Override
  public String toString() {
    return weights.toString();
  }
}
