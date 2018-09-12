package com.antfin.exam.filereader.services;

import com.antfin.exam.filereader.Models.TestData;
import com.antfin.exam.filereader.utils.TxtDataFactory;
import com.antfin.exam.filereader.utils.TxtDataReader;
import com.antfin.exam.filereader.utils.TxtDataWriter;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

public class CalculateDataService {
    private TxtDataFactory txtDataFactory;
    private String[] files = new String[3];
    private List<TestData> datas = new ArrayList<TestData>();
    private List<TestData> results = new ArrayList<TestData>();
    private ExecutorService pool ;
    private final int maxThreadNum = 10;
    private LinkedBlockingQueue<String> concurrentLinkedQueue = new LinkedBlockingQueue<String>();
    public CalculateDataService() {
        concurrentLinkedQueue.offer("C:\\test\\1.txt");
        concurrentLinkedQueue.offer("C:\\test\\2.txt");
        concurrentLinkedQueue.offer("C:\\test\\3.txt");
        pool =Executors.newFixedThreadPool(maxThreadNum);
    }

    public void method1() throws ExecutionException, InterruptedException {
        fillData();
        calculateData();
    }

    private void calculateData() {
        Thread t = new Thread() {
            @Override
            public void run() {
               lockTask();
            }
        };
        t.run();
    }

    private void lockTask() {
        while (!pool.isTerminated()) {
            List<TestData> temps = new ArrayList<TestData>();
            temps.addAll(datas);
            results = sort1(temps);
            printArray(results);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void printArray(List<TestData> target){
        if(target.size()==0)
            return;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0;i<target.size();i++) {
            stringBuilder.append(target.get(i).toString());
        }
        try {
            new TxtDataWriter().appendMethodB("C:\\test\\result.txt",stringBuilder.toString()+" \r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillData() throws ExecutionException, InterruptedException {

        for (int i = 0, l = concurrentLinkedQueue.size(); i < l; i++) {
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        datas.addAll(new TxtDataReader().readFileByLines(concurrentLinkedQueue.poll()));
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
            if(testData == null ){
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
            if (testData.getGroupId().equals( groupId)) {
                return testData;
            }
        }
        return null;
    }
}
