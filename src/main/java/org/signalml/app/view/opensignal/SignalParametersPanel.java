package org.signalml.app.view.opensignal;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI.ChangeHandler;
import org.signalml.app.action.selector.ActionFocusManager;
import org.signalml.app.view.element.IntegerSpinner;
import org.signalml.app.config.ApplicationConfiguration;
import org.signalml.app.view.element.ResolvableComboBox;
import org.signalml.codec.SignalMLCodecException;
import org.signalml.plugin.export.SignalMLException;
import org.springframework.context.support.MessageSourceAccessor;

/**
 * Panel allowing to present and edit signal parameters.
 *
 * @author Tomasz Sawicki
 */
public class SignalParametersPanel extends JPanel {

	public static String NUMBER_OF_CHANNELS_CHANGED_PROPERTY = "numberOfChannelsChangedProperty";

	/**
	 * the {@link MessageSourceAccessor source} of messages (labels).
	 */
	private MessageSourceAccessor messageSource;

        /**
         * Current model.
         */
        private Object currentModel;

        /**
	 * the combo box with the sampling frequency.
	 */
	private JComboBox samplingFrequencyComboBox;

	/**
	 * the text field with the number of channels.
	 */
	private IntegerSpinner channelCountSpinner;

	/**
	 * A text field allwing to change byte order.
	 */
	private ResolvableComboBox byteOrderComboBox;

	/**
	 * A text field allwing to change the sample type.
	 */
	private ResolvableComboBox sampleTypeComboBox;

	/**
	 * the text field with the size of the page of signal in seconds.
	 */
	private JTextField pageSizeField;

	/**
	 * the text field with the number of blocks that fit into one page of
	 * the signal.
	 */
	private JTextField blocksPerPageField;

        /**
         * Button that opens a dialog allowing to edit gain and offset.
         */
        private JButton editGainAndOffsetButton;

        /**
         * Application configuration.
         */
        private ApplicationConfiguration applicationConfiguration;

        /**
         * Edit gain and offset dialog.
         */
        private EditGainAndOffsetDialog editGainAndOffsetDialog;

        /**
         * Default constructor. Creates the interface.
         * 
         * @param messageSource {@link #messageSource}
         */
        public SignalParametersPanel(MessageSourceAccessor messageSource, ApplicationConfiguration applicationConfiguration) {

                super();
                this.messageSource = messageSource;
                this.applicationConfiguration = applicationConfiguration;
                createInterface();
        }

        /**
         * Fills this panel from a model
         *
         * @param model the model
         * @throws SignalMLException when model is not supported
         */
        public final void fillPanelFromModel(Object model) throws SignalMLException {

                if (model instanceof AmplifierConnectionDescriptor) {
                        fillPanelForAmplifierConnection((AmplifierConnectionDescriptor) model);
                } else {
                        setEnabledAll(false);
                        throw new SignalMLCodecException(messageSource.getMessage("error.modelNotSupported"));
                }

                currentModel = model;
        }

        /**
         * Fills a model from this panel.
         *
         * @param model the model
         * @throws SignalMLException when input data is invalid or model is
         * not supported.
         */
        public final void fillModelFromPanel(Object model) throws SignalMLException {

                if (model instanceof AmplifierConnectionDescriptor) {
                        fillModelForAmplifierConnection((AmplifierConnectionDescriptor) model);
                } else {
                        setEnabledAll(false);
                        throw new SignalMLCodecException(messageSource.getMessage("error.modelNotSupported"));
                }
        }

        /**
         * Fills this panel for amplifier connection
         *
         * @param descriptor the descriptor
         */
        private void fillPanelForAmplifierConnection(AmplifierConnectionDescriptor descriptor) {
                
                if (descriptor.getAmplifierInstance() == null) {
                        setEnabledAll(false);
                } else {
                        setEnabledAll(true);

                        getSamplingFrequencyComboBox().setEditable(false);
                        getSamplingFrequencyComboBox().setModel(new DefaultComboBoxModel(
                                descriptor.getAmplifierInstance().getDefinition().getAvailableFrequencies().toArray()));

                        getChannelCountSpinner().setEnabled(false);
                        getChannelCountSpinner().setValue(String.valueOf(
                                descriptor.getOpenMonitorDescriptor().getChannelCount()));

                        getByteOrderComboBox().setModel(new DefaultComboBoxModel());
                        getByteOrderComboBox().setEnabled(false);

                        getSampleTypeComboBox().setModel(new DefaultComboBoxModel());
                        getSampleTypeComboBox().setEnabled(false);

                        double pageSize = applicationConfiguration.getPageSize();
                        try {
                                pageSize = descriptor.getOpenMonitorDescriptor().getPageSize();
                        } catch (Exception ex) {
                        }
                        getPageSizeField().setText(String.valueOf(pageSize));

                        getBlocksPerPageField().setText("");
                        getBlocksPerPageField().setEditable(false);
                }
        }

