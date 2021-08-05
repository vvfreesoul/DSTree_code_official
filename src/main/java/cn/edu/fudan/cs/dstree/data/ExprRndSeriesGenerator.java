package cn.edu.fudan.cs.dstree.data;

import cn.edu.fudan.cs.dstree.util.CalcUtil;
import cn.edu.fudan.cs.dstree.util.TimeSeriesFileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: wangyang
 * Date: 12-3-4
 * Time: 下午2:50
 * To change this template use File | Settings | File Templates.
 * save the random series into file for experiment,length = 1204
 */
public class ExprRndSeriesGenerator {
    public static void main(String[] args) throws IOException {
        String filePath = "G:\\dstree_data\\";
        if (args.length > 0) {
            filePath = args[0];
        }

        int seriesLength = 64;
        if (args.length > 1) {
            seriesLength = Integer.parseInt(args[1]);
        }

        int size = 300;
        if (args.length > 2) {
            size = Integer.parseInt(args[2]);
        }

        String fileName = filePath +"Series_" + seriesLength + "_" +size + ".txt.z";
        System.out.println("fileNames = " + fileName);
        File file = new File(fileName);
        if (file.exists())
            file.delete();

        RandomSeriesGenerator randomSeriesGenerator = new RandomSeriesGenerator();

//        GaussianGenerator gaussianGenerator = new GaussianGenerator(seriesLength,-50,50,0,10);
//        randomSeriesGenerator.addGenerator(gaussianGenerator);

        RandomWalkGenerator randomWalkGenerator = new RandomWalkGenerator(seriesLength,-50,50,0,10);
        randomSeriesGenerator.addGenerator(randomWalkGenerator);
        randomSeriesGenerator.addGenerator(randomWalkGenerator);
        randomSeriesGenerator.addGenerator(randomWalkGenerator);
        randomSeriesGenerator.addGenerator(randomWalkGenerator);

//        SegmentGaussianGenerator segmentGaussianGenerator = new SegmentGaussianGenerator(seriesLength,3,10,-50,50,0,10);
//        randomSeriesGenerator.addGenerator(segmentGaussianGenerator);
//        randomSeriesGenerator.addGenerator(segmentGaussianGenerator);
//        randomSeriesGenerator.addGenerator(segmentGaussianGenerator);

        SineGenerator sineGenerator = new SineGenerator(seriesLength,2,20,2,20,-50,50);
        randomSeriesGenerator.addGenerator(sineGenerator);
        randomSeriesGenerator.addGenerator(sineGenerator);

        FileOutputStream fos = new FileOutputStream(fileName);

        int batchWriteSize = 10000;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            double[] series = randomSeriesGenerator.generate();
            series = CalcUtil.z_Normalize(series);
            sb.append(TimeSeriesFileUtil.timeSeries2Line(series));
            if (i % batchWriteSize == batchWriteSize - 1)
            {
                System.out.println("i = " + i);
                fos.write(sb.toString().getBytes());
                fos.flush();
                sb = new StringBuilder();
            }
        }
        if (sb.length() > 0)
            fos.write(sb.toString().getBytes());

        fos.flush();
        fos.close();
    }
}
