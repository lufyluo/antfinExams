package com.antfin.exam.lazysingleton.util;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
public class LazyManTest {
    private AtomicInteger numTasks;
    public  int maxThreads;
    public  int maxNumTask;
    @Before
    public void initData(){
        numTasks = new AtomicInteger();
        maxNumTask = 40;
        maxThreads = 4;
    }
    @Test
    public void multiThreadGetInstanceTest() throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newFixedThreadPool(maxThreads);
        while (numTasks.get()<maxNumTask){
            Future future =pool.submit(new Thread() {
                public void run() {
                    numTasks.addAndGet(1);
                    LazyMan lazyMan = LazyMan.getInstance();
                    assertEquals(LazyMan.getInitCount(),1);
                }
            });
            Object obj = future.get();
        }
        assertEquals(LazyMan.getInitCount(),1);
    }
    @Test
    public void getInstanceTest(){
        LazyMan.getInstance();
        assertEquals(LazyMan.getInitCount(),1);
        LazyMan.getInstance();
        assertEquals(LazyMan.getInitCount(),1);
    }
}