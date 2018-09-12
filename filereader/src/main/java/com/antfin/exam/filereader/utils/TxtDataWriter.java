package com.antfin.exam.filereader.utils;

import java.io.*;

public class TxtDataWriter {
    FileWriter writer;
    public  void append(String fileName, String content) throws IOException {

        try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileWriter(fileName, true);
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            writer.close();
        }
    }
}