        /**
         * Fills the model for amplifier connection
         *
         * @param descriptor the descriptor
         * @throws SignalMLException when input data is invalid
         */
        private void fillModelForAmplifierConnection(AmplifierConnectionDescriptor descriptor) throws SignalMLException {

                Float samplingFrequency;
                Double pageSize;

                try {
                        samplingFrequency = Float.parseFloat(getSamplingFrequencyComboBox().getModel().getSelectedItem().toString());
                } catch (NumberFormatException ex) {
                        throw new SignalMLException(messageSource.getMessage("error.invalidData"));
                }

                try {
                        pageSize = Double.parseDouble(getPageSizeField().getText());
                } catch (NumberFormatException ex) {
                        throw new SignalMLException(messageSource.getMessage("error.invalidData"));
                }

                descriptor.getOpenMonitorDescriptor().setSamplingFrequency(samplingFrequency);
                descriptor.getOpenMonitorDescriptor().setPageSize(pageSize);
        }

        /**
         * Clears all fields
         */
        public void clearAllFields() {
                
                getSamplingFrequencyComboBox().getModel().setSelectedItem("");
                getChannelCountSpinner().setValue("");
                getByteOrderComboBox().getModel().setSelectedItem("");
                getSampleTypeComboBox().getModel().setSelectedItem("");
                getPageSizeField().setText("");
                getBlocksPerPageField().setText("");
        }

        /**
         * Sets enabled to this panel and all it's children.
         * Clears all fields if enabled == false.
         *
         * @param enabled true or false
         */
        public void setEnabledAll(boolean enabled) {
                
                setEnabledToChildren(this, enabled);
                if (!enabled)
                        clearAllFields();
        }

        /**
         * Sets enabled to a component and all of it's children.
         *
         * @param component target component
         * @param enabled true or false
         * @param omit wheter to omit component
         */
        private void setEnabledToChildren(Component component, boolean enabled) {

                component.setEnabled(enabled);
                if (component instanceof Container) {
                        Component[] children = ((Container) component).getComponents();
                        for (Component child : children) {
                                setEnabledToChildren(child, enabled);
                        }
                }
        }

        /**
         * Creates the interface.
         */
        private void createInterface() {

                JLabel samplingFrequencyLabel = new JLabel(messageSource.getMessage("opensignal.parameters.samplingFrequency"));
                JLabel channelCountLabel = new JLabel(messageSource.getMessage("opensignal.parameters.channelCount"));
                JLabel byteOrderLabel = new JLabel(messageSource.getMessage("opensignal.parameters.byteOrder"));
                JLabel sampleTypeLabel = new JLabel(messageSource.getMessage("opensignal.parameters.sampleType"));
                JLabel pageSizeLabel = new JLabel(messageSource.getMessage("opensignal.parameters.pageSize"));
                JLabel blocksPerPageLabel = new JLabel(messageSource.getMessage("opensignal.parameters.blocksPerPage"));

                setBorder(new CompoundBorder(
		        new TitledBorder(messageSource.getMessage("opensignal.signalParametersPanelTitle")),
		        new EmptyBorder(3,3,3,3)
		));

                setLayout(new BorderLayout(0, 10));
                
                JPanel fieldsPanel = new JPanel(new GridBagLayout());

                GridBagConstraints constraints = new GridBagConstraints();
                constraints.anchor = GridBagConstraints.CENTER;
                constraints.fill = GridBagConstraints.HORIZONTAL;

                fillConstraints(constraints, 0, 0, 0, 0, 1);
                fieldsPanel.add(samplingFrequencyLabel, constraints);
                fillConstraints(constraints, 1, 0, 1, 0, 1);
                fieldsPanel.add(createWrapperPanel(getSamplingFrequencyComboBox(), 8), constraints);

                fillConstraints(constraints, 0, 1, 0, 0, 1);
                fieldsPanel.add(channelCountLabel, constraints);
                fillConstraints(constraints, 1, 1, 1, 0, 1);
                fieldsPanel.add(createWrapperPanel(getChannelCountSpinner(), 8), constraints);

                fillConstraints(constraints, 0, 2, 0, 0, 1);
                fieldsPanel.add(byteOrderLabel, constraints);
                fillConstraints(constraints, 1, 2, 1, 0, 1);
                fieldsPanel.add(createWrapperPanel(getByteOrderComboBox(), 8), constraints);

                fillConstraints(constraints, 0, 3, 0, 0, 1);
                fieldsPanel.add(sampleTypeLabel, constraints);
                fillConstraints(constraints, 1, 3, 1, 0, 1);
                fieldsPanel.add(createWrapperPanel(getSampleTypeComboBox(), 8), constraints);

                fillConstraints(constraints, 0, 4, 0, 0, 1);
                fieldsPanel.add(pageSizeLabel, constraints);
                fillConstraints(constraints, 1, 4, 1, 0, 1);
                fieldsPanel.add(createWrapperPanel(getPageSizeField(), 8), constraints);

                fillConstraints(constraints, 0, 5, 0, 0, 1);
                fieldsPanel.add(blocksPerPageLabel, constraints);
                fillConstraints(constraints, 1, 5, 1, 0, 1);
                fieldsPanel.add(createWrapperPanel(getBlocksPerPageField(), 8), constraints);

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                buttonPanel.add(getEditGainAndOffsetButton());

                add(fieldsPanel, BorderLayout.NORTH);
                add(buttonPanel, BorderLayout.SOUTH);

                //setEnabledAll(false);
        }

