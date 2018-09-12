package com.antfin.exam.spinlock.utils;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class SpinLockTest {
    public int maxThreads;

    private static SpinLock spinLock;
    private AtomicInteger currentNum;

    @Before
    public void initData() {
        maxThreads = 4;
        spinLock = new SpinLock();
        currentNum = new AtomicInteger();
    }

    @Test
    public void Lock() throws ExecutionException, InterruptedException {
        ExecutorService ticketPool = Executors.newFixedThreadPool(maxThreads);
        for (int i = 0; i < maxThreads; i++) {
            Future future = ticketPool.submit(new Runnable() {
                @Override
                public void run() {
                    spinLock.lock();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    currentNum.addAndGet(1);
                    System.out.println(currentNum.get());
                    spinLock.unlock();
                }
            });
            assertEquals(currentNum.get(), i);
            future.get();
        }
        assertEquals(currentNum.get(), 4);
    }

}