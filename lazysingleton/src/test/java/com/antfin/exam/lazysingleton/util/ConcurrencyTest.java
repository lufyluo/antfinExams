package com.antfin.exam.lazysingleton.util;


import com.antfin.exam.lazysingleton.util.extension.JUnitExtensions;

import static org.junit.Assert.assertEquals;

public class ConcurrencyTest extends JUnitExtensions.MultiThreadedTestCase {
    /**
     * Simple constructor.
     */
    public ConcurrencyTest(String s) {
        super(s);
    }
    public class CounterThread extends JUnitExtensions.MultiThreadedTestCase.TestCaseRunnable
    {
        public void runTestCase () throws Throwable
        {
            for (int i = 0; i < 1000; i++)
            {
                System.out.println ("Counter Thread: " + Thread.currentThread () + " : " + i);
                // Do some testing...
                LazyMan.getInstance();
                assertEquals(LazyMan.getInitCount(),1);
                System.out.println ("Counter InitCount: " + LazyMan.getInitCount() + " : " + i);
                if (Thread.currentThread ().isInterrupted ()) {
                    return;
                }
            }
        }
    }

    public void test1 ()
    {
        TestCaseRunnable tct [] = new TestCaseRunnable [5];
        for (int i = 0; i < 5; i++)
        {
            tct[i] = new CounterThread ();
        }
        runTestCaseRunnables (tct);
    }
}
