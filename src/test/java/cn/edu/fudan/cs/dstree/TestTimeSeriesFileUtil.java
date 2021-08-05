package cn.edu.fudan.cs.dstree;

import cn.edu.fudan.cs.dstree.util.TimeSeriesFileUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: wangyang
 * Date: 13-11-17
 * Time: 下午2:36
 * To change this template use File | Settings | File Templates.
 */
public class TestTimeSeriesFileUtil {
    String txtFileName = "data\\Series_64_1000000.z.txt";
    String binFileName = "data\\Series_64_1000000.z.bin";
    int tsLength = 64;
    StopWatch sw = new StopWatch();

    @Test
    public void testTxtFile2BinFile() throws IOException {
        sw.start();
        TimeSeriesFileUtil.txtFile2BinFile(txtFileName, binFileName);
        sw.suspend();
        System.out.println("sw.getTime() = " + sw.getTime());
    }

    @Test
    public void testReadTxtFile() throws IOException {
        System.out.println("TestTimeSeriesFileUtil.testReadTxtFile");
        sw.start();
        double[][] tss = TimeSeriesFileUtil.readSeriesFromFileAtOnce(txtFileName);
        sw.suspend();
        System.out.println("tss.length = " + tss.length);
        System.out.println("sw.getTime() = " + sw.getTime());
    }

    @Test
    public void testReadBinFile() throws IOException {
        System.out.println("TestTimeSeriesFileUtil.testReadBinFile");
        sw.start();
        double[][] tss = TimeSeriesFileUtil.readSeriesFromBinaryFileAtOnce(binFileName, tsLength);
        sw.suspend();
        System.out.println("tss.length = " + tss.length);
        System.out.println("sw.getTime() = " + sw.getTime());
    }
}
