package rainbownlp.analyzer.evaluation.classification;

import java.util.ArrayList;
import java.util.List;
import rainbownlp.machinelearning.MLExample;
import rainbownlp.util.SystemUtil;


public class Evaluator {
	public static boolean saveResult = false;
	public static String evaluation_mode = "HybridTest";

	public static ResultRow evaluateByClass(List<MLExample> pExamplesToTest, 
			Integer exampleClassToEvaluate)
	{
		ResultRow rr = new ResultRow();
		
		for(MLExample example : pExamplesToTest)
		{
			Double expected_class = example.getExpectedClass();
			Double predicted_class = example.getPredictedClass();
			if(exampleClassToEvaluate == expected_class.intValue())
			{
				if(expected_class.equals(predicted_class))
					rr.TP++;
				else
					rr.FN++;
			}
			else
			{
				if(exampleClassToEvaluate.equals(predicted_class.intValue()))
					rr.FP++;
				else
					rr.TN++;
			}
		}
		
		return rr;
	}

	public static void evaluateDevelopementResult(String resultsRoot)
	{
		SystemUtil.runShellCommand("a2-evaluate.pl -g gold-devel "+resultsRoot+"/*.a2");
	}
	public static EvaluationResult getEvaluationResult(List<MLExample> pExamplesToTest, 
			String[] class_titles) {
		EvaluationResult result = new EvaluationResult();
		
		for(int i=1;i<=class_titles.length;i++)
		{
			result.evaluationResultByClass.put(class_titles[i-1], 
					evaluateByClass(pExamplesToTest, i));
		}
		return result;
	}
	
	public static EvaluationResult getEvaluationResult(List<MLExample> pExamplesToTest) 
	{
		ArrayList<Integer> exampleClasses = new ArrayList<Integer>();
	
		for(MLExample example : pExamplesToTest)
		{
			if(!exampleClasses.contains(example.getExpectedClass().intValue()))
				exampleClasses.add(example.getExpectedClass().intValue());
		}
		
		EvaluationResult er = new EvaluationResult();
		for(Integer exampleClass : exampleClasses)
		{
			er.evaluationResultByClass.put(exampleClass.toString(), 
					evaluateByClass(pExamplesToTest, exampleClass));
		}
		return er;
	}
}
