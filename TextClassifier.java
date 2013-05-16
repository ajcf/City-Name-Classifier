
/**
 * Wrapper that reads input files, sends them to a 2-class classifier,
 * and evaluates its predictions.
 *
 * The two-class classifier is structured in terms of a "positive" class (1)
 * and a "negative" class (0).
 *
 * Input files can be any text. See getLinesFromFile() for preprocessing.
 */
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class TextClassifier {

	public static void main(String[] argv) {
		
		// Training and testing files can be given on the command line or from standard input.
		// If using comand-line arguments, use the syntax:
		//    java TextClassifier class1-trainfile class0-trainfile class1-testfile class0-testfile
		// Otherwise, you will be prompted to enter the four files in the order the previous line says.
		
		String posTrainFile, negTrainFile, posTestFile, negTestFile;
		
		if (argv.length != 4) {
			Scanner scan = new Scanner(System.in);
			System.out.print("Enter training file with positive examples: ");
			posTrainFile = scan.next();
			System.out.print("Enter training file with negative examples: ");
			negTrainFile = scan.next();
			System.out.print("Enter testing file with positive examples: ");
			posTestFile = scan.next();
			System.out.print("Enter testing file with negative examples: ");
			negTestFile = scan.next();
		}
		else {
			posTrainFile = argv[0];
			negTrainFile = argv[1];
			posTestFile = argv[2];
			negTestFile = argv[3];
		}

		// read in examples from files and train a classifier
		NaiveBayesClassifier nbc = trainClassifier(posTrainFile, negTrainFile);

		// how well does it do?
		evaluatePerformance(nbc, posTestFile, negTestFile);
	}

	protected static NaiveBayesClassifier trainClassifier(String posExampleFile, String negExampleFile) {
		List<String> posExamples = getLinesFromFile(posExampleFile);
		List<String> negExamples = getLinesFromFile(negExampleFile);
		NaiveBayesClassifier nbc = new NaiveBayesClassifier();

		for (String posEx : posExamples) {
			nbc.addTrainingExample(posEx, 1);
		}
		for (String negEx : negExamples) {
			nbc.addTrainingExample(negEx, 0);
		}
		return nbc;
	}

	private static void evaluatePerformance(NaiveBayesClassifier nbc, String posTestFile, String negTestFile) {
		List<String> posExamples = getLinesFromFile(posTestFile);
		List<String> negExamples = getLinesFromFile(negTestFile);

		// print out each test case, and calculate accuracy and squared error
		System.out.println("                                                       posterior");
		System.out.println("string                         true class pred.class   prob C=1  correct?");
		System.out.println("===========================================================================");
		//System.out.printf("%-30s%12s%12s%12s%10s\n", "string", "true class", "pred.class", "postr C=1", "correct?");
		//System.out.println("string\ttrue class\tprediction\tcorrect?");
		int numCorrect = 0;
		int numTried = 0;
		double squaredError = 0;
		for (String posEx : posExamples) {
			double probClass1 = nbc.getPosteriorProbability(1, posEx);//getProbabilityClass1(posEx);
			int predictedClass = nbc.classify(posEx);//probClass1 >= .5) ? 1 : 0;
			String truncatedString = (posEx.length() > 30) ? posEx.substring(0, 27) + "..." : posEx;
			System.out.printf("%-30s%11d%11d%11f%10s\n",
							truncatedString, 1, predictedClass, probClass1, predictedClass == 1 ? "Y" : "N");
			//System.out.format(posEx + "\t1\t%.4f\t" + predictedClass + "\n", probClass1);
			if (predictedClass == 1) {
				numCorrect++;
			}
			numTried++;
			double error = 1 - probClass1;
			squaredError += error * error;
		}

		for (String negEx : negExamples) {
			double probClass1 = nbc.getPosteriorProbability(1, negEx);//nbc.getProbabilityClass1(negEx);
			int predictedClass = nbc.classify(negEx);//(probClass1 >= .5) ? 1 : 0;
			String truncatedString = (negEx.length() > 30) ? negEx.substring(0, 27) + "..." : negEx;
			System.out.printf("%-30s%11d%11d%11f%10s\n",
							truncatedString, 0, predictedClass, probClass1, predictedClass == 0 ? "Y" : "N");
			//System.out.format(negEx + "\t0\t%.4f\t" + (1 - predictedClass) + "\n", probClass1);
			if (predictedClass == 0) {
				numCorrect++;
			}
			numTried++;
			double error = probClass1;
			squaredError += error * error;
		}

		System.out.println("\nSummary");
		System.out.format("Of " + numTried + " test cases, " + numCorrect + " correct; accuracy = %.4f\n", (double) numCorrect / numTried);
		System.out.format("Mean squared error: %.4f\n", squaredError / numTried);
	}

	// Extract strings from a text file, one string per line.
	protected static ArrayList<String> getLinesFromFile(String fileName) {

		ArrayList<String> lines = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = in.readLine()) != null) {
				line = line.replaceAll("[^a-zA-Z\\s]+", ""); // remove all non-alphabetic characters
				line = line.replaceAll("^\\s+", "");   // trim opening & trailing whitespace
				line = line.replaceAll("\\s+$", "");
				line = line.replaceAll("\\s\\s", "\\s"); // Remove double spaces
				line = line.toUpperCase();
				if (line.length() > 0) {
					lines.add(line);
				}
			}
			in.close();
		} catch (IOException e) {
		}
		return lines;
	}

	// Extract strings from a text file, tokenized by white space
	// (This isn't used, but the method is here if you want to use it for anything in part 4)
	protected static ArrayList<String> getWordsFromFile(String fileName) {

		ArrayList<String> lines = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = in.readLine()) != null) {
				line = line.replaceAll("[^a-zA-Z\\s]+", ""); // remove all non-alphabetic characters
				line = line.replaceAll("^\\s+", "");   // trim opening & trailing whitespace
				line = line.replaceAll("\\s+$", "");
				line = line.replaceAll("\\s\\s", "\\s"); // Remove double spaces
				if (line.length() > 0) {
					String[] pieces = line.split("\\s+");  // split on spaces
					for (String piece : pieces) {
						lines.add(piece.toUpperCase());
					}
				}

			}
			in.close();

		} catch (IOException e) {
		}
		return lines;
	}
}
