package org.signalml.app.view.opensignal;

import java.awt.BorderLayout;
import javax.swing.JComboBox;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import org.signalml.app.config.preset.StyledTagSetPresetManager;
import org.signalml.app.model.OpenMonitorDescriptor;
import org.signalml.app.view.element.AbstractSignalMLPanel;
import org.signalml.domain.tag.StyledTagSet;
import org.springframework.context.support.MessageSourceAccessor;

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
	 * @param messageSource message source for resolving localized messages
	 * @param styledTagSetPresetManager {@link PresetManager} handling the
	 * tag styles presets.
	 */
	public TagPresetSelectionPanel(MessageSourceAccessor messageSource, StyledTagSetPresetManager styledTagSetPresetManager) {
		super(messageSource);
		this.styledTagSetPresetManager = styledTagSetPresetManager;
		initialize();
	}

	/**
	 * Creates the components and adds them to this panel.
	 */
	private void initialize() {

		setLayout(new BorderLayout(10, 10));

		CompoundBorder border = new CompoundBorder(
			new TitledBorder(messageSource.getMessage("opensignal.tagPresetSelectionPanelTitle")),
			new EmptyBorder(3, 3, 3, 3));
		setBorder(border);

		this.add(getPresetComboBox());

	}

	/**
	 * Sets the appropriate tag style preset in the OpenMonitorDescriptor
	 * depending on the selected preset
	 * @param descriptor the model to be filled
	 */
	public void fillModelFromPanel(OpenMonitorDescriptor descriptor) {
		StyledTagSet selectedStylesPreset = (StyledTagSet) getPresetComboBox().getSelectedItem();
		descriptor.setTagStyles(selectedStylesPreset);
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