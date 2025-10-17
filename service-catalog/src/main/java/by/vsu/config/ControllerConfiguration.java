package by.vsu.config;

import by.vsu.config.util.ControllerContainer;
import by.vsu.web.controller.AuthorController;

public class ControllerConfiguration {
	public static void build(ControllerContainer.Registry registry) {
		registry.registerController("/author", AuthorController.class);
	}
}
