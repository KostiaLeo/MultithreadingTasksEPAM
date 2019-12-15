package task4;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

public class ForkJoinAdd extends RecursiveTask<Long> {

    private final long[]numbers;
    private final int start;
    private final int end;
    private static final long ARRAYS_ITEM_LIMIT = 20;
    private static final int MAX_THREADS_AMOUNT = 8;

    private ForkJoinAdd(long[]numbers) {
        this(numbers, 0, numbers.length);
    }

    private ForkJoinAdd(long[]numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {

        int length = end - start;
        if (length <= ARRAYS_ITEM_LIMIT) {
            return add();
        }

        ForkJoinAdd firstTask = new ForkJoinAdd(numbers, start, start + length/2);
        firstTask.fork();

        ForkJoinAdd secondTask = new ForkJoinAdd(numbers, start + length/2, end);

        Long secondTaskResult = secondTask.compute();
        Long firstTaskResult = firstTask.join();

        return firstTaskResult + secondTaskResult;

    }

    private long add() {
        long result = 0;
        for (int i = start; i < end; i++) {
            result += numbers[i];
        }
        return result;
    }

    public static long startForkJoinSum(long n) {
        long[]numbers = LongStream.rangeClosed(1, n).toArray();
        ForkJoinTask<Long> task = new ForkJoinAdd(numbers);
        return new ForkJoinPool(MAX_THREADS_AMOUNT).invoke(task);
    }
}