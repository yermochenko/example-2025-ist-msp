package by.vsu.web;

import by.vsu.config.ControllerConfiguration;
import by.vsu.config.IocTestConfiguration;
import by.vsu.config.ValidatorConfiguration;
import by.vsu.config.util.ControllerContainer;
import by.vsu.config.util.IocContainer;
import by.vsu.config.util.ValidatorContainer;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ApplicationInitializer implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent event) {
		// Initialization of validators
		ValidatorContainer.Registry validatorContainerRegistry = new ValidatorContainer.Registry();
		ValidatorConfiguration.build(validatorContainerRegistry);
		ValidatorContainer.init(validatorContainerRegistry);
		// Initialization of IoC
		IocContainer.Registry iocContainerRegistry = new IocContainer.Registry();
		IocTestConfiguration.build(iocContainerRegistry);
		IocContainer.init(
			iocContainerRegistry.getImplementationsSupplier(),
			iocContainerRegistry.getDependenciesSupplier()
		);
		// Initialization of controllers
		ControllerContainer.Registry controllerContainerRegistry = new ControllerContainer.Registry();
		ControllerConfiguration.build(controllerContainerRegistry);
		ControllerContainer.init(controllerContainerRegistry);
	}
}
