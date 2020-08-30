/**
 * 
 */
package com.nvn.csv;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author naveenkumar
 *
 */
public class CSVUtils {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filePath ="C:\\Users\\naveenkumar\\eclipse-workspace\\Tracker\\logs\\From_server\\data_13jul.csv";
		String filePath2 ="C:\\Users\\naveenkumar\\eclipse-workspace\\Tracker\\logs\\From_server\\data_13jul_nv.csv";
		String outCSV ="C:\\Users\\naveenkumar\\eclipse-workspace\\Tracker\\logs\\From_server\\compare.csv";

		if(args==null || args.length !=3) {
			System.out.println("Argument missing. Example of usage: java jar <jar-name> <param1:  path to csv file 1> <param2: path to csv file 2> <Path to comparision output csv file>");
		return ;
		}
		filePath=args[0];
		filePath2=args[1];
		outCSV=args[2]; 

		try {
			 compareCsvData(filePath, filePath2, outCSV);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static List<String> readFilesToList(String filePath) throws IOException{
		List<String> allLines = Files.readAllLines(Paths.get(filePath));
		return allLines;

	}
	
	private static Map<String, List<String>>  parseCSV(String filePath) throws IOException {
		List<String> allLines = readFilesToList(filePath);
		Map<String, List<String>> csvData = new HashMap<String, List<String>>();

		allLines.stream().forEach((line) ->{
			String[] columns  =null;
			List<String> colsList = null;
			//System.out.println(line);
			if(line.contains("\",\"")) {
				line=line.substring(1, line.length()-2);

			 columns = line.split("\",\"");	
			}else {
			columns = line.split(",");	
			}
			if(null!=columns) {
				//System.out.println(line);
			colsList = Arrays.asList(columns);	
			//System.out.println(colsList.get(0));
					csvData.put(colsList.get(0),colsList);
			}else {
				System.out.println("columns is null");
			}
			});	
		
		return csvData;
	}
	private static void  compareCsvData(String leftFilePath, String rightFilePath, String outFile) throws IOException {
		System.out.println("Comparision started. ");
		Map<String, List<String>> leftCSV = parseCSV(leftFilePath);
		System.out.println("No of rows in File 1 : " +leftCSV.size());
		Map<String, List<String>> rightMapSV = parseCSV(rightFilePath);
		System.out.println("No of rows in File 2 : " +rightMapSV.size());
		List<List<String>> outCSV = new ArrayList<List<String>> ();
		leftCSV.entrySet().stream().forEach((val) -> {
			List<String> left = val.getValue();
			List<String> right = ( rightMapSV.get(val.getKey()) !=null)?rightMapSV.get(val.getKey()):null;
			if(left!=null) {
				
				if(!left.equals(right) || right ==null){
					//System.out.println("Mismatch : "+left);
					//System.out.println("Mismatch : "+right);

					List<String> res = new ArrayList<String>();
					res.add(left.get(0));
					for(int i =1;i<left.size();i++) {
						res.add(left.get(i));
						if(right!=null) {
						res.add(right.get(i));
						}else {
						res.add("~NA~");

						}
					}	
					outCSV.add(res);
				}
			}

			
		});
		System.out.println("Comparision done. Result :"+ outCSV.size()+" descrepencies");
		System.out.println("Going to write into file. "+ outFile);
		listToCSV(outCSV, outFile);

		
	}
	public static void listToCSV(List<List<String>> outCSV, String outFile) {
		if(null==outCSV) {
			return;
		}
	    try {
			FileWriter writer = new FileWriter(outFile );
		outCSV.stream().forEach((list) -> {
			if(null!=list) {				 
				    String collect = list.stream().collect(Collectors.joining(",")) +System.lineSeparator();

				    try {
						writer.write(collect);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
 			}
			
		});
	    writer.close();
		System.out.println("Writing into file has been completed. "+ outFile);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
