package is.it.good.repository.rest;

import is.it.good.model.QuestionModel;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "questions", collectionResourceRel = "question", itemResourceRel = "question")
public interface QuestionModelRestRepository extends PagingAndSortingRepository<QuestionModel, Integer> {

}
