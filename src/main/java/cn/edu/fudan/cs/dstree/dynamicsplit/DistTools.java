package cn.edu.fudan.cs.dstree.dynamicsplit;

import cn.edu.fudan.cs.dstree.util.CalcUtil;

import java.io.IOException;

public class DistTools {
    public static double plaLowerBound(Node node, double[] queryTs) throws IOException {
        double sum = 0;
        short[] points = node.nodePoints;

        double[][] pla = CalcUtil.plaBySegments(queryTs, points);

        //for each segment
        for (int i = 0; i < pla.length; i++) {
            double tempDist = 0;
            //
            double maxIntercept = node.nodeSegmentSketches[i].indicators[0];
            double minIntercept = node.nodeSegmentSketches[i].indicators[1];
            double maxSlope = node.nodeSegmentSketches[i].indicators[2];
            double minSlope = node.nodeSegmentSketches[i].indicators[3];
            double maxSse = node.nodeSegmentSketches[i].indicators[4];
            double minSse = node.nodeSegmentSketches[i].indicators[5];
            int len = node.getSegmentLength(i);

            for (int j = 0; j < len; j++) {
                //deal with pla line
                double upper = maxSlope * j + maxIntercept;
                double lower = minSlope * j + minIntercept;
                double current = pla[i][0] * j + pla[i][1];
                if (current >= upper)
                    tempDist = tempDist + (current - upper) * (current - upper);
                else if (current <= lower)
                    tempDist = tempDist + (lower - current) * (lower - current);

            }
//            deal with sse
            if (pla[i][2] >= maxSse) {
                double tmp = Math.sqrt(pla[i][2]) - Math.sqrt(maxSse);
                tempDist = tempDist + tmp * tmp;
            } else if (pla[i][2] <= minSse) {
                double tmp = Math.sqrt(pla[i][2]) - Math.sqrt(minSse);
                tempDist = tempDist + tmp * tmp;
            }
            sum += tempDist;
        }

        sum = Math.sqrt(sum);
        return sum;
    }

    public static double plaDistance(double slope1, double intercept1, double slope2, double intercept2, int len) {
        double ret = 0;

//        for (int i = 0; i < len; i++) {
//            double y1 = slope1 * i + intercept1;
//            double y2 = slope2 * i + intercept2;
//            ret += (y1 - y2) * (y1 - y2);
//        }

        final double diffSlope = slope1 - slope2;
        final double diffIntercept = intercept1 - intercept2;
        ret = diffSlope * diffSlope * (len -1) * len * (2 * len - 1) / 6 + len * diffIntercept * diffIntercept + diffSlope * diffIntercept * (len -1) * len;

        return ret;
    }

