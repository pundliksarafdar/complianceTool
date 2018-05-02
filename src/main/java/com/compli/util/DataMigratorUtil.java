package com.compli.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DataMigratorUtil {
	
	public static List<List<String>> readFile() throws FileNotFoundException{
		List<List<String>> vvBeans = null;
		ObjectMapper mapper = new ObjectMapper();
		InputStream inputStream = new FileInputStream(new File("G:\\corex\\ComplianceTool\\document\\DummyData1.csv"));//Thread.currentThread().getContextClassLoader().getSystemResourceAsStream("G:\\corex\\ComplianceTool\\document\\DummyData1.csv");
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(inputStreamReader);
		try {
			String jsonStr = "";
			for (String line; (line = reader.readLine()) != null;) {
				List list = Arrays.asList(line.split(","));
				System.out.println(list);
			}						
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			return vvBeans;
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		readFile();
	}
}
