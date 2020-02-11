package com.abnamro.client.reports.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.abnamro.client.reports.models.ClientTransactionsModel;
import com.abnamro.client.reports.models.SummaryReportModel;

@Service
public class DailySummaryReportGeneratorImpl implements SummaryReportGenerator {

	private static final Logger logger = LoggerFactory.getLogger(DailySummaryReportGeneratorImpl.class);
	
	@Autowired
	private CSVExportUtil csvExportUtil;
	
	public void generateDailySummaryReport(List<ClientTransactionsModel> dailyTransactionsList, final String transactionDateInput) {
		logger.info("Generate Daily Summary Report Starts");
		Map<String, List<ClientTransactionsModel>> transactionsbyClientMap = getTransactionsDataByClient(dailyTransactionsList);
        List<SummaryReportModel> dailySummaryReports = new ArrayList<SummaryReportModel>();
        
        transactionsbyClientMap.entrySet().stream().forEach(clientMapElement -> {
        	String clientNumber = clientMapElement.getKey();
     	   //Second segregate the transactions by datewise for each Client
        	logger.info("Seperate Transactions Data of a Client by Date Wise");
        	List<ClientTransactionsModel> transactionsbyClient = clientMapElement.getValue();
        	List<String> uniqueTransactionDatesList = null;
        	//If user provided transaction date is there then filter list by using it else take all date values from list
        	if (StringUtils.isNotBlank(transactionDateInput)) {
        			uniqueTransactionDatesList = Arrays.asList(transactionDateInput);
        	} else {
        	   uniqueTransactionDatesList = transactionsbyClient.stream()
    					.filter(distinctByKey(b -> b.getTransactionDate()))
                        .map(b -> b.getTransactionDate())
                        .collect(Collectors.toList());
        	}
        	
     
        	//Third get Transactions Data by transaction Date
            Map<String, List<ClientTransactionsModel>> transactionsbyDateMap = new HashMap<String, List<ClientTransactionsModel>>();
            for (String transactionDate : uniqueTransactionDatesList) {
            	List<ClientTransactionsModel> transactionsListbyDate = transactionsbyClient.stream()
    														.filter(x -> transactionDate.equalsIgnoreCase(x.getTransactionDate()))
    														.collect(Collectors.toList());
            	logger.info("Total number of transactions for a client on that transaction date is " + transactionsListbyDate.size());
            	if (!CollectionUtils.isEmpty(transactionsListbyDate)) {
            		transactionsbyDateMap.put(transactionDate, transactionsListbyDate);
            	}
            }
            if (CollectionUtils.isEmpty(transactionsbyDateMap)) {
            	return;
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
        
        if (CollectionUtils.isEmpty(dailySummaryReports)) {
        	logger.info("No transactions found with the given date. please give the correct transaction date");
        } else {
        	 generateSummaryCSVReportData(dailySummaryReports);
        }
	}

	private ClientTransactionsModel getClientTransactionDataByProduct(List<ClientTransactionsModel> transactionsListByDate, String productCode) {
		return transactionsListByDate.stream().filter(s -> s.getUniqueProductCode().equals(productCode)).findFirst().orElse(null);
	}
	
	private void generateSummaryCSVReportData(List<SummaryReportModel> dailySummaryReports) {
		List<String[]> dataLines = new ArrayList<>();
		String[] headerInfo = new String[] {ClientTransactionsConstants.CLIENT_NUMBER, ClientTransactionsConstants.CLIENT_INFORMATION,
											ClientTransactionsConstants.TRANSACTION_DATE, ClientTransactionsConstants.PRODUCT_CODE,
											ClientTransactionsConstants.PRODUCT_INFORMATION, ClientTransactionsConstants.TOTAL_TRANSACTION_AMOUNT };
		dataLines.add(headerInfo);
        for (SummaryReportModel summaryReportModel : dailySummaryReports) {
        	logger.info("Client Number :: " + summaryReportModel.getClientNumber() 
        	            + " Client Information :: " + summaryReportModel.getClientInformation() 
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
     	logger.info("Daily Summary Report Generated Successfully. Check in your project folder for Output file");
	}
	
	private String convertStringtoDate(String dateInString) {
		String formattedDate = dateInString;
		DateFormat formatter  = new SimpleDateFormat(ClientTransactionsConstants.TRANSACTION_DATE_FORMAT);
		Date date;
		try {
			date = formatter.parse(dateInString);
			DateFormat newformatter  = new SimpleDateFormat(ClientTransactionsConstants.TRANSACTION_DATE_REPORT_FORMAT);
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
