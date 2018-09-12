package com.antfin.exam.filereader.utils;


import java.io.*;

public class TxtDataWriter {
    FileWriter writer;
    public  void append(String path ,String fileName, String content) throws IOException {

        try {
            File file = new File(path);
            if(!file.exists()){
                file.mkdirs();
            }
                //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileWriter(path+fileName, true);
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            writer.close();
        }
    }
}
