package by.vsu.config;

import by.vsu.config.util.IocContainer;
import by.vsu.repository.AuthorRepository;
import by.vsu.repository.TransactionManager;
import by.vsu.repository.jdbc.AuthorRepositoryImpl;
import by.vsu.repository.jdbc.ConnectionFactory;
import by.vsu.repository.jdbc.TransactionManagerImpl;
import by.vsu.service.AuthorService;
import by.vsu.service.main.AuthorServiceImpl;
import by.vsu.web.controller.AuthorController;

import java.sql.Connection;
import java.util.List;

public class IocMainConfiguration {
	public static void build(IocContainer.Registry registry) {
		registry.registerFactory(Connection.class, new ConnectionFactory());
		registry.registerClass(TransactionManager.class, TransactionManagerImpl.class, List.of(Connection.class));
		registry.registerClass(AuthorRepository.class, AuthorRepositoryImpl.class, List.of(Connection.class));
		registry.registerClass(AuthorService.class, AuthorServiceImpl.class, List.of(AuthorRepository.class, TransactionManager.class));
		registry.registerClass(AuthorController.class, AuthorController.class, List.of(AuthorService.class));
	}
}
