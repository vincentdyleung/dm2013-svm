package org.ethz.las;

import java.util.*;

public class SVM {

  private final double B = 0.0041375;
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
      double value = instance.getLabel() * w.dotProduct(instance.getFeatures());
      if (value >= 1) {
        continue;
      } else {
        w.add(instance.getFeatures().scale(eta * instance.getLabel()));
        project(w, 1 / lambda);
      }
    }
    this.weights = w;
  }

  /** 
  * Helper functions for the SVM
  */

  /** 
  * Project a vector w into the feasible set with radius
  */
  private void project(RealVector w, double radius) {
    double scaleFactor = radius / w.getNorm();
    if (scaleFactor < 1) {
      w.scaleThis(scaleFactor);
    }
    return;
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
    System.out.println(this.toString());
  }

  /**
   * Given a training instance it returns the result of sign(weights'instanceFeatures).
   */
  public int classify(TrainingInstance ti) {
    RealVector features = ti.getFeatures();
    double result = ti.getFeatures().dotProduct(this.weights);
    //System.out.println(result);
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
