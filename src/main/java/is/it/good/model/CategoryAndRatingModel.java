package is.it.good.model;

public class CategoryAndRatingModel {

	public final static CategoryAndRatingModel SUPER_POSITIVE = new CategoryAndRatingModel();

	private Category category;
	private double rating;

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
