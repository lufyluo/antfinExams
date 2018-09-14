package com.antfin.exam.spinlock.utils;

import com.antfin.exam.spinlock.utils.extension.JUnitExtensions;

import org.junit.Before;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class ConcurrencyTest extends JUnitExtensions.MultiThreadedTestCase {
    private SpinLock spinLock = new SpinLock();
    private AtomicInteger currentNum = new AtomicInteger(0);
    private static int preNum = 0;

    /**
     * Simple constructor.
     */
    public ConcurrencyTest(String s) {
        super(s);
    }

    public class CounterThread extends JUnitExtensions.MultiThreadedTestCase.TestCaseRunnable {
        private int order = 0;

        public CounterThread(){}
        public CounterThread(int index){
            order = index;
        }
        public void runTestCase() {
            try {
                spinLock.lock();
                assertEquals(currentNum.get(),order);//测试先到先得 公平性
                assertEquals(order, preNum);//测试非线程安全语句得到同步锁
                System.out.println(currentNum.addAndGet(1) + "---  " + preNum);
            }catch (Exception e){
                throw e;
            }finally {
                preNum ++;
                spinLock.unlock();

                //assertEquals(currentNum.get(), 4);
            }
        }
    }

    public void test1() {
        int threadNum = 500;
        TestCaseRunnable tct[] = new TestCaseRunnable[threadNum];
        for (int i = 0; i < threadNum; i++) {
            tct[i] = new CounterThread(i);
        }
        runTestCaseRunnables(tct);
        assertEquals(currentNum.get(), threadNum);
    }

}
