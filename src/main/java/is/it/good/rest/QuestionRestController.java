package is.it.good.rest;

import is.it.good.model.QuestionModel;
import is.it.good.model.ResponseModel;
import is.it.good.service.loader.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionRestController {

	@Autowired
	private DataLoader dataLoader;

	@RequestMapping("/api/question")
	@ResponseBody
	public ResponseModel getResponse(@RequestParam String question,
			@RequestParam(required = false, defaultValue = "false") boolean forcePositive) {

		QuestionModel questionModel = dataLoader.getResponse(question, forcePositive);

		return new ResponseModel(questionModel.getCategory(), questionModel.getResponse());
	}

}
