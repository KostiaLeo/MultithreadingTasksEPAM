package task4;

public class Task4 {
    public static void main(String[] args) {
        int requiredNumber = 1_000_000;
        System.out.println(ForkJoinAdd.startForkJoinSum(requiredNumber));
    }
}