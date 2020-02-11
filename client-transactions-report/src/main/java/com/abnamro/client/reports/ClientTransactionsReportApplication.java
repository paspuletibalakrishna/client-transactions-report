package com.abnamro.client.reports;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.abnamro.client.reports.models.ClientTransactionsModel;
import com.abnamro.client.reports.service.ClientTransactionsConstants;
import com.abnamro.client.reports.service.DailySummaryReportGeneratorImpl;
import com.abnamro.client.reports.service.SummaryReportGenerator;
import com.abnamro.client.reports.service.TransactionsReader;
import com.abnamro.client.reports.service.TransactionsReaderImpl;

@SpringBootApplication
public class ClientTransactionsReportApplication implements ApplicationRunner {
	
	private static final Logger logger = LoggerFactory.getLogger(ClientTransactionsReportApplication.class);
	
	@Autowired
    private ApplicationContext applicationContext;
	
	public static void main(String[] args) throws Exception {

		SpringApplication application = new SpringApplication(ClientTransactionsReportApplication.class);
		application.setBannerMode(Banner.Mode.OFF);
		application.setWebApplicationType(WebApplicationType.NONE);
		application.run(args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		logger.info("Application started with command-line arguments: ", Arrays.toString(args.getSourceArgs()));
        logger.info("OptionNames: {}", args.getOptionNames());
        
        if (args.containsOption(ClientTransactionsConstants.INPUT_FILE_ARG)
        		&& args.containsOption(ClientTransactionsConstants.CONFIG_FILE_ARG)) {
        	Optional<String> inputFileName = args.getOptionValues(ClientTransactionsConstants.INPUT_FILE_ARG).stream().findFirst();
        	Optional<String> configFileName = args.getOptionValues(ClientTransactionsConstants.CONFIG_FILE_ARG).stream().findFirst();
        	Optional<String> transactionDate = Optional.empty();
        	
        	if (args.containsOption(ClientTransactionsConstants.TRANSACTION_DATE_ARG)) {
        	   transactionDate = args.getOptionValues(ClientTransactionsConstants.TRANSACTION_DATE_ARG).stream().findFirst();
        	}
        	
        	TransactionsReader transactionsReader = applicationContext.getBean(TransactionsReaderImpl.class);
    		List<ClientTransactionsModel> clientTransactionsList = transactionsReader.getFutureTransactions(inputFileName.get(), 
    																						 configFileName.get());
    		
            logger.info("Total number of transactions data rows in the input file is :: " + clientTransactionsList.size());
            //Generate the daily Report
            SummaryReportGenerator dailySummaryReportGenerator = applicationContext.getBean(DailySummaryReportGeneratorImpl.class);
            dailySummaryReportGenerator.generateDailySummaryReport(clientTransactionsList, transactionDate.orElse(null));
        } else {
        	logger.info("Please provide inputs in the format of input.file, transaction.date, config.file as arguments to generate Daily summary reports");
        }
        
	}
}