    public static double pla3LowerBound(Node node, double[] queryTs) throws IOException {
        double sum = 0;
        short[] points = node.nodePoints;

        double[][] pla = CalcUtil.plaBySegments(queryTs, points);

        //for each segment
        for (int i = 0; i < pla.length; i++) {
            double tempDist = 0;
            //
            double maxLeft = node.nodeSegmentSketches[i].indicators[0];
            double minLeft = node.nodeSegmentSketches[i].indicators[1];
            double maxRight = node.nodeSegmentSketches[i].indicators[6];
            double minRight = node.nodeSegmentSketches[i].indicators[7];

            int len = node.getSegmentLength(i);
            double upperSlope = (maxRight - maxLeft) / (len -1);
            double lowerSlope = (minRight - minLeft) / (len -1);

            double maxSse = node.nodeSegmentSketches[i].indicators[4];
            double minSse = node.nodeSegmentSketches[i].indicators[5];

            //find the most nearest line

            double queryLeft = pla[i][1];
            double queryRight = pla[i][1] + pla[i][0] * (len -1);
            double querySlope = pla[i][0];
            double queryIntercept = pla[i][1];

            if (queryLeft >= maxLeft) {
                if (queryRight >= maxRight) {
                    tempDist += plaDistance(querySlope, queryIntercept, upperSlope, maxLeft, len);
                } else if (queryRight >= minRight) {
                    //calc the slope
                    double slope = querySlope + 3 * (queryIntercept - maxLeft) / (2 * len - 1);
                    tempDist += plaDistance(querySlope, queryIntercept, slope, maxLeft, len);
                } else //queryRight < minRight
                {
                    double slope = (minRight - maxLeft) / (len -1);
                    tempDist += plaDistance(querySlope, queryIntercept, slope, maxLeft, len);
                }
            } else if (queryLeft >= minLeft) {
                if (queryRight >= maxRight) {
                    double slope = querySlope - 3 * (queryRight - maxRight) / (2 * len - 1);
                    double intercept = maxRight - slope * (len -1);
                    //calc
                    tempDist += plaDistance(querySlope, queryIntercept, slope, intercept, len);
                } else if (queryRight >= minRight) {
                    tempDist += 0; //
                } else //queryRight < minRight
                {
                    double slope = querySlope - 3 * (queryRight - minRight) / (2 * len - 1);
                    double intercept = minRight - slope * (len -1);
                    //calc
                    tempDist += plaDistance(querySlope, queryIntercept, slope, intercept, len);
                }
            } else  //queryLeft < minLeft
            {
                if (queryRight >= maxRight) {
                    //calc the slope
                    double slope = (maxRight - minLeft) / (len -1);
                    tempDist += plaDistance(querySlope, queryIntercept, slope, minLeft, len);
                } else if (queryRight >= minRight) {
                    //calc the slope
                    double slope = querySlope + 3 * (queryIntercept - minLeft) / (2 * len - 1);
                    tempDist += plaDistance(querySlope, queryIntercept, slope, minLeft, len);
                } else //queryRight < minRight
                {
                    tempDist += plaDistance(querySlope, queryIntercept, lowerSlope, minLeft, len);
                }
            }

//            for (int j = 0; j < len; j++) {
//                //deal with pla line
//                double upper = upperSlope * j + maxIntercept;
//                double lower = lowerSlope * j + minIntercept;
//                double current = pla[i][0] * j + pla[i][1];
//                if (current >= upper)
//                    tempDist = tempDist + (current - upper) * (current - upper);
//                else if (current <= lower)
//                    tempDist = tempDist + (lower - current) * (lower - current);
//
//            }

//            deal with sse
            if (pla[i][2] >= maxSse) {
                double tmp = Math.sqrt(pla[i][2]) - Math.sqrt(maxSse);
                tempDist = tempDist + tmp * tmp;
            } else if (pla[i][2] <= minSse) {
                double tmp = Math.sqrt(pla[i][2]) - Math.sqrt(minSse);
                tempDist = tempDist + tmp * tmp;
            }
            sum += tempDist;
        }

        sum = Math.sqrt(sum);
        return sum;
    }

    public static double pla2LowerBound(Node node, double[] queryTs) throws IOException {
        double sum = 0;
        short[] points = node.nodePoints;

        double[][] pla = CalcUtil.plaBySegments(queryTs, points);

        //for each segment
        for (int i = 0; i < pla.length; i++) {
            double tempDist = 0;
            //
            double maxIntercept = node.nodeSegmentSketches[i].indicators[0];
            double minIntercept = node.nodeSegmentSketches[i].indicators[1];
            //calc upperSlope(maxSlope) and lowerSlope(minSlope)
            int len = node.getSegmentLength(i);
            double maxSlope = (node.nodeSegmentSketches[i].indicators[6] - maxIntercept) / (len - 1);
            double minSlope = (node.nodeSegmentSketches[i].indicators[7] - minIntercept) / (len - 1);

            double maxSse = node.nodeSegmentSketches[i].indicators[4];
            double minSse = node.nodeSegmentSketches[i].indicators[5];

            for (int j = 0; j < len; j++) {
                //deal with pla line
                double upper = maxSlope * j + maxIntercept;
                double lower = minSlope * j + minIntercept;
                double current = pla[i][0] * j + pla[i][1];
                if (current >= upper)
                    tempDist = tempDist + (current - upper) * (current - upper);
                else if (current <= lower)
                    tempDist = tempDist + (lower - current) * (lower - current);

            }
//            deal with sse
            if (pla[i][2] >= maxSse) {
                double tmp = Math.sqrt(pla[i][2]) - Math.sqrt(maxSse);
                tempDist = tempDist + tmp * tmp;
            } else if (pla[i][2] <= minSse) {
                double tmp = Math.sqrt(pla[i][2]) - Math.sqrt(minSse);
                tempDist = tempDist + tmp * tmp;
            }
            sum += tempDist;
        }

        sum = Math.sqrt(sum);
        return sum;
    }

