package NeuralNetwork;

import java.util.Random;

public class MathUtil {

    public static double randomMinusOneToOne() {
        Random random = new Random();
        double d = random.nextDouble();
        if (random.nextBoolean()) {
            d *= -1;
        }
        return cut(d);
    }

    public static double randomZeroToOne() {
        Random random = new Random();
        double d = random.nextDouble();
        return cut(d);
    }

    private static double cut(double d) {
        return Math.rint(d * 1000) / 1000;
    }
}
