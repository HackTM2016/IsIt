package is.it.good.service.loader;

import com.datumbox.framework.applications.nlp.TextClassifier;
import com.datumbox.framework.common.Configuration;
import com.datumbox.framework.common.dataobjects.AssociativeArray;
import com.datumbox.framework.common.dataobjects.Dataframe;
import com.datumbox.framework.common.dataobjects.Record;
import com.datumbox.framework.common.interfaces.Extractable;
import com.datumbox.framework.common.persistentstorage.inmemory.InMemoryConfiguration;
import com.datumbox.framework.common.utilities.RandomGenerator;
import com.datumbox.framework.common.utilities.StringCleaner;
import com.datumbox.framework.core.machinelearning.classification.BinarizedNaiveBayes;
import com.datumbox.framework.core.machinelearning.common.abstracts.modelers.AbstractClassifier;
import com.datumbox.framework.core.machinelearning.common.interfaces.ValidationMetrics;
import com.datumbox.framework.core.machinelearning.featureselection.scorebased.TFIDF;
import com.datumbox.framework.core.utilities.text.extractors.AbstractTextExtractor;
import com.datumbox.framework.core.utilities.text.extractors.NgramsExtractor;
import is.it.good.model.Category;
import is.it.good.model.CategoryAndRatingModel;
import is.it.good.model.QuestionModel;
import is.it.good.settings.AppConfigurations;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class DLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(DLoader.class);

	@Autowired
	private AppConfigurations appConfigurations;

	@Autowired
	private AsyncPersistenceService asyncPersistenceService;

	private Random rd = new Random();

	private Analyzer analyzer = new EnglishAnalyzer();

	private TextClassifier classifier;

	@PostConstruct
	public void postConstruct() throws IOException {
		RandomGenerator.setGlobalSeed(42L); //optionally set a specific seed for all Random objects
		Configuration conf = Configuration.getConfiguration(); //default configuration based on properties file
		conf.setDbConfig(new InMemoryConfiguration()); //use In-Memory storage (default)
		conf.getConcurrencyConfig().setParallelized(false); //turn on/off the parallelization

		List<String> positiveLines = org.apache.commons.io.IOUtils
				.readLines(new ClassPathResource("data/positive-words.txt").getInputStream());
		List<String> negativeLines = org.apache.commons.io.IOUtils
				.readLines(new ClassPathResource("data/negative-words.txt").getInputStream());

		Map<String, List<String>> data = new HashMap<>();
		data.put("positive", positiveLines);
		data.put("negative", negativeLines);

		TextClassifier.TrainingParameters trainingParameters = new TextClassifier.TrainingParameters();

		trainingParameters.setModelerClass(BinarizedNaiveBayes.class);
		trainingParameters.setModelerTrainingParameters(new BinarizedNaiveBayes.TrainingParameters());

		//Set feature selection configuration
		trainingParameters.setFeatureSelectorClass(TFIDF.class);
		TFIDF.TrainingParameters featureParameters = new TFIDF.TrainingParameters();

		trainingParameters.setFeatureSelectorTrainingParameters(featureParameters);

		//Set text extraction configuration
		trainingParameters.setTextExtractorClass(NgramsExtractor.class);

		NgramsExtractor.Parameters textParameters = new NgramsExtractor.Parameters();
		trainingParameters.setTextExtractorParameters(textParameters);

		//Fit the classifier
		//------------------
		classifier = new TextClassifier("Analyzer", conf);

		Dataframe df = convert(data, AbstractTextExtractor.newInstance(trainingParameters.getTextExtractorClass(),
				trainingParameters.getTextExtractorParameters()),conf);
		classifier.fit(df, trainingParameters);

		//Use the classifier
		//------------------

		//Get validation metrics on the training set
		ValidationMetrics vm = classifier.validate(df);
		classifier.setValidationMetrics(vm); //store them in the model for future reference

		AbstractClassifier.AbstractValidationMetrics vv = (AbstractClassifier.AbstractValidationMetrics) vm;

		LOGGER.info("Accuracy : " + vv.getAccuracy());
		for (Map.Entry<List<Object>, Double> entry : vv.getContingencyTable().entrySet()) {

			LOGGER.info("Entry : " + entry.toString());

		}

	}

	@PreDestroy
	protected void preDestroy() {
		classifier.delete();
	}

	public QuestionModel getResponse(String question, boolean forceTrue) {

		QuestionModel questionModel = new QuestionModel();

		questionModel.setForceGood(forceTrue);
		questionModel.setQuestion(question);

		if (forceTrue) {

			questionModel.setResponseRating(1.0d);
			questionModel.setCategory(Category.POSITIVE);
			questionModel.setResponse(getResponse(CategoryAndRatingModel.SUPER_POSITIVE));

		} else {

			CategoryAndRatingModel categoryAndRating = getResponse(question);
			questionModel.setResponseRating(categoryAndRating.getRating());
			questionModel.setCategory(categoryAndRating.getCategory());

			questionModel.setResponse(getResponse(categoryAndRating));

		}

		asyncPersistenceService.persistQuestionModel(questionModel);

		return questionModel;
	}

	private String getResponse(CategoryAndRatingModel categoryAndRating) {

		LOGGER.info("Getting Data for : " + categoryAndRating);
		List<AppConfigurations.CategoryEntry> entries = appConfigurations.getEntries()
				.get(categoryAndRating.getCategory());

		for (AppConfigurations.CategoryEntry entry : entries) {
			if (entry.getTo() >= categoryAndRating.getRating() && entry.getFrom() < categoryAndRating.getRating()) {
				return entry.getResponses().get(rd.nextInt(entry.getResponses().size()));
			}
		}

		return "The App is not configured to handle such questions!";
	}

	private CategoryAndRatingModel getResponse(String question) {
		CategoryAndRatingModel response = new CategoryAndRatingModel();

		//question = tokenizeString(analyzer, question.replaceAll("\n", "").replaceAll("\r", ""));

		Record r = classifier.predict(question);
		response.setCategory(Category.valueOf(r.getYPredicted().toString().toUpperCase()));
		response.setRating(Double.parseDouble(r.getYPredictedProbabilities().get(r.getYPredicted()).toString()));
		return response;
	}

	private String tokenizeString(Analyzer analyzer, String string) {
		List<String> result = new ArrayList<String>();
		try {
			TokenStream stream = analyzer.tokenStream(null, new StringReader(string));
			stream.reset();
			while (stream.incrementToken()) {
				result.add(stream.getAttribute(CharTermAttribute.class).toString());
			}
		} catch (IOException e) {
			// not thrown b/c we're using a string reader...
			throw new RuntimeException(e);
		}
		return StringUtils.join(result, " ");
	}

	public Dataframe convert(Map<String, List<String>> data, Extractable textExtractor,
			com.datumbox.framework.common.Configuration conf) {
		Dataframe dataSet = new Dataframe(conf);

		for (Map.Entry<String, List<String>> entry : data.entrySet()) {

			int baseCounter = dataSet.size();
			int index = 0;
			for (String line : entry.getValue()) {

				Integer rId = baseCounter + index;
				AssociativeArray xData = new AssociativeArray(textExtractor.extract(StringCleaner.clear(line)));
				Record r = new Record(xData, entry.getKey());
				dataSet.set(rId, r);
				index++;
			}
		}
		return dataSet;
	}

}
