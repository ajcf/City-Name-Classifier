
/**
 * Two-class naive Bayes classifier.
 * 
 * This classifier stores information about two classes, known as "1" or
 * "positive," and "0" or negative.
 */
import java.util.ArrayList;
import java.util.List;

public class NaiveBayesClassifier {

 /* "features" maintains a list of all the features we are considering
  * in this classifier.  
  * 
  * "classTotals" counts how many training examples we've seen for each
  * of the two classes (0 and 1).
  */
 private List<Feature> features = new ArrayList<Feature>();
 private int[] classTotals = new int[2];
 public NaiveBayesClassifier() {

    for(int m = 0; m< 26; m++){
     for(int j = 0; j<26; j++){
       for(int i = 0; i< 26; i++){
         String word = Character.toString((char)(65+m));
         word += (char)(65+j);
         word += (char)(65+i);
         Feature feature = new Feature(word);
         features.add(feature);
       }
     }
   }

   for(int m = 0; m< 26; m++){
     for(int j = 0; j<26; j++){
       for(int i = 0; i< 26; i++){
         String word = Character.toString((char)(65+m));
         word += (char)(65+j);
         word += (char)(65+i);
         Feature feature = new Feature(word);
         features.add(feature);
       }
     }
   }
   for(int j = 0; j<26; j++){
     for(int i = 0; i< 26; i++){
       String word = Character.toString((char)(65+j));
       word += (char)(65+i);
       Feature feature = new Feature(word);
       features.add(feature);
     }
   }
   
   for(int k = 0; k<25; k++){
     String letter = Character.toString((char)(65+k));
     Feature feature = new Feature(letter);
     features.add(feature);
   }
  /* YOUR CODE HERE
   *
   * Create the set of features that we'll use.
   * For part 1 of the assignment, there are 26 features:
   * one for each letter of the alphabet.  So, you need to
   * create each feature and add it to the "features" List.
   */
 }

 public void addTrainingExample(String s, int classNumber) {
   for(Feature f : features){
     f.addTrainingExample(f.isFeaturePresent(s), classNumber);
   }
   classTotals[classNumber]++;
  /* YOUR CODE HERE
   *
   * Given a string and its correct classification, do the following:
   * 
   * (1) Update the counts that are used to compute the probability of a 
   *     feature given a class.  For each feature, you will need to call
   *     Feature.isFeaturePresent() to determine if the feature is in 
   *     String s, then call Feature.addTrainingExample() with appropriate
   *     arguments.
   * (2) Update the counts that are used to compute the prior probability
   *     (classTotals).
   */
 }

 private double getPriorProbability(int classNumber) {
   double pos = (double)classTotals[1];
   double neg = (double)classTotals[0];
   double retVal = (double)classTotals[classNumber];
   return (1+retVal)/(2+pos+neg);
  /* YOUR CODE HERE
   *
   * Compute and return the prior probability that a string belongs to a class.
   * That is, compute P(C=classNum).
   * 
   * Hint: Remember about integer division in Java.
   */

  //return 0; // OK to remove this line when writing method
 }

 private double getLikelihood(int classNumber, String s)
 {
   double returnValue = 1.0;
   for(Feature f : features){
     returnValue = returnValue * f.getProbOfFeatureGivenClass(f.isFeaturePresent(s), classNumber);
   }
   return returnValue;
  /* YOUR CODE HERE
   *
   * Compute and return the likelihood of the class given the features, which
   * is equivalent to the probability of the features given the class.
   * 
   * That is, compute P(F1=f1 and F2=f2 and ... Fn=fn | C=classNumber)
   *   where f1, f2, ..., fn are 0 or 1 depending on the presence or absence
   *   of the feature in that string.
   * 
   * Be sure to use the Naive Bayes independence assumption!
   */

  //return 0; // OK to remove this line when writing method
 }

 public int classify(String s) {
   double probability0 = getPriorProbability(0) * getLikelihood(0, s);
   double probability1 = getPriorProbability(1) * getLikelihood(1, s);
   if(probability0 > probability1) return 0;
   return 1;
  /* YOUR CODE HERE
   *
   * Compute and return the most probable class (0 or 1) that this string
   * belongs to, using the maximum a posteriori (MAP) decision rule.
   *
   * You can do this using calls to getPriorProbability and getLikelihood.
   * You do not need to call getPosteriorProbability.
   */

  //return 0; // OK to remove this line when writing method
 }

 public double getPosteriorProbability(int classNumber, String s)
 {
   double hiPhil = getPriorProbability(classNumber)*getLikelihood(classNumber,s);
   double totalProbability = getPriorProbability(0)*getLikelihood(0,s) + getPriorProbability(1)*getLikelihood(1,s);
   return hiPhil/totalProbability;
   
   
  /* YOUR CODE HERE
   *
   * Compute and return the probability that the given string belongs to
   * class classNumber.
   * 
   * That is, compute P(C=classNumber | F1=f1 and F2=f2 and ... Fn=fn)
   *   where f1, f2, ..., fn are 0 or 1 depending on the presence or absence
   *   of the feature in that string.
   *
   * The code will be similar to classify, except that you need to also
   * need to use Bayes' rule and the law of total probability.
   *
   * Note that this function is not necessary for a working classifier.
   * That is, you don't need to call getPosteriorProbability inside of classify.
   */

  //return 0; // OK to remove this line when writing method
 }
}
