/* NewStagerSleepComparisonTableModel.java created 2008-03-03
 *
 */

package org.signalml.plugin.newstager.ui;

import static org.signalml.plugin.i18n.PluginI18n._;

import javax.swing.table.AbstractTableModel;

import org.signalml.plugin.newstager.data.NewStagerSleepComparison;

/**
 * NewStagerSleepComparisonTableModel
 *
 *
 * @author Michal Dobaczewski &copy; 2007-2008 CC Otwarte Systemy Komputerowe
 *         Sp. z o.o.
 */
public class NewStagerSleepComparisonTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private ColumnTableModel columnTableModel;
	private RowTableModel rowTableModel;

	private NewStagerSleepComparison result;

	public NewStagerSleepComparisonTableModel() {
		super();
	}

	public ColumnTableModel getColumnTableModel() {
		if (columnTableModel == null) {
			columnTableModel = new ColumnTableModel();
		}
		return columnTableModel;
	}

	public RowTableModel getRowTableModel() {
		if (rowTableModel == null) {
			rowTableModel = new RowTableModel();
		}
		return rowTableModel;
	}

	private void reset() {
		fireTableStructureChanged();
		if (columnTableModel != null) {
			columnTableModel.fireTableStructureChanged();
		}
		if (rowTableModel != null) {
			rowTableModel.fireTableStructureChanged();
		}
	}

	public NewStagerSleepComparison getResult() {
		return result;
	}

	public void setResult(NewStagerSleepComparison result) {
		if (this.result != result) {
			this.result = result;
			reset();
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return Integer.class;
	}

	@Override
	public int getColumnCount() {
		if (result == null) {
			return 0;
		}
		return result.getStyleCount() + 1;
	}

	@Override
	public int getRowCount() {
		if (result == null) {
			return 0;
		}
		return result.getStyleCount();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return new Integer(result.getStyleOverlay(rowIndex, columnIndex));
	}

	public class ColumnTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		@Override
		public int getColumnCount() {
			if (result == null) {
				return 0;
			}
			return result.getStyleCount() + 1;
		}

		@Override
		public int getRowCount() {
			if (result == null) {
				return 0;
			}
			return 1;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (columnIndex < result.getStyleCount()) {
				return result.getStyleAt(columnIndex).getDescriptionOrName();
			} else {
				return _("Other");
			}
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return String.class;
		}

	}

	public class RowTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		@Override
		public int getColumnCount() {
			if (result == null) {
				return 0;
			}
			return 1;
		}

		@Override
		public int getRowCount() {
			if (result == null) {
				return 0;
			}
			return result.getStyleCount();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return result.getStyleAt(rowIndex).getDescriptionOrName();
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return String.class;
		}

	}

}
