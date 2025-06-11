package sample;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

@SuppressWarnings("NewClassNamingConvention")
public class SinglePublicClassWithPackageSample {
    @Test
    void isPositive() {
        final int number = new Random()
                .nextInt(1, Integer.MAX_VALUE);
        Assertions.assertTrue(number > 0);
    }
}
