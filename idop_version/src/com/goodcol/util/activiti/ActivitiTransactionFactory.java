package com.goodcol.util.activiti;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;


public class ActivitiTransactionFactory implements TransactionFactory {
	@Override
	public Transaction newTransaction(Connection paramConnection, boolean paramBoolean) {
		return new ActivitiTransaction(paramConnection,paramBoolean);
	}

	@Override
	public void setProperties(Properties paramProperties) {}
}