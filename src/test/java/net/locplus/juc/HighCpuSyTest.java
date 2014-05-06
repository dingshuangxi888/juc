package net.locplus.juc;

/**
 * Created by Administrator on 2014/5/6.
 */
public class HighCpuSyTest {

    public void go() {
        for (int i = 0; i < 10; i++) {
            new Thread(new Yield()).start();
        }
    }

    class Yield implements Runnable {

        @Override
        public void run() {
            while (true) {
                Thread.yield();
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new HighCpuSyTest().go();
    }
}
