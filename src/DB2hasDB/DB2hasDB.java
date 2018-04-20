package DB2hasDB;

import Grib2dat.ByteData;
import Grib2dat.ChooseDir;
import ucar.ma2.InvalidRangeException;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Properties;

/**
 * Created by ZH on 2018/1/25.
 * function:把db数据转化成一个文件,用有无区分;(改了全加)
 */
public class DB2hasDB {
    String inPath=null;
    String outPath=null;
    final int lat_num=241;
    final int lon_num=480;
    float[][] hasDB=new float[lat_num][lon_num];
    public DB2hasDB() {
        loadPath();
        convers(inPath,outPath);
    }
//转化
    public void convers(String inPath,String outPath){
        ChooseDir chooseDir=new ChooseDir();
        List<String> dir=chooseDir.getFileList(inPath,"dat");
        for (String dirName:dir){
            String outfilePath = outPath + "/" + "XHGFS_G_DB_has_"  + dirName.substring(dirName.length() - 10,dirName.length())+".dat";  //输出文件
            hasDB=new float[lat_num][lon_num];
            List<String> fileNames=chooseDir.getFileList(dirName,"dat");
            for(int i=0;i<16;i++){
                System.out.println(i);
                readDBToHasDB(fileNames.get(i));  //读取文件的时候顺便处理了
            }
            writeHasDB(outfilePath,lat_num,lon_num,hasDB);
            System.out.println(dirName);
        }
        System.out.println();
    }

//路径
    public void loadPath(){
        Properties properties=new Properties();
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(System.getProperty("user.dir")+"/config/DBhasDB.properties"));
            properties.load(in);
            inPath=properties.getProperty("inPath");
            outPath=properties.getProperty("outPath");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//读二进制文件数据(float)
    public void readDBToHasDB(String fileName){
        RandomAccessFile rf = null;
        try {
            rf = new RandomAccessFile(fileName, "r");
            ByteBuffer byteBuffer = ByteBuffer.allocate((lat_num * lon_num+5) * 4);//5个经纬度信息
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            rf.seek(4*5);
            rf.read(byteBuffer.array());
            for (int i = 0; i < lat_num; i++) {
                for (int j = 0; j < lon_num; j++) {
//                    System.out.println(byteBuffer.getFloat());
                    if(byteBuffer.getFloat()>0) {
//                        System.out.println("i:"+i+"j:"+j);
                        hasDB[i][j] +=byteBuffer.getFloat();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                rf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//写二进制文件数据(float)
    public void writeHasDB(String fileName,int lat_num,int lon_num,float[][]hasDB){
        BufferedOutputStream bufferedOutputStream=null;
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File(fileName)));
            for (int i = 0; i < lat_num; i++) {
                byte[] temp = new byte[lon_num * 4];
                for (int j = 0; j < lon_num; j++) {
                    byte[] temp_simple = ByteData.float2byte(hasDB[i][j]);
                    System.arraycopy(temp_simple, 0, temp, (j) * 4, 4);
                }
                bufferedOutputStream.write(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
