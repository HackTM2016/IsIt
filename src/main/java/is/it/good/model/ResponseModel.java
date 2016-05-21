package is.it.good.model;

public class ResponseModel {

	private Category category;

	private String message;

	public ResponseModel(Category category, String message) {
		this.category = category;
		this.message = message;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
