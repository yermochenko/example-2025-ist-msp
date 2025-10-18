package by.vsu.service.main;

import by.vsu.repository.TransactionManager;

public class BaseServiceImpl {
	private TransactionManager transactionManager;

	public void setTransactionManager(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	protected TransactionManager getTransactionManager() {
		return transactionManager;
	}
}
