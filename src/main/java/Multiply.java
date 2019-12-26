import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.Callable;

public class Multiply implements Callable<BigInteger> {
    private BigInteger mul = BigInteger.ONE;
    private final List<String[]> data;

    public Multiply(List<String[]> data) {
        this.data = data;
    }

    @Override
    public BigInteger call() {
        for (String[] numbers : data) {
            for (String number : numbers) {
                mul = mul.multiply(BigInteger.valueOf(Long.parseLong(number)));
            }
        }
        return mul;
    }
}