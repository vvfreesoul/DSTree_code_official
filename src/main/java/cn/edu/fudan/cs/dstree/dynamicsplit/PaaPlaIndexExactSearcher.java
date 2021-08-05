package cn.edu.fudan.cs.dstree.dynamicsplit;


import cn.edu.fudan.cs.dstree.util.CalcUtil;
import cn.edu.fudan.cs.dstree.util.DistUtil;
import cn.edu.fudan.cs.dstree.util.TimeSeriesFileUtil;
import cn.edu.fudan.cs.dstree.util.TimeSeriesReader;
import org.apache.commons.lang.time.StopWatch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.Date;
import java.util.PriorityQueue;

/**
 * Created by IntelliJ IDEA.
 * User: ZhuShengqi
 * Date: 11-7-18
 * Time: 下午10:18
 * To change this template use File | Settings | File Templates.
 */
public class PaaPlaIndexExactSearcher {
    //classic exactFile
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("usage: java -cp uber-dstree-version.jar cn.edu.fudan.cs.dstree.dynamicsplit.IndexExactSearcher searchfilename indexdir");
        System.out.println("eg: java -cp uber-dstree-1.0-SNAPSHOT.jar cn.edu.fudan.cs.dstree.dynamicsplit.IndexExactSearcher data\\Series_64_1000000.z.search.txt data\\Series_64_1000000.z.txt.idx_dyn_100_1");

        String searchFileName = "G:\\dstree_data\\Series_64_1000000.z.1.search.txt";
        if (args.length >= 1)
            searchFileName = args[0];

        System.out.println("searchFileName = " + searchFileName);

        String indexPath = "G:\\dstree_data\\Series_64_1000000.z.1.txt.idx_dynpla_100_1";
//        String indexPath = "data\\Series_64_1000000.z.txt.idx_dyn_100_1_10000";
        if (args.length >= 2)
            indexPath = args[1];

        System.out.println("indexPath = " + indexPath);

