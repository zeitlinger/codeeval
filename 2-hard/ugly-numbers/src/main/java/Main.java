import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;


public class Main {

    public interface Operation {
        long apply(long total, long running);
    }

    public static final Operation PLUS = new Operation() {
        @Override
        public long apply(long total, long running) {
            return total + running;
        }
    };
    public static final Operation MINUS = new Operation() {
        @Override
        public long apply(long total, long running) {
            return total - running;
        }
    };

    private Main() {
    }

    public static void main(String[] args) throws IOException {
        for (String line : Files.readLines(new File(args[0]), Charsets.UTF_8)) {
            System.out.println(getUglyCount(line));
        }
    }

    public static int getUglyCount(String number) {
        List<Integer> list = new ArrayList<>();
        for (char c : number.toCharArray()) {
            list.add(Integer.parseInt(new String(new char[]{c})));
        }
        return getUglyCount(0, 0, PLUS, list);
    }

    private static int getUglyCount(long total, long running, Operation operation, List<Integer> digits) {
        long nextRunning = 10 * running + digits.get(0);
        long nextTotal = operation.apply(total, nextRunning);

        List<Integer> nextDigits = digits.subList(1, digits.size());

        if (nextDigits.isEmpty()) {
            return isUgly(nextTotal) ? 1 : 0;
        }

        return getUglyCount(nextTotal, 0, PLUS, nextDigits) +
            getUglyCount(nextTotal, 0, MINUS, nextDigits) +
            getUglyCount(total, nextRunning, operation, nextDigits);
    }

    public static boolean isUgly(long number) {
        return number % 2 == 0
            || number % 3 == 0
            || number % 5 == 0
            || number % 7 == 0;
    }
}
