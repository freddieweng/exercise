import com.google.common.collect.Table;

public interface WriterInterface {
	public boolean write(Table<String, String, String> dataSet, String destination);
}
