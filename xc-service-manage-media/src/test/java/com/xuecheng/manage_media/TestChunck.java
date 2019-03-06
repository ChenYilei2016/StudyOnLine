package com.xuecheng.manage_media;

import com.xuecheng.framework.utils.PathUtil;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * --添加相关注释--
 *
 * @author chenyilei
 * @date 2019/03/05- 17:35
 */
public class TestChunck {
    public static final String readFile = "F:\\Xu2\\学成在线\\hls\\lucene.mp4";
    public static final String taggetPath= "F:\\Xu2\\学成在线\\chunks\\";
    public static final String taggetFile= "F:\\Xu2\\学成在线\\chunks\\target.mp4";
    public static final int chunckSize = 5*1024*1024; //5MB 一块
    public static byte[] buf = new byte[1024];

    @Test
    public void t1() throws IOException {
        File readFile = new File( TestChunck.readFile ) ;
        RandomAccessFile readAcc = new RandomAccessFile(readFile,"r");
        int totalChunck = (int) Math.ceil(readAcc.length() *1.0/ TestChunck.chunckSize );
        for(int i = 0 ;i< totalChunck ;i++){
            //填充这一块5M
            File targetFile = new File(TestChunck.taggetPath+"chunck_"+i );
            RandomAccessFile writeAcc = new RandomAccessFile(targetFile,"rw");
            int count = 0;
            while( (count = readAcc.read(buf))!= -1 ){
                writeAcc.write(buf,0,count);
                if( writeAcc.length() >= TestChunck.chunckSize ){
                    break;
                }
            }
            writeAcc.close();
        }
        readAcc.close();
    }
    @Test
    public void merge() throws IOException {
        int i =0 ;
        File targetFile = new File(TestChunck.taggetFile);
        RandomAccessFile writeAcc = new RandomAccessFile(targetFile,"rw");

        while(true){
            File readFile = new File(TestChunck.taggetPath+"chunck_"+i );
            if(!readFile.exists()){
                break;
            }
            RandomAccessFile readAcc = new RandomAccessFile(readFile,"r");
            int count = 0;
            while((count = readAcc.read(buf))!=-1){
                writeAcc.write(buf,0,count);
            }
            readAcc.close();
            i++;
        }

        writeAcc.close();
    }

    @Test
    public void testPath(){
        System.out.println(PathUtil.getPathByMd5("f:/dsf/sfd","fasdfasdf"));
        System.out.println(new File("C:\\Users\\Administrator\\Desktop\\黑.txt").getParentFile());
    }
}
