/* RawSignalMRUDEntry.java created 2008-01-30
 *
 */

package org.signalml.app.document;

import org.signalml.domain.signal.raw.RawSignalDescriptor;
import org.signalml.plugin.export.signal.Document;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Serializable description of a file with a signal stored in it in a raw
 * form. Contains:
 * <ul>
 * <li>the {@link RawSignalDescriptor descriptor} of the parameters of the
 * signal</li>
 * </ul>
 *
 * @author Michal Dobaczewski &copy; 2007-2008 CC Otwarte Systemy Komputerowe Sp. z o.o.
 */
@XStreamAlias("mrud-rawsignal")
public class RawSignalMRUDEntry extends MRUDEntry {

	/**
	 * the {@link RawSignalDescriptor descriptor} of the parameters of the
	 * signal
	 */
	private RawSignalDescriptor descriptor;

	/**
	 * Empty constructor.
	 */
	protected RawSignalMRUDEntry() {
		super();
	}

	/**
	 * Constructor. Sets:
	 * <ul>
	 * <li>the type of {@link ManagedDocumentType type} of the {@link Document
	 * document},</li>
	 * <li>the class of the document,</li>
	 * <li>the new file created on the basis of the provided path,</li>
	 * <li>the path to the file converted to the absolute path,</li>
	 * <li>the {@link RawSignalDescriptor descriptor} of the parameters of the
	 * signal.</li>
	 * </ul>
	 * @param documentType the type of type of the document
	 * @param documentClass the class of the document
	 * @param path the path to the file
	 * @param descriptor the descriptor of the parameters of the signal
	 */
	public RawSignalMRUDEntry(ManagedDocumentType documentType, Class<?> documentClass, String path, RawSignalDescriptor descriptor) {
		super(documentType, documentClass, path);
		this.descriptor = descriptor;
	}

	/**
	 * Returns the {@link RawSignalDescriptor descriptor} of the parameters of
	 * the signal.
	 * @return the descriptor of the parameters of the signal
	 */
	public RawSignalDescriptor getDescriptor() {
		return descriptor;
	}

	/**
	 * Sets the {@link RawSignalDescriptor descriptor} of the parameters of
	 * the signal.
	 * @param descriptor the descriptor of the parameters of the signal
	 */
	public void setDescriptor(RawSignalDescriptor descriptor) {
		this.descriptor = descriptor;
	}

}