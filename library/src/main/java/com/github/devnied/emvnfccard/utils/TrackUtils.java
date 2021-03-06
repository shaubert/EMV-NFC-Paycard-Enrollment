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

import com.github.devnied.emvnfccard.iso7816emv.EmvTags;
import com.github.devnied.emvnfccard.model.EmvCard;
import com.github.devnied.emvnfccard.model.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extract track data
 * 
 * @author MILLAU Julien
 * 
 */
public final class TrackUtils {

	public static final String TAG = TrackUtils.class.getSimpleName();

	/**
	 * Track 2 pattern
	 */
	private static final Pattern TRACK2_PATTERN = Pattern.compile("([0-9]{1,19})D([0-9]{4})([0-9]{3})?(.*)");

	/**
	 * Extract track 2 data
	 * 
	 * @param pEmvCard
	 *            Object card representation
	 * @param pData
	 *            data to parse
	 * @return true if the extraction succeed false otherwise
	 */
	public static boolean extractTrack2Data(final EmvCard pEmvCard, final byte[] pData) {
		boolean ret = false;
		byte[] track2 = TlvUtil.getValue(pData, EmvTags.TRACK_2_EQV_DATA, EmvTags.TRACK2_DATA);

		if (track2 != null) {
			String data = BytesUtils.bytesToStringNoSpace(track2);
			Matcher m = TRACK2_PATTERN.matcher(data);
			// Check pattern
			if (m.find()) {
				// read card number
				pEmvCard.setCardNumber(m.group(1));
				// Read expire date
				SimpleDateFormat sdf = new SimpleDateFormat("yyMM", Locale.getDefault());
				TimeZone gmt = TimeZone.getTimeZone("GMT");
				sdf.setTimeZone(gmt);
				try {
					Date parsedDate = sdf.parse(m.group(2));
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeZone(gmt);
					calendar.setTime(parsedDate);
					calendar.set(Calendar.MILLISECOND, 0);
					calendar.set(Calendar.SECOND, 0);
					calendar.set(Calendar.HOUR_OF_DAY, 0);
					calendar.set(Calendar.DAY_OF_MONTH, 1);
					calendar.set(Calendar.MILLISECOND, 0);
					pEmvCard.setExpireDate(calendar.getTime());
					pEmvCard.setExpireMonth(calendar.get(Calendar.MONTH));
					pEmvCard.setExpireYear(calendar.get(Calendar.YEAR));
				} catch (ParseException e) {
					Logger.e(TAG, "Unparsable expire card date : {}", e);
					return ret;
				}
				// Read service
				pEmvCard.setService(new Service(m.group(3)));
				ret = true;
			}
		}
		return ret;
	}

	/**
	 * Private constructor
	 */
	private TrackUtils() {
	}

}
