package is.it.good.repository;

import is.it.good.model.QuestionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionModelRepository extends JpaRepository<QuestionModel, Long> {

}
