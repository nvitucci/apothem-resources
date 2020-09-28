import org.apache.commons.numbers.angle.PlaneAngle;
import org.apache.commons.numbers.arrays.CosAngle;
import org.apache.commons.numbers.arrays.LinearCombination;
import org.apache.commons.numbers.arrays.MultidimensionalCounter;
import org.apache.commons.numbers.arrays.SafeNorm;
import org.apache.commons.numbers.combinatorics.*;
import org.apache.commons.numbers.complex.Complex;
import org.apache.commons.numbers.core.ArithmeticUtils;
import org.apache.commons.numbers.core.Precision;
import org.apache.commons.numbers.fraction.Fraction;
import org.apache.commons.numbers.gamma.*;
import org.apache.commons.numbers.primes.Primes;
import org.apache.commons.numbers.quaternion.Quaternion;
import org.apache.commons.numbers.quaternion.Slerp;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class NumbersExamples {
    public static void main(String[] args) {
        CoreArithmeticUtilsExamples();
        CorePrecisionExamples();

        ArraysExamples();
        ComplexExamples();
        AngleExamples();
        FractionExamples();
        PrimesExamples();
        QuaternionExamples();
        CombinatoricsExamples();
        GammaExamples();
    }

    private static void CoreArithmeticUtilsExamples() {
        System.out.println("\nExamples for the Core package (ArithmeticUtils module)\n");

        int x = 256;
        int y = 6;

        System.out.printf("%d / %d = %d%n",
                x, y, ArithmeticUtils.divideUnsigned(x, y));
        System.out.printf("%d %% %d = %d%n",
                x, y, ArithmeticUtils.remainderUnsigned(x, y));

        System.out.printf("gcd(%d, %d) = %d%n",
                x, y, ArithmeticUtils.gcd(x, y));
        System.out.printf("lcm(%d, %d) = %d%n",
                x, y, ArithmeticUtils.lcm(x, y));

        System.out.printf("%d ^ %d = %d%n",
                x, 2, ArithmeticUtils.pow(x, 2));
        System.out.printf("%d ^ %d = %d%n",
                x, y, ArithmeticUtils.pow(BigInteger.valueOf(x), y));

        System.out.printf("Is %d a power of 2? %b%n",
                x, ArithmeticUtils.isPowerOfTwo(x));
        System.out.printf("Is %d + 1 a power of 2? %b%n",
                x, ArithmeticUtils.isPowerOfTwo(x + 1));
    }

    private static void CorePrecisionExamples() {
        System.out.println("\nExamples for the Core package (Precision module)\n");

        double x = 1.23456789;

        for (int places : Arrays.asList(2, 4, 6)) {
            System.out.printf("Rounding %f to %d places = %f%n",
                    x, places, Precision.round(x, places));
        }

        for (double eps : Arrays.asList(1e-3, 1e-6, 1e-9)) {
            System.out.printf("%.9f == %.9f (up to %e)? %b%n",
                    x, x + 1e-6, eps, Precision.equals(x, x + 1e-6, eps));
        }

        double nan = Double.NaN;
        double inf = Double.POSITIVE_INFINITY;

        System.out.printf("%f == %f? %b%n",
                nan, nan, Precision.equals(nan, nan));
        System.out.printf("%f - %f == %f - %f? %b%n",
                inf, inf, inf, inf, Precision.equals(inf - inf, inf - inf));
        System.out.printf("%f == %f (including NaN)? %b%n",
                nan, nan, Precision.equalsIncludingNaN(nan, nan));
        System.out.printf("%f - %f == %f - %f (including NaN)? %b%n",
                inf, inf, inf, inf, Precision.equalsIncludingNaN(inf - inf, inf - inf));
    }

    private static void ArraysExamples() {
        System.out.println("\nExamples for the Arrays package\n");

        double[] array1 = {0.1, 0.4, 0.1};
        double[] array2 = {-0.1, -0.4, -0.1};

        System.out.printf("Norm of %s = %f%n",
                Arrays.toString(array1), SafeNorm.value(array1));

        System.out.printf("Angle between %s and %s = %f%n",
                Arrays.toString(array1), Arrays.toString(array2), CosAngle.value(array1, array2));
        System.out.printf("dot(%s, %s) = %f%n",
                Arrays.toString(array1), Arrays.toString(array2), LinearCombination.value(array1, array2));

        MultidimensionalCounter counter = MultidimensionalCounter.of(4, 4, 3);
        int pos = 23;
        int[] index = {1, 3, 2};

        System.out.printf("Array of dimensions %s: position %d corresponds to index %s%n",
                Arrays.toString(counter.getSizes()), pos, Arrays.toString(counter.toMulti(pos)));
        System.out.printf("Array of dimensions %s: index %s corresponds to position %d%n",
                Arrays.toString(counter.getSizes()), Arrays.toString(index), counter.toUni(index));
    }

    private static void ComplexExamples() {
        System.out.println("\nExamples for the Complex package\n");

        Complex z = Complex.ofCartesian(1, 1);
        Complex z2 = Complex.ofPolar(Math.sqrt(2), Math.PI / 4);
        Complex z3 = Complex.ofCis(Math.PI);

        System.out.printf("z: %s, z2: %s, z3: %s%n",
                z, z2, z3);

        System.out.printf("%s == %s? %b%n",
                z, z2, z.equals(z2));

        System.out.printf("Given z = %s:%n", z);
        System.out.printf("\tRe(z) = %s, Im(z) = %s%n",
                z.real(), z.imag());
        System.out.printf("\tabs(z) = %s, arg(z) = %s%n",
                z.abs(), z.arg());
        System.out.printf("\tconj(z) = %s%n", z.conj());
        System.out.printf("\tnorm(z) = %f%n", z.norm()); // Note: this is the squared norm
        System.out.printf("\tasin(z) = %s%n", z.asin());
        System.out.printf("\tacos(z) = %s%n", z.acos());
        System.out.printf("\tatan(z) = %s%n", z.atan());
        System.out.printf("\tasinh(z) = %s%n", z.asinh());
        System.out.printf("\tacosh(z) = %s%n", z.acosh());
        System.out.printf("\tatanh(z) = %s%n", z.atanh());

        System.out.printf("Given z = %s anz z2 = %s:%n", z, z2);
        System.out.printf("\tz + z2 = %s%n", z.add(z2));
        System.out.printf("\tz - z2 = %s%n", z.subtract(z2));
        System.out.printf("\tz * z2 = %s%n", z.multiply(z2));
        System.out.printf("\tz / z2 = %s%n", z.divide(z2));
        System.out.printf("\tz ^ z2 = %s%n", z.pow(z2));

        double eps = 1e-15;
        System.out.printf("Re(%s) == -1 up to %s? %b%n",
                z3, eps, Precision.equals(z3.real(), -1, eps));
        System.out.printf("Im(%s) == 0 up to %s? %b%n",
                z3, eps, Precision.equals(z3.imag(), 0, eps));
    }

    private static void AngleExamples() {
        System.out.println("\nExamples for the Angle package\n");

        PlaneAngle alpha = PlaneAngle.ofDegrees(180);
        PlaneAngle beta = PlaneAngle.ofRadians(Math.PI);

        System.out.printf("Angle alpha in degrees: %f%n",
                alpha.toDegrees());
        System.out.printf("Angle alpha in radians: %f%n",
                alpha.toRadians());
        System.out.printf("Angle alpha in turns: %f%n",
                alpha.toTurns());

        System.out.printf("Angle beta in degrees: %f%n",
                alpha.toDegrees());
        System.out.printf("Angle beta in radians: %f%n",
                alpha.toRadians());
        System.out.printf("Angle beta in turns: %f%n",
                alpha.toTurns());

        System.out.printf("Are angles alpha and beta equal? %b%n",
                alpha.equals(beta));
    }

    private static void FractionExamples() {
        System.out.println("\nExamples for the Fraction package\n");

        List<Fraction> fractions = Arrays.asList(
                Fraction.of(4, 9),
                Fraction.of(1, 2),
                Fraction.from(0.99245),
                Fraction.from(0.99245, 10000000));

        System.out.printf("Fractions: %s%n",
                fractions.stream().map(Fraction::toString).collect(Collectors.joining(" ; ")));

        System.out.printf("The fraction %s has numerator %d and denominator %d%n",
                fractions.get(0), fractions.get(0).getNumerator(), fractions.get(0).getDenominator());
        System.out.printf("%s as a decimal number: %f%n",
                fractions.get(0), fractions.get(0).doubleValue());
        System.out.printf("Reciprocal of %s: %s%n",
                fractions.get(0), fractions.get(0).reciprocal());
        System.out.printf("Additive inverse of %s: %s%n",
                fractions.get(0), fractions.get(0).negate());
        System.out.printf("(%s) ^ 3 = %s%n",
                fractions.get(0), fractions.get(0).pow(3));

        System.out.printf("(%s) + (%s) = %s%n",
                fractions.get(0), fractions.get(1), fractions.get(0).add(fractions.get(1)));
        System.out.printf("(%s) - (%s) = %s%n",
                fractions.get(0), fractions.get(1), fractions.get(0).subtract(fractions.get(1)));
        System.out.printf("(%s) * (%s) = %s%n",
                fractions.get(0), fractions.get(1), fractions.get(0).multiply(fractions.get(1)));
        System.out.printf("(%s) / (%s) = %s%n",
                fractions.get(0), fractions.get(1), fractions.get(0).divide(fractions.get(1)));
    }

    private static void PrimesExamples() {
        System.out.println("\nExamples for the Primes package\n");

        for (int n : Arrays.asList(2, 4, 42)) {
            System.out.printf("Is %d prime? %b%n",
                    n, Primes.isPrime(n));
            System.out.printf("What is the closest prime to %d? %d%n",
                    n, Primes.nextPrime(n)); // Note: why is it 2 when n = 2?
            System.out.printf("Prime factors of %d: %s%n",
                    n, Primes.primeFactors(n));
        }
    }

    private static void QuaternionExamples() {
        System.out.println("\nExamples for the Quaternion package\n");

        List<Quaternion> quaternions = Arrays.asList(
                Quaternion.of(new double[]{1, 1, 1}),
                Quaternion.of(2, new double[]{1, 1, 1}),
                Quaternion.of(0.5, 1, 1, 1));

        for (Quaternion q : quaternions) {
            System.out.printf("Quaternion %s%n", q);
            System.out.printf("\tIs it pure (up to 1e-10)? %b%n",
                    q.isPure(1e-10));
            System.out.printf("\tConjugate: %s%n",
                    q.conjugate());
            System.out.printf("\tInverse: %s%n",
                    q.inverse());
            System.out.printf("\tNormalized form: %s%n",
                    q.normalize());
            System.out.printf("\tPositive polar form: %s%n",
                    q.positivePolarForm());
            System.out.printf("\tQuaternion divided by its norm %f: %s%n",
                    q.norm(), q.divide(q.norm()));
        }

        System.out.printf("%s + %s = %s%n",
                quaternions.get(0), quaternions.get(1), quaternions.get(0).add(quaternions.get(1)));
        System.out.printf("%s - %s = %s%n",
                quaternions.get(0), quaternions.get(1), quaternions.get(0).subtract(quaternions.get(1)));
        System.out.printf("%s * %s = %s%n",
                quaternions.get(0), quaternions.get(1), quaternions.get(0).multiply(quaternions.get(1)));

        System.out.printf("dot(%s, %s) = %s%n",
                quaternions.get(0), quaternions.get(1), quaternions.get(0).dot(quaternions.get(1)));

        Slerp slerp = new Slerp(Quaternion.I, Quaternion.J);
        System.out.printf("Slerp demo%n");
        for (double t = 0; t <= 1.0; t += 0.1) {
            System.out.printf("\tQuaternion at t = %f: %s%n",
                    t, slerp.apply(t));
        }
    }

    private static void CombinatoricsExamples() {
        System.out.println("\nExamples for the Combinatorics package\n");

        int n = 10;

        System.out.printf("%d! = %d%n",
                n, Factorial.value(n)); // n must be <= 20
        System.out.printf("%d! = %f (double)%n",
                n, FactorialDouble.create().value(n));
        System.out.printf("log(%d!) = %f%n",
                n, LogFactorial.create().value(n));

        int k = 3;

        System.out.printf("Binomial coefficient (%d %d) = %d%n",
                n, k, BinomialCoefficient.value(n, k)); // n should be <= 66
        System.out.printf("Binomial coefficient (%d %d) = %f (double)%n",
                n, k, BinomialCoefficientDouble.value(n, k));
        System.out.printf("Log of the binomial coefficient (%d %d) = %f%n",
                n, k, LogBinomialCoefficient.value(n, k));

        Combinations comb = Combinations.of(n, k);
        List<int[]> combList = new ArrayList<>();
        for (int[] element : comb) {
            combList.add(element);
        }

        System.out.printf("Number of combinations C(%d, %d): %d%n",
                n, k, combList.size());
        System.out.printf("First three combinations: %s, %s, %s%n",
                Arrays.toString(combList.get(0)), Arrays.toString(combList.get(1)),
                Arrays.toString(combList.get(2)));
    }

    private static void GammaExamples() {
        System.out.println("\nExamples for the Gamma package\n");

        for (double value : Arrays.asList(1., 2., 3.)) {
            System.out.printf("erf(%f) = %f%n",
                    value, Erf.value(value));
        }

        for (double value : Arrays.asList(0.9999, 0.9953, 0.8427)) {
            System.out.printf("erf^(-1)(%f) = %f%n",
                    value, InverseErf.value(value));
        }

        System.out.printf("gamma(1) = %f%n",
                Gamma.value(1));
        System.out.printf("gamma(0.5) = %f (sqrt(pi))%n",
                Gamma.value(0.5)); // sqrt(pi)

        System.out.printf("digamma(1) = %f (-g)%n",
                Digamma.value(1)); // -g (where g is the Euler–Mascheroni constant)
        System.out.printf("digamma(0.5) = %f (-2 * ln(2) - g)%n",
                Digamma.value(0.5)); // -2 * ln(2) - g

        System.out.printf("trigamma(1) = %f (pi ** 2 / 6)%n",
                Trigamma.value(1)); // pi ** 2 / 6
        System.out.printf("trigamma(0.5) = %f (pi ** 2 / 2)%n",
                Trigamma.value(0.5)); // pi ** 2 / 2

        System.out.printf("log-gamma(1) = %f%n",
                LogGamma.value(1));
        System.out.printf("log-gamma(0.5) = %f (log(sqrt(pi)))%n",
                LogGamma.value(0.5)); // log(sqrt(pi))
    }
}
