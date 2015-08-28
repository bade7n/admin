package pro.deta.detatrak.view.layout;

import java.io.Serializable;
import java.util.function.Function;

import pro.deta.detatrak.dao.data.T0;

public class EditableTableParameters implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2583613869866426503L;
	private TableColumnInfo[] columnHeaders;
	private Class<?> targetClass;
	private int tabIndex;
	private Function<T0, Object> createNew = null;

	public EditableTableParameters(Class<?> targetClass,TableColumnInfo[] columns) {
		this.targetClass = targetClass;
		this.columnHeaders = columns;
	}

	public EditableTableParameters(Class<?> targetClass,TableColumnInfo[] columns,Function<T0, Object> createNew) {
		this(targetClass,columns);
		this.createNew = createNew;
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

	public Function<T0, Object> getCreateNew() {
		return createNew;
	}

	public void setCreateNew(Function<T0, Object> createNew) {
		this.createNew = createNew;
	}

}
