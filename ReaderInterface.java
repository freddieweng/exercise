import com.google.common.collect.Table;

public interface ReaderInterface {
	public Table<String, String, String> read(String source);
}
