package pro.deta.detatrak.view.layout;

import java.io.Serializable;
import java.util.function.Function;

import pro.deta.detatrak.dao.data.T0;
import ru.yar.vi.rm.data.CustomFieldDO;

/**
 * Входящие параметры для таблицы, в которой можно редактировать строки и соответственно Beans.
 * На текущий момент поддерживается редактирование только String полей.
 * При этом field который редактируется должен быть помечен как cascade=CascadeType.MERGE например {@link CustomFieldDO} criteria. Иначе при сохранении JPA возьмёт 
 * актуальные значения из БД. 
 * 
 * @author vi
 *
 * @param <T>
 */

public class EditableTableParameters<T> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2583613869866426503L;
	private TableColumnInfo[] columnHeaders;
	private Class<T> targetClass;
	private int tabIndex;
	private Function<T0, T> createNew = null;
	private String beanIdProperty;

	public EditableTableParameters(Class<T> targetClass,TableColumnInfo[] columns) {
		this.targetClass = targetClass;
		this.columnHeaders = columns;
	}

	public EditableTableParameters(Class<T> targetClass,Function<T0, T> createNew) {
		this.targetClass = targetClass;
		this.createNew = createNew;
	}

	public EditableTableParameters(Class<T> targetClass,TableColumnInfo[] columns,Function<T0, T> createNew,String beanIdProperty) {
		this(targetClass,columns);
		this.createNew = createNew;
		this.beanIdProperty = beanIdProperty;
	}

	public TableColumnInfo[] getColumnHeaders() {
		return columnHeaders;
	}
	public void setColumnHeaders(TableColumnInfo[] columnHeaders) {
		this.columnHeaders = columnHeaders;
	}
	public Class<T> getTargetClass() {
		return targetClass;
	}
	public void setTargetClass(Class<T> targetClass) {
		this.targetClass = targetClass;
	}

	public int getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}

	public Function<T0, T> getCreateNew() {
		return createNew;
	}

	public void setCreateNew(Function<T0, T> createNew) {
		this.createNew = createNew;
	}

	public String getBeanIdProperty() {
		return beanIdProperty;
	}

	public void setBeanIdProperty(String beanIdProperty) {
		this.beanIdProperty = beanIdProperty;
	}

}
