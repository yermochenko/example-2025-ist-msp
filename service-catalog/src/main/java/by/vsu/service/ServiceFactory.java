package by.vsu.service;

import by.vsu.util.Settings;

import java.lang.reflect.InvocationTargetException;

public interface ServiceFactory {
	AuthorService newAuthorServiceInstance();

	static ServiceFactory newInstance() throws ServiceException {
		String factoryClassName = Settings.getProperty(Settings.Key.SERVICE_FACTORY);
		try {
			return (ServiceFactory) Class.forName(factoryClassName).getConstructor().newInstance();
		} catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new ServiceException(e);
		}
	}
}
