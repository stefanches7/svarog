package org.signalml.app.view.tag.styles.attributes;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import org.apache.xerces.parsers.AbstractSAXParser;
import org.signalml.app.view.element.AbstractSignalMLPanel;
import org.signalml.app.view.element.TagStylePropertiesPanel;
import org.signalml.plugin.export.signal.TagStyle;
import org.signalml.plugin.export.signal.tagStyle.TagStyleAttributeDefinition;
import org.signalml.plugin.export.signal.tagStyle.TagStyleAttributes;
import org.springframework.context.support.MessageSourceAccessor;

/**
 * The panel for editing tag attributes definitions for the current tag style.
 * Shows the list of available tag attributes definitions and allows to change
 * their display name and visibility.
 *
 * @author Piotr Szachewicz
 */
public class TagAttributesDefinitionsEditPanel extends AbstractSignalMLPanel {

	/**
	 * The panel for editing tag styles properties. It is alarmed by this panel
	 * when tag attributes definitions are changed.
	 */
	private TagStylePropertiesPanel tagStylePropertiesPanel;
	/**
	 * Table for displaying tag attributes definitions.
	 */
	private JTable table;
	/**
	 * Table model for displaying tag attributes definitions.
	 */
	private TagAttributesDefinitionsTableModel tableModel;

	/**
	 * Constructor.
	 * @param messageSource message source for resolving localized messages
	 * @param tagStylePropertiesPanel panel for editing tag styled properties
	 */
	public TagAttributesDefinitionsEditPanel(MessageSourceAccessor messageSource, TagStylePropertiesPanel tagStylePropertiesPanel) {
		super(messageSource);
		this.messageSource = messageSource;
		this.tagStylePropertiesPanel = tagStylePropertiesPanel;
		initialize();
	}

	/**
	 * Creates components and adds them to this panel.
	 */
	private void initialize() {
		this.setLayout(new BorderLayout(10, 10));
		this.setBorder(new TitledBorder("Tag attributes"));

		table = new JTable();
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);

		tableModel = new TagAttributesDefinitionsTableModel();

		this.add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * Fills the list of attributes definitions from the given tag style.
	 * @param style the model for this panel
	 */
	public void fillPanelFromModel(TagStyle style) {
		TagStyleAttributes attributesDefinitions;

		if (style == null) {
			attributesDefinitions = new TagStyleAttributes();
		} else {
			attributesDefinitions = style.getAttributesDefinitions().clone();
		}

		tableModel.setData(attributesDefinitions);
		tableModel.setTagStylePropertiesPanel(tagStylePropertiesPanel);
		table.setModel(tableModel);
	}

	/**
	 * Changes the attributes definitions in the tag style according to the
	 * changes made in GUI.
	 * @param style the style to be filled from GUI
	 */
	public void fillModelFromPanel(TagStyle style) {
		TagStyleAttributes oldDefinitions = style.getAttributesDefinitions();
		TagStyleAttributes newDefinitions = tableModel.getTagStyleAttributes();

		int i = 0;
		for (; i < newDefinitions.getSize(); i++) {
			TagStyleAttributeDefinition newDefinition = newDefinitions.getAttributeDefinition(i);
			TagStyleAttributeDefinition oldDefinition = oldDefinitions.getAttributeDefinition(i);

			//we assume that nobody will change the order of elements
			if (!newDefinition.equals(oldDefinition)) {
				oldDefinition.copyFrom(newDefinition);
			}
		}

		for (; i < oldDefinitions.getSize(); i++) {
			style.getAttributesDefinitions().removeAttributeDefinition(i);
		}
	}

}