package org.signalml.app.method.ep.view.time;

import static org.signalml.app.util.i18n.SvarogI18n._;

import org.signalml.app.view.common.components.panels.PanelWithEnablingCheckbox;
import org.signalml.method.ep.EvokedPotentialParameters;

/**
 * This is a panel for selecting which parts of the signal should be
 * treated as baseline.
 *
 * @author Piotr Szachewicz
 */
public class BaselineSelectionPanel extends PanelWithEnablingCheckbox<TimeSelectionPanel> {

	public BaselineSelectionPanel() {
		super(_("Baseline correction"));
	}

	public void fillModelFromPanel(EvokedPotentialParameters parameters) {
		parameters.setBaselineCorrectionEnabled(isCheckboxSelected());
		parameters.setBaselineTimeBefore(getPanel().getStartTimeSpinner().getValue());
		parameters.setBaselineTimeAfter(getPanel().getEndTimeSpinner().getValue());
	}

	@Override
	protected String getEnableCheckboxText() {
		return _("enable baseline correction");
	}

	@Override
	protected TimeSelectionPanel getPanel() {
		if (panel == null) {
			panel = new TimeSelectionPanel("");
		}
		return panel;
	}

	public void fillPanelFromModel(EvokedPotentialParameters parameters) {
		setCheckboxSelected(parameters.isBaselineCorrectionEnabled());
		getPanel().getStartTimeSpinner().setValue(parameters.getBaselineTimeBefore());
		getPanel().getEndTimeSpinner().setValue(parameters.getBaselineTimeAfter());
	}

}
