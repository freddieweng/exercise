import java.io.File;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class ReaderHtml implements ReaderInterface {

	@Override
	public Table<String, String, String> read(String source) {
		Elements rows;
		int idIndex = -1;

		try {
			Document doc = Jsoup.parse(new File(source), "UTF-8");
			Element tab = doc.select("table").get(0); // select the first table.
			rows = tab.select("tr");
		} catch (Exception exp) {
			System.err.println(exp);
			return null;
		}

		if (rows == null || rows.size() < 2) {
			System.err.println("No valid data in file: " + source);
			return null;
		}

		Element headerRow = rows.get(0);
		Elements headerCols = headerRow.select("th");
		for (int i = 0; i < headerCols.size(); i++) {
			if (headerCols.get(i).text().equalsIgnoreCase(ReaderFactory.COLUMN_ID)) {
				idIndex = i;
				break;
			}
		}
		if (idIndex < 0) {
			System.err.println("ID column missing in file: " + source);
			return null;
		}

		Table<String, String, String> dataSet = HashBasedTable.create();
		for (int i = 1; i < rows.size(); i++) {
			Elements columns = rows.get(i).select("td");
			for (int j = 0; j < columns.size(); j++) {
				if (j == idIndex) {
					continue;
				}
				dataSet.put(columns.get(idIndex).text(), headerCols.get(j).text(), columns.get(j).text());
			}
		}

		return dataSet;
	}
}
