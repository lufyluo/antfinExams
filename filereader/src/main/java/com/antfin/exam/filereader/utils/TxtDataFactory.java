package com.antfin.exam.filereader.utils;

import com.antfin.exam.filereader.Models.TestData;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TxtDataFactory {
    private String[] files;
    private final int threadNum = 10;
    private  LinkedBlockingQueue<String> concurrentLinkedQueue = new LinkedBlockingQueue<String>();
    public TxtDataFactory(String[] files){
        convert(files);
    }
    public List<TestData> GetData() throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(threadNum);
        List<TestData> datas = new ArrayList<TestData>();
        for (int i = 0,l = files.length;i<l;i++){
            Future future = pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        datas .addAll(new TxtDataReader().readFileByLines(concurrentLinkedQueue.poll()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            future.get();
        }
        return datas;
    }
    private void convert(@NonNull String[] files){
        for (int i =0;i<files.length;i++) {
            concurrentLinkedQueue.offer(files[i]);
        }
    }
}
