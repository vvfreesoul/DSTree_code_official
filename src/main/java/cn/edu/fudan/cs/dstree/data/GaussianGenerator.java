package cn.edu.fudan.cs.dstree.data;

/**
 * Created by IntelliJ IDEA.
 * User: wangyang
 * Date: 11-6-21
 * Time: 下午7:36
 * To change this template use File | Settings | File Templates.
 */
public class GaussianGenerator implements SeriesGenerator {
    int length;

    double minMean;
    double maxMean;

    double minStd;
    double maxStd;

    public GaussianGenerator(int length, double minMean, double maxMean, double minStd, double maxStd) {
        this.length = length;
        this.minMean = minMean;
        this.maxMean = maxMean;
        this.minStd = minStd;
        this.maxStd = maxStd;
    }

    public double[] generate() {
        double mean = Utils.random(minMean, maxMean);
        double std = Utils.random(minStd, maxStd);
        V1 = 0;
        V2 = 0;
        S = 0;
        phase = 0;

        return generate(this.length, mean, std);
    }

    public static double[] generate(int length, double mean, double std) {
        double[] timeSeries = new double[length];

        for (int i = 0; i < timeSeries.length; i++) {
            timeSeries[i] = mean + std * gaussrand();
        }

        return timeSeries;
    }

    static double V1 = 0, V2 = 0, S = 0;
    static int phase = 0;

    public static double gaussrand() {
        double X;

        if (phase == 0) {
            do {
                double U1 = (double) Math.random() / 1;
                double U2 = (double) Math.random() / 1;

                V1 = 2 * U1 - 1;
                V2 = 2 * U2 - 1;
                S = V1 * V1 + V2 * V2;
            } while (S >= 1 || S == 0);

            X = V1 * Math.sqrt(-2 * Math.log(S) / S);
        } else
            X = V2 * Math.sqrt(-2 * Math.log(S) / S);

        phase = 1 - phase;

        return X;
    }
}
