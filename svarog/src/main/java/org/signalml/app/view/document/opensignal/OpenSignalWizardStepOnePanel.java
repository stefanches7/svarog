package org.signalml.app.view.document.opensignal;

import static org.signalml.app.util.i18n.SvarogI18n._;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.signalml.app.model.document.opensignal.AbstractOpenSignalDescriptor;
import org.signalml.app.view.document.opensignal.elements.ManageSignalMLCodecsButtonPanel;
import org.signalml.app.view.document.opensignal.elements.ManageSignalMLCodecsDialog;
import org.signalml.app.view.document.opensignal.elements.SignalParametersPanel;
import org.signalml.app.view.document.opensignal.elements.SignalSourceTabbedPane;
import org.signalml.app.view.document.opensignal_old.SignalSource;
import org.signalml.app.view.document.opensignal_old.monitor.ChannelSelectPanel;
import org.signalml.app.view.document.opensignal_old.monitor.TagPresetSelectionPanel;
import org.signalml.app.view.montage.EegSystemSelectionPanel;
import org.signalml.app.view.workspace.ViewerElementManager;
import org.signalml.domain.montage.system.EegSystem;

public class OpenSignalWizardStepOnePanel extends JPanel implements ChangeListener, PropertyChangeListener {

	private SignalSourceTabbedPane signalSourceTabbedPane;
	private ViewerElementManager viewerElementManager;

	private SignalParametersPanel signalParametersPanel;
	private ChannelSelectPanel channelSelectPanel;
	
	private EegSystemSelectionPanel eegSystemSelectionPanel;
	private TagPresetSelectionPanel tagPresetSelectionPanel;
	private ManageSignalMLCodecsButtonPanel manageSignalMLCodecsButtonPanel;
	
	private AbstractOpenSignalDescriptor openSignalDescriptor;

	public OpenSignalWizardStepOnePanel(ViewerElementManager viewerElementManager) {
		this.viewerElementManager = viewerElementManager;
		
		this.setLayout(new GridLayout(1, 2));
		this.add(createLeftPanel());
		this.add(createRightPanel());
		
		prepareChannelsForSignalSource();
	}
	
	protected JPanel createLeftPanel() {
		JPanel leftPanel = new JPanel(new BorderLayout());

		leftPanel.add(getSignalSourceTabbedPane(), BorderLayout.NORTH);
		signalParametersPanel = new SignalParametersPanel();
		leftPanel.add(signalParametersPanel, BorderLayout.CENTER);
		
		return leftPanel;
	}
	
	protected JPanel createRightPanel() {
		JPanel rightPanel = new JPanel(new BorderLayout());
		
		channelSelectPanel = new ChannelSelectPanel();
		rightPanel.add(channelSelectPanel, BorderLayout.CENTER);
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
		
		southPanel.add(getTagPresetSelectionPanel());
		southPanel.add(getEegSystemSelectionPanel());
		southPanel.add(getManageSignalMLCodecsButtonPanel());
		
		rightPanel.add(southPanel, BorderLayout.SOUTH);
		
		return rightPanel;
	}
	
	/**
	 * Returns the panel for selecting tag style preset to be used by the
	 * monitor to be opened.
	 * @return panel for selecting tag style preset
	 */
	public TagPresetSelectionPanel getTagPresetSelectionPanel() {
		if (tagPresetSelectionPanel == null) {
			tagPresetSelectionPanel = new TagPresetSelectionPanel(viewerElementManager.getStyledTagSetPresetManager());
		}
		return tagPresetSelectionPanel;
	}
	
	/**
	 * Returns the panel for selecting the currently used {@link EegSystem
	 * EEG system}.
	 * @return the EEG system selection panel
	 */
	protected EegSystemSelectionPanel getEegSystemSelectionPanel() {
		if (eegSystemSelectionPanel == null) {
			eegSystemSelectionPanel = new EegSystemSelectionPanel(viewerElementManager.getEegSystemsPresetManager());
		}
		return eegSystemSelectionPanel;
	}
	
	protected ManageSignalMLCodecsButtonPanel getManageSignalMLCodecsButtonPanel() {
		if (manageSignalMLCodecsButtonPanel == null)
			manageSignalMLCodecsButtonPanel = new ManageSignalMLCodecsButtonPanel(viewerElementManager);
		return manageSignalMLCodecsButtonPanel;
	}
	
	protected SignalSourceTabbedPane getSignalSourceTabbedPane() {
		if (signalSourceTabbedPane == null) {
			signalSourceTabbedPane = new SignalSourceTabbedPane(viewerElementManager);
			signalSourceTabbedPane.addChangeListener(this);
			signalSourceTabbedPane.addPropertyChangeListener(this);
		}
		return signalSourceTabbedPane;
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		if (event.getSource() == signalSourceTabbedPane) {
			prepareChannelsForSignalSource();
			fillPanelFromModel(signalSourceTabbedPane.getOpenSignalDescriptor());
		}
	}

	protected void prepareChannelsForSignalSource() {
		SignalSource selectedSignalSource = signalSourceTabbedPane.getSelectedSignalSource();
		signalParametersPanel.preparePanelForSignalSource(selectedSignalSource);
		channelSelectPanel.preparePanelForSignalSource(selectedSignalSource);
		tagPresetSelectionPanel.setEnabledAll(selectedSignalSource.isOpenBCI());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (SignalSourceTabbedPane.OPEN_SIGNAL_DESCRIPTOR_PROPERTY.equals(evt.getPropertyName())) {
			openSignalDescriptor = (AbstractOpenSignalDescriptor) evt.getNewValue();
			fillPanelFromModel(openSignalDescriptor);
		}
	}
	
	public void fillPanelFromModel(AbstractOpenSignalDescriptor openSignalDescriptor) {
		signalParametersPanel.fillPanelFromModel(openSignalDescriptor);
		channelSelectPanel.fillPanelFromModel(openSignalDescriptor);
		eegSystemSelectionPanel.fillPanelFromModel(openSignalDescriptor);
	}

	public AbstractOpenSignalDescriptor getOpenSignalDescriptor() {
		if (openSignalDescriptor != null)
			signalParametersPanel.fillModelFromPanel(openSignalDescriptor);
		return openSignalDescriptor;
	}
}
