package Grib2dat.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by desktop13 on 2017/12/20.
 */
public class Test {

    public Test() {
    }

    public static void main(String[] args) {
//    new SmallRead().read();

        System.out.println(new File("2/1.txt").getAbsolutePath());
        System.out.println((float) (180+ Math.atan2(0,1)*180/Math.PI));

        
    }
}

