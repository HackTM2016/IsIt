package is.it.good.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class QuestionModel {

	@Id
	@GeneratedValue
	@Column
	private long id;

	@Column
	private String question;

	@Column
	private double responseRating;

	@Column
	private boolean forceGood;

	@Column
	private String response;

	@Column
	private Category category;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public double getResponseRating() {
		return responseRating;
	}

	public void setResponseRating(double responseRating) {
		this.responseRating = responseRating;
	}

	public boolean isForceGood() {
		return forceGood;
	}

	public void setForceGood(boolean forceGood) {
		this.forceGood = forceGood;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "QuestionModel{" +
				"id=" + id +
				", question='" + question + '\'' +
				", responseRating=" + responseRating +
				", forceGood=" + forceGood +
				", response='" + response + '\'' +
				", category=" + category +
				'}';
	}
}
