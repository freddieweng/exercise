import java.util.Map;
import java.util.Set;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

public class RecordMerger {

	public static final String FILENAME_COMBINED = "combined.csv";

	private Table<String, String, String> mergedDataSet;
	private ConflictResolverInterface resolver;

	/**
	 * Entry point of this test.
	 *
	 * @param args command line arguments: first.html and second.csv.
	 * @throws Exception bad things had happened.
	 */
	public static void main(final String[] args) throws Exception {

		if (args.length == 0) {
			System.err.println("Usage: java RecordMerger file1 [ file2 [...] ]");
			System.exit(1);
		}

		// your code starts here.
		RecordMerger merger = new RecordMerger(new ConflictResolverUseLeft());
		for (String source : args) {
			if (merger.add(source)) {
				System.out.println("Managed to merge file: " + source);
			} else {
				System.err.println("Failed to merge file: " + source);
			}
		}

		if (merger.dump(FILENAME_COMBINED)) {
			System.out.println("Final data dumped to file: " + FILENAME_COMBINED);
		} else {
			System.out.println("Final data cannot dump to file: " + FILENAME_COMBINED);
		}
	}

	public RecordMerger(ConflictResolverInterface resolver) {
		this.mergedDataSet = TreeBasedTable.create();
		this.resolver = resolver;
	}

	private boolean add(String source) {
		Table<String, String, String> dataSet = ReaderFactory.getInstance().readDataSet(source);
		if (dataSet == null) {
			return false;
		}

		mergeDataSet(mergedDataSet, dataSet);
		return true;
	}

	private void mergeDataSet(Table<String, String, String> left, Table<String, String, String> right) {

		Set<String> rightRowKeys = right.rowKeySet();
		for (String rowKey : rightRowKeys) {
			Map<String, String> rightColumns = right.row(rowKey);
			if (!left.containsRow(rowKey)) {
				rightColumns.forEach((k, v) -> left.put(rowKey, k, v));
			} else {
				Map<String, String> leftColumns = left.row(rowKey);
				rightColumns.forEach((k, v) -> {
					if (!leftColumns.containsKey(k)) {
						left.put(rowKey, k, v);
					} else if (!v.equalsIgnoreCase(leftColumns.get(k))) {
						// merge conflict!! Log it and proceed using ConflictResolver.
						System.out.println("Attention: Merge conflict!! left value(" + leftColumns.get(k)
								+ ") vs. right value(" + v + ")");
						left.put(rowKey, k, resolver.resolve(leftColumns.get(k), v));
					}
				});
			}
		}
	}

	private boolean dump(String destination) {
		return WriterFactory.getInstance().dump(mergedDataSet, destination);
	}
}
