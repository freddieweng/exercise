
public class ConflictResolverUseLeft implements ConflictResolverInterface {

	@Override
	public <T> T resolve(T left, T right) {
		return left;
	}
}
