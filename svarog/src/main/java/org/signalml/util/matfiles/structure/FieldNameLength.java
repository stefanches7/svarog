package org.signalml.util.matfiles.structure;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import org.signalml.util.matfiles.DataElement;
import org.signalml.util.matfiles.types.DataType;

public class FieldNameLength extends DataElement {

	public static int MAXIMUM_FIELD_NAME_LENGTH = 32;

//	public FieldNameLength() {
//		super(DataType.MI_INT32, MAXIMUM_FIELD_NAME_LENGTH);
//	}

	private int maximumSize;

	public FieldNameLength(List<String> keys) {
		super(DataType.MI_INT32);

		maximumSize = 0;
		for (String key: keys)
			if (key.length() > maximumSize)
				maximumSize = key.length();

		maximumSize++; //NULL termination
	}

	public int getFieldNameMaximumSize() {
		return maximumSize;
	}

	@Override
	public void write(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeShort(4);
		dataOutputStream.writeShort(dataType.getValue());

		dataOutputStream.writeInt(maximumSize);
	}

}
