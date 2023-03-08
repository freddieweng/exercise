import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Table;
import com.google.common.io.Files;

public class WriterFactory {
	public static final String FILETYPE_CSV = "csv";
	public static final String COLUMN_ID = "ID";

	private static WriterFactory instance = null;
	private static final Map<String, WriterInterface> registry = new HashMap<>();

	static {
		registry.put(FILETYPE_CSV, new WriterCsv());
	}

	private WriterFactory() {
	}

	public static WriterFactory getInstance() {
		if (instance == null) {
			instance = new WriterFactory();
		}

		return instance;
	}

	public boolean dump(Table<String, String, String> dataSet, String destination) {
		if (dataSet == null) {
			System.err.println("Output dataSet is not valid");
			return false;
		}

		String fileExtension = Files.getFileExtension(destination);
		WriterInterface writer = registry.get(fileExtension.toLowerCase());
		if (writer == null) {
			System.err.println("Output destination type not supported: " + destination);
			return false;
		}

		return writer.write(dataSet, destination);
	}
}
