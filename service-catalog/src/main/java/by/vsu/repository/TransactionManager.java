package by.vsu.repository;

public interface TransactionManager {
	void startTransaction() throws RepositoryException;

	void commitTransaction() throws RepositoryException;

	void rollbackTransaction() throws RepositoryException;
}
