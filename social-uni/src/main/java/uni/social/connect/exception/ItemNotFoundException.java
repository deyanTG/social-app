package uni.social.connect.exception;

import org.springframework.dao.EmptyResultDataAccessException;

public class ItemNotFoundException extends EmptyResultDataAccessException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6939940925813013132L;

	public ItemNotFoundException(Class<?> clazz, Long id) {
        super("No " + clazz.getSimpleName() + " entity with id " + id + " exists!", 1);
    }
}
