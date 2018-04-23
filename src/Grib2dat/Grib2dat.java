package Grib2dat;

import Grib2dat.temp.CacheDataFrame;
import Grib2dat.temp.GFSMem;
import org.joda.time.DateTime;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

/**
 * Created by desktop13 on 2017/12/20.
 */
public class Grib2dat implements Runnable {
    //配置文件信息
    static String inPath;
    static String inPath_JBDB;
    static String outPath;
    static String fileStyle;
    public static String[] isobaric;
    public static String[] latlonArea;
    public static String[] fTime;
    static   String[] START_PARSE_TIME;
    static  float DTIME;
    public static String timeFlag;
    public DecimalFormat decimalFormat=new DecimalFormat("00");

    private static Grib2dat instance;
    private Grib2dat(){
        loadPath();
    }
    public static Grib2dat getInstance() {
        if (instance == null) {
            synchronized (Grib2dat.class) {
                if (instance == null) {
                    instance = new Grib2dat();
                }
            }
        }
        return instance;
    }
    Grib2dat(String inPath,String outPath){
        setInPath(inPath);
        setOutPath(outPath);
    }
//主逻辑方法parseGrib
    public boolean parseGrib(){
//        List<String> fileNames_0=ChooseDir.getFileList(inPath,fileStyle);
        String datePath = getDatePath(inPath);
        List<String> fileNames=ChooseDir.getFilterFileList(datePath,fileStyle);//文件选择
        if(fileNames==null){return false;}
        long start00000=System.currentTimeMillis(); /////////////////////////////////////////////////////
        for(String fileName:fileNames){
            System.out.println("正在解析："+fileName);
            long start=System.currentTimeMillis(); /////////////////////////////////////////////////////
//            for(ElementName elementName:ElementName.values()) {////////////////////////////////////////////////
            for(int kkk=0;kkk<1;kkk++) {
                ElementName elementName=ElementName.MSL;
                System.out.println("正在解析：：："+elementName.geteName());//////////////////////////////////////////////////
                if(elementName.getType()=="isobaric") {                   //多层
                    for (String isobaricName : isobaric) {
                        System.out.println("正在解析：：："+isobaricName);///////////////////////////////
                        CacheDataFrame cacheDataFrame = CacheDataFrame.getInstance();
                        GFSMem gfsMem = ReadGrib.getInstance().readGrib(fileName, elementName, isobaricName);
                        String datName = new File(fileName).getName();
                        String date = datName.substring(0, 10);
                        String VTI = datName.substring(datName.length()-3);
                        cacheDataFrame.pushData(gfsMem, date +VTI+ "_" + isobaricName+"_"+elementName  );
                    }
                }else {                     //单层
                    CacheDataFrame cacheDataFrame = CacheDataFrame.getInstance();
                    GFSMem gfsMem = ReadGrib.getInstance().readGrib(fileName, elementName, "surface");
                    String datName = new File(fileName).getName();
                    String date = datName.substring(0, 10);
                    String VTI = datName.substring(datName.length()-3);
                    cacheDataFrame.pushData(gfsMem, date +VTI+ "_" + "-9999"+"_"+elementName  );
                }
            }
            long end=System.currentTimeMillis();
            System.out.println("读取45数组："+(end-start));
        }
        long end00000=System.currentTimeMillis(); /////////////////////////////////////////////////////
        System.out.println(end00000-start00000);
        return true;
    }
    public boolean parseDat(){
        String datePath = getDatePath(inPath_JBDB);
        List<String> fileNames=ChooseDir.getFilterFileList_JBDB(datePath,fileStyle);//文件选择
        if(fileNames==null){return false;}
        long start=System.currentTimeMillis(); /////////////////////////////////////////////////////
        for(String fileName:fileNames){
            System.out.println("正在解析："+fileName);   ///////////////////////////////
            ElementName elementName = (fileName.contains("JB"))? ElementName.JB:ElementName.DB;
            for(String isobaricName:Config.getIsobaric()) {
                System.out.println("正在解析：：："+isobaricName);///////////////////////////////
                CacheDataFrame cacheDataFrame = CacheDataFrame.getInstance();
                GFSMem gfsMem = ReadGrib.getInstance().readDat(fileName, elementName, isobaricName);  /////////
                String datName = new File(fileName).getName();
                String date = datName.substring(11, 24);
                cacheDataFrame.pushData(gfsMem, date + "_" + isobaricName+"_"+elementName  );
            }
        }
        long end=System.currentTimeMillis();
        System.out.println("读取一个dat文件夹："+(end-start));

        return true;
    }

//加载配置文件加载路径
    public static void loadPath() {
        inPath=Config.getInPath();
        inPath_JBDB=Config.getInPath_JBDB();
        outPath=Config.getOutPath();
        fileStyle=Config.getFileStyle();
        isobaric=Config.getIsobaric();
        latlonArea=Config.getLatlonArea();
        fTime=Config.getfTime();
        START_PARSE_TIME = Config.getStartParseTime();
        DTIME = Config.getDTIME();
    }
    public void setInPath(String inPath) {
        this.inPath = inPath;
    }

    public void setOutPath(String outPath) {
        this.outPath = outPath;
    }

    //都是前一天的文件
    public String getDatePath( String inpath){
        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        c.setTimeInMillis(c.getTimeInMillis()-24*3600*1000);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;  //注意月份要加一
        int date = c.get(Calendar.DATE);
        String path=null;
        if(timeFlag==START_PARSE_TIME[0]) path = inpath+"/"+Integer.toString(year)+decimalFormat.format(month)+decimalFormat.format(date)+"00";
        if(timeFlag==START_PARSE_TIME[1]) path = inpath+"/"+Integer.toString(year)+decimalFormat.format(month)+decimalFormat.format(date)+"12";
        return path;
    }

    @Override
    public void run() {
        this.timing();
    }
    //定时  数据加载程序定时启动（5点/18点）
            //逻辑：对于启动时间附件11min内启动，每隔20min检测一次

    public void timing() {
        while (true){
            Calendar c0 = Calendar.getInstance();//可以对每个时间域单独修改
            Calendar c1 = Calendar.getInstance();//可以对每个时间域单独修改
            int year = c0.get(Calendar.YEAR);
            int month = c0.get(Calendar.MONTH);
            int date = c0.get(Calendar.DATE);
//            int hour = c0.get(Calendar.HOUR_OF_DAY);
//            int minute = c0.get(Calendar.MINUTE);
//            int second = c0.get(Calendar.SECOND);
            for (int i = 0; i < START_PARSE_TIME.length; i++) {
                c1.set(year,month,date,(int)Float.parseFloat(START_PARSE_TIME[i]),0,0);
                if(Math.abs(c1.getTimeInMillis()-c0.getTimeInMillis())<(DTIME/2+1)*60*1000){
                    timeFlag=START_PARSE_TIME[i];
                    Grib2dat.getInstance().parseGrib();
//                    Grib2dat.getInstance().parseDat(); ///////////////////////////////////////////////////////////
                    try {
                        Thread.sleep((long)(DTIME)*60*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep((long)DTIME*60*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
