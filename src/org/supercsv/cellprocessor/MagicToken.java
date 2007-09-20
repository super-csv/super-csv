/*
 * SuperCSV is Copyright 2007, Kasper B. Graversen Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * This class is deprecated and will be removed in comming releases. Instead you should use the <tt>Token</tt>
 * cellprocessor instead
 * 
 * @deprecated
 * @author Kasper B. Graversen
 */
@Deprecated
public class MagicToken extends Token {

	/**
	 * @deprecated use Token class instead
	 */
	@Deprecated
	public MagicToken(final Object magicToken, final Object returnValue) {
		super(magicToken, returnValue);
	}

	/**
	 * @deprecated use Token class instead
	 */
	@Deprecated
	public MagicToken(final Object magicToken, final Object returnValue, final CellProcessor next) {
		super(magicToken, returnValue, next);
	}
}
