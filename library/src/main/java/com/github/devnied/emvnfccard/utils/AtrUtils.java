/*
 * Copyright (C) 2013 MILLAU Julien
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.devnied.emvnfccard.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Class used to find ATR description
 * 
 * @author Millau Julien
 * 
 */
public final class AtrUtils {

	public static final String TAG = AtrUtils.class.getSimpleName();

	/**
	 * MultiMap containing ATR
	 */
	private static final Map<String, List<String>> MAP = new HashMap<String, List<String>>();

	static {
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			is = AtrUtils.class.getResourceAsStream("/smartcard_list.txt");
			isr = new InputStreamReader(is, "UTF-8");
			br = new BufferedReader(isr);

			int lineNumber = 0;
			String line;
			String currentATR = null;
			while ((line = br.readLine()) != null) {
				++lineNumber;
				if (line.startsWith("#") || line.trim().length() == 0) { // comment ^#/ empty line ^$/
					continue;
				} else if (line.startsWith("\t") && currentATR != null) {
					List<String> list = MAP.get(currentATR);
					if (list == null) {
						list = new ArrayList<String>();
						MAP.put(currentATR, list);
					}
					list.add(line.replace("\t", "").trim());
				} else if (line.startsWith("3")) { // ATR hex
					currentATR = EmvStringUtils.deleteWhitespace(line.toUpperCase());
				} else {
					Logger.e(TAG, "Encountered unexpected line in atr list: currentATR=" + currentATR + " Line(" + lineNumber
							+ ") = " + line);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			EmvIOUtils.closeQuietly(br);
			EmvIOUtils.closeQuietly(isr);
			EmvIOUtils.closeQuietly(is);
		}
	}

	/**
	 * Method used to find description from ATR
	 * 
	 * @param pAtr
	 *            Card ATR
	 * @return list of description
	 */
	@SuppressWarnings("unchecked")
	public static final Collection<String> getDescription(final String pAtr) {
		Collection<String> ret = null;
		if (!EmvStringUtils.isBlank(pAtr)) {
			String val = EmvStringUtils.deleteWhitespace(pAtr);
			for (String key : MAP.keySet()) {
				if (val.matches("^" + key + "$")) {
					ret = MAP.get(key);
					break;
				}
			}
		}
		return ret;
	}

	/**
	 * Method used to find ATR description from ATS (Answer to select)
	 * 
	 * @param pAts
	 *            EMV card ATS
	 * @return card description
	 */
	@SuppressWarnings("unchecked")
	public static final Collection<String> getDescriptionFromAts(final String pAts) {
		Collection<String> ret = null;
		if (!EmvStringUtils.isBlank(pAts)) {
			String val = EmvStringUtils.deleteWhitespace(pAts);
			for (String key : MAP.keySet()) {
				if (key.contains(val)) { // TODO Fix this
					ret = MAP.get(key);
					break;
				}
			}
		}
		return ret;
	}

	/**
	 * Private constructor
	 */
	private AtrUtils() {
	}

}
