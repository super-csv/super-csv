package org.supercsv.exception;

/** If anything goes wrong, we throw one of these bad boys here */
public class SuperCSVException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SuperCSVException(final String msg) {
		super(msg);
	}

	public SuperCSVException(final String msg, final Throwable t) {
		super(msg, t);
	}
}
