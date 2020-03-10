package com.test.blog.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ReplaceLocalImageToOnline {
    private String filePath;
    private String destPath;
    private String targetStr;
    private String desStr;

    public String getDestPath() {
        return destPath;
    }

    public void setDestPath(String destPath) {
        this.destPath = destPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTargetStr() {
        return targetStr;
    }

    public void setTargetStr(String targetStr) {
        this.targetStr = targetStr;
    }

    public String getDesStr() {
        return desStr;
    }

    public void setDesStr(String desStr) {
        this.desStr = desStr;
    }

    public static void main(String[] args) {
        ReplaceLocalImageToOnline replaceLocalImageToOnline = new ReplaceLocalImageToOnline();
//        使用反斜杠\\，在匹配字符串的时候始终报错，换成4个反斜杠就行了
        replaceLocalImageToOnline.setDestPath("D:/temp/a.txt");
        replaceLocalImageToOnline.setFilePath("D:/private/学习/blog/2020/在CentOS下使用ffmepg将哔哩哔哩的ms4音视频文件转换为mp4.md");
        replaceLocalImageToOnline.setDesStr("http://114.55.147.153/image/blog/");
        replaceLocalImageToOnline.setTargetStr("D:\\private\\学习\\blog\\2020\\03\\");
        replaceLocalImageToOnline.readData();
    }

    private void readData(){
        if (filePath ==null || filePath.equals("")){
            System.out.println("error: the filePath doesn't exist");
            return;
        }
        BufferedWriter writer = null;
        BufferedReader reader =null;
        String temp =null;
        try {
            reader =new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            writer =new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destPath)));
            while ((temp = reader.readLine()) != null){
                System.out.println(temp);
                if (temp.contains(targetStr))
                    temp.replace(targetStr,desStr);
                writer.write(temp);
                writer.newLine();
            }
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (reader !=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writer !=null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
