package is.it.good.settings;

import is.it.good.model.Category;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties("is.it.good")
public class AppConfigurations {

	private Map<Category, List<CategoryEntry>> entries;

	public Map<Category, List<CategoryEntry>> getEntries() {
		return entries;
	}

	public void setEntries(Map<Category, List<CategoryEntry>> entries) {
		this.entries = entries;
	}

	@Override
	public String toString() {
		return "AppConfigurations{" +
				"entries=" + entries +
				'}';
	}

	public static class CategoryEntry {
		private double from;
		private double to;
		private List<String> responses;

		public double getFrom() {
			return from;
		}

		public void setFrom(double from) {
			this.from = from;
		}

		public double getTo() {
			return to;
		}

		public void setTo(double to) {
			this.to = to;
		}

		public List<String> getResponses() {
			return responses;
		}

		public void setResponses(List<String> responses) {
			this.responses = responses;
		}

		@Override
		public String toString() {
			return "CategoryEntry{" +
					"from=" + from +
					", to=" + to +
					", responses=" + responses +
					'}';
		}
	}
}
