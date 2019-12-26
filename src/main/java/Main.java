import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("in1.txt"));
        BufferedWriter writer = new BufferedWriter(new FileWriter("out.txt"));
        BigInteger mul = BigInteger.ONE;
        final int countOfThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(countOfThreads);

        long time = System.currentTimeMillis();

        List<String[]> data = reader.lines().map(line -> line.split("\t")).collect(Collectors.toList());
        List<Callable<BigInteger>> callables = new ArrayList<>();
        
        int countOfRows = data.size();
        int low;
        int high;
        
        for (int i = 0; i < countOfThreads; i++) {
            low = (countOfRows / countOfThreads + 1) * i;
            if (i == countOfThreads - 1) {
                high = countOfRows;
            } else {
                high = low + countOfRows / countOfThreads + 1;
            }
            callables.add(new Multiply(data.subList(low, high)));
        }

        try {
            List<Future<BigInteger>> futures = service.invokeAll(callables);
            for (Future<BigInteger> future : futures) {
                mul = mul.multiply(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            service.shutdown();
        }

        writer.write(mul.toString() + "\n" + (double) (System.currentTimeMillis() - time) / 1000);

        try {
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}