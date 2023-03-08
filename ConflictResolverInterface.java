
public interface ConflictResolverInterface {
	public <T> T resolve(T left, T right);
}
