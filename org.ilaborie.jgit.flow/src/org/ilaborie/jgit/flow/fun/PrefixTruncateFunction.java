package org.ilaborie.jgit.flow.fun;

import org.eclipse.jgit.lib.Constants;

import com.google.common.base.Function;

/**
 * The Class PrefixTruncacteFunction.
 */
public class PrefixTruncateFunction implements Function<String, String> {

	/** The Constant LOCAL_PREFIX. */
	public static final Function<String, String> LOCAL_PREFIX = new PrefixTruncateFunction(
			Constants.R_HEADS);

	/** The prefix. */
	private final String prefix;

	/**
	 * Instantiates a new prefix truncate function.
	 * 
	 * @param prefix
	 *            the prefix
	 */
	public PrefixTruncateFunction(String prefix) {
		super();
		this.prefix = prefix;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	public String apply(String s) {
		String result = null;
		if (s != null && s.startsWith(prefix)) {
			result = s.substring(prefix.length());
		}

		return result;
	}

}
