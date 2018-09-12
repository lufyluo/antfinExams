package com.antfin.exam.filereader.utils;

import com.antfin.exam.filereader.Models.TestData;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TxtDataReader {
    private String fileName;


    public List<TestData> readFileByLines(String fileName) throws IOException {
        List<TestData> testDatas = new ArrayList<TestData>();
        File file;
        BufferedReader reader = null;
        try {
            file = new File(fileName);
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                testDatas.add(new TestData().deserialize(tempString));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return testDatas;
    }
}
