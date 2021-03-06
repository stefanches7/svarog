/* NewStagerBasicParametersPanel.java created 2008-02-14
 *
 */
package org.signalml.plugin.newstager.ui;

import static org.signalml.plugin.i18n.PluginI18n._;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.signalml.app.model.components.validation.ValidationErrors;
import org.signalml.app.util.SwingUtils;
import org.signalml.app.view.common.components.CompactButton;
import org.signalml.app.view.common.dialogs.AbstractDialog;
import org.signalml.plugin.newstager.data.NewStagerConstants;
import org.signalml.plugin.newstager.data.NewStagerFASPThreshold;
import org.signalml.plugin.newstager.data.NewStagerParameterThresholds;
import org.signalml.plugin.newstager.data.NewStagerParameters;
import org.signalml.plugin.newstager.data.NewStagerParametersPreset;
import org.signalml.plugin.newstager.data.NewStagerRules;
import org.signalml.plugin.newstager.helper.NewStagerAutoParametersHelper;
import org.signalml.plugin.newstager.ui.components.NewStagerAutoSpinnerPanel;

/**
 * NewStagerBasicParametersPanel
 *
 *
 * @author Michal Dobaczewski &copy; 2007-2008 CC Otwarte Systemy Komputerowe
 *         Sp. z o.o.
 */
