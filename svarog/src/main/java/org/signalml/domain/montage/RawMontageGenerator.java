/* RawMontageGenerator.java created 2007-11-23
 *
 */

package org.signalml.domain.montage;

import org.springframework.validation.Errors;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class represents a generator for a raw (without type) {@link SourceMontage montage}.
 * It generates raw montage from the given montage and checks if the given
 * montages are valid raw montages.
 * Every montage is valid raw montage.
 *
 * @author Michal Dobaczewski &copy; 2007-2008 CC Otwarte Systemy Komputerowe Sp. z o.o.
 */
@XStreamAlias("rawmontage")
public class RawMontageGenerator implements MontageGenerator {

	private static final long serialVersionUID = 1L;
	private static final Object[] ARGUMENTS = new Object[0];
	private static final String[] CODES = new String[] { "montageGenerator.raw" };

        /**
         * Creates a raw {@link Montage montage} from a given montage.
         * @param montage a montage to be used
         */
	@Override
	public void createMontage(Montage montage) {

		boolean oldMajorChange = montage.isMajorChange();

		try {
			montage.setMajorChange(true);

			montage.reset();

			int size = montage.getSourceChannelCount();
			for (int i=0; i<size; i++) {
				montage.addMontageChannel(i);
			}
		} finally {
			montage.setMajorChange(oldMajorChange);
		}

		montage.setMontageGenerator(this);
		montage.setChanged(false);

	}

        /**
         * Checks if a {@link SourceMontage montage} is a valid raw montage.
         * True for all montages.
         * @param sourceMontage a montage to be checked
         * @param errors an Errors object used to report errors (here never)
         * @return true if a montage is a valid raw montage, false otherwise
         * (here never)
         */
	@Override
	public boolean validateSourceMontage(SourceMontage sourceMontage, Errors errors) {
		// ok for any montage
		return true;
	}

        /**
         * Compares a given object to this generator. Always true if an object
         * is of type RawMontageGenerator (all raw montage generators are equal).
         * @param obj an object to be compared with a current object
         * @return true if obj is equal to this generator (is of type
         * RawMontageGenerator), false otherwise
         */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		return (obj.getClass() == RawMontageGenerator.class);
	}


	@Override
	public Object[] getArguments() {
		return ARGUMENTS;
	}

	@Override
	public String[] getCodes() {
		return CODES;
	}

	@Override
	public String getDefaultMessage() {
		return CODES[0];
	}

}