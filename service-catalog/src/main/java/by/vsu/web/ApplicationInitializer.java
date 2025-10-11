package by.vsu.web;

import by.vsu.config.ValidatorConfiguration;
import by.vsu.config.util.ValidatorContainer;
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
			// Initialization of settings
			Settings.init();
		} catch(IOException e) {
			log.fatal("Could not initialize settings", e);
		}
	}
}
