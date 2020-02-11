package com.abnamro.client.reports.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.abnamro.client.reports.models.ClientTransactionsModel;

import net.sf.flatpack.DataSet;
import net.sf.flatpack.brparse.BuffReaderFixedParser;
import net.sf.flatpack.brparse.BuffReaderParseFactory;

@Service
public class TransactionsReaderImpl implements TransactionsReader {

	private static final Logger logger = LoggerFactory.getLogger(TransactionsReaderImpl.class);
	
	private enum ClientInformation {
	    CLIENTTYPE, CLIENTNUMBER, ACCOUNTNUMBER, SUBACCOUNTNUMBER
	}
	
	private enum ProductInformation {
	    EXCHANGECODE, PRODUCTGROUPCODE, SYMBOL, EXPIRATIONDATE
	}
	
	private enum TransactionInformation {
	    QTYLONG, QTYSHORT, TRANSACTIONDATE
	}
	
	@Override
	public List<ClientTransactionsModel> getFutureTransactions(String inputFileName, String configFileName) throws IOException {
        List<ClientTransactionsModel> clientTransactionsList = new ArrayList<ClientTransactionsModel>();
        try (BuffReaderFixedParser pzparse = (BuffReaderFixedParser) BuffReaderParseFactory.getInstance()
                .newFixedLengthParser(new InputStreamReader(new FileInputStream(ResourceUtils.getFile(configFileName))),
                						new InputStreamReader(new FileInputStream(ResourceUtils.getFile(inputFileName))))){
        	final DataSet ds = pzparse.parse();
        	String[] colNames = ds.getColumns();
            
            while (ds.next()) {
            	ClientTransactionsModel clientTransactionsModel = new ClientTransactionsModel();
                for (final String colName : colNames) {
                	//logger.info("COLUMN NAME: " + colName + " VALUE: " + ds.getString(colName));
                	if (EnumUtils.isValidEnumIgnoreCase(ClientInformation.class, colName)) {
                		setClientInformation(clientTransactionsModel, ds, colName);
                	}
                	if (EnumUtils.isValidEnumIgnoreCase(ProductInformation.class, colName)) {
                		setProductInformation(clientTransactionsModel, ds, colName);
                	}
                	if (EnumUtils.isValidEnumIgnoreCase(TransactionInformation.class, colName)) 
                		setTransactionInformation(clientTransactionsModel, ds, colName);
                   }
                clientTransactionsList.add(clientTransactionsModel);
                //logger.info("===========================================================================");
            }
        } 
        logger.info("Total number of transactions data rows in the input file is :: " + clientTransactionsList.size());
        return clientTransactionsList;
    }
	
	private void setClientInformation(ClientTransactionsModel clientTransactionsModel,
			DataSet dataSet, String fieldName) {
		
        ClientInformation clientInformation = Enum.valueOf(ClientInformation.class, fieldName.toUpperCase());
        
        String dataValue = dataSet.getString(fieldName);
        switch (clientInformation) {
	        case CLIENTTYPE:
	        	clientTransactionsModel.setClientType(dataValue);
	            break;
	        case CLIENTNUMBER:
	            clientTransactionsModel.setClientNumber(dataValue);
	            break;
	        case ACCOUNTNUMBER:
	            clientTransactionsModel.setAccountNumber(dataValue);
	            break;
	        case SUBACCOUNTNUMBER:
	        	clientTransactionsModel.setSubAccountNumber(dataValue);
	            break;
	        default:
	            break;
        }
    }
	
	private void setProductInformation(ClientTransactionsModel clientTransactionsModel,
			DataSet dataSet, String fieldName) {
		ProductInformation productInformation = Enum.valueOf(ProductInformation.class, fieldName.toUpperCase());
        String dataValue = dataSet.getString(fieldName);
        switch (productInformation) {
	        case EXCHANGECODE:
	        	clientTransactionsModel.setExchangeCode(dataValue);
	            break;
	        case PRODUCTGROUPCODE:
	            clientTransactionsModel.setProductGroupCode(dataValue);
	            break;
	        case SYMBOL:
	            clientTransactionsModel.setSymbol(dataValue);
	            break;
	        case EXPIRATIONDATE:
	        	clientTransactionsModel.setExpirationDate(dataValue);
	            break;
	        default:
	            break;
        }
    }
	
	
	private void setTransactionInformation(ClientTransactionsModel clientTransactionsModel,
			DataSet dataSet, String fieldName) {
		TransactionInformation transactionInformation = Enum.valueOf(TransactionInformation.class, fieldName.toUpperCase());
        switch (transactionInformation) {
	        case QTYLONG:
	        	clientTransactionsModel.setQtyLong(dataSet.getLong(fieldName));
	            break;
	        case QTYSHORT:
	            clientTransactionsModel.setQtyShort(dataSet.getLong(fieldName));
	            break;
	        case TRANSACTIONDATE:
	        	clientTransactionsModel.setTransactionDate(dataSet.getString(fieldName));
	        default:
	            break;
        }
    }
	
}
