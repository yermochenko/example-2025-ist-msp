package by.vsu.config;

import by.vsu.config.util.IocContainer;
import by.vsu.service.AuthorService;
import by.vsu.service.test.AuthorServiceImpl;

import java.util.List;

public class IocTestConfiguration {
	public static void build(IocContainer.Registry registry) {
		registry.registerClass(AuthorService.class, AuthorServiceImpl.class, List.of());
	}
}
