/* EEGLabExportDialog.java created 2010-11-29
 *
 */

package org.signalml.app.view.components.dialogs;

import static org.signalml.app.util.i18n.SvarogI18n._;

import java.awt.BorderLayout;
import java.awt.Window;
import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.signalml.app.document.SignalDocument;
import org.signalml.app.model.montage.MontageDescriptor;
import org.signalml.app.view.components.FileSelectPanel;
import org.signalml.domain.montage.Montage;
import org.signalml.domain.montage.SourceMontage;
import org.signalml.domain.signal.eeglab.EEGLabSignalWriter;
import org.signalml.plugin.export.SignalMLException;

import org.springframework.validation.Errors;

/**
 * Enables saving the signal to a chosen file in the EEGLab format.
 *
 * @author Tomasz Sawicki
 */
public class EEGLabExportDialog extends AbstractDialog   {

        /**
         * Currently open document.
         */
        private SignalDocument signalDocument;

        /**
         * Current montage.
         */
        private Montage currentMontage;

        /**
         * A {@link FileSelectPanel} object - part of the interface.
         */
        private FileSelectPanel fileSelectPanel;

	/**
	 * Constructor. Sets parent window and if this dialog
	 * blocks top-level windows.
	 * @param w the parent window or null if there is no parent
	 * @param isModal dialog blocks top-level windows if true
	 */
        public EEGLabExportDialog(Window w, boolean isModal) {
                super(w, isModal);
        }

        /**
         * Sets window's title then calls {@link AbstractDialog#initialize()}.
         */
	@Override
	protected void initialize() {

		setTitle(_("Export to EEGLab"));
		super.initialize();
	}

        /**
         * Creates the interface - a panel with {@link #fileSelectPanel}.
         *
         * @return the interface
         */
        @Override
        protected JComponent createInterface() {

                JPanel interfacePanel = new JPanel(new BorderLayout());                
                CompoundBorder panelBorder = new CompoundBorder(new TitledBorder(""), new EmptyBorder(3, 3, 3, 3));
                interfacePanel.setBorder(panelBorder);
                interfacePanel.add(getFileSelectPanel(), BorderLayout.CENTER);
                return interfacePanel;
        }

        /**
         * Gets the {@link #fileSelectPanel}.
         *
         * @return {@link #fileSelectPanel}
         */
	private FileSelectPanel getFileSelectPanel() {

		if (fileSelectPanel == null) {

                        HashMap<String, String[]> filters = new HashMap<String, String[]>();
                        filters.put(_("Dateset files"), new String[] { "set" } );
			fileSelectPanel = new FileSelectPanel(_("Output file name: "), filters, true);
		}
		return fileSelectPanel;
	}

        @Override
        public boolean supportsModelClass(Class<?> clazz) {

                return MontageDescriptor.class.isAssignableFrom(clazz);
        }

        @Override
        public void fillDialogFromModel(Object model) throws SignalMLException {

                MontageDescriptor descriptor = (MontageDescriptor) model;
		Montage montage = descriptor.getMontage();
		signalDocument = descriptor.getSignalDocument();

		if (montage == null) {
                        currentMontage = new Montage(new SourceMontage(signalDocument));
                } else {
                        currentMontage = new Montage(montage);
                }

		getOkButton().setVisible(true);
		getRootPane().setDefaultButton(getOkButton());
        }

        @Override
        public void fillModelFromDialog(Object model) throws SignalMLException {

		MontageDescriptor descriptor = (MontageDescriptor) model;
		descriptor.setMontage(currentMontage);
        }

        /**
         * On close with ok export the signal, clear the text box,
         * then call {@link AbstractDialog#onDialogCloseWithOK()}.
         */
        @Override
        protected void onDialogCloseWithOK() {
                
                String fileName = getFileSelectPanel().getFileName();
                fileSelectPanel.setFileName("");
                
                EEGLabSignalWriter signalWriter = new EEGLabSignalWriter((signalDocument));
                try {
                        signalWriter.writeSignal(fileName);
                } catch (IOException ex) {
                        Logger.getLogger(EEGLabExportDialog.class.getName()).log(Level.SEVERE, null, ex);
                }

                super.onDialogCloseWithOK();
	}

        /**
         * On close clear the text box, then call {@link AbstractDialog#onDialogClose()}.
         */
        @Override
        protected void onDialogClose() {

                fileSelectPanel.setFileName("");
                super.onDialogClose();                
        }

        /**
         * Checks wheter the file name is correct.
         *
         * @param model model for this panel
         * @param errors object in whick errors are stored
         * @throws SignalMLException TODO when it is thrown
         */
        @Override
	public void validateDialog(Object model, Errors errors) throws SignalMLException {

                super.validateDialog(model, errors);
		fillModelFromDialog(model);

                String fileName = getFileSelectPanel().getFileName();
                File file = new File(fileName);

		if (fileName.isEmpty()) {
			errors.reject("error.eeglabExport.incorrectFileName");
		}
		else if (file.exists()) {
			int anwser = JOptionPane.showConfirmDialog(null, _("File already exists! Do you want to overwrite?"));
			if (anwser == JOptionPane.CANCEL_OPTION || anwser == JOptionPane.NO_OPTION)
				errors.reject("");

                        if (!file.canWrite()) {
                                errors.reject("error.eeglabExport.cantWrite");
                        }
		}
	}
}