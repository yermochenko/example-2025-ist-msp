package by.vsu.service.test;

import by.vsu.service.AuthorService;
import by.vsu.service.ServiceFactory;

public class ServiceFactoryTestImpl implements ServiceFactory {
	@Override
	public AuthorService newAuthorServiceInstance() {
		return new AuthorServiceImpl();
	}
}
