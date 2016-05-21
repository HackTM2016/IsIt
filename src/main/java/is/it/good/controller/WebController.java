package is.it.good.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebController.class);

	@RequestMapping(value = "/")
	public String home() {

		LOGGER.info("Getting home!");
		return "index.html";
	}

}
