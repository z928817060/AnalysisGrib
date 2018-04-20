package Plot;

/**
 * Created by desktop13 on 2017/12/22.
 */
public class Plot {
    Data data;
    public Plot() {
        data=new DataModle();
    }

    public void plot(){
        float[][] f=data.getData();
    }
}
