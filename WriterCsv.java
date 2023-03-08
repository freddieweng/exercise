import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Table;

import au.com.bytecode.opencsv.CSVWriter;

public class WriterCsv implements WriterInterface {

	@Override
	public boolean write(Table<String, String, String> dataSet, String destination) {
		if (dataSet == null) {
			System.err.println("Output dataSet is not valid");
			return false;
		}

		List<String[]> csvData = new ArrayList<>();

		List<String> columns = new ArrayList<>(dataSet.columnKeySet());
		String[] header = new String[columns.size() + 1];
		header[0] = WriterFactory.COLUMN_ID;
		for (int i = 0; i < columns.size(); i++) {
			header[i + 1] = columns.get(i);
		}
		csvData.add(header);

		Set<String> rowKeys = dataSet.rowKeySet();
		for (String rowKey : rowKeys) {
			String[] row = new String[columns.size() + 1];
			row[0] = rowKey;
			for (int i = 0; i < columns.size(); i++) {
				row[i + 1] = dataSet.get(rowKey, columns.get(i));
			}
			csvData.add(row);
		}

		try {
			CSVWriter writer = new CSVWriter(new FileWriter(destination));
			writer.writeAll(csvData);
			writer.close();
		} catch (IOException exp) {
			System.err.println("Error while writing file: " + destination + ", Exception: " + exp);
			return false;
		}

		return true;
	}
}
