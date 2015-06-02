package com.github.devnied.emvnfccard.provider;

import com.github.devnied.emvnfccard.parser.IProvider;
import com.github.devnied.emvnfccard.utils.EmvStringUtils;
import fr.devnied.bitlib.BytesUtils;
import org.fest.assertions.Assertions;

public class ProviderSelectPaymentEnvTest implements IProvider {

	/**
	 * Expected data
	 */
	private String expectedData = EmvStringUtils.EMPTY;

	/**
	 * Returned data
	 */
	private String returnedData = EmvStringUtils.EMPTY;

	@Override
	public byte[] transceive(final byte[] pCommand) {
		Assertions.assertThat(BytesUtils.bytesToStringNoSpace(pCommand)).isEqualTo(expectedData);
		return returnedData != null ? BytesUtils.fromString(returnedData) : null;
	}

	/**
	 * Setter for the field expectedData
	 * 
	 * @param expectedData
	 *            the expectedData to set
	 */
	public void setExpectedData(final String expectedData) {
		this.expectedData = expectedData;
	}

	/**
	 * Setter for the field returnedData
	 * 
	 * @param returnedData
	 *            the returnedData to set
	 */
	public void setReturnedData(final String returnedData) {
		this.returnedData = returnedData;
	}

}