        /**
         * Method fills a {@link GridBagConstraints} object with data.
         *
         * @param constraints a {@link GridBagConstraints}
         * @param gridx {@link GridBagConstraints#gridx}
         * @param gridy {@link GridBagConstraints#gridy}
         * @param weightx {@link GridBagConstraints#weightx}
         * @param weighty {@link GridBagConstraints#weighty}
         * @param gridwidth {@link GridBagConstraints#gridwidth}
         */
        private void fillConstraints(GridBagConstraints constraints, int gridx, int gridy, int weightx, int weighty, int gridwidth) {
                
                constraints.gridx = gridx;
                constraints.gridy = gridy;
                constraints.weightx = weightx;
                constraints.weighty = weighty;
                constraints.gridwidth = gridwidth;
        }

        /**
         * Creates a wrapper panel with an empty border.
         * Inside component is stretched horizontaly.
         *
         * @param component inside component
         * @param border border size
         * @return the wrapper panel
         */
        private JPanel createWrapperPanel(JComponent component, int border) {

                JPanel wrapperPanel = new JPanel(new GridBagLayout());
                wrapperPanel.setBorder(new EmptyBorder(border, border, border, border));

                GridBagConstraints constraints = new GridBagConstraints();
                constraints.anchor = GridBagConstraints.CENTER;
                constraints.fill = GridBagConstraints.HORIZONTAL;
                fillConstraints(constraints, 0, 0, 1, 1, 1);

                wrapperPanel.add(component, constraints);

                return wrapperPanel;
        }

        /**
	 * Returns the sampling frequency combo box.
         *
         * @return the sampling frequency combo box
	 */
	private JComboBox getSamplingFrequencyComboBox() {
		if (samplingFrequencyComboBox == null) {
			samplingFrequencyComboBox = new JComboBox();
		}
		return samplingFrequencyComboBox;
	}

	/**
	 * Returns the channel count field
         *
         * @return the channel count field
	 */
	public IntegerSpinner getChannelCountSpinner() {
		if (channelCountSpinner == null) {
			channelCountSpinner = new IntegerSpinner(new SpinnerNumberModel(4, 1, 50, 1));
			channelCountSpinner.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					System.out.println("spinner changed!");
					int numberOfChannels = getChannelCountSpinner().getValue();
					firePropertyChange(NUMBER_OF_CHANNELS_CHANGED_PROPERTY, 0, numberOfChannels);
				}
			});

		}
		return channelCountSpinner;
	}

	/**
	 * Returns the byte order combo box.
         *
         * @return the byte order combo box
	 */
	private ResolvableComboBox getByteOrderComboBox() {

                if (byteOrderComboBox == null) {
			byteOrderComboBox = new ResolvableComboBox(messageSource);			
		}
		return byteOrderComboBox;
	}

	/**
	 * Returns the sample type combo box.
         *
         * @return the sample type combo box
	 */
        private ResolvableComboBox getSampleTypeComboBox() {

                if (sampleTypeComboBox == null) {
			sampleTypeComboBox = new ResolvableComboBox(messageSource);			
		}
		return sampleTypeComboBox;
	}

	/**
	 * Returns the page size field.
         *
         * @return the page size field
	 */
	private JTextField getPageSizeField() {
		if (pageSizeField == null) {
			pageSizeField = new JTextField();
		}
		return pageSizeField;
	}

	/**
	 * Returns the blocks per page field.
         *
         * @return the blocks per page field
	 */
	private JTextField getBlocksPerPageField() {
		if (blocksPerPageField == null) {
			blocksPerPageField = new JTextField();
		}
		return blocksPerPageField;
	}

        /**
         * Returns the edit gain and offset button.
         *
         * @return the edit gain and offset button
         */
        private JButton getEditGainAndOffsetButton() {
                if (editGainAndOffsetButton == null) {
                        editGainAndOffsetButton = new JButton(new AbstractAction() {

                                @Override
                                public void actionPerformed(ActionEvent e) {
                                        try {
                                                fillModelFromPanel(currentModel);
                                                getEditGainAndOffsetDialog().showDialog(currentModel);
                                        } catch (SignalMLException ex) {
                                                JOptionPane.showMessageDialog(null, ex.getMessage(), messageSource.getMessage("error"), JOptionPane.ERROR_MESSAGE);
                                        }                                        
                                }
                        });
                        editGainAndOffsetButton.setText(messageSource.getMessage("opensignal.parameters.editGainAndOffset"));
                }
                return editGainAndOffsetButton;
        }

        /**
         * Returns the edit gain and offset dialog action
         *
         * @return the action
         */
        private EditGainAndOffsetDialog getEditGainAndOffsetDialog() {
                
                if (editGainAndOffsetDialog == null) {
                        editGainAndOffsetDialog = new EditGainAndOffsetDialog(messageSource, null, true);
                }
                return editGainAndOffsetDialog;
        }
}