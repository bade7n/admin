package pro.deta.detatrak.view;

import pro.deta.detatrak.MyUI;
import pro.deta.detatrak.controls.objects.ObjectView;
import pro.deta.detatrak.controls.objects.OfficeView;
import pro.deta.detatrak.controls.objects.RegionView;
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
import ru.yar.vi.rm.data.RegionDO;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.navigator.ViewChangeListener;

@TopLevelMenuView(icon="icon-object")
public class ObjectsTabsView extends NewRightPaneTabsView implements Captioned,Initializable,Restrictable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4457086225295000365L;
	public static final String NAV_KEY = "/objects";

	private JPAContainer<ObjectDO> objectsContainer;
	private JPAContainer<OfficeDO> officesContainer;
	private JPAContainer<RegionDO> regionsContainer;

	
    @Override
	public Layout getLayoutDefinition() {
		officesContainer = JPAUtils.createCachingJPAContainer(OfficeDO.class);
    	objectsContainer = JPAUtils.createCachingJPAContainer(ObjectDO.class);
		regionsContainer = JPAUtils.createCachingJPAContainer(RegionDO.class);
		objectsContainer.addNestedContainerProperty("office.name");
		objectsContainer.addNestedContainerProperty("type.name");
		
    	TabSheetLayout tsl = new TabSheetLayout();//officesContainer.getItem(10)
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
    	RegionView region = new RegionView();
    	tsl.addTab(new TableLayout(regionsContainer, bundle.getString("label.regions"),region.getNavKey(), 
    			new TableColumnLayout("name",  bundle.getString("label.name"))
    	));

        addForInitialization(this);
        addForInitialization(office,officesContainer);
        addForInitialization(object,objectsContainer);
        addForInitialization(region,regionsContainer);
        return tsl;
    }

	@Override
	public void changeView(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
		objectsContainer.removeAllContainerFilters();    		
		officesContainer.removeAllContainerFilters();
		regionsContainer.removeAllContainerFilters();

		OfficeDO office = MyUI.getCurrentUI().getOffice();
		if(office != null) {
			Filter filter = new Compare.Equal("office", office);
			objectsContainer.addContainerFilter(filter);

			Filter officeFilter = new Compare.Equal("id", office.getId());
			officesContainer.addContainerFilter(officeFilter);
		}
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
