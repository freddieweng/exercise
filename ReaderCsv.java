import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import au.com.bytecode.opencsv.CSVReader;

public class ReaderCsv implements ReaderInterface {

	@Override
	public Table<String, String, String> read(String source) {
		List<String[]> records;

		try {
			CSVReader csvReader = new CSVReader(new FileReader(source));
			records = csvReader.readAll();
		} catch (IOException exp) {
			System.err.println(exp);
			return null;
		}

		if (records.size() < 2) {
			System.err.println("No valid data in file: " + source);
			return null;
		}

		int idIndex = -1;
		String[] header = records.get(0);
		for (int i = 0; i < header.length; i++) {
			if (header[i].equalsIgnoreCase(ReaderFactory.COLUMN_ID)) {
				idIndex = i;
				break;
			}
		}
		if (idIndex < 0) {
			System.err.println("ID column missing in file: " + source);
			return null;
		}

		Table<String, String, String> dataSet = HashBasedTable.create();
		for (int i = 1; i < records.size(); i++) {
			String[] columns = records.get(i);
			for (int j = 0; j < columns.length; j++) {
				if (j == idIndex) {
					continue;
				}
				dataSet.put(columns[idIndex], header[j], columns[j]);
			}
		}

		return dataSet;
	}
}
