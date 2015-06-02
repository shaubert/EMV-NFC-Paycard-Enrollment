package com.github.devnied.emvnfccard.parser.apdu;

import com.github.devnied.emvnfccard.parser.apdu.annotation.AnnotationData;
import com.github.devnied.emvnfccard.parser.apdu.annotation.Data;
import com.github.devnied.emvnfccard.utils.EmvStringUtils;
import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

public class AnnotationDataTest {

	@Data(index = 1, tag = "", size = 12)
	public String value;

	@Data(index = 2, tag = "", size = 12)
	public String value2;

	@Test
	public void testEquals() {

		AnnotationData data1 = new AnnotationData();
		data1.initFromAnnotation(getField(AnnotationDataTest.class, "value").getAnnotation(Data.class));

		AnnotationData data2 = new AnnotationData();
		data2.initFromAnnotation(getField(AnnotationDataTest.class, "value").getAnnotation(Data.class));
		Assertions.assertThat(data1).isEqualTo(data2);

		AnnotationData data3 = new AnnotationData();
		data3.initFromAnnotation(getField(AnnotationDataTest.class, "value2").getAnnotation(Data.class));
		Assertions.assertThat(data1).isNotEqualTo(data3);
	}

	public static Field getField(Class<?> clazz, String fieldName)
			throws IllegalStateException {
		Assert.assertFalse("Class required", clazz == null);
		Assert.assertFalse("Field name required", EmvStringUtils.isEmpty(fieldName));

		try {
			return clazz.getDeclaredField(fieldName);
		}
		catch (NoSuchFieldException nsf) {
			// Try superclass
			if (clazz.getSuperclass() != null) {
				return getField(clazz.getSuperclass(), fieldName);
			}

			throw new IllegalStateException("Could not locate field '" + fieldName
					+ "' on class " + clazz);
		}
	}

}
