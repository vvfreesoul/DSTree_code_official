package cn.edu.fudan.cs.dstree.dynamicsplit;

import cn.edu.fudan.cs.dstree.util.CalcUtil;

/**
 * Created by IntelliJ IDEA.
 * User: wangyang
 * Date: 11-7-7
 * Time: 下午7:28
 * To change this template use File | Settings | File Templates.
 */
public class PlaSeriesSegmentSketcher implements ISeriesSegmentSketcher {

    public SeriesSegmentSketch doSketch(double[] series, int fromIdx, int toIdx) {
        SeriesSegmentSketch seriesSegmentSketch = new SeriesSegmentSketch();
        seriesSegmentSketch.indicators = new float[4];            //slope,intercept,sse

        double[] plaValues = CalcUtil.pla(series, fromIdx, toIdx);
        seriesSegmentSketch.indicators[0] = (float) plaValues[0];
        seriesSegmentSketch.indicators[1] = (float) plaValues[1];
        seriesSegmentSketch.indicators[2] = (float) plaValues[2];
        final int len = toIdx - fromIdx;
        seriesSegmentSketch.indicators[3] = (float) (plaValues[1] + plaValues[0] * (len -1));
        return seriesSegmentSketch;
    }
}
