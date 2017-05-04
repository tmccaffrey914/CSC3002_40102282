import java.util.Random;

public class GenerateCallOptionValue {
	private Random rnd = new Random();
	
	//This method is discussed in the dissertation, section 8.5 Calculating European Call Option Values
	public double simulatedEuropeanCallOptionPrice (double stockValue, double strikePrice, double interestRate,
			double sigma, double timeUntilExpiration, int no_simulations) {
		double r = (interestRate - 0.5 * Math.pow(sigma,  2)) * timeUntilExpiration;
		double standardDeviation = sigma * Math.sqrt(timeUntilExpiration);
		double sum_payoffs = 0.0;
		
		for ( int i = 1; i <= no_simulations; i++ ) {
			double s_T = stockValue * Math.exp(r + standardDeviation * rnd.nextGaussian());
			sum_payoffs += Math.max(0.0, s_T - strikePrice);
		}
		return Math.exp(interestRate * timeUntilExpiration) * (sum_payoffs / (double) no_simulations);
	}
}