        search(searchFileName, indexPath);

    }

    public static Node approximateSearch(double[] queryTs, Node currentNode) {
        if (currentNode.isTerminal()) {
            return currentNode;
        } else //internal node
        {
            if (currentNode.splitPolicy.routeToLeft(queryTs))
                return approximateSearch(queryTs, currentNode.left);
            else
                return approximateSearch(queryTs, currentNode.right);
        }
    }

    public static void search(String searchFileName, String indexPath)
            throws IOException, ClassNotFoundException {
        String resultFileString = indexPath + ".search.log.txt";

        calcDistCount = 0;
        leafCount = 0;

        PrintWriter pw = new PrintWriter(new FileWriter(resultFileString));

        pw.println("indexPath = " + indexPath);
        File file = new File(indexPath);
        Node newRoot;
        if (file.exists()) {
            String indexFileName = indexPath + "\\" + "root.idx";
            System.out.println("reading idx fileName..." + indexFileName);
            pw.println("reading idx fileName..." + indexFileName);
//            FileInputStream fis = new FileInputStream(indexFileName);
//            ObjectInputStream ios = new ObjectInputStream(fis);
//            newRoot = (Node) ios.readObject();
            newRoot = Node.loadFromFile(indexFileName);
        } else {
            System.out.println("indexPath not exists! " + indexPath);
            pw.println("indexPath not exists! " + indexPath);
            return;
        }

        int tsLength = TimeSeriesFileUtil.getTimeSeriesLength(searchFileName);
        System.out.println("tsLength = " + tsLength);
        pw.println("tsLength = " + tsLength);

        System.out.println("threshold = " + newRoot.threshold);
        pw.println("threshold = " + newRoot.threshold);
//        newRoot.printTreeInfo();

        int totalTsCount = newRoot.getSize();
        System.out.println("totalTsCount = " + totalTsCount);
        pw.println("totalTsCount = " + totalTsCount);

        pw.println("---------------Exact search-----------------" + new Date());
        int searchCount = (int) TimeSeriesFileUtil.getTimeSeriesCount(new File(searchFileName));
        System.out.println("searchCount = " + searchCount);
        pw.println("searchCount = " + searchCount);
        double[] leafCounts = new double[searchCount];
        double[] proneRatios = new double[searchCount];

        TimeSeriesReader timeSeriesReader = new TimeSeriesReader(searchFileName);
        timeSeriesReader.open();
        int c = 0;

        totalTime.reset();
        totalTime.start();
        totalTime.suspend();
        ioTime.reset();
        ioTime.start();
        ioTime.suspend();
        approTime.reset();
        approTime.start();
        approTime.suspend();

        while (timeSeriesReader.hasNext()) {
            c++;
            System.out.println("************   " + (c) + "   ******************" + new Date());
            pw.println("************   " + c + "   ******************" + new Date());
            double[] queryTs = timeSeriesReader.next();
            totalTime.resume();
            Node node = exactSearch(queryTs, newRoot);
            totalTime.suspend();
            System.out.println("node.level = " + node.level);
            System.out.println("node.size = " + node.size);
            leafCounts[c - 1] = leafCount;
            proneRatios[c - 1] = (1 - calcDistCount * 1.0 / totalTsCount);
            System.out.println("leafCount = " + leafCount);
            pw.println("leafCount = " + leafCount);
            System.out.println("calcDistCount = " + calcDistCount);
            pw.println("calcDistCount = " + calcDistCount);
            System.out.println("proneRatios[c-1] = " + proneRatios[c - 1]);
            pw.println("proneRatios[c-1] = " + proneRatios[c - 1]);
        }
        timeSeriesReader.close();

        totalTime.stop();
        ioTime.stop();
        approTime.stop();

        System.out.println("*********** statistics for exact search **********");
        pw.println("avg(leafCounts) = " + CalcUtil.avg(leafCounts));
        System.out.println("avg(leafCounts) = " + CalcUtil.avg(leafCounts));
        pw.println("avg(proneRatios) = " + CalcUtil.avg(proneRatios));
        System.out.println("avg(proneRatios) = " + CalcUtil.avg(proneRatios));
        pw.println("ExactSearch TotalTime = " + totalTime.getTime() / 1000 + "s");
        System.out.println("ExactSearch TotalTime = " + totalTime.getTime() / 1000 + "s");
        pw.println("ExactSearch ApproximateSearchTime = " + approTime.getTime() / 1000 + "s");
        System.out.println("ExactSearch ApproximateSearchTime = " + approTime.getTime() / 1000 + "s");
        pw.println("ExactSearch IOTime = " + ioTime.getTime() / 1000 + "s");
        System.out.println("ExactSearch IOTime = " + ioTime.getTime() / 1000 + "s");
        long locationTime = (totalTime.getTime() - ioTime.getTime()) / 1000;
        pw.println("ExactSearch LocationTime = " + locationTime + "s");
        System.out.println("ExactSearch LocationTime = " + locationTime + "s");
        pw.close();

        System.out.println("distCheckSum = " + distCheckSum);
    }

    static int calcDistCount = 0;
    static int leafCount = 0;
    static StopWatch totalTime = new StopWatch();
    static StopWatch ioTime = new StopWatch();
    static StopWatch approTime = new StopWatch();

    private static double pla3LowerBound(Node node, double[] queryTs) {
        double sum = 0;
        short[] points = node.nodePoints;

        double[][] pla = CalcUtil.plaBySegments(queryTs, points);

        //for each segment
        for (int i = 0; i < pla.length; i++) {
            double tempDist = 0;
            //
            double maxLeft = node.nodeSegmentSketches[i].indicators[4];
            double minLeft = node.nodeSegmentSketches[i].indicators[5];
            double maxRight = node.nodeSegmentSketches[i].indicators[10];
            double minRight = node.nodeSegmentSketches[i].indicators[11];

            int len = node.getSegmentLength(i);
            double upperSlope = (maxRight - maxLeft) / (len -1);
            double lowerSlope = (minRight - minLeft) / (len -1);

            double maxSse = node.nodeSegmentSketches[i].indicators[8];
            double minSse = node.nodeSegmentSketches[i].indicators[9];

            //find the most nearest line

            double queryLeft = pla[i][1];
            double queryRight = pla[i][1] + pla[i][0] * (len -1);
            double querySlope = pla[i][0];
            double queryIntercept = pla[i][1];

            if (queryLeft >= maxLeft) {
                if (queryRight >= maxRight) {
                    tempDist += DistTools.plaDistance(querySlope, queryIntercept, upperSlope, maxLeft, len);
                } else if (queryRight >= minRight) {
                    //calc the slope
                    double slope = querySlope + 3 * (queryIntercept - maxLeft) / (2 * len - 1);
                    tempDist += DistTools.plaDistance(querySlope, queryIntercept, slope, maxLeft, len);
                } else //queryRight < minRight
                {
                    double slope = (minRight - maxLeft) / (len -1);
                    tempDist += DistTools.plaDistance(querySlope, queryIntercept, slope, maxLeft, len);
                }
            } else if (queryLeft >= minLeft) {
                if (queryRight >= maxRight) {
                    double slope = querySlope - 3 * (queryRight - maxRight) / (2 * len - 1);
                    double intercept = maxRight - slope * (len -1);
                    //calc
                    tempDist += DistTools.plaDistance(querySlope, queryIntercept, slope, intercept, len);
                } else if (queryRight >= minRight) {
                    tempDist += 0; //
                } else //queryRight < minRight
                {
                    double slope = querySlope - 3 * (queryRight - minRight) / (2 * len - 1);
                    double intercept = minRight - slope * (len -1);
                    //calc
                    tempDist += DistTools.plaDistance(querySlope, queryIntercept, slope, intercept, len);
                }
            } else  //queryLeft < minLeft
            {
                if (queryRight >= maxRight) {
                    //calc the slope
                    double slope = (maxRight - minLeft) / (len -1);
                    tempDist += DistTools.plaDistance(querySlope, queryIntercept, slope, minLeft, len);
                } else if (queryRight >= minRight) {
                    //calc the slope
                    double slope = querySlope + 3 * (queryIntercept - minLeft) / (2 * len - 1);
                    tempDist += DistTools.plaDistance(querySlope, queryIntercept, slope, minLeft, len);
                } else //queryRight < minRight
                {
                    tempDist += DistTools.plaDistance(querySlope, queryIntercept, lowerSlope, minLeft, len);
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

    private static double lowerBound(Node node, double[] queryTs)
    {
        if (node.isTerminal())
        {
            double lowerBoundByPaa = DistTools.minDist(node, queryTs);
//            return lowerBoundByPaa;
            double lowerBoundByPla = pla3LowerBound(node,queryTs);
//            return lowerBoundByPla;
//            if (lowerBoundByPla > lowerBoundByPaa)
//            {
//                System.out.println("lowerPla > lowerPaa");
//                System.out.println("lowerBoundByPla = " + lowerBoundByPla);
//                System.out.println("lowerBoundByPaa = " + lowerBoundByPaa);
//            }
//            else
//            {
//                System.out.println("lowerPla < lowerPaa");
//                System.out.println("lowerBoundByPla = " + lowerBoundByPla);
//                System.out.println("lowerBoundByPaa = " + lowerBoundByPaa);
//            }

            return Math.max(lowerBoundByPaa,lowerBoundByPla);
        }
        else
            return DistTools.minDist(node, queryTs);
    }

    public static Node exactSearch(double[] queryTs, Node root) throws IOException {
        leafCount = 0;
        calcDistCount = 0;
        int processTerminalCount = 0;
        approTime.resume();
        Node approxNode = approximateSearch(queryTs, root);
        approTime.suspend();
        //get estimate lower bound from root
        PqItem bsfAnswer = new PqItem();
        bsfAnswer.node = approxNode;
        bsfAnswer.dist = DistUtil.minDistBinary(approxNode.getFileName(), queryTs);

        System.out.println("bsf.node = " + bsfAnswer.node.getFileName());
        System.out.println("bsf.dist = " + bsfAnswer.dist);
        //initialize priority queue;
        Comparator<PqItem> comparator = new DistComparator();
        PriorityQueue<PqItem> pq = new PriorityQueue<PqItem>(256, comparator);

        //initialize the priority queue
        PqItem tempItem = new PqItem();
        tempItem.node = root;
        tempItem.dist = lowerBound(root, queryTs);
        pq.add(tempItem);

        //process the priority queue
        PqItem minPqItem;
        while (!pq.isEmpty()) {
            minPqItem = pq.remove();
//            System.out.println("minPqItem.node.getFileName() = " + minPqItem.node.getFileName());
//            System.out.println("minPqItem.dist = " + minPqItem.dist);
            if (minPqItem.dist > bsfAnswer.dist) break;
            if (minPqItem.node.isTerminal()) {
                leafCount++;
                //verify the true distance,replace the estimate with the true dist
                calcDistCount += minPqItem.node.getSize();
                processTerminalCount++;
//                ioTime.resume();
//                minPqItem.dist = DistTools.IndexDist(minPqItem.node, queryTs);

                String fileName = minPqItem.node.getFileName();
                ioTime.resume();
//                double[][] tss = TimeSeriesFileUtil.readSeriesFromFileAtOnce(fileName);
                double[][] tss = TimeSeriesFileUtil.readSeriesFromBinaryFileAtOnce(fileName, queryTs.length);
//                double[][] tss = TimeSeriesFileUtil.readSeriesFromFile(fileName);
                ioTime.suspend();
                minPqItem.dist = DistUtil.minDist(tss, queryTs);

                //EuclideanCounter += minPqItem.node.amount;
                if (bsfAnswer.dist >= minPqItem.dist) {
                    bsfAnswer.dist = minPqItem.dist;
                    bsfAnswer.node = minPqItem.node;
                }
            } else {     //minPqItem is internal
                //for left
                tempItem = new PqItem();
                tempItem.node = minPqItem.node.left;
                tempItem.dist = lowerBound(tempItem.node, queryTs);

                if (tempItem.dist < bsfAnswer.dist)
                    pq.add(tempItem);

                //for right
                tempItem = new PqItem();
                tempItem.node = minPqItem.node.right;
                tempItem.dist = lowerBound(tempItem.node, queryTs);

                if (tempItem.dist < bsfAnswer.dist)
                    pq.add(tempItem);
            }
        }
        System.out.println("exact search node = " + bsfAnswer.node.getFileName());
        System.out.println("exact search node = " + bsfAnswer.node.getFileName());
        System.out.println("exact search dist = " + bsfAnswer.dist);
        distCheckSum += bsfAnswer.dist;
        System.out.println("calcDistCount = " + calcDistCount);
        System.out.println("processTerminalCount = " + processTerminalCount);
        return bsfAnswer.node;
    }

    static double distCheckSum = 0;

}
