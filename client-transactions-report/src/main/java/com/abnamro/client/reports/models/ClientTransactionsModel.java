package com.abnamro.client.reports.models;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientTransactionsModel {

	private String clientType;
	private String clientNumber;
	private String accountNumber;
	private String subAccountNumber;
	private String exchangeCode;
	private String productGroupCode;
	private String symbol;
	private String expirationDate;
	private Long qtyLong;
	private Long qtyShort;
	private String transactionDate;
	
	public String getUniqueProductCode() {
		return this.productGroupCode + this.exchangeCode;
	}
	
	public String getClientInformation() {
		return this.clientType + this.clientNumber + this.accountNumber + this.subAccountNumber;
	}
	
	public String getProductInformation() {
		return this.exchangeCode + this.productGroupCode + this.symbol + this.expirationDate;
	}
	
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getClientType() {
		return clientType;
	}
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	public String getClientNumber() {
		return clientNumber;
	}
	public void setClientNumber(String clientNumber) {
		this.clientNumber = clientNumber;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getSubAccountNumber() {
		return subAccountNumber;
	}
	public void setSubAccountNumber(String subAccountNumber) {
		this.subAccountNumber = subAccountNumber;
	}
	public String getExchangeCode() {
		return exchangeCode;
	}
	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}
	public String getProductGroupCode() {
		return productGroupCode;
	}
	public void setProductGroupCode(String productGroupCode) {
		this.productGroupCode = productGroupCode;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public Long getQtyLong() {
		return qtyLong;
	}
	public void setQtyLong(Long qtyLong) {
		this.qtyLong = qtyLong;
	}
	public Long getQtyShort() {
		return qtyShort;
	}
	public void setQtyShort(Long qtyShort) {
		this.qtyShort = qtyShort;
	}
	
	
}
