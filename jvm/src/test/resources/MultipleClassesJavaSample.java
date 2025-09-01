import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NewClassNamingConvention")
public class MultipleClassesJavaSample {
    @Test
    void testUsingPrivateClass() {
        final int actual = new NestedClass().number;
        final int expected = 42;
        Assertions.assertEquals(expected, actual);
    }

    private static class NestedClass {
        final int number = 42;
    }
}
