
public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.task2();
    }

    private void task1() {
        new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    Thread.sleep(1000);
                    System.out.println(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void task2() {
        Incrementor incrementor = new Incrementor();
        int num = 1_000_000;

        new Thread(() -> {
            for (int j = 0; j < num; j++) {
                incrementor.incA();
            }
        }).start();

        new Thread(() -> {
            for (int j = 0; j < num; j++) {
                incrementor.printA();
            }
        }).start();
    }
}

class Incrementor {
    private int a = 0;
    private volatile boolean isOpenForInc = true;

    synchronized void printA() {
        while (isOpenForInc) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        isOpenForInc = !isOpenForInc;
        System.out.println(a);
        notify();
    }

    synchronized void incA() {
        while (!isOpenForInc) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        a++;
        isOpenForInc = !isOpenForInc;
        notify();
    }
}