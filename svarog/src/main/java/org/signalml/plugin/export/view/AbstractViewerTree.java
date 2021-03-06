/* AbstractViewerTree.java created 2007-10-15
 *
 */

package org.signalml.plugin.export.view;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;

import org.signalml.app.view.I18nMessage;
import org.springframework.context.MessageSourceResolvable;

/**
 * Abstract class for a tree that is displayed in {@link ViewerTreePane tabs}
 * in the left tabbed pane (tree tabbed pane).
 *
 * @author Michal Dobaczewski &copy; 2007-2008 CC Otwarte Systemy Komputerowe Sp. z o.o.
 */
public abstract class AbstractViewerTree extends JTree {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs AbstractViewerTree based on a given model and source of labels
	 * @param model the model for this tree
	 */
	public AbstractViewerTree(TreeModel model) {
		super((TreeModel) null);
		setModel(model);
		setRootVisible(false);
		setShowsRootHandles(true);
		setEditable(false);
		setExpandsSelectedPaths(true);
	}

	@Override
	public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		{
			if (value instanceof I18nMessage) {
				return ((I18nMessage) value).i18n();
			}
			if (value instanceof MessageSourceResolvable) {
				return ((MessageSourceResolvable) value).getDefaultMessage();
			}
			String s = value.toString();
			if (s != null && s.length() > 0) {
				return s;
			}
		}
		return super.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
	}
}
