package com.abnamro.client.reports.service;

import java.util.List;

import com.abnamro.client.reports.models.ClientTransactionsModel;

public interface SummaryReportGenerator {

	void generateDailySummaryReport(List<ClientTransactionsModel> dailyTransactionsList, String transactionDate);
}
