package cn.edu.fudan.cs.dstree.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: wangyang
 * Date: 11-6-21
 * Time: 下午7:40
 * To change this template use File | Settings | File Templates.
 */
public class RandomSeriesGenerator {
    public List<SeriesGenerator> generators = new ArrayList<SeriesGenerator>();

    public void addGenerator(SeriesGenerator generator)
    {
        generators.add(generator);
    }

    double[] generate()
    {
        int p = Utils.random(0,generators.size()-1);

        SeriesGenerator seriesGenerator = generators.get(p);
        return seriesGenerator.generate();
    }

    public RandomSeriesGenerator() {
    }

    public static void main(String[] args) {
        int seriesLength = 1024;

        RandomSeriesGenerator randomSeriesGenerator = new RandomSeriesGenerator();
        SineGenerator sineGenerator = new SineGenerator(seriesLength,2,10,2,10,-5,5);
        randomSeriesGenerator.addGenerator(sineGenerator);

        GaussianGenerator gaussianGenerator = new GaussianGenerator(seriesLength,-5,5,0,2);
        randomSeriesGenerator.addGenerator(gaussianGenerator);

        RandomWalkGenerator randomWalkGenerator = new RandomWalkGenerator(seriesLength,-5,5,0,2);
        randomSeriesGenerator.addGenerator(randomWalkGenerator);

        SegmentGaussianGenerator segmentGaussianGenerator = new SegmentGaussianGenerator(seriesLength,3,10,-5,5,0,2);
        randomSeriesGenerator.addGenerator(segmentGaussianGenerator);

        for (int i = 0; i < 1000*1000; i++) {
            double[] series = randomSeriesGenerator.generate();

        }
    }

}
