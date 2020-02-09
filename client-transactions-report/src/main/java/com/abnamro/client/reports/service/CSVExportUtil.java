package com.abnamro.client.reports.service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CSVExportUtil {
  
	private static final Logger logger = LoggerFactory.getLogger(CSVExportUtil.class);
	
	//Can be configured filename in application.properties file
	private final String SUMMARY_OUTPUT_FILE = "Output.csv";
	
	public void generateCSVReport(List<String[]> dataLines) {
		logger.info("Generating report with data lines");
	    File csvOutputFile = new File(SUMMARY_OUTPUT_FILE);
	    try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
	    	dataLines.stream()
	        .map(this::convertToCSV)
	        .forEach(pw::println);
	    } catch(IOException exp) {
	    	logger.error("error opening or writing the output file " + exp);
	    }
	}
	
	private String convertToCSV(String[] data) {
	    return Stream.of(data)
	      .map(this::escapeSpecialCharacters)
	      .collect(Collectors.joining(","));
	}
	
	private String escapeSpecialCharacters(String data) {
	    String escapedData = data.replaceAll("\\R", " ");
	    if (data.contains(",") || data.contains("\"") || data.contains("'")) {
	        data = data.replace("\"", "\"\"");
	        escapedData = "\"" + data + "\"";
	    }
	    return escapedData;
	}
	
}
