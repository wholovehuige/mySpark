package com.trans;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class JavaFile {
    public static void task(String str, BufferedWriter bw, URL url,int number) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            ConnetAPI(str, connection);
            InputStream is = connection.getInputStream();

            InputStreamReader isr = new InputStreamReader(is, "UTF-8");

            BufferedReader brd = new BufferedReader(isr);

            String line = null;
            StringBuilder builder = new StringBuilder();
            while ((line = brd.readLine()) != null) {
                builder.append(line);
            }

            JSONObject js = new JSONObject(builder.toString());
            String jstr = (js.get("translation").toString()).replace("[", "");
            String jstrj = jstr.replaceAll("]", "");
            String jstrjs = jstrj.replace("\"", "");
            bw.write(number+" "+"update t_user_company set company_english_name ='"+jstrjs+"' where id="+"id" + "\n");
//            bw.newLine();
            bw.flush();
            brd.close();

            is.close();
            isr.close();
        } catch (Exception e) {
            try {
                bw.write(number+"   (这条没有翻译成功)");
                bw.newLine();
            } catch (IOException e1) {
                System.out.println("===");
                e1.printStackTrace();
            }
            System.out.println("==========");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int number = 0;

        Thread thread = new Thread();
        try {

            // 写文件
            File file = new File("/home/roy/tran_last_com");
            FileWriter writer = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(writer);
            // 读文件
            FileReader reader = new FileReader("/home/roy/tranS");
            BufferedReader br = new BufferedReader(reader);
            String str = null;

            // 连接外界api
            URL url = new URL("http://fanyi.youdao.com/openapi.do");

            while ((str = br.readLine()) != null) {
                if (number > 0) {
                    thread.sleep(3700);
//                    System.out.println("翻译了第" + num + "条");
                    task(str, bw, url,number);

                }number++;
            }
            bw.close();
            writer.close();
            br.close();
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("文件没找到");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO异常");
        } catch (InterruptedException e) {
            System.out.println("线程出错了");
            e.printStackTrace();
        }

    }

    public static void ConnetAPI(String str, HttpURLConnection connection) {
        try {
            connection.addRequestProperty("encoding", "UTF-8");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            OutputStream os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bwr = new BufferedWriter(osw);
            bwr.write("keyfrom=domytest007&key=84562674&type=data&doctype=json&version=1.1&q=" + str);
            bwr.flush();
            os.close();
            osw.close();
        } catch (Exception e) {
            System.out.println("API出问题了");
            e.printStackTrace();
        }

    }
}
