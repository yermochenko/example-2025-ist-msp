package by.vsu.config;

import by.vsu.config.util.IocContainer;
import by.vsu.service.AuthorService;
import by.vsu.service.test.AuthorServiceImpl;
import by.vsu.web.controller.AuthorController;

import java.util.List;

public class IocTestConfiguration {
	public static void build(IocContainer.Registry registry) {
		registry.registerClass(AuthorService.class, AuthorServiceImpl.class, List.of());
		registry.registerClass(AuthorController.class, AuthorController.class, List.of(AuthorService.class));
	}
}
