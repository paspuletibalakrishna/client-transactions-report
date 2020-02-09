package com.abnamro.client.reports;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.abnamro.client.reports.models.ClientTransactionsModel;
import com.abnamro.client.reports.service.DailySummaryReportGeneratorImpl;
import com.abnamro.client.reports.service.SummaryReportGenerator;
import com.abnamro.client.reports.service.TransactionsReader;
import com.abnamro.client.reports.service.TransactionsReaderImpl;

@SpringBootApplication
public class ClientTransactionsReportApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(ClientTransactionsReportApplication.class);
	
	public static void main(String[] args) throws Exception {
		
		ApplicationContext context = SpringApplication.run(ClientTransactionsReportApplication.class, args);
		
		TransactionsReader transactionsReader = context.getBean(TransactionsReaderImpl.class);
		
		SummaryReportGenerator dailySummaryReportGenerator = context.getBean(DailySummaryReportGeneratorImpl.class);
		
		List<ClientTransactionsModel> clientTransactionsList = transactionsReader.getFutureTransactions();
        logger.info("Total number of transactions data rows in the input file is :: " + clientTransactionsList.size());
        //Generate the daily Report
        dailySummaryReportGenerator.generateDailySummaryReport(clientTransactionsList);
        logger.info("Daily Summary Report Generated Successfully. Check in your project folder for Output file");
	}
}
