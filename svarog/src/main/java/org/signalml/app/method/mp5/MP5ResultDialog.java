/* MP5ResultDialog.java created 2008-02-20
 *
 */

package org.signalml.app.method.mp5;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.signalml.app.util.IconUtils;
import org.signalml.app.view.ViewerFileChooser;
import org.signalml.plugin.export.SignalMLException;
import org.signalml.plugin.export.view.AbstractDialog;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.validation.Errors;

/** MP5ResultDialog
 *
 *
 * @author Michal Dobaczewski &copy; 2007-2008 CC Otwarte Systemy Komputerowe Sp. z o.o.
 */
public class MP5ResultDialog extends AbstractDialog {

	private static final long serialVersionUID = 1L;

	private ViewerFileChooser fileChooser;

	private JCheckBox openInWindowCheckBox;
	private JCheckBox saveToFileCheckBox;

	private MP5ResultBookPanel bookPanel;

	public MP5ResultDialog(MessageSourceAccessor messageSource) {
		super(messageSource);
	}

	public MP5ResultDialog(MessageSourceAccessor messageSource, Window w, boolean isModal) {
		super(messageSource, w, isModal);
	}

	@Override
	protected void initialize() {
		setTitle(messageSource.getMessage("mp5Method.dialog.result.title"));
		setIconImage(IconUtils.loadClassPathImage(MP5MethodDescriptor.ICON_PATH));
		setResizable(false);
		super.initialize();
	}

	@Override
	public JComponent createInterface() {

		JPanel interfacePanel = new JPanel(new BorderLayout());

		JPanel checkBoxPanel = new JPanel();
		checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));

		CompoundBorder border = new CompoundBorder(
		        new TitledBorder(messageSource.getMessage("mp5Method.dialog.result.actionTitle")),
		        new EmptyBorder(3,3,3,3)
		);
		checkBoxPanel.setBorder(border);

		checkBoxPanel.add(getOpenInWindowCheckBox());
		checkBoxPanel.add(getSaveToFileCheckBox());

		interfacePanel.add(checkBoxPanel, BorderLayout.NORTH);
		interfacePanel.add(getBookPanel(), BorderLayout.CENTER);

		return interfacePanel;

	}

	public JCheckBox getOpenInWindowCheckBox() {
		if (openInWindowCheckBox == null) {
			openInWindowCheckBox = new JCheckBox(messageSource.getMessage("mp5Method.dialog.result.openInWindow"));
		}
		return openInWindowCheckBox;
	}

	public JCheckBox getSaveToFileCheckBox() {
		if (saveToFileCheckBox == null) {
			saveToFileCheckBox = new JCheckBox(messageSource.getMessage("mp5Method.dialog.result.saveToFile"), true);

			saveToFileCheckBox.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {

					boolean selected = (e.getStateChange() == ItemEvent.SELECTED);

					getBookPanel().setEnabled(selected);

				}

			});

		}
		return saveToFileCheckBox;
	}

	public MP5ResultBookPanel getBookPanel() {
		if (bookPanel == null) {
			bookPanel = new MP5ResultBookPanel(messageSource, fileChooser);
		}
		return bookPanel;
	}

	@Override
	public void fillDialogFromModel(Object model) throws SignalMLException {

		MP5ResultTargetDescriptor descriptor = (MP5ResultTargetDescriptor) model;

		getOpenInWindowCheckBox().setSelected(descriptor.isOpenInWindow());
		getSaveToFileCheckBox().setSelected(descriptor.isSaveToFile());
		getBookPanel().fillPanelFromModel(descriptor);

	}

	@Override
	public void fillModelFromDialog(Object model) throws SignalMLException {

		MP5ResultTargetDescriptor descriptor = (MP5ResultTargetDescriptor) model;

		descriptor.setOpenInWindow(getOpenInWindowCheckBox().isSelected());
		boolean saveToFile = getSaveToFileCheckBox().isSelected();
		descriptor.setSaveToFile(saveToFile);
		if (saveToFile) {
			getBookPanel().fillModelFromPanel(descriptor);
		} else {
			descriptor.setBookFile(null);
		}

	}

	@Override
	public void validateDialog(Object model, Errors errors) throws SignalMLException {
		super.validateDialog(model, errors);

		if (getSaveToFileCheckBox().isSelected()) {
			getBookPanel().validatePanel(errors);
		}

	}

	@Override
	public boolean supportsModelClass(Class<?> clazz) {
		return MP5ResultTargetDescriptor.class.isAssignableFrom(clazz);
	}

	public ViewerFileChooser getFileChooser() {
		return fileChooser;
	}

	public void setFileChooser(ViewerFileChooser fileChooser) {
		this.fileChooser = fileChooser;
	}

}