package io.github.rhemon.aggregatesieveanalysis;

import java.util.List;

/**
 * Provides static functions for performing the calculations for sieve analysis of aggregate  
 * @author Ridhwanul Haque
 * @version 0.1
 */
public class SieveAnalysisCalc {
	
	/**
	 * This static function calculates the percent of aggregate retained on a sieve, and to do
	 * that, it has to be provided with the weight retained at the sieve and the 
	 * total weight of the sample of aggregate.
	 * 
	 * @param weightRetained A double value that should represent the mass of the amount
	 * 						 of aggregate that didn't pass the sieve
	 * @param totalWeight    A double value that should represent the mass of the sample
	 * 						 of aggregate taken for measurement 
	 * @return 			 	 The function returns a double value calculating the 
	 * 						 percentage of aggregate retained
	 * 
	 */
	public static double CalcPerecentRetained(double weightRetained, double totalWeight){
		
		return ((weightRetained/totalWeight)*100);
	}
	
	/**
	 * This static function calculates the cumulative percent of aggregate retained and 
	 * to do that, it has to be provided with the values that represent percentage 
	 * of aggregate retained 
	 * 
	 * @param percentRetainedValues Double values that represents percentage of aggregate retained
	 * @return 			 	 		The function returns a double value that is the total of all 
	 * 								the values provide as input
	 * 
	 */
	public static double CaclCumulativePercentRetained(List<Double> percentRetainedValues) {
		double cumulativePercentRetained = 0;
		for (double percentRetained : percentRetainedValues) {
			cumulativePercentRetained += percentRetained;
		}
		return cumulativePercentRetained;
	}
	
	/**
	 * This static function calculates percent finer (the percentage of aggregate that passes through
	 * To do that it has to be provided with the cumulative percentage retained.
	 * 
	 * @param cumulativePercentRetained A double value that should represent the cumulative percent retained
	 * @return 			 	 			The function returns a double value, that is the percent finer 
	 * 									(100 - cumulative percent retained) 
	 * 
	 */
	public static double CalcPercentFiner(double cumulativePercentRetained) {
		return (100.0 - cumulativePercentRetained);
	}
	
	/**
	 * This static function calculates the fineness modulus of the aggregate sample, and to do that it needs to be 
	 * provided with the cumulativePercentRetainedValues.
	 * 
	 * @param cumulativePercentRetainedValues Double values that represent cumulative percentage of aggregate retained till 
	 * 										  sieve no.100
	 * @return								  Returns a double value, which is the fineness modulus of the aggregate 
	 */
	public static double CalcFinenessModulus(List<Double> cumulativePercentRetainedValues){
		double total = 0;
		for (double cumulativePercentRetained: cumulativePercentRetainedValues){
			total+= cumulativePercentRetained;
		}
		return total/100;
	}
	
	/**
	 * This static function calculates the uniformity coefficient of the aggregate. It needs D60 and D10 to do so.
	 * 
	 * @param d60 The diameter value when percent finer is 60
	 * @param d10 The diameter value when percent finer is 10
	 * @return    A double value, that is the uniformity coefficient of the aggregate (D60/D10)
	 */
	public static double CalcUniformityCoefficient(double d60, double d10){
		return d60/d10;
	}
	
}
