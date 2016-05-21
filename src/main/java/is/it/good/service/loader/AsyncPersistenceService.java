package is.it.good.service.loader;

import is.it.good.model.QuestionModel;
import is.it.good.repository.QuestionModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncPersistenceService {

	@Autowired
	private QuestionModelRepository repository;

	@Async
	public void persistQuestionModel(QuestionModel model) {
		repository.save(model);
	}
}
