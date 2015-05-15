package pro.deta.detatrak.common;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import pro.deta.detatrak.MyUI;
import pro.deta.detatrak.confirmdialog.ConfirmDialog;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

public class TableBuilder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5700756697064905578L;
	private Map<String,Integer> columnsWidth = new HashMap<>();
	private Map<String,Float> columnsExpandRatio = new HashMap<>();
	private Map<String,ColumnGenerator> columnsGen = new HashMap<>();
	private List<String> columnsCaptions = new LinkedList<>();
	private List<String> columnsNames = new LinkedList<>();
	protected Container container;
	private String editItemKey;
	private String caption;
	static final Action ACTION_DELETE = new Action("Delete");
	static final Action ACTION_EDIT = new Action("Edit");


	public String getCaption() {
		return caption;
	}

	public TableBuilder setCaption(String caption) {
		this.caption = caption;
		return this;
	}

	public TableBuilder setBeanContainer(Container container) {
		this.container = container;
		return this;
	}

	public TableBuilder addColumn(String name, String caption) {
		columnsNames.add(name);
		columnsCaptions.add(caption);
		return this;
	}

	public TableBuilder addColumn(String name, String caption,Integer width) {
		columnsNames.add(name);
		columnsCaptions.add(caption);
		columnsWidth.put(name, width);
		return this;
	}

	public TableBuilder addColumn(String name, String caption,ColumnGenerator gener) {
		columnsNames.add(name);
		columnsCaptions.add(caption);
		columnsGen.put(name, gener);
		return this;
	}

	public TableBuilder addColumn(String name, String caption,Float expandRatio) {
		columnsNames.add(name);
		columnsCaptions.add(caption);
		columnsExpandRatio.put(name, expandRatio);
		return this;
	}

	public TableBuilder addColumn(String name, String caption,Float expandRatio,ColumnGenerator gen) {
		columnsNames.add(name);
		columnsCaptions.add(caption);
		columnsExpandRatio.put(name, expandRatio);
		columnsGen.put(name, gen);
		return this;
	}

	public TableBuilder setEditItemKey(String key) {
		this.editItemKey = key;
		return this;
	}

	CssLayout l1 = new CssLayout();
	Table table = null;

	public Table getTable() {
		return table;
	}


	public Container getContainer() {
		return container;
	}


	public AbstractComponent createTable() {
		l1.setSizeFull();
		VerticalLayout layout = new VerticalLayout();
		l1.addComponent(layout);
		l1.addStyleName("tabsheet-content");
		table = createTableInstance();
		table.addStyleName("listing");
		table.setContainerDataSource(container);
		for (int i = 0; i < columnsNames.size(); ++i) {
			String name = columnsNames.get(i);
			table.setColumnHeader(name, columnsCaptions.get(i));
			if(columnsWidth.containsKey(name))
				table.setColumnWidth(name, columnsWidth.get(name));
			if(columnsExpandRatio.containsKey(name))
				table.setColumnExpandRatio(name, columnsExpandRatio.get(name));
			if(columnsGen.containsKey(name))
				table.addGeneratedColumn(name, columnsGen.get(name));
		}
		table.setVisibleColumns(columnsNames.toArray());

		table.addItemClickListener(new ItemClickListener() {

			@Override
			public void itemClick(ItemClickEvent event) {
				if(event.isDoubleClick()) {
					MyUI.getCurrentUI().getViewDisplay().setContainer(l1);
					MyUI.getCurrentUI().getNavigator().navigateTo(editItemKey + '/' + event.getItemId());
				}
			}

		});

		table.addActionHandler(new Action.Handler() {
			public Action[] getActions(Object target, Object sender) {
				return new Action[] { ACTION_EDIT,ACTION_DELETE };
			}

			public void handleAction(Action action, Object sender,final Object target) {
				if(action == ACTION_DELETE) {
					ConfirmDialog.show(MyUI.getCurrent(), "Подтверждение", "Вы уверены?",
							"Да", "Нет", new ConfirmDialog.Listener() {

						public void onClose(ConfirmDialog dialog) {
							if (dialog.isConfirmed()) {
								Item trg = container.getItem(target);
								container.removeItem(target);
							} else {
							}
						}
					});
				} else if(action == ACTION_EDIT) {
					MyUI.getCurrentUI().getViewDisplay().setContainer(l1);
					MyUI.getCurrentUI().getNavigator().navigateTo(editItemKey + '/' + target);
				}
			}
		});
		table.setSizeFull();
		table.addStyleName("plain");
		table.addStyleName("borderless");
		//		table.setImmediate(true);
		table.setPageLength(100);

		addServiceColumn();

		addPlusButton(layout);
		layout.addComponent(table);
		layout.setExpandRatio(table, 1.0f);
		layout.setSizeFull();
		layout.addStyleName("right-panel");
		return l1;
	}

	protected void addServiceColumn() {
		table.addGeneratedColumn("", new Table.ColumnGenerator() {
			Layout vlayout = l1;

			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				HorizontalLayout layout = new HorizontalLayout();
				final Button editButton = new Button();
				editButton.setStyleName(BaseTheme.BUTTON_LINK);
				editButton.addStyleName("glyphicon");
				editButton.addStyleName("glyphicon-pencil");
				editButton.addClickListener(new Button.ClickListener() {
					@Override
					public void buttonClick(Button.ClickEvent event) {
						MyUI.getCurrentUI().getViewDisplay().setContainer(vlayout);
						MyUI.getCurrentUI().getNavigator().navigateTo(editItemKey + '/' + itemId);
					}
				});
				layout.addComponent(editButton);
				Button deleteButton = new Button();
				deleteButton.setStyleName(BaseTheme.BUTTON_LINK);
				deleteButton.addStyleName("glyphicon");
				deleteButton.addStyleName("glyphicon-remove");
				deleteButton.addClickListener(new Button.ClickListener() {
					@Override
					public void buttonClick(Button.ClickEvent clickEvent) {
						ConfirmDialog.show(MyUI.getCurrent(), "Подтверждение", "Вы уверены?",
								"Да", "Нет", new ConfirmDialog.Listener() {

							public void onClose(ConfirmDialog dialog) {
								if (dialog.isConfirmed()) {
									table.getContainerDataSource().removeItem(itemId);
									table.removeItem(itemId);
								} else {
									// User did not confirm
									//                    	                    feedback(dialog.isConfirmed());
								}
							}
						});
					}
				});
				layout.addComponent(deleteButton);
				return layout;
			}

		});
	}

	protected void addPlusButton(VerticalLayout layout) {
		Button button = new Button("Добавить");
		button.addStyleName("small");
		button.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				MyUI.getCurrentUI().getViewDisplay().setContainer(l1);
				MyUI.getCurrentUI().getNavigator().navigateTo(editItemKey);
			}
		});
		layout.addComponent(button);
	}

	protected Table createTableInstance() {
		return new Table() {
			private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			@Override
			protected String formatPropertyValue(Object rowId, Object colId,
					Property property) {
				try {
					Object v = property.getValue();
					if (v instanceof Date) {
						Date dateValue = (Date) v;
						return sdf.format(dateValue);
					}
				} catch(Exception e) {
					LoggerFactory.getLogger(TableBuilder.class).error("Error while formatting property " + colId +" on " + rowId, e);
					return "RowId: " + rowId +" ColId: " + colId;
				}
				return super.formatPropertyValue(rowId, colId, property);

			}
		};
	}

	
}
