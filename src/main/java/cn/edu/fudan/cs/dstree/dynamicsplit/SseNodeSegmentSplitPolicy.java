package cn.edu.fudan.cs.dstree.dynamicsplit;

/**
 * Created by IntelliJ IDEA.
 * User: wangyang
 * Date: 11-7-10
 * Time: 上午10:30
 * To change this template use File | Settings | File Templates.
 */
public class SseNodeSegmentSplitPolicy implements INodeSegmentSplitPolicy {
    public NodeSegmentSketch[] split(NodeSegmentSketch nodeSegmentSketch) {
        float max_sse = nodeSegmentSketch.indicators[4];                //todo
        float min_sse = nodeSegmentSketch.indicators[5];
        indicatorSplitValue = (max_sse + min_sse) / 2;  //the mean value is split value

        NodeSegmentSketch[] ret = new NodeSegmentSketch[2]; //split into 2 node
        ret[0] = new NodeSegmentSketch();
        ret[0].indicators = new float[nodeSegmentSketch.indicators.length];  //todo
        ret[1] = new NodeSegmentSketch();
        ret[1].indicators = new float[nodeSegmentSketch.indicators.length];   //todo
        for (int i = 0; i < ret.length; i++) {
            System.arraycopy(nodeSegmentSketch.indicators, 0, ret[i].indicators, 0, ret[i].indicators.length);
        }

        ret[0].indicators[5] = indicatorSplitValue;
        ret[1].indicators[4] = indicatorSplitValue;
        return ret;  //To change body of implemented methods use File | Settings | File Templates.
    }

    int indicatorSplitIdx = 2;
    float indicatorSplitValue;           //todo

    public int getIndicatorSplitIdx() {
        return indicatorSplitIdx;
    }

    public float getIndicatorSplitValue() {       //todo
        return indicatorSplitValue;
    }
}
