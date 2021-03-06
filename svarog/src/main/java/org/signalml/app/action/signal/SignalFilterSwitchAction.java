/* SignalFilterSwitchAction.java created 2008-02-07
 *
 */

package org.signalml.app.action.signal;

import static org.signalml.app.util.i18n.SvarogI18n._;

import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;
import org.signalml.app.action.AbstractFocusableSignalMLAction;
import org.signalml.app.action.selector.SignalDocumentFocusSelector;
import org.signalml.app.document.signal.SignalDocument;
import org.signalml.domain.montage.Montage;

/** SignalFilterSwitchAction
 *
 *
 * @author Michal Dobaczewski &copy; 2007-2008 CC Otwarte Systemy Komputerowe Sp. z o.o.
 */
public class SignalFilterSwitchAction extends AbstractFocusableSignalMLAction<SignalDocumentFocusSelector> {

	private static final long serialVersionUID = 1L;

	protected static final Logger logger = Logger.getLogger(SignalFilterSwitchAction.class);

	public SignalFilterSwitchAction(SignalDocumentFocusSelector signalDocumentFocusSelector) {
		super(signalDocumentFocusSelector);
		setText(_("Filtering"));
		setIconPath("org/signalml/app/icon/filter.png");
		setToolTip(_("Switch filtering on/off"));
	}

	@Override
	public void actionPerformed(ActionEvent ev) {

		logger.debug("Filter switch");

		SignalDocument signalDocument = getActionFocusSelector().getActiveSignalDocument();
		if (signalDocument == null) {
			logger.warn("Target document doesn't exist or is not a signal");
			return;
		}

		ItemSelectable button = (ItemSelectable) ev.getSource();
		Object[] selectedObjects = button.getSelectedObjects();
		boolean selected = (selectedObjects != null && selectedObjects.length != 0);

		putValue(SELECTED_KEY, new Boolean(selected));
		Montage montage = signalDocument.getMontage();
		if (selected != montage.isFilteringEnabled()) {
			montage = new Montage(montage);
			montage.setFilteringEnabled(selected);
			signalDocument.setMontage(montage);
		}

	}

	@Override
	public void setEnabledAsNeeded() {
		setEnabled(getActionFocusSelector().getActiveSignalDocument() != null);
	}

}
