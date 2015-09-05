package pro.deta.detatrak.view;

import java.util.List;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.navigator.ViewChangeListener;

import pro.deta.detatrak.MyUI;
import pro.deta.detatrak.controls.objects.ObjectView;
import pro.deta.detatrak.controls.objects.OfficeView;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.util.NewRightPaneTabsView;
import pro.deta.detatrak.util.TopLevelMenuView;
import pro.deta.detatrak.view.layout.Layout;
import pro.deta.detatrak.view.layout.TabSheetLayout;
import pro.deta.detatrak.view.layout.TableColumnLayout;
import pro.deta.detatrak.view.layout.TableLayout;
import pro.deta.security.SecurityElement;
import ru.yar.vi.rm.data.ObjectDO;
import ru.yar.vi.rm.data.OfficeDO;

@TopLevelMenuView(icon="icon-object")
public class ObjectsTabsView extends NewRightPaneTabsView implements Captioned,Initializable,Restrictable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4457086225295000365L;
	public static final String NAV_KEY = "/objects";

	private JPAContainer<ObjectDO> objectsContainer;
	private BeanContainer<Integer, OfficeDO> officesContainer;

	
    @Override
	public Layout getLayoutDefinition() {
    	officesContainer = MyUI.getCurrentUI().getOfficeContainer();
    	
    	objectsContainer = MyUI.getCurrentUI().getObjectContainer();
		objectsContainer.addNestedContainerProperty("office.name");
		objectsContainer.addNestedContainerProperty("type.name");
		
    	TabSheetLayout tsl = new TabSheetLayout();
    	OfficeView office = new OfficeView();
    	tsl.addTab(new TableLayout(officesContainer, bundle.getString("label.offices"),office.getNavKey(), 
    			new TableColumnLayout("name", bundle.getString("label.name")),
    			new TableColumnLayout("schedule", bundle.getString("label.schedule")),
    			new TableColumnLayout("security", bundle.getString("label.security"))
    	));
    	ObjectView object = new ObjectView();
    	tsl.addTab(new TableLayout(objectsContainer,bundle.getString("label.objects"), object.getNavKey(), 
    			new TableColumnLayout("name", bundle.getString("label.name")),
    			new TableColumnLayout("type.name", bundle.getString("label.type")),
    			new TableColumnLayout("office.name", bundle.getString("label.office"))
    	));

        addForInitialization(this);
        addForInitialization(office,officesContainer);
        addForInitialization(object,objectsContainer);
        return tsl;
    }

	@Override
	public void changeView(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
		officesContainer = MyUI.getCurrentUI().getOfficeContainer();
		objectsContainer = MyUI.getCurrentUI().getObjectContainer();
	}

	
	@Override
	public String getCaption() {
		return bundle.getString("label.objects");
	}

	@Override
	public SecurityElement getRestriction() {
		return SecurityElement.OBJECTS_TAB;
	}
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}
