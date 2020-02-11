package com.abnamro.client.reports.service;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TransactionsReaderTest {
	
    @Autowired
    TransactionsReader transactionsReader;
    
    @DisplayName("Test input File and config file not present")
    @Test
    void testInputFileAndConfigFileNotPresent() {
    	
    	Exception exception = assertThrows(IOException.class, () -> {
    		String inputFileName = "";
        	String configFileName = "";
            transactionsReader.getFutureTransactions(inputFileName, configFileName);
    	});
        assertEquals(" (No such file or directory)", exception.getMessage());
        assertTrue(exception.getMessage().contains("file or directory"));
    }
    
    @DisplayName("Test input File not present")
    @Test
    void testInputFileNotPresent() {
    	
    	Exception exception = assertThrows(IOException.class, () -> {
    		String inputFileName = "";
        	String configFileName = "future-transactions-fixedlength.pzmap.xml";
            transactionsReader.getFutureTransactions(inputFileName, configFileName);
    	});
        assertEquals(" (No such file or directory)", exception.getMessage());
        assertTrue(exception.getMessage().contains("file or directory"));
    }
    
    
    @DisplayName("Test config file not present")
    @Test
    void testConfigFileNotPresent() {
    	
    	Exception exception = assertThrows(IOException.class, () -> {
    		String inputFileName = "Input.txt";
        	String configFileName = "";
            transactionsReader.getFutureTransactions(inputFileName, configFileName);
    	});
        assertEquals(" (No such file or directory)", exception.getMessage());
        assertTrue(exception.getMessage().contains("file or directory"));
    }
    
    @DisplayName("Test Wrong input file format")
    @Test
    void testInputFileWrongFormat() {
    	
    	Exception exception = assertThrows(IOException.class, () -> {
    		String inputFileName = "Input.txtsls";
        	String configFileName = "";
            transactionsReader.getFutureTransactions(inputFileName, configFileName);
    	});
        assertEquals(" (No such file or directory)", exception.getMessage());
        assertTrue(exception.getMessage().contains("file or directory"));
    }
    
    @DisplayName("Test Wrong config file format")
    @Test
    void testConfigFileWrongFormat() {
    	
    	Exception exception = assertThrows(IOException.class, () -> {
    		String inputFileName = "Input.txts";
        	String configFileName = "future-transactions-fixedlength.pzmap.txt";
            transactionsReader.getFutureTransactions(inputFileName, configFileName);
    	});
        assertEquals("future-transactions-fixedlength.pzmap.txt (No such file or directory)", exception.getMessage());
        assertTrue(exception.getMessage().contains("file or directory"));
    }

}
