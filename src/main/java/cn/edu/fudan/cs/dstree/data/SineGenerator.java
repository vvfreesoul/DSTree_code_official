package cn.edu.fudan.cs.dstree.data;

/**
 * Created by IntelliJ IDEA.
 * User: wangyang
 * Date: 11-6-21
 * Time: 下午7:33
 * To change this template use File | Settings | File Templates.
 */
public class SineGenerator implements SeriesGenerator {
    int length;

    double minFrequency;
    double maxFrequency;

    double minAmplitude;
    double maxAmplitude;

    double minMean;
    double maxMean;

    public SineGenerator(int length, double minFrequency, double maxFrequency, double minAmplitude, double maxAmplitude, double minMean, double maxMean) {
        this.length = length;
        this.minFrequency = minFrequency;
        this.maxFrequency = maxFrequency;
        this.minAmplitude = minAmplitude;
        this.maxAmplitude = maxAmplitude;
        this.minMean = minMean;
        this.maxMean = maxMean;
    }

    public double[] generate() {
        double[] timeSeries = new double[length];
        double frequency = Utils.random(minFrequency, maxFrequency);
        double amplitude = Utils.random(minAmplitude, maxAmplitude);
        double mean = Utils.random(minMean, maxMean);
        double phase = Utils.random(0, 2 * Math.PI);
        for (int i = 0; i < timeSeries.length; i++) {
            timeSeries[i] = mean + amplitude * Math.sin(2 * i * (Math.PI / timeSeries.length) * frequency + phase);
        }

        return timeSeries;
    }

    public static void main(String[] args) {
        SineGenerator sineGenerator = new SineGenerator(10, 0.1, 0.5, 2, 5, 0, 5);
        for (int j = 0; j < 10; ++j) {
            double[] tempTs = sineGenerator.generate();
            for (int i = 0; i < tempTs.length; i++) {
                double tempT = tempTs[i];
                System.out.print(tempT + " ");
            }
            System.out.println();
        }
    }
}
