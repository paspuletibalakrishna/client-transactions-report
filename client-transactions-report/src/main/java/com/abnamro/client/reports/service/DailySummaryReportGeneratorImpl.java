package com.abnamro.client.reports.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abnamro.client.reports.models.ClientTransactionsModel;
import com.abnamro.client.reports.models.SummaryReportModel;

@Service
public class DailySummaryReportGeneratorImpl implements SummaryReportGenerator {

	private static final Logger logger = LoggerFactory.getLogger(DailySummaryReportGeneratorImpl.class);
	
	@Autowired
	private CSVExportUtil csvExportUtil;
	
	public void generateDailySummaryReport(List<ClientTransactionsModel> dailyTransactionsList) {
		logger.info("Generate Daily Summary Report Starts");
		Map<String, List<ClientTransactionsModel>> transactionsbyClientMap = getTransactionsDataByClient(dailyTransactionsList);
        List<SummaryReportModel> dailySummaryReports = new ArrayList<SummaryReportModel>();
        
        transactionsbyClientMap.entrySet().stream().forEach(clientMapElement -> {
        	String clientNumber = clientMapElement.getKey();
     	   //Second segregate the transactions by datewise for each Client
        	logger.info("Seperate Transactions Data of a Client by Date Wise");
        	List<ClientTransactionsModel> transactionsbyClient = clientMapElement.getValue();
        	List<String> uniqueTransactionDatesList = transactionsbyClient.stream()
					.filter(distinctByKey(b -> b.getTransactionDate()))
                    .map(b -> b.getTransactionDate())
                    .collect(Collectors.toList());
        	
        	//Third get Transactions Data by transaction Date
            Map<String, List<ClientTransactionsModel>> transactionsbyDateMap = new HashMap<String, List<ClientTransactionsModel>>();
            for (String transactionDate : uniqueTransactionDatesList) {
            	List<ClientTransactionsModel> transactionsListbyDate = transactionsbyClient.stream()
    														.filter(x -> transactionDate.equalsIgnoreCase(x.getTransactionDate()))
    														.collect(Collectors.toList());
                transactionsbyDateMap.put(transactionDate, transactionsListbyDate);
            }
            
            logger.info("Calcualte total Transactions amount of a Client by Product Wise");
            transactionsbyDateMap.entrySet().stream().forEach(transactionDate -> {
            	//Generate Daily TransactionAmounts by Product
            	List<ClientTransactionsModel> transactionsListByDate = transactionDate.getValue();
            	
            	Map<String, Long> totalTransactionAmountsbyProduct = getTotalTransactionAmountsByProduct(transactionsListByDate);
            	
            	totalTransactionAmountsbyProduct.entrySet().stream().forEach(product -> {
            		ClientTransactionsModel clientTransactionsModel = getClientTransactionDataByProduct(transactionsListByDate, product.getKey());
            		SummaryReportModel dailySummaryReportModel = new SummaryReportModel();
            		dailySummaryReportModel.setClientNumber(clientNumber);
            		dailySummaryReportModel.setClientInformation(clientTransactionsModel.getClientInformation());
            		dailySummaryReportModel.setProductInformation(clientTransactionsModel.getProductInformation());
            		dailySummaryReportModel.setTransactionDate(transactionDate.getKey());
            		dailySummaryReportModel.setProductCode(product.getKey());
            		dailySummaryReportModel.setTotalTransactionAmount(product.getValue());
            		dailySummaryReports.add(dailySummaryReportModel);
            	});
            });
        });
        
        generateSummaryCSVReportData(dailySummaryReports);
		
	}

