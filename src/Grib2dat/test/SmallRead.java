package Grib2dat.test;

import Grib2dat.Grib2dat;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;


/**
 * Created by desktop13 on 2017/12/21.
 * 功能：用于验证生成的数据
 */
public class SmallRead {
    public SmallRead() {

    }
public void read(){
        RandomAccessFile rf = null;
    String filename="E:\\IDEA\\earth\\data\\db\\DB\\2014101706\\XHGFS_G_SCT_0300_2014101706000.dat";
        File file=new File(filename);
    try {
        rf = new RandomAccessFile(file, "r");
        ByteBuffer byteBuffer = ByteBuffer.allocate(241*480*26*4);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        rf.read(byteBuffer.array());
        float value;
        int ii=0;
        Grib2dat grib2dat=Grib2dat.getInstance();
        String[] press=grib2dat.isobaric;
        List list=new ArrayList<>();
        while ((value=byteBuffer.getFloat())!=-1){
            list.add(value);
//            System.out.println(ii);
//                if (value>0) {
//                    int press_num=ii/(241*480);
//                    int lat_num=ii%(241*480)/480;
//                    int lon_num=ii%(241*480)%480;
//                    float lat=(lat_num * 180 / 241 - 90);
//                    float lon = (lon_num * 360 / 480 - 180);
//                    if(lat>5.5 &&lat<60&&lon>63.5&&lon<156.5) {
//                        System.out.println("num :" + (ii) + "   press:" + press[press_num] + "   hang,lie:" + lat + " " + lon + " *********" + value);
//                    }
//                }
//                try {
//                    Thread.sleep((long)100);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            if(ii==3007679) {
                break;
            }
            ii++;
        }
        Collections.sort(list);
        Collections.reverse(list);

        System.out.println();
//        for(int i=0;i<10;i++){
//           value = byteBuffer.getFloat();
//            System.out.println("num :"+i+" *********"+value);
//        }
//        for(int k=0;k<26;k++){
//            for(int i=0;i<241;i++) {
//                for (int j = 0; j < 480; j++) {
//                    float value = byteBuffer.getFloat();
//                    if (k==0&& i == 0 && j == 0) {
//                        System.out.println(value);
//                    }
//                }
//            }
//        }
//        for (int i = 0; i < 721; ++i) {
//            for (int j = 0; j < 1440; ++j) {
//                float jbValue = byteBuffer.getFloat();
//                jbcompare[24-k][i*1440+j]=jbValue;  //1038240  对同一地区点值大于0做筛选
//                dbcompare[24-k][i*1440+j]=dbValue;
//            }
//        }

    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            rf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
}
