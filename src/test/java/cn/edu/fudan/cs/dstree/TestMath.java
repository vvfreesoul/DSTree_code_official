package cn.edu.fudan.cs.dstree;

import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: wangyang
 * Date: 13-11-16
 * Time: 下午5:50
 * To change this template use File | Settings | File Templates.
 */
public class TestMath {
    @Test
    public void testPower()
    {
        double d = Math.random();
        double d2 = 0;
        int count = 1000 * 1000 * 100;
        StopWatch sw =  new StopWatch();
        sw.start();
        for (int i = 0; i < count; i++) {
            d2 = d * d;
        }
        sw.suspend();
        System.out.println("d2 = " + d2);
        System.out.println("sw.getTime() = " + sw.getTime());

        sw.reset();
        sw.start();
        for (int i = 0; i < count; i++) {
            d2 = Math.pow(d,2);
        }
        sw.suspend();
        System.out.println("d2 = " + d2);
        System.out.println("sw.getTime() = " + sw.getTime());

    }

    public void testDist()
    {

    }

    public static double plaDistance(double slope1, double intercept1, double slope2, double intercept2, int len) {
        double ret = 0;

        for (int i = 0; i < len; i++) {
            double y1 = slope1 * i + intercept1;
            double y2 = slope2 * i + intercept2;
            ret += (y1 - y2) * (y1 - y2);
        }
        System.out.println("ret = " + ret);

        final double diffSlope = slope1 - slope2;
        final double diffIntercept = intercept1 - intercept2;
        final double r = diffSlope * diffSlope * (len -1) * len * (2 * len - 1) / 6 + len * diffIntercept * diffIntercept + diffSlope * diffIntercept * (len -1) * len;
        if (Math.abs(r -ret) > 0.00000001)
            throw new RuntimeException("ret:" + ret + "  r:" + r);

        return ret;
    }

    @Test
    public void test1()
    {
        double v = plaDistance(1.1,1.34, 1.34, 2.32, 200);
        System.out.println("v = " + v);

    }
}
