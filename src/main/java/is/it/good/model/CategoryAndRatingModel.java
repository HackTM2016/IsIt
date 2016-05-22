package is.it.good.model;

public class CategoryAndRatingModel {

	public final static CategoryAndRatingModel SUPER_POSITIVE = new CategoryAndRatingModel(Category.POSITIVE,1.0d);

	private Category category;
	private double rating;

	public CategoryAndRatingModel() {
	}

	public CategoryAndRatingModel(Category category, double rating) {
		this.category = category;
		this.rating = rating;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return "CategoryAndRatingModel{" +
				"category=" + category +
				", rating=" + rating +
				'}';
	}
}
