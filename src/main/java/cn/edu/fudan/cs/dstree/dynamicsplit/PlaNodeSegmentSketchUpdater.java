package cn.edu.fudan.cs.dstree.dynamicsplit;

/**
 * Created by IntelliJ IDEA.
 * User: wangyang
 * Date: 11-7-7
 * Time: 下午7:40
 * To change this template use File | Settings | File Templates.
 */
public class PlaNodeSegmentSketchUpdater implements INodeSegmentSketchUpdater {
    ISeriesSegmentSketcher seriesSegmentSketcher;

    public NodeSegmentSketch updateSketch(NodeSegmentSketch nodeSegmentSketch, double[] series, int fromIdx, int toIdx) {
        SeriesSegmentSketch seriesSegmentSketch = seriesSegmentSketcher.doSketch(series, fromIdx, toIdx);

        if (nodeSegmentSketch.indicators == null) //not initial
        {
//            nodeSegmentSketch.indicators = new float[8];                //float
//            nodeSegmentSketch.indicators[0] = Float.MAX_VALUE * -1; //for left top,max intercept
//            nodeSegmentSketch.indicators[1] = Float.MAX_VALUE; //for left bottom,min intercept
//            nodeSegmentSketch.indicators[2] = Float.MAX_VALUE * -1; //for right top
//            nodeSegmentSketch.indicators[3] = Float.MAX_VALUE; //for right bottom
//            nodeSegmentSketch.indicators[4] = Float.MAX_VALUE * -1; //for max sse
//            nodeSegmentSketch.indicators[5] = Float.MAX_VALUE; //for min sse
//            nodeSegmentSketch.indicators[6] = Float.MAX_VALUE * -1; //for max slope
//            nodeSegmentSketch.indicators[7] = Float.MAX_VALUE; //for min slope
            nodeSegmentSketch.indicators = new float[8];                //float
            nodeSegmentSketch.indicators[0] = Float.MAX_VALUE * -1; //for max intercept       max left
            nodeSegmentSketch.indicators[1] = Float.MAX_VALUE; //for min intercept            min left
            nodeSegmentSketch.indicators[2] = Float.MAX_VALUE * -1; //for max slope
            nodeSegmentSketch.indicators[3] = Float.MAX_VALUE; //for min slope
            nodeSegmentSketch.indicators[4] = Float.MAX_VALUE * -1; //for max sse
            nodeSegmentSketch.indicators[5] = Float.MAX_VALUE; //for min sse
            //for right top and right bottom
            nodeSegmentSketch.indicators[6] = Float.MAX_VALUE * -1; //for right top          max right
            nodeSegmentSketch.indicators[7] = Float.MAX_VALUE; //for right bottom            min right
        }

//        int len = toIdx - fromIdx;
//        nodeSegmentSketch.indicators[0] = Math.max(nodeSegmentSketch.indicators[0], seriesSegmentSketch.indicators[1]);//a*0 + b
//        nodeSegmentSketch.indicators[1] = Math.min(nodeSegmentSketch.indicators[1], seriesSegmentSketch.indicators[1]);
//        nodeSegmentSketch.indicators[2] = Math.max(nodeSegmentSketch.indicators[2], seriesSegmentSketch.indicators[0] * (len - 1) + seriesSegmentSketch.indicators[1]); //a * (len-1) + b
//        nodeSegmentSketch.indicators[3] = Math.min(nodeSegmentSketch.indicators[3], seriesSegmentSketch.indicators[0] * (len - 1) + seriesSegmentSketch.indicators[1]);
//        nodeSegmentSketch.indicators[4] = Math.max(nodeSegmentSketch.indicators[4], seriesSegmentSketch.indicators[2]);
//        nodeSegmentSketch.indicators[5] = Math.min(nodeSegmentSketch.indicators[5], seriesSegmentSketch.indicators[2]);
//        nodeSegmentSketch.indicators[6] = Math.max(nodeSegmentSketch.indicators[6], seriesSegmentSketch.indicators[0]);
//        nodeSegmentSketch.indicators[7] = Math.min(nodeSegmentSketch.indicators[7], seriesSegmentSketch.indicators[0]);

        nodeSegmentSketch.indicators[0] = Math.max(nodeSegmentSketch.indicators[0], seriesSegmentSketch.indicators[1]);
        nodeSegmentSketch.indicators[1] = Math.min(nodeSegmentSketch.indicators[1], seriesSegmentSketch.indicators[1]);
        nodeSegmentSketch.indicators[2] = Math.max(nodeSegmentSketch.indicators[2], seriesSegmentSketch.indicators[0]);
        nodeSegmentSketch.indicators[3] = Math.min(nodeSegmentSketch.indicators[3], seriesSegmentSketch.indicators[0]);
        nodeSegmentSketch.indicators[4] = Math.max(nodeSegmentSketch.indicators[4], seriesSegmentSketch.indicators[2]);
        nodeSegmentSketch.indicators[5] = Math.min(nodeSegmentSketch.indicators[5], seriesSegmentSketch.indicators[2]);
        //for right top and right bottom
        int len = toIdx - fromIdx;
        nodeSegmentSketch.indicators[6] = Math.max(nodeSegmentSketch.indicators[6], seriesSegmentSketch.indicators[0] * (len - 1) + seriesSegmentSketch.indicators[1]); //a * (len-1) + b
        nodeSegmentSketch.indicators[7] = Math.min(nodeSegmentSketch.indicators[7], seriesSegmentSketch.indicators[0] * (len - 1) + seriesSegmentSketch.indicators[1]);
        return nodeSegmentSketch;
    }

    public PlaNodeSegmentSketchUpdater(ISeriesSegmentSketcher seriesSegmentSketcher) {
        this.seriesSegmentSketcher = seriesSegmentSketcher;
    }
}
