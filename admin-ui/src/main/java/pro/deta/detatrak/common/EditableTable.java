package pro.deta.detatrak.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
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
import pro.deta.detatrak.view.layout.EditableTableParameters;
import pro.deta.detatrak.view.layout.TableColumnInfo;

public class EditableTable extends Table {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2851285307777397438L;
	protected static final Action REMOVE = new Action("Remove row");
	protected static final Action ADD = new Action("Add row");

	
	
	private Class<?> targetClass;
	private Container.Ordered orderedContainer;
	private List<?> original = null;
	private boolean baseTypeContainer;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getOriginalList() {
		ArrayList al = new ArrayList();
		if(baseTypeContainer) {
			List<BaseTypeContainer> btc = (List<BaseTypeContainer>) orderedContainer.getItemIds();
			al = btc.stream().map(item -> item.getValue()).collect(Collectors.toCollection(ArrayList::new));
		} else
			al.addAll(orderedContainer.getItemIds());
		return al;
	}

	public void setOriginalList(List<?> values) {
		this.original = values;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	@Override
	public void setContainerDataSource(Container orderedContainer) {
		if(orderedContainer != null) {
			if(orderedContainer instanceof Container.Indexed)
				setContainerDataSource((Container.Indexed) orderedContainer);
			else
				throw new IllegalArgumentException("Should use Container.Ordered interface");
		} else 
			super.setContainerDataSource(orderedContainer);
	}
		
	public void setContainerDataSource(Container.Ordered orderedContainer) {
		super.setContainerDataSource(orderedContainer);
		this.orderedContainer = orderedContainer;
	}
	
	public void initialize(EditableTableParameters p) {
		final EditableTable table = this;
		table.setDragMode(TableDragMode.ROW);
		table.setSizeFull();
		table.setHeight(300, Unit.PIXELS);
		table.addStyleName("editable-table");
		table.setTabIndex(p.getTabIndex());
		setTargetClass(p.getTargetClass());

		BeanItemContainer bic = new BeanItemContainer(getTargetClass()); 
		if("java.lang".equalsIgnoreCase(getTargetClass().getPackage().getName()))
			baseTypeContainer = true;
		
		if(baseTypeContainer) {
			// list of values is simple type, should be packed in BaseTypeContainer.
			// we should ignore column information
			p.setColumnHeaders(new TableColumnInfo[] {new TableColumnInfo("value", getCaption())});
			ArrayList<BaseTypeContainer> al = original.stream().map(value -> new BaseTypeContainer(value)).collect(Collectors.toCollection(ArrayList::new)); 
			bic = new BeanItemContainer(BaseTypeContainer.class);
			bic.addAll(al);
		} else {
			bic.addAll(original);
		}

		this.setContainerDataSource(bic);
		
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
		EditableTable table = this;
		setDropHandler(new DropHandler() {
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

				// Let's remove the source of the drag so we can add it back where requested...
				orderedContainer.removeItem(sourceItemId);

				if ( dropData.getDropLocation() == VerticalDropLocation.BOTTOM ) {
					orderedContainer.addItemAfter(targetItemId,sourceItemId);
				} else {
					Object prevItemId = orderedContainer.prevItemId(targetItemId);
					orderedContainer.addItemAfter(prevItemId, sourceItemId);
				}
				// tell that the persons are related
//				Notification.show(sourceItemId + " is related to " + targetItemId);
			}
		});
		table.setSelectable(true);
		table.addShortcutListener(new ShortcutListener("Shortcut Name", ShortcutAction.KeyCode.ENTER, null) {
			
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
					table.setData(null);
					table.setData(value);
					table.refreshRowCache();
				}
				
			}
		});
	}

	private void addEditAbility() {
		EditableTable table = this;
		table.setTableFieldFactory(new TableFieldFactory() {
			@Override
			public Field createField(Container container, Object itemId, Object propertyId, Component uiContext) {
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
					Object newItem = new BaseTypeContainer<String>("New item");
					orderedContainer.addItemAfter(target, newItem);
					table.setData(newItem);
					table.refreshRowCache();
				} else if (action == REMOVE) {
					orderedContainer.removeItem(target);
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
