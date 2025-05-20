import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NewClassNamingConvention")
public class HelloJavaSample {
    @Test
    void greet() {
        final String name = "Sample";
        final String greeting = HelloKt.greet(name);
        final String expected = "Hello %s!".formatted(name);
        Assertions.assertEquals(expected, greeting);
    }
}
