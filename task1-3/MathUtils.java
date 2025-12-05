import java.util.Random;

/**
 * AUTHOR: Subair
 * STYLE: Procedural Programming
 * This class uses procedural programming because it provides standalone utility methods (gaussian), focusing on performing
 * mathematical operations without encapsulating data or behaviors in an object. The method is stateless and does not
 * interact with or manage instance variables, following a functional, procedure-oriented approach.
 *
 * This class contains useful, self-defined mathematical methods that
 * are useful for complex calculations in other classes.
 */
public interface MathUtils {

    /**
     * Returns a pseudorandom number following the normal distribution with the parameters mean and standard deviation.
     *
     * @param mean the mean value of the normal distribution
     * @param stdDev the standard deviation of the normal distribution
     * @return a normal distributed pseudorandom number
     *
     * Precondition: mean and stdDev should be valid double values.
     * Postcondition: The returned number is within the expected range of a normal distribution defined by the parameters.
     *
     * BAD: The gaussian method is static, making it inflexible if we later want to adjust
     * the distribution behavior without modifying MathUtils. By using dynamic binding with an
     * instance method, this could allow for different implementations of Gaussian distribution in subclasses.
     */
    static int gaussian(double mean, double stdDev) {
        if(Double.isNaN(mean) || Double.isNaN(stdDev)) { throw new IllegalArgumentException("The mean or standard deviation is not a number."); }
        return (int) (mean + (new Random()).nextGaussian() * stdDev);
    }
}

