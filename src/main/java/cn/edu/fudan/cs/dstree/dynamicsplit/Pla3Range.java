package cn.edu.fudan.cs.dstree.dynamicsplit;

/**
 * Created by IntelliJ IDEA.
 * User: wangyang
 * Date: 11-7-5
 * Time: 下午8:11
 * To change this template use File | Settings | File Templates.
 */
public class Pla3Range implements IRange {
    public double calc(Sketch sketch, int len) {
        double rangeBySlopeAndIntercept = 0;

        double maxLeft = sketch.indicators[0];
        double minLeft = sketch.indicators[1];
        double maxRight = sketch.indicators[6];
        double minRight = sketch.indicators[7];

        double upperSlope = (maxRight - maxLeft) / (len - 1);
        double lowerSlope = (minRight - minLeft) / (len - 1);

        final double startDiff = maxLeft - minLeft;
        final double slopeDiff = upperSlope - lowerSlope;
//        System.out.println("startDiff = " + startDiff);
//        for (int i = 0; i < len; i++) {                   //todo: can optimize to a formula
//            double width = startDiff + slopeDiff * i;
//            rangeBySlopeAndIntercept += width * width;
//        }
        rangeBySlopeAndIntercept = DistTools.plaDistance(upperSlope, maxLeft, lowerSlope, minLeft, len);
//        System.out.println("rangeBySlopeAndIntercept = " + rangeBySlopeAndIntercept);
        final double sse_upper = sketch.indicators[4];
        double deltaRight = maxRight - minRight;
        double deltaLeft = maxLeft - minLeft;
        double factory  = Math.abs(Math.max(deltaLeft,deltaRight)/Math.min(deltaLeft,deltaRight));

        return rangeBySlopeAndIntercept + sse_upper;
    }
}
