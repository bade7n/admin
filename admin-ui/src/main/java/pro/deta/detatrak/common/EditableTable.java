package pro.deta.detatrak.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanContainer;
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
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Table;
import com.vaadin.ui.TableFieldFactory;
import com.vaadin.ui.TextField;

import pro.deta.detatrak.BaseTypeContainer;
import pro.deta.detatrak.dao.data.T0;
import pro.deta.detatrak.view.layout.EditableTableParameters;
import pro.deta.detatrak.view.layout.TableColumnInfo;

public class EditableTable<T> extends Table {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2851285307777397438L;
	private static final Logger logger = LoggerFactory.getLogger(EditableTable.class);
	protected static final Action REMOVE = new Action("Remove row");
	protected static final Action ADD = new Action("Add row");

	
	
	private Class<T> targetClass;
	private BeanContainer<Object, T> beanContainer;
	private List original = null;
	private Function<T0, T> createNewFunction;

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
	
	public void initialize(EditableTableParameters<T> p) {
		final EditableTable<T> table = this;
		table.setDragMode(TableDragMode.ROW);
		table.setSizeFull();
		table.setHeight(300, Unit.PIXELS);
		table.addStyleName("editable-table");
		table.setTabIndex(p.getTabIndex());
		setTargetClass(p.getTargetClass());
		this.createNewFunction = p.getCreateNew();
		
		beanContainer = new BeanContainer<>(getTargetClass());
		if(isBaseTypeContainer()) {
			// need BaseTypeContainer
			p.setColumnHeaders(new TableColumnInfo[] {new TableColumnInfo("value", getCaption())});
			p.setBeanIdProperty("value");
			original = (List<T>) original.stream().map(value -> new BaseTypeContainer(value)).collect(Collectors.toCollection(ArrayList::new)); 
			
		}
		beanContainer.setBeanIdProperty(p.getBeanIdProperty());
		beanContainer.addAll(original);
		this.setContainerDataSource(beanContainer);
			
			
		if(p.getColumnHeaders() != null)
			for (TableColumnInfo tableColumnInfo : p.getColumnHeaders()) {
				table.setColumnHeader(tableColumnInfo.getName(), tableColumnInfo.getCaption());
				if(tableColumnInfo.getWidth() != null)
					table.setColumnWidth(tableColumnInfo.getName(), tableColumnInfo.getWidth());
				if(tableColumnInfo.getExpandRatio() != null)
					table.setColumnExpandRatio(tableColumnInfo.getName(), tableColumnInfo.getExpandRatio());
			}
		table.setVisibleColumns(Arrays.asList(p.getColumnHeaders()).stream().map(item -> item.getName()).toArray());
		
		addEditAbility();
		addDragAndDropAbility();
	}

	private void addDragAndDropAbility() {
		final EditableTable<T> self = this;
		setDropHandler(new DropHandler() {
			@Override
			public AcceptCriterion getAcceptCriterion() {
				return new And(new SourceIs(self), AcceptItem.ALL);
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
		self.setSelectable(true);
		self.addShortcutListener(new ShortcutListener("Another Shortcut Name", ShortcutAction.KeyCode.ENTER, null) {
			
			@Override
			public void handleAction(Object sender, Object target) {
				EditableTable<T> table1 = null;
				if(target instanceof EditableTable) {
					table1 = (EditableTable<T>)target;
				} else if(target instanceof TextField) {
					TextField tf = (TextField) target;
					table1 = (EditableTable) tf.getParent();
				}
				if(table1 != null) {
					Object value = table1.getValue();

					if (table1.getData() == null) {
						table1.setData(value);
						table1.refreshRowCache();
					} else if (value == table1.getData()) {
						table1.setData(null);
						table1.refreshRowCache();
						table1.focus();
					} else if(value != table1.getData()) {
						table1.setData(value);
						table1.refreshRowCache();
					}
				}
			}
		});
	}

	private void addEditAbility() {
		EditableTable table = this;
		table.setTableFieldFactory(new TableFieldFactory() {
			@Override
			public Field createField(final Container container, final Object itemId, final Object propertyId, final Component uiContext) {
				if (itemId == table.getData()) {
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
					return f;
				}
				return null;
			}
		});
		table.addActionHandler(new Action.Handler() {
			
			@Override
			public void handleAction(Action action, Object sender, Object target) {
				if(action == ADD) {
					T newItem = null;
					if(isBaseTypeContainer() || createNewFunction != null) {
							newItem	= createNewFunction.apply(null);
					} else {
							try {
								newItem	= targetClass.newInstance();
							} catch (Exception e) {
								logger.error("Error while creating new bean",e);
							}
					}
					beanContainer.addBeanAfter(target, newItem);
					table.setData(newItem);
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

}