    public static double pla2UpperBound(Node node, double[] queryTs) throws IOException {
        double sum = 0;
        short[] points = node.nodePoints;

        double[][] pla = CalcUtil.plaBySegments(queryTs, points);

        //for each segment
        for (int i = 0; i < pla.length; i++) {
            double tempDist = 0;
            //
            double maxIntercept = node.nodeSegmentSketches[i].indicators[0];
            double minIntercept = node.nodeSegmentSketches[i].indicators[1];
            //calc upperSlope(maxSlope) and lowerSlope(minSlope)
            int len = node.getSegmentLength(i);
            double maxSlope = (node.nodeSegmentSketches[i].indicators[6] - maxIntercept) / (len - 1);
            double minSlope = (node.nodeSegmentSketches[i].indicators[7] - minIntercept) / (len - 1);

            double maxSse = node.nodeSegmentSketches[i].indicators[4];
//            double minSse = node.nodeSegmentSketches[i].indicators[5];

            double distForTopPla = 0, distForBottomPla = 0;
            for (int j = 0; j < len; j++) {
                //deal with pla line
                double upper = maxSlope * j + maxIntercept;
                double lower = minSlope * j + minIntercept;
                double current = pla[i][0] * j + pla[i][1];
                distForTopPla += (upper - current) * (upper - current);
                distForBottomPla += (lower - current) * (lower - current);
            }

            tempDist = Math.max(distForTopPla, distForBottomPla);

//            deal with sse
//            if (pla[i][2] >= maxSse) {
//                double tmp = Math.sqrt(pla[i][2]) + Math.sqrt(maxSse);
//                tempDist = tempDist + tmp * tmp;
//            } else if (pla[i][2] <= minSse) {
//                double tmp = Math.sqrt(pla[i][2]) - Math.sqrt(minSse);
//                tempDist = tempDist + tmp * tmp;
//            }
            tempDist += 2 * (pla[i][2] + maxSse);
            sum += tempDist;
        }

        sum = Math.sqrt(sum);
        return sum;
    }

    public static double lowerBoundByAvgAndStd(Node node, double[] queryTs) throws IOException {
        return minDist(node, queryTs);
    }

    public static double upperBoundByAvgAndStd(Node node, double[] queryTs) throws IOException {
        return maxDist(node, queryTs);
    }

    public static double lowerBoundByAvg(Node node, double[] queryTs) throws IOException {
        return minDistByAvg(node, queryTs);
    }

