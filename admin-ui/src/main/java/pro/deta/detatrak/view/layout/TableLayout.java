package pro.deta.detatrak.view.layout;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.slf4j.LoggerFactory;

import pro.deta.detatrak.MyUI;
import pro.deta.detatrak.common.TableBuilder;
import pro.deta.detatrak.confirmdialog.ConfirmDialog;
import pro.deta.detatrak.confirmdialog.ConfirmDialog.Listener;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

public class TableLayout  implements Layout<FormParameter<Object>> {
	private TableColumnLayout[] columns = new TableColumnLayout[0];
	private String editItemNavigationKey;
	private Container container;
	private String caption;
	
	public TableLayout(Container container,String caption,String editItemNavigationKey,TableColumnLayout ...columns) {
		this.container = container;
		this.editItemNavigationKey = editItemNavigationKey;
		this.columns = columns;
		this.caption = caption;
	}
	
	@Override
	public Component build(BuildLayoutParameter<FormParameter<Object>> param)
			throws LayoutDefinitionException {
		final Table table = createTable();
		BuildLayoutParameter<TableParameter> blp = new BuildLayoutParameter<TableParameter>() {
			@Override
			public TableParameter getData() {
				return new TableParameter(table);
			}
		};
		table.setContainerDataSource(container);
		for (TableColumnLayout tableColumnLayout : columns) {
			tableColumnLayout.build(blp);
		}
		Object[] clmn = Arrays.stream(columns).map(x -> x.getName()).toArray();
		table.setVisibleColumns(clmn);
		
		Component root = createLayout(table);
		return root;
	}
	private void addItemClickListener(ComponentContainer componentContainer, Table table) {
		table.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = 1436529222668499538L;

			@Override
			public void itemClick(ItemClickEvent event) {
				if(event.isDoubleClick()) {
					MyUI.getCurrentUI().getViewDisplay().setContainer(componentContainer);
					MyUI.getCurrentUI().getNavigator().navigateTo(editItemNavigationKey + '/' + event.getItemId());
				}
			}

		});
	}
	
	private Component createLayout(Table table) {
		CssLayout css = new CssLayout();
		css.setSizeFull();
		css.addStyleName("tabsheet-content");
		css.setCaption(caption);

		VerticalLayout verticalLayout = new VerticalLayout();
		css.addComponent(verticalLayout);
		
		verticalLayout.setSizeFull();
		verticalLayout.addStyleName("right-panel");
		
		Component plusButton = addPlusButton(css);
		addServiceColumn(css,table);
		addItemClickListener(css,table);
		addActionHandlers(css,table);

		verticalLayout.addComponent(plusButton);
		verticalLayout.addComponent(table);
		verticalLayout.setExpandRatio(table, 1.0f);

		return css;
	}

	static final Action ACTION_DELETE = new Action("Delete");
	static final Action ACTION_EDIT = new Action("Edit");
	
	private void addActionHandlers(ComponentContainer componentContainer, Table table) {
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
					MyUI.getCurrentUI().getViewDisplay().setContainer(componentContainer);
					MyUI.getCurrentUI().getNavigator().navigateTo(editItemNavigationKey + '/' + target);
				}
			}
		});		
	}

	protected Table createTable() {
		Table table = new Table() {
			private static final long serialVersionUID = 2689155068351476684L;
			
			private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			@Override
			protected String formatPropertyValue(Object rowId, Object colId,
					Property<?> property) {
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
		table.setSizeFull();
		table.addStyleName("plain");
		table.addStyleName("borderless");
		table.addStyleName("listing");
		//		table.setImmediate(true);
		table.setPageLength(100);
		return table;
	}
	
	protected void addServiceColumn(ComponentContainer componentContainer,Table table) {
		table.addGeneratedColumn("", (source, itemId, columnId) -> {
			HorizontalLayout layout = new HorizontalLayout();
			final Button editButton = new Button();
			editButton.setStyleName(BaseTheme.BUTTON_LINK);
			editButton.addStyleName("glyphicon");
			editButton.addStyleName("glyphicon-pencil");
			editButton.addClickListener(event -> {
				MyUI.getCurrentUI().getViewDisplay().setContainer(componentContainer);
				MyUI.getCurrentUI().getNavigator().navigateTo(editItemNavigationKey + '/' + itemId);
			});
			layout.addComponent(editButton);
			Button deleteButton = new Button();
			deleteButton.setStyleName(BaseTheme.BUTTON_LINK);
			deleteButton.addStyleName("glyphicon");
			deleteButton.addStyleName("glyphicon-remove");
			deleteButton.addClickListener(clickEvent -> ConfirmDialog.show(MyUI.getCurrent(), "Подтверждение", "Вы уверены?",
					"Да", "Нет", 
					(Listener) dialog -> {
						if (dialog.isConfirmed()) {
							table.getContainerDataSource().removeItem(itemId);
							table.removeItem(itemId);
						} else {
						}
					}));
			layout.addComponent(deleteButton);
			return layout;
		});
	}

	protected Button addPlusButton(ComponentContainer componentContainer) {
		Button button = new Button("Добавить");
		button.addStyleName("small");
		button.addClickListener(event -> {
			MyUI.getCurrentUI().getViewDisplay().setContainer(componentContainer);
			MyUI.getCurrentUI().getNavigator().navigateTo(editItemNavigationKey);
		});
		return button;
	}

	public void save(BuildLayoutParameter<FormParameter<Object>> p) throws LayoutRuntimeException {
		for (TableColumnLayout layout : columns) {
			layout.save(p);
		}
	}
	
	public void cancel(BuildLayoutParameter<FormParameter<Object>> p) throws LayoutRuntimeException {
		for (TableColumnLayout layout : columns) {
			layout.cancel(p);
		}		
	}
}
