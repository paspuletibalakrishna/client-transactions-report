package com.abnamro.client.reports.models;

public class SummaryReportModel {
	
	private String clientInformation;
	private String productCode;
	private String productInformation;
	private String clientNumber;
	private String transactionDate;
	private Long totalTransactionAmount;
	
	public String getClientInformation() {
		return clientInformation;
	}
	public void setClientInformation(String clientInformation) {
		this.clientInformation = clientInformation;
	}
	public String getProductInformation() {
		return productInformation;
	}
	public void setProductInformation(String productInformation) {
		this.productInformation = productInformation;
	}
	public String getClientNumber() {
		return clientNumber;
	}
	public void setClientNumber(String clientNumber) {
		this.clientNumber = clientNumber;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public Long getTotalTransactionAmount() {
		return totalTransactionAmount;
	}
	public void setTotalTransactionAmount(Long totalTransactionAmount) {
		this.totalTransactionAmount = totalTransactionAmount;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	

}
