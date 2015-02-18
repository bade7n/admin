package pro.deta.detatrak;

import java.util.ArrayList;
import java.util.List;

import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.controls.admin.RoleView;
import pro.deta.detatrak.listbuilder.ListBuilder;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.util.MyTwinColSelectStringConverter;
import pro.deta.detatrak.view.ScheduleTabsView;
import ru.yar.vi.rm.data.ActionDO;
import ru.yar.vi.rm.data.ObjectDO;
import ru.yar.vi.rm.data.UserDO;
import ru.yar.vi.rm.data.WeekendDO;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.fieldfactory.FieldFactory;
import com.vaadin.annotations.Theme;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;


@Theme("mytheme")
@SuppressWarnings("serial")
public class MyCKEUI extends MyUI {
	JPAContainer<ObjectDO> availableObjects = null;
	private JPAContainer<ActionDO> actionItems;
	private EntityItem<ActionDO> actionItem;
	BeanFieldGroup<ActionDO> binder = null;
	FieldGroup fieldBinder = null;
	
	protected void postInit() {
		user = new UserDO();
		user.setName("admin");
		user.setDescription("Администратор");
		buildOfficeChooser();
//		getNavigator().navigateTo(ScheduleTabsView.NAV_KEY);
		
		buildOfficeChooser();
		root.removeAllComponents();
		actionItems = JPAUtils.createCachingJPAContainer(ActionDO.class);
		
		generate2(root);
		
	}

	private void generate2(HorizontalLayout root) {
		final Form  objectForm  = new Form();
		final FieldFactory ff = new FieldFactory();
		objectForm.setFormFieldFactory(ff);
		actionItem = actionItems.getItem(1);
		fieldBinder = new FieldGroup(actionItem); 
		root.addComponent(ComponentsBuilder.createCKEditorTextField("Описание", fieldBinder, "description"));
	}
}

