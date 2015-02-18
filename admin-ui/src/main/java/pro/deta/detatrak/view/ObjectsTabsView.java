package pro.deta.detatrak.view;

import pro.deta.detatrak.MyUI;
import pro.deta.detatrak.common.TableBuilder;
import pro.deta.detatrak.controls.objects.ObjectView;
import pro.deta.detatrak.controls.objects.OfficeView;
import pro.deta.detatrak.controls.objects.RegionView;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.util.RightPaneTabsView;
import pro.deta.detatrak.util.TopLevelMenuView;
import pro.deta.security.SecurityElement;
import ru.yar.vi.rm.data.ObjectDO;
import ru.yar.vi.rm.data.OfficeDO;
import ru.yar.vi.rm.data.RegionDO;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.TabSheet;

@TopLevelMenuView(icon="icon-object")
public class ObjectsTabsView extends RightPaneTabsView implements Captioned,Initializable,Restrictable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4457086225295000365L;
	public static final String NAV_KEY = "/objects";

	private JPAContainer<ObjectDO> objectsContainer;
	private JPAContainer<OfficeDO> officesContainer;
	private JPAContainer<RegionDO> regionsContainer;

	public void initTabs(TabSheet tabs) {
		objectsContainer = JPAUtils.createCachingJPAContainer(ObjectDO.class);
		officesContainer = JPAUtils.createCachingJPAContainer(OfficeDO.class);
		regionsContainer = JPAUtils.createCachingJPAContainer(RegionDO.class);
		objectsContainer.addNestedContainerProperty("office.name");
		objectsContainer.addNestedContainerProperty("type.name");
		
		addForInitialization(this);
		addTab(createObjectsTable());
		addTab(createOfficesTable());
		addTab(createRegionsTable());
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

	private TableBuilder createObjectsTable() {
		ObjectView view = new ObjectView();
		TableBuilder tb = new TableBuilder()
		.setCaption(bundle.getString("label.objects"))
		.addColumn("name", bundle.getString("label.name"))
		.addColumn("type.name", "Тип")
        .addColumn("office.name", "Офис")
		.setBeanContainer(objectsContainer)
		.setEditItemKey(view.getNavKey());
		addForInitialization(view);
		return tb;
	}

	private TableBuilder createOfficesTable() {
		OfficeView view = new OfficeView();
		TableBuilder tb =
				new TableBuilder()
		.setCaption(bundle.getString("label.offices"))
		.addColumn("name", bundle.getString("label.name"))
		.addColumn("security", "Уровень доступа")
		.addColumn("schedule", "Расписание")
		.setEditItemKey(view.getNavKey())
		.setBeanContainer(officesContainer);
		addForInitialization(view);
		return tb;
	}

	private TableBuilder createRegionsTable() {
		RegionView view = new RegionView();
		TableBuilder panel = new TableBuilder()
		.setCaption(bundle.getString("label.regions"))
		.addColumn("name", bundle.getString("label.region"))
		.setEditItemKey(view.getNavKey())
		.setBeanContainer(regionsContainer);
		addForInitialization(view);
		return panel;
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
