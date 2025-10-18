package by.vsu.web;

import by.vsu.config.ControllerConfiguration;
import by.vsu.config.IocMainConfiguration;
import by.vsu.config.ValidatorConfiguration;
import by.vsu.config.util.ControllerContainer;
import by.vsu.config.util.IocContainer;
import by.vsu.config.util.ValidatorContainer;
import by.vsu.repository.jdbc.DatabaseConnector;
import by.vsu.util.Settings;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebListener
public class ApplicationInitializer implements ServletContextListener {
	private static final Logger log = LogManager.getLogger();

	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			// Initialization of validators
			ValidatorContainer.Registry validatorContainerRegistry = new ValidatorContainer.Registry();
			ValidatorConfiguration.build(validatorContainerRegistry);
			ValidatorContainer.init(validatorContainerRegistry);
			// Initialization of IoC
			IocContainer.Registry iocContainerRegistry = new IocContainer.Registry();
			IocMainConfiguration.build(iocContainerRegistry);
			IocContainer.init(
					iocContainerRegistry.getImplementationsSupplier(),
					iocContainerRegistry.getDependenciesSupplier(),
					iocContainerRegistry.getFactoriesSupplier()
			);
			// Initialization of controllers
			ControllerContainer.Registry controllerContainerRegistry = new ControllerContainer.Registry();
			ControllerConfiguration.build(controllerContainerRegistry);
			ControllerContainer.init(controllerContainerRegistry);
			// Initialization of database connector
			Settings.init();
			DatabaseConnector.init(
					Settings.getProperty(Settings.Key.JDBC_DRIVER),
					Settings.getProperty(Settings.Key.JDBC_URL),
					Settings.getProperty(Settings.Key.JDBC_USERNAME),
					Settings.getProperty(Settings.Key.JDBC_PASSWORD)
			);
			StringBuilder message = new StringBuilder("Application initialized successfully\nSettings:");
			for(Settings.Key key : Settings.Key.values()) {
				message.append("\n\t").append(key.getName()).append("=").append(Settings.getProperty(key));
			}
			log.info(message.toString());
		} catch(IOException e) {
			log.fatal("Could not initialize settings", e);
		} catch(ClassNotFoundException e) {
			log.fatal("Could not load JDBC driver", e);
		}
	}
}
