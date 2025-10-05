import java.util.Random;

public class CommonUtils {
    public static int generateId() {
        int minValue = 1, maxValue = Integer.MAX_VALUE;
        Random random = new Random();

        return random.nextInt(maxValue - minValue) + minValue;
    }
}
