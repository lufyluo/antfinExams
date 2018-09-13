package com.antfin.exam.filereader.services;

import com.antfin.exam.filereader.Models.TestData;
import com.antfin.exam.filereader.utils.TxtDataReader;
import com.antfin.exam.filereader.utils.TxtDataWriter;

import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class CalculateDataService {
    private List<TestData> datas = new ArrayList<TestData>();
    private List<TestData> results = new ArrayList<TestData>();
    private ExecutorService pool;
    private final int maxThreadNum = 10;
    private LinkedBlockingQueue<String> concurrentLinkedQueue = new LinkedBlockingQueue<String>();
    private AtomicBoolean reaaderState = new AtomicBoolean(true);
    private AtomicBoolean writorState = new AtomicBoolean(true);

    public CalculateDataService() {
        concurrentLinkedQueue.offer("classpath:test\\1.txt");
        concurrentLinkedQueue.offer("classpath:test\\2.txt");
        concurrentLinkedQueue.offer("classpath:test\\3.txt");
        pool = Executors.newFixedThreadPool(maxThreadNum);
    }

    public void method1() throws ExecutionException, InterruptedException {
        readData();
        calculateData();
    }

    public boolean state() {
        return reaaderState.get() && writorState.get();
    }

    public void close() {
        pool.shutdownNow();
        reaaderState.set(false);
        writorState.set(false);
    }

    private void calculateData() {
        Thread t = new Thread() {
            @Override
            public void run() {
                while (reaaderState.get()) {
                    writeResult();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                pool.shutdownNow();
                writeResult();
                writorState.set(false);
            }
        };
        t.run();
    }

    private void writeResult() {
        List<TestData> temps = new ArrayList<TestData>();
        temps.addAll(datas);
        results = sort1(temps);
        writeArray(results);
    }

    private void writeArray(List<TestData> target) {
        if (target.size() == 0)
            return;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < target.size(); i++) {
            stringBuilder.append(target.get(i).toString());
        }
        try {
            new TxtDataWriter().append("c:\\test\\", "result.txt", stringBuilder.toString() + " \r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readData() throws ExecutionException, InterruptedException {

        for (int i = 0, l = concurrentLinkedQueue.size(); i < l; i++) {
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        datas.addAll(new TxtDataReader().readFileByLines(concurrentLinkedQueue.poll()));
                        if (concurrentLinkedQueue.size() == 0) {
                            reaaderState.set(false);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void convert(@NonNull String[] files) {
        for (int i = 0; i < files.length; i++) {
            concurrentLinkedQueue.offer(files[i]);
        }
    }


    private List<TestData> sort1(@NonNull List<TestData> list) {
        List<TestData> target = new ArrayList<TestData>();
        Iterator<TestData> iterator = list.iterator();
        while (iterator.hasNext()) {
            TestData temp = iterator.next();
            TestData testData = getSameGroupId(target, temp.getGroupId());
            if (testData == null) {
                target.add(temp);
                continue;
            }
            if (testData.getQuota() > temp.getQuota()) {
                target.remove(testData);
                target.add(temp);
            }
        }
        Collections.sort(target);
        return target;
    }

    private TestData getSameGroupId(List<TestData> target, String groupId) {
        Iterator<TestData> iterator = target.iterator();
        while (iterator.hasNext()) {
            TestData testData = iterator.next();
            if (testData.getGroupId().equals(groupId)) {
                return testData;
            }
        }
        return null;
    }
}
