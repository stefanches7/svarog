package org.signalml.app.view.document.opensignal.elements;

import static org.signalml.app.util.i18n.SvarogI18n._;

import java.awt.BorderLayout;
import javax.swing.JComboBox;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import org.signalml.app.config.preset.StyledTagSetPresetManager;
import org.signalml.app.model.document.opensignal.AbstractOpenSignalDescriptor;
import org.signalml.app.model.document.opensignal.ExperimentDescriptor;
import org.signalml.app.model.document.opensignal.elements.TagPresetComboBoxModel;
import org.signalml.app.view.components.AbstractSignalMLPanel;
import org.signalml.domain.tag.StyledTagSet;

/**
 * A panel containing a ComboBox with available tag styles presets with
 * a properly titled border.
 *
 * @author Piotr Szachewicz
 */
public class TagPresetSelectionPanel extends AbstractSignalMLPanel {

	/**
	 * {@link PresetManager} that handles tag styles presets.
	 */
	private final StyledTagSetPresetManager styledTagSetPresetManager;
	/**
	 * {@link JComboBox} that displays the list of available presets.
	 */
	private JComboBox presetComboBox;

	/**
	 * Constructor.
	 * @param styledTagSetPresetManager {@link PresetManager} handling the
	 * tag styles presets.
	 */
	public TagPresetSelectionPanel(StyledTagSetPresetManager styledTagSetPresetManager) {
		super();
		this.styledTagSetPresetManager = styledTagSetPresetManager;
		initialize();
	}

	/**
	 * Creates the components and adds them to this panel.
	 */
	protected void initialize() {

		setTitledBorder(_("Select tag styles preset for monitor"));
		this.add(getPresetComboBox());

	}

	/**
	 * Sets the appropriate tag style preset in the ExperimentDescriptor
	 * depending on the selected preset
	 * @param descriptor the model to be filled
	 */
	public void fillModelFromPanel(AbstractOpenSignalDescriptor descriptor) {
		if (descriptor instanceof ExperimentDescriptor) {
			ExperimentDescriptor experimentDescriptor = (ExperimentDescriptor) descriptor;
			StyledTagSet selectedStylesPreset = (StyledTagSet) getPresetComboBox().getSelectedItem();
			experimentDescriptor.setTagStyles(selectedStylesPreset);
		}
	}

	/**
	 * Returns the {@link JComboBox} that lists the available tag style presets.
	 * @return the ComboBox with tag style presets.
	 */
	public JComboBox getPresetComboBox() {
		if (presetComboBox == null) {
			TagPresetComboBoxModel model = new TagPresetComboBoxModel(styledTagSetPresetManager);
			presetComboBox = new JComboBox(model);
		}
		return presetComboBox;
	}

}
