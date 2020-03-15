/*
 * Copyright 2007 Kasper B. Graversen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.supercsv.prefs;

/**
 * DelayCellProcessorExceptions class for delaying CellProcessor Exceptions. Default Constructor the delayException is
 * false, the CellProcessor will throw immediately. The other Constructors the delayException is true, the CellProcessor
 * Exceptions will throw when row is parsed completely.
 *
 * @author Chen Guoping
 */
public class DelayCellProcessorExceptions {

	private final boolean delayExceptions;

	private final boolean skipExceptionsRow;

	private final CallBackOnException callBack;

	/**
	 * Default Constructor
	 */
	public DelayCellProcessorExceptions() {
		this.delayExceptions = false;
		this.skipExceptionsRow = false;
		this.callBack = null;
	}

	/**
	 * Constructs DelayCellProcessorExceptions with skipCellProcessorExceptionsRow
	 *
	 * @param skipCellProcessorExceptionsRow
	 *           the boolean skipCellProcessorExceptionsRow if value is <tt>true</tt>, then skip Exception row
	 */
	public DelayCellProcessorExceptions(boolean skipCellProcessorExceptionsRow) {
		this.delayExceptions = true;
		this.skipExceptionsRow = skipCellProcessorExceptionsRow;
		this.callBack = new CallBackOnException() {
			public Object process(Object rawColumns) {
				return null;
			}
		};
	}

	/**
	 * Constructs DelayCellProcessorExceptions with skipCellProcessorExceptionsRow and callBack method
	 *
	 * @param skipCellProcessorExceptionsRow
	 *           the boolean skipCellProcessorExceptionsRow if value is <tt>true</tt>, then skip Exception row
	 * @param callBack
	 *           Delay CellProcessor Exception callBack method.
	 */
	public DelayCellProcessorExceptions(boolean skipCellProcessorExceptionsRow, CallBackOnException callBack) {
		if( callBack == null ) {
			throw new NullPointerException("callBack should not be null");
		}
		this.delayExceptions = true;
		this.skipExceptionsRow = skipCellProcessorExceptionsRow;
		this.callBack = callBack;
	}

	/**
	 * Returns the delayExceptions
	 *
	 * @return the delayExceptions
	 */
	public boolean isDelayExceptions() {
		return delayExceptions;
	}

	/**
	 * Returns the skipExceptionsRow
	 *
	 * @return the skipExceptionsRow
	 */
	public boolean isSkipExceptionsRow() {
		return skipExceptionsRow;
	}

	/**
	 * Returns the callBack class
	 * @return the callBack class
	 */
	public CallBackOnException getCallBack() {
		return callBack;
	}
}
