package cn.edu.fudan.cs.dstree.data;

/**
 * Created by IntelliJ IDEA.
 * User: wangyang
 * Date: 11-6-21
 * Time: 下午7:50
 * To change this template use File | Settings | File Templates.
 */
public class Utils {
    public static double random(double min, double max) {
        return min + Math.random() * (max - min);
    }

    public static int random(int min, int max) {
        return (int) (Math.round(min + Math.random() * (max - min)));
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            double d = Utils.random(-5.0, 5.0);
            System.out.println("d = " + d);
        }

        for (int i = 0; i < 20; i++) {
            int n = Utils.random(0, 4);
            System.out.println("n = " + n);
        }

    }


}