	private ClientTransactionsModel getClientTransactionDataByProduct(List<ClientTransactionsModel> transactionsListByDate, String productCode) {
		return transactionsListByDate.stream().filter(s -> s.getUniqueProductCode().equals(productCode)).findFirst().orElse(null);
	}
	private void generateSummaryCSVReportData(List<SummaryReportModel> dailySummaryReports) {
		List<String[]> dataLines = new ArrayList<>();
		String[] headerInfo = new String[] {"Client Number", "Client Information",
				                             "Transaction Date", "Product Code",
				                             "Product Information", "Total Transaction Amount"};
		dataLines.add(headerInfo);
        for (SummaryReportModel summaryReportModel : dailySummaryReports) {
        	logger.info("Client Number :: " + summaryReportModel.getClientNumber() 
        	            + "Client Informatuon :: " + summaryReportModel.getClientInformation() 
        	            + " On Transaction Date :: " + convertStringtoDate(summaryReportModel.getTransactionDate())
        	            + " for Product Group :: " + summaryReportModel.getProductCode()
        	            + " for Product Information :: " + summaryReportModel.getProductInformation()
        	            + " with Total Transactions Amounts :: " + summaryReportModel.getTotalTransactionAmount());
        	
        	dataLines.add(new String[] 
        	          { summaryReportModel.getClientNumber(), summaryReportModel.getClientInformation(), 
        	        	  convertStringtoDate(summaryReportModel.getTransactionDate()), 
        	            summaryReportModel.getProductCode(), summaryReportModel.getProductInformation(), String.valueOf(summaryReportModel.getTotalTransactionAmount()) });
        	
        }
     	csvExportUtil.generateCSVReport(dataLines);
	}
	
	private String convertStringtoDate(String dateInString) {
		String formattedDate = dateInString;
		DateFormat formatter  = new SimpleDateFormat("yyyyMMdd");
		Date date;
		try {
			date = formatter.parse(dateInString);
			DateFormat newformatter  = new SimpleDateFormat("dd-MMM-yyyy");
			formattedDate = newformatter.format(date);
		} catch (ParseException exp) {
			logger.error("Unable to parse the data properly" + exp);
		}
		return formattedDate;
	}
	
	private Map<String, Long> getTotalTransactionAmountsByProduct(List<ClientTransactionsModel> dailyTransactionsList) {
		//Combining product group code and exchange code to get unique product code.
		   Map<String, Long> totalTransactionAmountsbyProduct = new HashMap<String, Long>();
		   List<String> uniqueProductsList = dailyTransactionsList.stream()
				.filter(distinctByKey(b -> b.getUniqueProductCode()))
                .map(b -> b.getUniqueProductCode())
                .collect(Collectors.toList());
		
			for (String product : uniqueProductsList) {
	        	Long totalQuantityLong = dailyTransactionsList.stream()
															.filter(x -> product.equalsIgnoreCase(x.getUniqueProductCode()))
															.map(x -> x.getQtyLong())
															.reduce(0L, Long::sum);
	        	Long totalQuantityShort = dailyTransactionsList.stream()
						.filter(x -> product.equalsIgnoreCase(x.getUniqueProductCode()))
						.map(x -> x.getQtyShort())
						.reduce(0L, Long::sum);
	        	Long totalTransactionAmount = totalQuantityLong - totalQuantityShort;
	        	totalTransactionAmountsbyProduct.put(product, totalTransactionAmount);
	        	logger.info("totalTransactionAmount " + totalTransactionAmount + " for ProductCode " + product);
			 }
			return totalTransactionAmountsbyProduct;
		
	}
	
	private Map<String, List<ClientTransactionsModel>> getTransactionsDataByClient(List<ClientTransactionsModel> dailyTransactionsList) {
		logger.info("Seperate Transactions Data by Client Wise");
		//get TransactionsData by ClientNumber
		List<String> clientsList = dailyTransactionsList.stream()
				.filter(distinctByKey(b -> b.getClientNumber()))
                .map(b -> b.getClientNumber())
                .collect(Collectors.toList());
		Map<String, List<ClientTransactionsModel>> transactionsbyClientMap = new HashMap<String, List<ClientTransactionsModel>>();
		for (String clientNumber : clientsList) {
			List<ClientTransactionsModel> transactionsListbyClient = dailyTransactionsList.stream()
														.filter(x -> clientNumber.equalsIgnoreCase(x.getClientNumber()))
														.collect(Collectors.toList());
			transactionsbyClientMap.put(clientNumber, transactionsListbyClient);
		 }
		return transactionsbyClientMap;
	}
	
	private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
	
}
