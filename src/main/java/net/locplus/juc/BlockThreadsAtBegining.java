package net.locplus.juc;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 在有线程做rtn > m中间的动作时，其他进入next()的代码就等在最开始
 * Created by Administrator on 2014/5/6.
 */
public class BlockThreadsAtBegining {

    private AtomicBoolean readFlag = new AtomicBoolean(true);
    private ReentrantLock lock = new ReentrantLock();
    private Condition waitReadyFlag = lock.newCondition();

    public long next() {
        while (!readFlag.get()) {
            try {
                lock.lock();
                if (!readFlag.get()) {
                    waitReadyFlag.await();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        long m = 500;
        long rtn = (long) (Math.random() * 2000);
        if (rtn > m) {
            try {
                lock.lock();
                if (readFlag.get()) {
                    return next();
                }
                readFlag.set(false);
                System.out.println("set read flag to false.");
            } finally {
                if (!readFlag.get()) {
                    readFlag.set(true);
                    waitReadyFlag.signalAll();
                }
                lock.unlock();
            }
        }
        return rtn;
    }

    public static void main(String[] args) {
        final BlockThreadsAtBegining btab = new BlockThreadsAtBegining();
        for (;;)
            new Thread(new Runnable() {
                @Override
                public void run() {
                        System.out.println(btab.next());
                }
            }).start();
    }
}
