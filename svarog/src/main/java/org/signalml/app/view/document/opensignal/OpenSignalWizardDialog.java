package org.signalml.app.view.document.opensignal;

import static org.signalml.app.util.i18n.SvarogI18n._;

import java.awt.Window;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.signalml.app.document.ManagedDocumentType;
import org.signalml.app.model.document.OpenDocumentDescriptor;
import org.signalml.app.model.document.opensignal.AbstractOpenSignalDescriptor;
import org.signalml.app.model.document.opensignal.ExperimentDescriptor;
import org.signalml.app.model.document.opensignal.elements.SignalParameters;
import org.signalml.app.view.components.dialogs.AbstractWizardDialog;
import org.signalml.app.view.components.dialogs.errors.Dialogs;
import org.signalml.app.view.montage.SignalMontagePanel;
import org.signalml.app.view.workspace.ViewerElementManager;
import org.signalml.domain.montage.Montage;
import org.signalml.domain.montage.SignalConfigurer;
import org.signalml.domain.montage.system.EegSystem;
import org.signalml.plugin.export.SignalMLException;

import org.apache.log4j.Logger;

public class OpenSignalWizardDialog extends AbstractWizardDialog {
	protected static final Logger log = Logger.getLogger(OpenSignalWizardDialog.class);

	private static final long serialVersionUID = -6697344610944631342L;

	private ViewerElementManager viewerElementManager;

	private OpenSignalWizardStepOnePanel stepOnePanel;
	private SignalMontagePanel stepTwoPanel;

	public OpenSignalWizardDialog(ViewerElementManager viewerElementManager, Window f, boolean isModal) {
		super(f, isModal);
		this.viewerElementManager = viewerElementManager;
		this.setTitle(_("Open signal"));
	}

	@Override
	public int getStepCount() {
		return 2;
	}

	@Override
	protected boolean onStepChange(int toStep, int fromStep, Object model)
	throws SignalMLException {

		if (toStep == 1 && fromStep == 0) {
			AbstractOpenSignalDescriptor openSignalDescriptor = getStepOnePanel().getOpenSignalDescriptor();

			if (openSignalDescriptor == null) {
				Dialogs.showError(_("Please select a proper file or monitor signal source!"));
				return false;
			}

			SignalParameters signalParameters = openSignalDescriptor.getSignalParameters();
			Montage createdMontage = SignalConfigurer.createMontage(signalParameters.getChannelCount());

			EegSystem selectedEegSystem = getStepOnePanel().getOtherSettingsPanel().getSelectedEegSystem();
			createdMontage.setEegSystem(selectedEegSystem);

			String[] channelLabels = openSignalDescriptor.getChannelLabels();
			for (int i = 0; i < channelLabels.length; i++) {
				createdMontage.setSourceChannelLabelAt(i, channelLabels[i]);
			}
			createdMontage.getMontageGenerator().createMontage(createdMontage);

			getSignalMontagePanel().fillPanelFromModel(createdMontage);
			getSignalMontagePanel().setSamplingFrequency(signalParameters.getSamplingFrequency());

			getSignalMontagePanel().resetPreset();
		}

		return super.onStepChange(toStep, fromStep, model);
	}

	@Override
	protected JComponent createInterfaceForStep(int step) {

		switch (step) {
		case 0:
			return getStepOnePanel();
		case 1:
			return getSignalMontagePanel();
		default:
			return new JPanel();
		}

	}

	protected OpenSignalWizardStepOnePanel getStepOnePanel() {
		if (stepOnePanel == null) {
			stepOnePanel = new OpenSignalWizardStepOnePanel(viewerElementManager);
		}
		return stepOnePanel;
	}

	protected SignalMontagePanel getSignalMontagePanel() {
		if (stepTwoPanel == null) {
			stepTwoPanel = new SignalMontagePanel(viewerElementManager);
		}
		return stepTwoPanel;
	}

	@Override
	public boolean supportsModelClass(Class<?> clazz) {
		return OpenDocumentDescriptor.class.isAssignableFrom(clazz);
	}

	@Override
	protected void fillDialogFromModel(Object model) throws SignalMLException {
	}

	@Override
	public void fillModelFromDialog(Object model) throws SignalMLException {
		OpenDocumentDescriptor openDocumentDescriptor = (OpenDocumentDescriptor) model;

		AbstractOpenSignalDescriptor openSignalDescriptor = getStepOnePanel().getOpenSignalDescriptor();

		if (openSignalDescriptor instanceof ExperimentDescriptor) {
			openDocumentDescriptor.setType(ManagedDocumentType.MONITOR);
		}
		else {
			openDocumentDescriptor.setType(ManagedDocumentType.SIGNAL);
			File file = getStepOnePanel().getSignalSourceTabbedPane().getFileChooserPanel().getSelectedFile();
			openDocumentDescriptor.setFile(file);
		}
		openSignalDescriptor.setMontage(getSignalMontagePanel().getCurrentMontage());

		openDocumentDescriptor.setOpenSignalDescriptor(openSignalDescriptor);

	}

	@Override
	public boolean isFinishAllowedOnStep(int step) {
		return step == 1;
	}

	@Override
	protected void onDialogCloseWithOK() {
		log.debug("onDialogCloseWithOK");
		super.onDialogCloseWithOK();

		this.getStepOnePanel().onDialogCloseWithOK();
	}
}