package pro.deta.detatrak.view.layout;

import java.io.Serializable;

public class EditableTableParameters implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2583613869866426503L;
	private TableColumnInfo[] columnHeaders;
	private Class<?> targetClass;
	private int tabIndex;

	public EditableTableParameters(Class<?> targetClass,TableColumnInfo[] columns) {
		this.targetClass = targetClass;
		this.columnHeaders = columns;
	}

	public TableColumnInfo[] getColumnHeaders() {
		return columnHeaders;
	}
	public void setColumnHeaders(TableColumnInfo[] columnHeaders) {
		this.columnHeaders = columnHeaders;
	}
	public Class<?> getTargetClass() {
		return targetClass;
	}
	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	public int getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}

}
