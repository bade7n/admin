package pro.deta.detatrak;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.fieldfactory.FieldFactory;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Container;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
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
import com.vaadin.navigator.Navigator;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.AbstractSelect.AbstractSelectTargetDetails;
import com.vaadin.ui.AbstractSelect.AcceptItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.ui.TableFieldFactory;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;

import pro.deta.detatrak.common.EditableTablePanel;
import pro.deta.detatrak.controls.extra.SiteView;
import pro.deta.detatrak.listbuilder.ListBuilder;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.util.MyTwinColSelectStringConverter;
import pro.deta.detatrak.view.layout.EditableTableParameters;
import pro.deta.detatrak.view.layout.TableColumnInfo;
import ru.yar.vi.rm.data.CustomerDO;
import ru.yar.vi.rm.data.ObjectDO;
import ru.yar.vi.rm.data.SiteDO;
import ru.yar.vi.rm.data.WeekendDO;


@Theme("mytheme")
@SuppressWarnings("serial")

public class MyTestUI extends MyUI {
	protected static final Action REMOVE = new Action("Remove row");
	protected static final Action ADD = new Action("Add row");
	JPAContainer<ObjectDO> availableObjects = null;
	private JPAContainer<WeekendDO> weekendItems;
	private EntityItem<WeekendDO> weekendItem;
	private TwinColSelect objectsSelect;
	BeanFieldGroup<WeekendDO> binder = null;
	FieldGroup fieldBinder = null;
	private ListBuilder listBuilder;


	protected void postInit() {
		//		user = new UserDO();
		//		
		//		user.setDescription("Администратор");
		//		buildOfficeChooser();
		root.removeAllComponents();
//		generateEditableTableList(root);
		setNavigator(new Navigator(this, root));
		SiteView av = new SiteView();
		av.setContainer(JPAUtils.createCachingJPAContainer(SiteDO.class));
		getNavigator().addView("siteView", av);
		getNavigator().navigateTo("siteView/684");
	}



	private void generateEditableTableList(HorizontalLayout root) {
		ArrayList<CustomerDO> customer = new ArrayList<>();
		customer.add(new CustomerDO(1,"Физическое лицо"));
		customer.add(new CustomerDO(2,"Юридическое лицо"));
		customer.add(new CustomerDO(3,"Юридическое лицо"));
		final EditableTablePanel<CustomerDO> table = new EditableTablePanel<>();
		table.setOriginalList(customer);
		table.setCaption("Пользователи");
		table.initialize(new EditableTableParameters<CustomerDO>(CustomerDO.class, new TableColumnInfo[]{new TableColumnInfo("name", "Тип пользователя")},
				t0 -> {
					return new CustomerDO(0,"Новый тип пользователя");
				},"id"));
		root.addComponent(table);
		Button btn = new Button("Clickme");
		btn.addClickListener(event -> {Notification.show(""+table.getOriginalList());});
		root.addComponent(btn);
	}


