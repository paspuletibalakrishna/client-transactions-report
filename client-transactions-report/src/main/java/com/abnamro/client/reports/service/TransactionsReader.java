package com.abnamro.client.reports.service;

import java.util.List;

import com.abnamro.client.reports.models.ClientTransactionsModel;

public interface TransactionsReader {

	List<ClientTransactionsModel> getFutureTransactions(String inputFileName, String configFileName);
	
}
