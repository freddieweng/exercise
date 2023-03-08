import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Table;
import com.google.common.io.Files;

public class ReaderFactory {
	public static final String FILETYPE_CSV = "csv";
	public static final String FILETYPE_HTML = "html";
	public static final String COLUMN_ID = "ID";

	private static ReaderFactory instance = null;
	private static final Map<String, ReaderInterface> registry = new HashMap<>();

	static {
		registry.put(FILETYPE_CSV, new ReaderCsv());
		registry.put(FILETYPE_HTML, new ReaderHtml());
	}

	private ReaderFactory() {
	}

	public static ReaderFactory getInstance() {
		if (instance == null) {
			instance = new ReaderFactory();
		}

		return instance;
	}

	public Table<String, String, String> readDataSet(String source) {
		String fileExtension = Files.getFileExtension(source);
		ReaderInterface reader = registry.get(fileExtension.toLowerCase());
		if (reader == null) {
			System.err.println("Input source type not supported: " + source + ". SKIP!");
			return null;
		}

		return reader.read(source);
	}
}