	private void generateTableList(HorizontalLayout root) {
		List<String> list = Arrays.asList(new String[]{"as","bsd"});
		ArrayList<BaseTypeContainer> values = list.stream().map(value -> new BaseTypeContainer(value)).collect(Collectors.toCollection(ArrayList::new)); 
		BeanItemContainer<BaseTypeContainer> bic = new BeanItemContainer<>(BaseTypeContainer.class);
		bic.addAll(values);
		final Table table = new Table();
		table.setDragMode(TableDragMode.ROW);
		table.setContainerDataSource(bic);
		table.setWidth(500.0f, Unit.PIXELS);
		table.setHeight(400.0f, Unit.PIXELS);
		table.setColumnHeader("value", "URL Mapping");
		table.setVisibleColumns("value");
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

				// Let's remove the source of the drag so we can add it back where requested...
				bic.removeItem(sourceItemId);

				if ( dropData.getDropLocation() == VerticalDropLocation.BOTTOM ) {
					bic.addItemAfter(targetItemId,sourceItemId);
				} else {
					Object prevItemId = bic.prevItemId(targetItemId);
					bic.addItemAfter(prevItemId, sourceItemId);
				}
				// tell that the persons are related
				Notification.show(sourceItemId + " is related to " + targetItemId);
			}
		});
		table.setTableFieldFactory(new TableFieldFactory() {

			@Override
			public Field createField(Container container, Object itemId, Object propertyId, Component uiContext) {
				if (itemId == table.getData()) {
					TextField f = (TextField) DefaultFieldFactory.get().createField(container, itemId, propertyId, uiContext);
					f.addShortcutListener(new ShortcutListener("Shortcut Name", ShortcutAction.KeyCode.ENTER, null) {
						public void handleAction(Object sender, Object target) {
							table.setData(null);
							table.refreshRowCache();
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
					Object newItem = new BaseTypeContainer("New item");
					bic.addItemAfter(target, newItem);
					table.setData(newItem);
					table.refreshRowCache();
				} else if (action == REMOVE) {
					bic.removeItem(target);
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
				if (table.getData() == null) {
					table.setData(event.getItemId());
					table.refreshRowCache();
				} else if (event.getItemId() == table.getData()) {
					table.setData(null);
					table.refreshRowCache();
				} else if(event.getItemId() != table.getData()) {
					table.setData(null);
//					table.refreshRowCache();
					table.setData(event.getItemId());
					table.refreshRowCache();
					
				}
			}
		});

		root.addComponent(table);
		Button btn = new Button("Habahaba");
		btn.addClickListener(event -> {Notification.show(Arrays.toString(bic.getItemIds().toArray()));});
		root.addComponent(btn);
	}

	private void generate3(VerticalLayout root) {
		weekendItem = weekendItems.getItem(3102);
		List objects  = weekendItem.getEntity().getObjects();

		listBuilder = new ListBuilder("ListBuilder component - preserves the item order");

		listBuilder.setImmediate(true);

		listBuilder.setLeftColumnCaption("Select from here");
		listBuilder.setRightColumnCaption("Selected items");

		listBuilder.setContainerDataSource(availableObjects);
		listBuilder.setItemCaptionPropertyId("name");

		List<ObjectDO> objList = weekendItem.getEntity().getObjects();
		List<Integer> objIdList = new ArrayList<Integer>();
		for (ObjectDO objectDO : objList) {
			objIdList.add(objectDO.getId());
		}
		listBuilder.setValue(objIdList);
		root.addComponent(listBuilder);

		Button ok = new Button("OK");
		ok.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent clickEvent) {
				ListBuilder o = listBuilder;
				o.commit(); // отражает существующее состояние объекта - без него изменения в контроле не попадают в entity
				JPAUtils.getEntityManager().getTransaction().begin();
				WeekendDO week = JPAUtils.getEntityManager().find(weekendItem.getEntity().getClass(), weekendItem.getItemId());
				week.getObjects().clear();
				for (Integer id : (List<Integer>)o.getValue()) {
					week.getObjects().add(availableObjects.getItem(id).getEntity());
				}
				System.out.println("Ids: "+o.getValue() + "WeekendDO " + week.getObjects());
				JPAUtils.getEntityManager().getTransaction().commit();
				//            	fieldBinder.setItemDataSource(weekendItem);
				//            	fieldBinder.bind(objectsSelect, "objects");
				//            	binder.setItemDataSource(weekendItem.getEntity());
			}
		});
		root.addComponent(ok);
	}


	private void generate2(VerticalLayout root) {
		final Form  objectForm  = new Form();
		final FieldFactory ff = new FieldFactory();
		objectForm.setFormFieldFactory(ff);
		weekendItem = weekendItems.getItem(3102);
		List objects  = weekendItem.getEntity().getObjects();
		objects.add(new Integer(1));
		objects.add(new Integer(2));
		objects.add(new Integer(3));
		for (ObjectDO o: weekendItem.getEntity().getObjects()) {
			o.getChilds().size();
		}
		objectForm.setItemDataSource(weekendItem);
		root.addComponent(objectForm);
	}


	private void generate1(VerticalLayout root) {
		weekendItem = weekendItems.getItem(new Integer(3102));
		List objects  = weekendItem.getEntity().getObjects();
		objectsSelect = new TwinColSelect("SomeCaption");
		objectsSelect.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
		objectsSelect.setItemCaptionPropertyId("name");
		objectsSelect.setLeftColumnCaption("Available");
		objectsSelect.setRightColumnCaption("Selected");
		objectsSelect.setContainerDataSource(availableObjects);
		objectsSelect.setConverter(new MyTwinColSelectStringConverter<ObjectDO>(availableObjects));

		//        objectsSelect.setPropertyDataSource();
		//        objectsSelect.setPropertyDataSource(new ListTranslator(objectsSelect));
		//        objectsSelect.setConverter(new MyMultiSelectConverter(objectsSelect));

		fieldBinder = new FieldGroup(weekendItem);
		fieldBinder.bind(objectsSelect, "objects");

		root.addComponent(objectsSelect);

		Button ok = new Button("OK");
		ok.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent clickEvent) {
				objectsSelect.commit(); // отражает существующее состояние объекта - без него изменения в контроле не попадают в entity
				JPAUtils.getEntityManager().getTransaction().begin();
				System.out.println("Ids: "+objectsSelect.getValue() + "WeekendDO " + weekendItem.getEntity().getObjects());
				JPAUtils.getEntityManager().getTransaction().commit();
				fieldBinder.setItemDataSource(weekendItem);
				fieldBinder.bind(objectsSelect, "objects");
				binder.setItemDataSource(weekendItem.getEntity());
			}
		});
		root.addComponent(ok);

	}

}

