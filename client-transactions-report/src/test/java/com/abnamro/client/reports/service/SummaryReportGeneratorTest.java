package com.abnamro.client.reports.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.abnamro.client.reports.models.ClientTransactionsModel;

public class SummaryReportGeneratorTest {
	
	    @Autowired
	    SummaryReportGenerator summaryReportGenerator;

	    @DisplayName("Test No transaction data in file")
	    @Test
	    void testNoTransactionDatainFile() {
		    Exception exception = assertThrows(NullPointerException.class, () -> {
	    		String transactionDate = "";
	        	List<ClientTransactionsModel> dailyTransactionsList = new ArrayList<>();
	        	summaryReportGenerator.generateDailySummaryReport(dailyTransactionsList, transactionDate);
	    	});
	        assertEquals(null, exception.getMessage()); 
	    }
}
