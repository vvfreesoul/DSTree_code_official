package cn.edu.fudan.cs.dstree.data;

import cn.edu.fudan.cs.dstree.util.TimeSeriesFileUtil;

/**
 * Created by IntelliJ IDEA.
 * User: wangyang
 * Date: 11-6-21
 * Time: 下午7:36
 * To change this template use File | Settings | File Templates.
 */
public class SegmentGaussianGenerator implements SeriesGenerator {
    int length;

    int minSegmentSize;
    int maxSegmentSize;

    double minMean;
    double maxMean;

    double minStd;
    double maxStd;

    public SegmentGaussianGenerator(int length, int minSegmentSize, int maxSegmentSize, double minMean, double maxMean, double minStd, double maxStd) {
        this.length = length;
        this.minSegmentSize = minSegmentSize;
        this.maxSegmentSize = maxSegmentSize;
        this.minMean = minMean;
        this.maxMean = maxMean;
        this.minStd = minStd;
        this.maxStd = maxStd;
    }

    public double[] generate() {
        double[] ret = new double[length];
        int segSize = Utils.random(minSegmentSize, maxSegmentSize);
        //cut length into segSize random
        int[] segLengths = new int[segSize];
        int leftLegnth = length;
        //the last one is leftLength,so to length-1
        for (int i = 0; i < segLengths.length - 1; i++) {
            segLengths[i] = Utils.random(1, leftLegnth - segLengths.length + i);//save room for left segment
            leftLegnth = leftLegnth - segLengths[i];
        }
        segLengths[segLengths.length - 1] = leftLegnth;

//        System.out.println("segLengths = " + SaxUtil.timeSeries2Line(segLengths));

        int startPos = 0;
        for (int i = 0; i < segLengths.length; i++) {
            int segLength = segLengths[i];
            GaussianGenerator gaussianGenerator = new GaussianGenerator(segLength, minMean, maxMean, minStd, maxStd);
            double[] doubles = gaussianGenerator.generate();
            System.arraycopy(doubles, 0, ret, startPos, doubles.length);
            startPos += doubles.length;
//            System.out.println("startPos = " + startPos);
        }
        return ret;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public static void main(String[] args) {
        SegmentGaussianGenerator generator = new SegmentGaussianGenerator(100, 3, 10, 10, 20, 2, 10);
        double[] doubles = generator.generate();
        System.out.println("doubles = " + TimeSeriesFileUtil.timeSeries2Line(doubles));
    }
}
