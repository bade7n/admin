package pro.deta.detatrak.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.event.Action;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.And;
import com.vaadin.event.dd.acceptcriteria.SourceIs;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.ui.AbstractSelect.AbstractSelectTargetDetails;
import com.vaadin.ui.AbstractSelect.AcceptItem;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.ui.TableFieldFactory;
import com.vaadin.ui.TextField;

import pro.deta.detatrak.BaseTypeContainer;
import pro.deta.detatrak.view.layout.EditableTableParameters;
import pro.deta.detatrak.view.layout.FormParameter;
import pro.deta.detatrak.view.layout.TableColumnInfo;

public class EditableTablePanel<T> extends Panel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2851285307777397438L;
	private static final Logger logger = LoggerFactory.getLogger(EditableTablePanel.class);
	protected static final Action REMOVE = new Action("Remove row");
	protected static final Action ADD = new Action("Add row");



	private Class<T> targetClass;
	private BeanContainer<Object, T> beanContainer;
	private List original = null;
	private Function<FormParameter<Object>, T> createNewFunction;
	private Iterator<Integer>  newItemIdIterator = null; 
	private FormParameter<Object> formParameter;
	private Table table = new Table();
	private EditableTableParameters<T> parameters;



	public void initialize(final EditableTableParameters<T> p) {
		setContent(table);
		setParameters(p);
		getTable().setDragMode(TableDragMode.ROW);
		getTable().setSizeFull();
		getTable().setHeight(300, Unit.PIXELS);
		getTable().addStyleName("editable-table");
		getTable().setTabIndex(p.getTabIndex());
		setTargetClass(p.getTargetClass());
		this.createNewFunction = p.getCreateNew();

		beanContainer = new BeanContainer<>(getTargetClass());
		if(isBaseTypeContainer()) {
			// need BaseTypeContainer
			p.setColumnHeaders(new TableColumnInfo[] {new TableColumnInfo("value", getCaption())});
			p.setBeanIdProperty("value");
			original = (List<T>) original.stream().map(value -> new BaseTypeContainer(value)).collect(Collectors.toCollection(ArrayList::new)); 
		}
		if(p.getBeanIdProperty() != null)
			beanContainer.setBeanIdProperty(p.getBeanIdProperty());

		newItemIdIterator = p.getNewItemIdGenerator().iterator();

		if(p.getBeanIdResolver() != null) {
			beanContainer.setBeanIdResolver((T bean) -> {
				Object itemId = p.getBeanIdResolver().apply(bean);
				if(itemId == null)
					itemId = newItemIdIterator.next();
				return itemId;
			});
		}
		beanContainer.addAll(original);
		getTable().setContainerDataSource(beanContainer);


		if(p.getColumnHeaders() != null)
			for (TableColumnInfo tableColumnInfo : p.getColumnHeaders()) {
				getTable().setColumnHeader(tableColumnInfo.getName(), tableColumnInfo.getCaption());
				if(tableColumnInfo.getWidth() != null)
					getTable().setColumnWidth(tableColumnInfo.getName(), tableColumnInfo.getWidth());
				if(tableColumnInfo.getExpandRatio() != null)
					getTable().setColumnExpandRatio(tableColumnInfo.getName(), tableColumnInfo.getExpandRatio());
			}
		getTable().setVisibleColumns(Arrays.asList(p.getColumnHeaders()).stream().map(item -> item.getName()).toArray());

		addEditAbility();
		addDragAndDropAbility();
	}

	private void addDragAndDropAbility() {
		table.setDropHandler(new DropHandler() {
			@Override
			public AcceptCriterion getAcceptCriterion() {
				return new And(new SourceIs(table), AcceptItem.ALL);
			}

			@Override
			public void drop(DragAndDropEvent dropEvent) {
				DataBoundTransferable t = (DataBoundTransferable)dropEvent.getTransferable();
				Object sourceItemId = t.getItemId(); // returns our Bean

				AbstractSelectTargetDetails dropData = ((AbstractSelectTargetDetails) dropEvent.getTargetDetails());
				Object targetItemId = dropData.getItemIdOver(); // returns our Bean

				// No move if source and target are the same, or there is no target
				if ( sourceItemId == targetItemId || targetItemId == null)
					return;

				T bean = beanContainer.getItem(sourceItemId).getBean();

				// Let's remove the source of the drag so we can add it back where requested...
				beanContainer.removeItem(sourceItemId);

				if ( dropData.getDropLocation() == VerticalDropLocation.BOTTOM ) {
					beanContainer.addItemAfter(targetItemId, sourceItemId, bean);
				} else {
					Object prevItemId = beanContainer.prevItemId(targetItemId);
					beanContainer.addItemAfter(prevItemId, sourceItemId, bean);
				}
				// tell that the persons are related
				//				Notification.show(sourceItemId + " is related to " + targetItemId);
			}
		});
		table.setSelectable(true);

	}

	private TableColumnInfo getTableColumnInfo(Object propertyId) {
		for (TableColumnInfo column : parameters.getColumnHeaders()) {
			if(column.getName().equals(propertyId))
				return column;
		}
		return null;
	}

	private void addEditAbility() {
		table.setTableFieldFactory(new TableFieldFactory() {
			@Override
			public Field createField(final Container container, final Object itemId, final Object propertyId, final Component uiContext) {
				if (itemId == table.getData()) {
					Field<?> field = null;
					TableColumnInfo columnInfo = getTableColumnInfo(propertyId);
					if(columnInfo == null || columnInfo.getFieldInitializer() == null) {
						TextField f = (TextField) DefaultFieldFactory.get().createField(container, itemId, propertyId, uiContext);
						Object backupValue = container.getItem(itemId).getItemProperty(propertyId).getValue();
						f.addShortcutListener(new ShortcutListener("Shortcut Name", ShortcutAction.KeyCode.ENTER, null) {
							public void handleAction(Object sender, Object target) {
								table.setData(null);
								table.refreshRowCache();
							}
						});
						f.addShortcutListener(new ShortcutListener("Shortcut Name", ShortcutAction.KeyCode.ESCAPE, null) {
							public void handleAction(Object sender, Object target) {
								f.setValue((String)backupValue);
								table.setData(null);
								table.refreshRowCache();
								table.focus();
							}
						});
						f.focus();
						field = f;
					} else {
						Function<FormParameter<Object>, Field<?>> init = columnInfo.getFieldInitializer();
						field = init.apply(formParameter);
					}
					return field;
				}
				return null;
			}


		});

		addShortcutListener(new ShortcutListener("Another Shortcut Name", ShortcutAction.KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				Object value = table.getValue();

				if (table.getData() == null) {
					table.setData(value);
					table.refreshRowCache();
				} else if (value == table.getData()) {
					table.setData(null);
					table.refreshRowCache();
					table.focus();
				} else if(value != table.getData()) {
					table.setData(value);
					table.refreshRowCache();
				}
			}
		});
		addActionHandler(new Action.Handler() {

			@Override
			public void handleAction(Action action, Object sender, Object target) {
				if(action == ADD) {
					T newBean = null;
					if(isBaseTypeContainer() || createNewFunction != null) {
						newBean	= createNewFunction.apply(getFormParameter());
					} else {
						try {
							newBean	= targetClass.newInstance();
						} catch (Exception e) {
							logger.error("Error while creating new bean",e);
						}
					}
					Object itemId = beanContainer.getBeanIdResolver().getIdForBean(newBean);
					BeanItem<T> beanItem = beanContainer.addItemAfter(target, itemId, newBean);
					table.setData(itemId);
					table.refreshRowCache();
				} else if (action == REMOVE) {
					beanContainer.removeItem(target);
				}
			}

			@Override
			public Action[] getActions(Object target, Object sender) {
				return new Action[] {ADD,REMOVE};
			}
		});
		table.setEditable(true);
		table.addItemClickListener(event -> {
			if(event.isDoubleClick()) {
				Object itemId = event.getItemId();
				if (table.getData() == null) {
					table.setData(itemId);
					table.refreshRowCache();
				} else if (itemId == table.getData()) {
					table.setData(null);
					table.refreshRowCache();
				} else if(itemId != table.getData()) {
					table.setData(null);
					table.setData(itemId);
					table.refreshRowCache();

				}
			}
		});		

	}

	public FormParameter<Object> getFormParameter() {
		return formParameter;
	}

	public void setFormParameter(FormParameter<Object> formParameter) {
		this.formParameter = formParameter;
	}

	@SuppressWarnings("unchecked" )
	public List getOriginalList() {
		ArrayList al = new ArrayList<>();
		for (Object object : beanContainer.getItemIds()) {
			T bean = beanContainer.getItem(object).getBean();
			if(isBaseTypeContainer())
				al.add(((BaseTypeContainer) bean).getValue());
			else 
				al.add(bean);
		}
		return al;
	}

	public void setOriginalList(List values) {
		this.original = values;
	}

	public Class<T> getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(Class<T> targetClass) {
		this.targetClass = targetClass;
	}

	public boolean isBaseTypeContainer() {
		return targetClass == BaseTypeContainer.class;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public EditableTableParameters<T> getParameters() {
		return parameters;
	}

	public void setParameters(EditableTableParameters<T> parameters) {
		this.parameters = parameters;
	}


}
