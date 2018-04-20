package Grib2dat;

/**
 * Created by desktop13 on 2017/12/21.
 */
public class Main {
    public Main() {
    }
    public static void main(String[] args) {
            Config.loadAll();
            Thread t1=new Thread(Grib2dat.getInstance());
        t1.start();

//        Grib2dat grid2dat=Grib2dat.getInstance();
//        grid2dat.parseGrib();
    }
}
