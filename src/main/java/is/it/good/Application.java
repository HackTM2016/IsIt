package is.it.good;

import is.it.good.settings.AppConfigurations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;

@Configuration
@ComponentScan
@EnableAsync
@EnableWebMvc
@EnableAutoConfiguration
public class Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	@Autowired
	private AppConfigurations appConfigurations;

	@PostConstruct
	protected void postConstruct() {

		LOGGER.info("Running with configurations : " + appConfigurations);
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
