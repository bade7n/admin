package pro.deta.detatrak;

import java.util.ArrayList;
import java.util.List;

import pro.deta.detatrak.controls.service.ActionView;
import pro.deta.detatrak.listbuilder.ListBuilder;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.util.MyTwinColSelectStringConverter;
import pro.deta.detatrak.view.ScheduleTabsView;
import ru.yar.vi.rm.data.ObjectDO;
import ru.yar.vi.rm.data.UserDO;
import ru.yar.vi.rm.data.WeekendDO;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.fieldfactory.FieldFactory;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;


@Theme("mytheme")
@SuppressWarnings("serial")
@PreserveOnRefresh
public class MyTestUI extends MyUI {
	JPAContainer<ObjectDO> availableObjects = null;
	private JPAContainer<WeekendDO> weekendItems;
	private EntityItem<WeekendDO> weekendItem;
	private TwinColSelect objectsSelect;
	BeanFieldGroup<WeekendDO> binder = null;
	FieldGroup fieldBinder = null;
	private ListBuilder listBuilder;
	
	
	protected void postInit() {
		user = new UserDO();
		
//		user.setName("admin");
		user.setDescription("Администратор");
		buildOfficeChooser();
//		getNavigator().navigateTo(ScheduleTabsView.NAV_KEY);
		
//		buildOfficeChooser();
		root.removeAllComponents();
//		buildMenu();
		setNavigator(new Navigator(this, root));
		getNavigator().addView("actionView", new ActionView());
		getNavigator().navigateTo("actionView/1");
//		getNavigator().navigateTo("/service");
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

