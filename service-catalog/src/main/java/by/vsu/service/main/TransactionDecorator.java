package by.vsu.service.main;

import by.vsu.config.util.IocContainer;
import by.vsu.config.util.exception.IocException;
import by.vsu.repository.RepositoryException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TransactionDecorator implements IocContainer.Decorator {
	@Override
	public Object execute(Object object, Method method, Object[] args) throws IocException {
		boolean inTransaction = method.getDeclaredAnnotation(Transaction.class) != null;
		BaseServiceImpl service = (BaseServiceImpl) object;
		try {
			if(inTransaction) {
				service.getTransactionManager().startTransaction();
			}
			Object result = method.invoke(service, args);
			if(inTransaction) {
				service.getTransactionManager().commitTransaction();
			}
			return result;
		} catch(InvocationTargetException | RepositoryException e) {
			if(inTransaction) {
				try { service.getTransactionManager().rollbackTransaction(); } catch(RepositoryException ignored) {}
			}
			throw new IocException(e instanceof InvocationTargetException ? e.getCause() : e);
		} catch(IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