    /**
     * using avg and std
     *
     * @param node
     * @param queryTs
     * @return
     * @throws IOException
     */
    public static double minDist(Node node, double[] queryTs) {
        double sum = 0;
        short[] points = node.nodePoints;
        double[] avg = CalcUtil.avgBySegments(queryTs, points);
        double[] stdDev = CalcUtil.devBySegments(queryTs, points);

        for (int i = 0; i < avg.length; i++) {
            //use mean and standardDeviation to estimate the distance
            double tempDist = 0;
            //stdDev out the range of min std and max std
            if ((stdDev[i] - node.nodeSegmentSketches[i].indicators[2]) * (stdDev[i] - node.nodeSegmentSketches[i].indicators[3]) > 0) {
                tempDist += Math.pow(Math.min(Math.abs(stdDev[i] - node.nodeSegmentSketches[i].indicators[2]), Math.abs(stdDev[i] - node.nodeSegmentSketches[i].indicators[3])), 2);
            }

            //avg out the range of min mean and max mean
            if ((avg[i] - node.nodeSegmentSketches[i].indicators[0]) * (avg[i] - node.nodeSegmentSketches[i].indicators[1]) > 0) {
                tempDist += Math.pow(Math.min(Math.abs(avg[i] - node.nodeSegmentSketches[i].indicators[0]), Math.abs(avg[i] - node.nodeSegmentSketches[i].indicators[1])), 2);
            }
            sum += tempDist * node.getSegmentLength(i);
        }
        sum = Math.sqrt(sum);
        return sum;
    }

    /**
     * use only avg
     *
     * @param node
     * @param queryTs
     * @return
     * @throws IOException
     */
    public static double minDistByAvg(Node node, double[] queryTs) throws IOException {
        double sum = 0;
        short[] points = node.nodePoints;
        double[] avg = CalcUtil.avgBySegments(queryTs, points);
        for (int i = 0; i < avg.length; i++) {
            double tempDist = 0;
            if ((avg[i] - node.nodeSegmentSketches[i].indicators[0]) * (avg[i] - node.nodeSegmentSketches[i].indicators[1]) > 0) {
                tempDist += Math.pow(Math.min(Math.abs(avg[i] - node.nodeSegmentSketches[i].indicators[0]), Math.abs(avg[i] - node.nodeSegmentSketches[i].indicators[1])), 2);
            }
            sum += tempDist * node.getSegmentLength(i);
        }
        sum = Math.sqrt(sum);
        return sum;
    }

    public static double maxDist(Node node, double[] queryTs) {
        double sum = 0;
        short[] points = node.nodePoints;
        double[] avg = CalcUtil.avgBySegments(queryTs, points);
        double[] stdDev = CalcUtil.devBySegments(queryTs, points);

        for (int i = 0; i < avg.length; i++) {
            double tempDist = 0;
            //using max std
            tempDist += Math.pow(stdDev[i] + node.nodeSegmentSketches[i].indicators[2], 2);
            //max of (avg to min mean and max mean)
            tempDist += Math.pow(Math.max(Math.abs(avg[i] - node.nodeSegmentSketches[i].indicators[0]), Math.abs(avg[i] - node.nodeSegmentSketches[i].indicators[1])), 2);
            sum += tempDist * node.getSegmentLength(i);
        }
        sum = Math.sqrt(sum);
        return sum;
    }

    public static double lowerBoundBy2Timeseries(double[] ts1, double[] ts2) {
        double avg1 = CalcUtil.avg(ts1);
        double stdDev1 = CalcUtil.deviation(ts1);
        double avg2 = CalcUtil.avg(ts2);
        double stdDev2 = CalcUtil.deviation(ts2);
        double lowerBound = Math.sqrt(ts1.length * (Math.pow(stdDev1 - stdDev2, 2) + Math.pow(avg1 - avg2, 2)));
        return lowerBound;
    }

    public static double upperBoundBy2Timeseries(double[] ts1, double[] ts2) {
        double avg1 = CalcUtil.avg(ts1);
        double stdDev1 = CalcUtil.deviation(ts1);
        double avg2 = CalcUtil.avg(ts2);
        double stdDev2 = CalcUtil.deviation(ts2);
        double upperBound = Math.sqrt(ts1.length * (Math.pow(stdDev1 + stdDev2, 2) + Math.pow(avg1 - avg2, 2)));
        return upperBound;
    }
}
