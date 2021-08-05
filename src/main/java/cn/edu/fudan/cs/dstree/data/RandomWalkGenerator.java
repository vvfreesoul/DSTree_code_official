package cn.edu.fudan.cs.dstree.data;

/**
 * Created by IntelliJ IDEA.
 * User: wangyang
 * Date: 11-6-21
 * Time: 下午7:36
 * To change this template use File | Settings | File Templates.
 */
public class RandomWalkGenerator implements SeriesGenerator{
    int length;

    double minStart;
    double maxStart;

    double minStep;
    double maxStep;

    public RandomWalkGenerator(int length, double minStart, double maxStart, double minStep, double maxStep) {
        this.length = length;
        this.minStart = minStart;
        this.maxStart = maxStart;
        this.minStep = minStep;
        this.maxStep = maxStep;
    }

    public double[] generate() {
        double[] timeSeries = new double[length];
        timeSeries[0] = Utils.random(minStart, maxStart);

        for (int i = 1; i < timeSeries.length; i++) {
            double sign = Math.random() < 0.5 ? -1 : 1;
            double step = Math.random() * Utils.random(minStep, maxStep);
            timeSeries[i] = timeSeries[i - 1] + sign * step;
        }
        return timeSeries;
    }

    public static void main(String[] args) {
        RandomWalkGenerator randomWalkGenerator = new RandomWalkGenerator(10, 0, 4, 1, 3);
        for (int j = 0; j < 10; ++j) {
            double[] tempTs = randomWalkGenerator.generate();
            for (int i = 0; i < tempTs.length; i++) {
                double tempT = tempTs[i];
                System.out.print(tempT + " ");
            }
            System.out.println();
        }
    }
}
