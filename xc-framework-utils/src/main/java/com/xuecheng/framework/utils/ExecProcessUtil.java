package com.xuecheng.framework.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *  防止缓冲区满了后不能及时得到信息
 *  对于 process.waitFor() 返回执行的日志.
 */
public class ExecProcessUtil {


    public ExecProcessUtil( ){
    }

     public static String waitFor(Process p) {
        InputStream in = null;
        InputStream error = null;
        String result = "error";
        int exitValue = -1;
        StringBuffer outputString = new StringBuffer();
        try {
            in = p.getInputStream();
            error = p.getErrorStream();
            boolean finished = false;
            int maxRetry = 600;//每次休眠1秒，最长执行时间10分种
            int retry = 0;
            while (!finished) {
                if (retry > maxRetry) {
                    return "error";
                }
                try {
                    while (in.available() > 0) {
                        Character c = new Character((char) in.read());
                        outputString.append(c);
                        System.out.print(c);
                    }
                    while (error.available() > 0) {
                        Character c = new Character((char) in.read());
                        outputString.append(c);
                        System.out.print(c);
                    }
                    //进程未结束时调用exitValue将抛出异常
                    exitValue = p.exitValue();
                    finished = true;

                } catch (IllegalThreadStateException e) {
                    Thread.currentThread().sleep(1000);//休眠1秒
                    retry++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
       return outputString.toString();

    }


    public static void main(String[] args) throws IOException {

    }
}
