/* CheckSignalAction.java created 2010-10-24
 *
 */

package org.signalml.app.action;

import org.signalml.app.view.monitor.CheckSignalDialog;
import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import org.signalml.app.action.selector.SignalDocumentFocusSelector;
import org.signalml.app.document.MonitorSignalDocument;
import org.signalml.app.document.SignalDocument;
import org.signalml.app.model.MontageDescriptor;
import org.springframework.context.support.MessageSourceAccessor;

/** CheckSignalAction
 *
 *
 * @author Tomasz Sawicki
 */
public class CheckSignalAction extends AbstractFocusableSignalMLAction<SignalDocumentFocusSelector> {

	private static final long serialVersionUID = 1L;	
        private CheckSignalDialog checkSignalDialog;

        protected static final Logger logger = Logger.getLogger(CheckSignalAction.class);
	
	public CheckSignalAction(MessageSourceAccessor messageSource, SignalDocumentFocusSelector signalDocumentFocusSelector) {

                super(messageSource, signalDocumentFocusSelector);
		setText("action.checkSignalLabel");
		setToolTip("action.checkSignalToolTip");
	}

	@Override
	public void actionPerformed(ActionEvent ev) {

		logger.debug("Check signal");
                
                SignalDocument signalDocument = getActionFocusSelector().getActiveSignalDocument();
		if (signalDocument == null) {
			logger.warn("Target document doesn't exist or is not a signal");
			return;
		}

		MontageDescriptor descriptor = new MontageDescriptor(signalDocument.getMontage(), signalDocument);

		boolean ok = checkSignalDialog.showDialog(descriptor, true);
		if (!ok) {
			return;
		}

		signalDocument.setMontage(descriptor.getMontage());
	}

	@Override
	public void setEnabledAsNeeded() {

                SignalDocument signalDocument = getActionFocusSelector().getActiveSignalDocument();
		setEnabled((signalDocument != null) && (signalDocument instanceof MonitorSignalDocument));
	}

	public CheckSignalDialog getCheckSignalDialog() {

                return checkSignalDialog;
	}

	public void setCheckSignalDialog(CheckSignalDialog checkSignalDialog) {

                if( checkSignalDialog == null ) {
			throw new NullPointerException();
		}
		this.checkSignalDialog = checkSignalDialog;
	}
}