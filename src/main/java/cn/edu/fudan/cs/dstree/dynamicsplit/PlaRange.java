package cn.edu.fudan.cs.dstree.dynamicsplit;

/**
 * Created by IntelliJ IDEA.
 * User: wangyang
 * Date: 11-7-5
 * Time: 下午8:11
 * To change this template use File | Settings | File Templates.
 */
public class PlaRange implements IRange {
    public double calc(Sketch sketch, int len) {
        double rangeBySlopeAndIntercept = 0;
//        final double slopeUpper = (sketch.indicators[2] - sketch.indicators[0]) / (len - 1);
//        final double slopeLower = (sketch.indicators[3] - sketch.indicators[1]) / (len - 1);
        final double slopeUpper = sketch.indicators[2];
        final double slopeLower = sketch.indicators[3];
        final double slopeDiff = slopeUpper - slopeLower;
//        System.out.println("slopeDiff = " + slopeDiff);
        final double maxIntercept = sketch.indicators[0];
        final double minIntercept = sketch.indicators[1];
        final double startDiff = maxIntercept - minIntercept;
//        System.out.println("startDiff = " + startDiff);
        for (int i = 0; i < len; i++) {                   //todo: can optimize to a formula
            double width = startDiff + slopeDiff * i;
            rangeBySlopeAndIntercept += width * width;
        }
//        System.out.println("rangeBySlopeAndIntercept = " + rangeBySlopeAndIntercept);
        final double sse_upper = sketch.indicators[4];
        return rangeBySlopeAndIntercept + sse_upper;
    }
}
