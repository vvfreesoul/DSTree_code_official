/**
 * 
 */
package cn.edu.fudan.cs.dstree.dynamicsplit;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import cn.edu.fudan.cs.dstree.util.CalcUtil;
import cn.edu.fudan.cs.dstree.util.TimeSeriesReader;

/**
 * @author admin
 *
 */
public class BestMatchingDistanceCalculator {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		String dataset = args[0];
		int numTs = Integer.parseInt(args[1]);
		int tsLen = Integer.parseInt(args[2]);
		int minSLen = Integer.parseInt(args[3]);
		int sLenStep = Integer.parseInt(args[4]);
		int maxSLen = Integer.parseInt(args[5]);
		int maxNodeSize = Integer.parseInt(args[6]);
		String readPath = args[7];
		String savePath = args[8];
		
//		String dataset = "pamap";
//		int numTs = 72;
//		int tsLen = 500;
//		int minSLen = 10;
//		int sLenStep = 10;
//		double maxSLenPercentage = 1.0 / 3;
//		String readPath = "D:\\Academic\\UCR_Dataset_Archive\\UCR_TS_Archive_2015";
//		String savePath = "D:\\Academic\\Shapelet\\Mine\\semi_supervised_shapelets\\greedy shapelet transform";
		
		for(int i = 0; i < args.length; i++){
			System.out.print(args[i] + " ");
		}
		System.out.println();
		
		//read the time series
		String tsFname = readPath + "\\" + dataset + "\\" + dataset + "_TRAIN";
		double[][] tss = new double[numTs][tsLen];
		TimeSeriesReader tsReader = new TimeSeriesReader(tsFname);
		tsReader.open();
		int i = 0;
		while(tsReader.hasNext())
			tss[i++] = tsReader.next_l();
		
		//construct matrix
//		int maxSLen = (int)Math.ceil(tsLen * maxSLenPercentage);
		String distFname = savePath + "\\dstree_nnDistMtx_all_train_" + dataset + "_" +  Integer.toString(minSLen) + "_" + Integer.toString(maxSLen) + "_" + Integer.toString(sLenStep);
		PrintWriter pw = new PrintWriter(new FileWriter(distFname));
		for(int ind = 0; ind < numTs; ind++){
			for(int sLen = minSLen; sLen <= maxSLen; sLen += sLenStep){
				System.out.printf("tsId = %d, sLen = %d\n", ind, sLen);
				double[] distVec = getDistVec(tss, ind, sLen, maxNodeSize);
				for(i = 0; i < distVec.length; i++)
					pw.printf("%f ", distVec[i]);
			}
			pw.println();
		}
		pw.close();
		System.out.println("Finished!");
	}
	
	public static double[] getDistVec(double[][] tss, int ind, int sLen, int maxNodeSize) throws IOException{	//get the distance matrix for all subsequences of a single time series
		double[][] sss = extractSubsequence(tss[ind], sLen);
		Node root = IndexBuilder.buildIndex(sss, 1, maxNodeSize);
		int numSTotal = tss.length * sss.length;
		double[] distVec = new double[numSTotal];
		for(int i = 0; i < tss.length; i++){
			if(i == ind){
				for(int j = 0; j < sss.length; j++)
					distVec[i * sss.length + j] = 0;
				continue;
			}
			double[][] curSSS = extractSubsequence(tss[i], sLen);
			for(int j = 0; j < sss.length; j++)
				distVec[i * sss.length + j] = IndexExactSearcher.distExactSearch(curSSS[j], root) / Math.sqrt(sLen);
		}
		return distVec;
	}
	
	public static double[][] extractSubsequence(double[] ts, int sLen){
		int numS = ts.length - sLen + 1;
		double[][] subsequences = new double[numS][sLen];
		for(int i = 0; i < numS; i++){
			for(int j = 0; j < sLen; j++)
				subsequences[i][j] = ts[i+j];
			subsequences[i] = CalcUtil.z_Normalize(subsequences[i]);
		}
		return subsequences;
	}

}
