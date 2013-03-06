package org.ilaborie.jgit.flow.fun;

import org.eclipse.jgit.lib.Constants;

import com.google.common.base.Predicate;

/**
 * The Class PrefixTruncacteFunction.
 */
public class PrefixPredicate implements Predicate<String> {

	/** The Constant LOCAL_PREFIX. */
	public static final Predicate<String> LOCAL_PREFIX = new PrefixPredicate(
			Constants.R_HEADS);

	/** The prefix. */
	private final String prefix;

	/**
	 * Instantiates a new prefix truncate function.
	 * 
	 * @param prefix
	 *            the prefix
	 */
	public PrefixPredicate(String prefix) {
		super();
		this.prefix = prefix;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	public boolean apply(String s) {
		return (s != null && s.startsWith(prefix));
	}

}