package org.ilaborie.jgit.flow.fun;

import org.eclipse.jgit.lib.Ref;

import com.google.common.base.Function;

/**
 * The Class FunctionImplementation.
 */
public class RefNameFunction implements Function<Ref, String> {

	/** The Constant INSTANCE. */
	public static final RefNameFunction INSTANCE = new RefNameFunction();

	/**
	 * Instantiates a new ref name function.
	 */
	private RefNameFunction() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	public String apply(Ref ref) {
		return ref != null ? ref.getName() : null;
	}
}