public class NewStagerBasicParametersPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private AbstractDialog owner;

	private JComboBox rulesComboBox;

	private NewStagerAutoSpinnerPanel deltaMinAmplitudePanel;
	private NewStagerAutoSpinnerPanel alphaMinAmplitudePanel;
	private NewStagerAutoSpinnerPanel spindleMinAmplitudePanel;

	private JCheckBox primaryHypnogramCheckBox;

	public NewStagerBasicParametersPanel(AbstractDialog owner,
			NewStagerAdvancedConfigObservable advancedConfigObservable) {
		super();
		this.owner = owner;
		initialize();

		final NewStagerAdvancedConfigObservable observable = advancedConfigObservable;
		advancedConfigObservable.addObserver(new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				boolean flag = !observable.getEnabled();

				getDeltaMinAmplitudePanel().setEnabled(flag);
				getAlphaMinAmplitudePanel().setEnabled(flag);
				getSpindleMinAmplitudePanel().setEnabled(flag);
			}
		});
	}

	private void initialize() {

		setLayout(new BorderLayout());

		CompoundBorder border = new CompoundBorder(new TitledBorder(
				_("Key parameters")), new EmptyBorder(3, 3, 3, 3));
		setBorder(border);

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setAutoCreateContainerGaps(false);
		layout.setAutoCreateGaps(true);

		JLabel rulesLabel = new JLabel(_("Scoring criteria"));
		JLabel deltaMinAmplitudeLabel = new JLabel(
				_("Amplitude threshold for delta waves [uV]"));
		JLabel alphaMinAmplitudeLabel = new JLabel(
				_("Amplitude threshold for alpha waves [uV]"));
		JLabel spindleMinAmplitudeLabel = new JLabel(
				_("Amplitude threshold for sleep spindles [uV]"));
		JLabel primaryHypnogramLabel = new JLabel(
				_("Show primary hypnogram and markers of waveforms in result"));
		primaryHypnogramLabel.setMinimumSize(new Dimension(25, 35));
		primaryHypnogramLabel.setVerticalAlignment(JLabel.CENTER);

		Component glue1 = Box.createHorizontalGlue();
		Component glue2 = Box.createHorizontalGlue();
		Component glue3 = Box.createHorizontalGlue();
		Component glue4 = Box.createHorizontalGlue();
		Component glue5 = Box.createHorizontalGlue();

		CompactButton rulesHelpButton = SwingUtils.createFieldHelpButton(owner,
				NewStagerMethodDialog.HELP_RULES);
		CompactButton deltaMinAmplitudeHelpButton = SwingUtils
				.createFieldHelpButton(owner,
						NewStagerMethodDialog.HELP_DELTA_MIN_AMPLITUDE);
		CompactButton alphaMinAmplitudeHelpButton = SwingUtils
				.createFieldHelpButton(owner,
						NewStagerMethodDialog.HELP_ALPHA_MIN_AMPLITUDE);
		CompactButton spindleMinAmplitudeHelpButton = SwingUtils
				.createFieldHelpButton(owner,
						NewStagerMethodDialog.HELP_SPINDLE_MIN_AMPLITUDE);
		CompactButton primaryHypnogramHelpButton = SwingUtils
				.createFieldHelpButton(owner,
						NewStagerMethodDialog.HELP_PRIMARY_HYPNOGRAM);

		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();

		hGroup.addGroup(layout.createParallelGroup().addComponent(rulesLabel)
				.addComponent(deltaMinAmplitudeLabel)
				.addComponent(alphaMinAmplitudeLabel)
				.addComponent(spindleMinAmplitudeLabel)
				.addComponent(primaryHypnogramLabel));

		hGroup.addGroup(layout.createParallelGroup().addComponent(glue1)
				.addComponent(glue2).addComponent(glue3).addComponent(glue4)
				.addComponent(glue5));

		hGroup.addGroup(layout.createParallelGroup(Alignment.TRAILING)
				.addComponent(getRulesComboBox())
				.addComponent(getDeltaMinAmplitudePanel())
				.addComponent(getAlphaMinAmplitudePanel())
				.addComponent(getSpindleMinAmplitudePanel())
				.addComponent(getPrimaryHypnogramCheckBox()));

		hGroup.addGroup(layout.createParallelGroup()
				.addComponent(rulesHelpButton)
				.addComponent(deltaMinAmplitudeHelpButton)
				.addComponent(alphaMinAmplitudeHelpButton)
				.addComponent(spindleMinAmplitudeHelpButton)
				.addComponent(primaryHypnogramHelpButton));

		layout.setHorizontalGroup(hGroup);

		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

		vGroup.addGroup(layout.createParallelGroup(Alignment.CENTER)
				.addComponent(rulesLabel).addComponent(glue1)
				.addComponent(getRulesComboBox()).addComponent(rulesHelpButton));

		vGroup.addGroup(layout.createParallelGroup(Alignment.CENTER)
				.addComponent(deltaMinAmplitudeLabel).addComponent(glue2)
				.addComponent(getDeltaMinAmplitudePanel())
				.addComponent(deltaMinAmplitudeHelpButton));

		vGroup.addGroup(layout.createParallelGroup(Alignment.CENTER)
				.addComponent(alphaMinAmplitudeLabel).addComponent(glue3)
				.addComponent(getAlphaMinAmplitudePanel())
				.addComponent(alphaMinAmplitudeHelpButton));

		vGroup.addGroup(layout.createParallelGroup(Alignment.CENTER)
				.addComponent(spindleMinAmplitudeLabel).addComponent(glue4)
				.addComponent(getSpindleMinAmplitudePanel())
				.addComponent(spindleMinAmplitudeHelpButton));

		vGroup.addGroup(layout.createParallelGroup(Alignment.CENTER)
				.addComponent(primaryHypnogramLabel).addComponent(glue5)
				.addComponent(getPrimaryHypnogramCheckBox())
				.addComponent(primaryHypnogramHelpButton));

		layout.setVerticalGroup(vGroup);

	}

	public JComboBox getRulesComboBox() {
		if (rulesComboBox == null) {
			rulesComboBox = new JComboBox();

			rulesComboBox.setRenderer(new DefaultListCellRenderer() {
				private static final long serialVersionUID = 1L;

				@Override
				public Component getListCellRendererComponent(JList list,
						Object value, int index, boolean isSelected,
						boolean cellHasFocus) {
					try {
						NewStagerRules rulesValue = (NewStagerRules) value;
						switch (rulesValue) {
						case RK:
							value = _("Rechtshaffen and Kales (R&K 1967) rules");
							break;
						case AASM:
							value = _("AASM (2007) rules");
							break;
						default:
							;
						}
					} catch (ClassCastException e) {
						// do nothing
					}

					return super.getListCellRendererComponent(list, value,
							index, isSelected, cellHasFocus);
				}

			});

			rulesComboBox.setModel(new DefaultComboBoxModel(NewStagerRules
					.values()));
		}
		return rulesComboBox;
	}

	public NewStagerAutoSpinnerPanel getDeltaMinAmplitudePanel() {
		if (deltaMinAmplitudePanel == null) {
			deltaMinAmplitudePanel = new NewStagerAutoSpinnerPanel(
					NewStagerConstants.MIN_AMPLITUDE,
					NewStagerConstants.MIN_AMPLITUDE,
					NewStagerConstants.MAX_AMPLITUDE,
					NewStagerConstants.INCR_AMPLITUDE, false);
		}
		return deltaMinAmplitudePanel;
	}

	public NewStagerAutoSpinnerPanel getAlphaMinAmplitudePanel() {
		if (alphaMinAmplitudePanel == null) {
			alphaMinAmplitudePanel = new NewStagerAutoSpinnerPanel(
					NewStagerConstants.MIN_AMPLITUDE,
					NewStagerConstants.MIN_AMPLITUDE,
					NewStagerConstants.MAX_AMPLITUDE,
					NewStagerConstants.INCR_AMPLITUDE, false);
		}
		return alphaMinAmplitudePanel;
	}

	public NewStagerAutoSpinnerPanel getSpindleMinAmplitudePanel() {
		if (spindleMinAmplitudePanel == null) {
			spindleMinAmplitudePanel = new NewStagerAutoSpinnerPanel(
					NewStagerConstants.MIN_AMPLITUDE,
					NewStagerConstants.MIN_AMPLITUDE,
					NewStagerConstants.MAX_AMPLITUDE,
					NewStagerConstants.INCR_AMPLITUDE, false);
		}
		return spindleMinAmplitudePanel;
	}

	public JCheckBox getPrimaryHypnogramCheckBox() {
		if (primaryHypnogramCheckBox == null) {
			primaryHypnogramCheckBox = new JCheckBox();
			primaryHypnogramCheckBox.setPreferredSize(new Dimension(25, 25));
		}
		return primaryHypnogramCheckBox;
	}

	public void fillPanelFromParameters(
			NewStagerParametersPreset parametersPreset) {
		NewStagerParameters parameters = parametersPreset.parameters;

		getRulesComboBox().setSelectedItem(parameters.rules);

		NewStagerParameterThresholds thresholds = parameters.thresholds;

		this.setPanelAmplitude(
				getDeltaMinAmplitudePanel(),
				thresholds.deltaThreshold,
				parametersPreset.isAutoDeltaAmplitude ? NewStagerAutoParametersHelper
						.GetAutoDeltaAmplitude() : null);
		this.setPanelAmplitude(
				getAlphaMinAmplitudePanel(),
				thresholds.alphaThreshold,
				parametersPreset.isAutoAlphaAmplitude ? NewStagerAutoParametersHelper
						.GetAutoAlphaAmplitude() : null);
		this.setPanelAmplitude(
				getSpindleMinAmplitudePanel(),
				thresholds.spindleThreshold,
				parametersPreset.isAutoSpindleAmplitude ? NewStagerAutoParametersHelper
						.GetAutoSpindleAmplitude() : null);

		getPrimaryHypnogramCheckBox().setSelected(
				parameters.primaryHypnogramFlag);
	}

	public void fillParametersFromPanel(
			NewStagerParametersPreset parametersPreset) {
		NewStagerParameters parameters = parametersPreset.parameters;

		parameters.rules = (NewStagerRules) getRulesComboBox()
				.getSelectedItem();

		NewStagerParameterThresholds thresholds = parameters.thresholds;

		parametersPreset.isAutoDeltaAmplitude = this.setAmplitude(
				getDeltaMinAmplitudePanel(), thresholds.deltaThreshold);
		parametersPreset.isAutoAlphaAmplitude = this.setAmplitude(
				getAlphaMinAmplitudePanel(), thresholds.alphaThreshold);
		parametersPreset.isAutoSpindleAmplitude = this.setAmplitude(
				getSpindleMinAmplitudePanel(), thresholds.spindleThreshold);

		if (parametersPreset.isAutoDeltaAmplitude) {
			thresholds.deltaThreshold.amplitude
					.setMinWithUnlimited(NewStagerAutoParametersHelper
							.GetAutoDeltaAmplitude());
		}
		if (parametersPreset.isAutoAlphaAmplitude) {
			thresholds.alphaThreshold.amplitude
					.setMinWithUnlimited(NewStagerAutoParametersHelper
							.GetAutoAlphaAmplitude());
		}
		if (parametersPreset.isAutoSpindleAmplitude) {
			thresholds.spindleThreshold.amplitude
					.setMinWithUnlimited(NewStagerAutoParametersHelper
							.GetAutoSpindleAmplitude());
		}

		parameters.primaryHypnogramFlag = getPrimaryHypnogramCheckBox()
				.isSelected();
	}

	public void validatePanel(ValidationErrors errors) {
		// nothing to do
	}

	private boolean setAmplitude(NewStagerAutoSpinnerPanel amplitudePanel,
			NewStagerFASPThreshold threshold) {
		threshold.amplitude.setMinWithUnlimited(amplitudePanel
				.getValueWithAuto());
		return amplitudePanel.isUnlimited();
	}

	private void setPanelAmplitude(NewStagerAutoSpinnerPanel amplitudePanel,
			NewStagerFASPThreshold threshold, Double autoValue) {
		if (autoValue == null) {
			amplitudePanel.setValueWithAuto(threshold.amplitude
					.getMinWithUnlimited());
		} else {
			amplitudePanel.setValueWithAuto(autoValue);
			amplitudePanel.setAuto(true);
		}
	}

}
