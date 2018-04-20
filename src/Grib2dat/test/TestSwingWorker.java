package Grib2dat.test;

/**
 * Created by zh on 2018/3/8.
 */
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 实现功能：SwingWorker  线程加进度条
 * SwingWorker<T,V>  ： T是结果值，主线程doInBackground() 的返回值；
 *                      V是中间值，主线程publish（）方法的输入值，process（List<V> chunks）的输入参数
 *
 */
public class TestSwingWorker extends SwingWorker<List<BufferedImage>,String>{

    private JLabel status;
    private JPanel viewer;
    private String[] imagesName;
    private JProgressBar jpb;
    List<BufferedImage> image;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                JFrame frame = new JFrame();
                final JPanel panel = new JPanel();
                final JLabel label = new JLabel();

                final String[] imagesName = new String[2];
                for(int i=0; i<imagesName.length; i++){
                    imagesName[i] = "images//("+(i+1)+").jpg";//
                }
                JScrollPane sp = new JScrollPane(panel);
                sp.setSize(new Dimension(700,500));
                frame.add(sp,BorderLayout.CENTER);

                JPanel stp = new JPanel();
                final JProgressBar jpb = new JProgressBar();
                jpb.setMinimum(1);
                jpb.setMaximum(imagesName.length);
                stp.add(jpb);
                stp.add(label);
                frame.add(stp,BorderLayout.SOUTH);

                JButton button = new JButton("load image");
                button.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        TestSwingWorker sw = new TestSwingWorker(label, panel, imagesName, jpb);
                        sw.execute();
                    }
                });
                frame.add(button,BorderLayout.NORTH);
                frame.setVisible(true);
            }
        });
    }
    public TestSwingWorker(JLabel status, JPanel viewer, String[] imagesName, JProgressBar jpb){
        this.status = status;
        this.viewer = viewer;
        this.imagesName = imagesName;
        this.jpb = jpb;
    }
    @Override
    protected List<BufferedImage> doInBackground() throws Exception {  //本线程的主线程
         image = new ArrayList<BufferedImage>();
        for(int i=0; i<imagesName.length; i++){
//            image.add(ImageIO.read(getClass().getClassLoader().getResource(imagesName[i])));
            image.add((BufferedImage)ImageIO.read(new File(imagesName[i])));
            publish("已经加载了  "+(i+1)+"/"+imagesName.length+" : "+imagesName[i]);
            Thread.sleep(1000);
        }
        return image;
    }
    @Override
    protected void process(List<String> chunks) {          //线程中处理过程的反馈等
        status.setText(chunks.get(chunks.size()-1));
        int x = Integer.parseInt(chunks.get(chunks.size()-1).substring(chunks.get(chunks.size()-1).indexOf("(")+1,chunks.get(chunks.size()-1).indexOf(")")).trim());
        jpb.setValue(x);
        for(String str : chunks){
            System.out.println(str);
        }
    }
    @Override
    protected void done() {          //完成本线程之后处理
        try {
            for(BufferedImage image : getImage()){
                JLabel label = new JLabel(new ImageIcon(image));
                label.setSize(160, 120);
                label.setPreferredSize(new Dimension(160,120));
                viewer.add(label);
                viewer.revalidate();
//                viewer.repaint();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected List<BufferedImage> getImage(){
        return image;
    }
}