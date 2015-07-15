package pro.deta.detatrak.view;


import java.util.List;

import pro.deta.detatrak.MyUI;
import pro.deta.detatrak.controls.schedule.DurationView;
import pro.deta.detatrak.controls.schedule.ScheduleView;
import pro.deta.detatrak.controls.schedule.WeekendOfficeQueryModifierDelegate;
import pro.deta.detatrak.controls.schedule.WeekendView;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.util.NewRightPaneTabsView;
import pro.deta.detatrak.util.TopLevelMenuView;
import pro.deta.detatrak.view.layout.Layout;
import pro.deta.detatrak.view.layout.TabSheetLayout;
import pro.deta.detatrak.view.layout.TableColumnLayout;
import pro.deta.detatrak.view.layout.TableLayout;
import pro.deta.security.SecurityElement;
import ru.yar.vi.rm.data.DurationDO;
import ru.yar.vi.rm.data.ObjectDO;
import ru.yar.vi.rm.data.OfficeDO;
import ru.yar.vi.rm.data.ScheduleDO;
import ru.yar.vi.rm.data.WeekendDO;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;

@TopLevelMenuView(icon="icon-schedule")
public class ScheduleTabsView extends NewRightPaneTabsView implements Captioned,Initializable,Restrictable {

	private static final long serialVersionUID = 8005585643379672225L;
	public static final String NAV_KEY = "/schedule";
	private static final int DATE_WIDTH = 85;
	private JPAContainer<ScheduleDO> scheduleContainer;
	private JPAContainer<DurationDO> durationContainer;
	private JPAContainer<WeekendDO> weekendContainer;    

	@Override
	public void changeView(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
			scheduleContainer.removeAllContainerFilters();    		
			durationContainer.removeAllContainerFilters();

			OfficeDO office = MyUI.getCurrentUI().getOffice();
			if(office != null) {
				Filter filter = new Compare.Equal("object.office", office);
				scheduleContainer.addContainerFilter(filter);

				Filter durationFilter = new Compare.Equal("object.office", office);
				durationContainer.addContainerFilter(durationFilter);

			}
	}
	
	@Override
	public Layout getLayoutDefinition() {
		scheduleContainer = JPAUtils.createCachingJPAContainer(ScheduleDO.class);
		durationContainer = JPAUtils.createCachingJPAContainer(DurationDO.class);
		weekendContainer = JPAUtils.createCachingJPAContainer(WeekendDO.class);
		scheduleContainer.addNestedContainerProperty("object.name");
		durationContainer.addNestedContainerProperty("type.name");
		durationContainer.addNestedContainerProperty("object.name");
		durationContainer.addNestedContainerProperty("action.name");

		weekendContainer.getEntityProvider().setQueryModifierDelegate(new WeekendOfficeQueryModifierDelegate() );
		
    	TabSheetLayout tsl = new TabSheetLayout();
    	ScheduleView schedule = new ScheduleView();
    	tsl.addTab(new TableLayout(scheduleContainer,bundle.getString("label.schedule"), schedule.getNavKey(),
    			new TableColumnLayout("start",bundle.getString("label.start"),DATE_WIDTH),
    			new TableColumnLayout("end",bundle.getString("label.end"),DATE_WIDTH),
    			new TableColumnLayout("object.name", bundle.getString("label.object"),0.3f),
    			new TableColumnLayout("schedule", bundle.getString("label.schedule"),new Table.ColumnGenerator() {
    				private static final long serialVersionUID = -2724424682295129487L;

    				@Override
    				public Object generateCell(Table source, Object itemId, Object columnId) {
    					String value = (String) source.getContainerProperty(itemId, "schedule").getValue();
    					if(value == null)
    						value = "";
    					int rows = value.split(";").length;

    					int length = value.length();
    					value = value.replaceAll(";", "; ");
    					if(length > 80 && rows < 2)
    						//rows++;
    						if(length > 110 && rows < 3)
    							rows++;
    					if(length < 35 && rows > 1)
    						rows--;
    					TextArea ta = new TextArea();
    					ta.addStyleName("hidden");
    					ta.setRows(rows);
    					ta.setWordwrap(true);
    					ta.setValue(value);
    					ta.setSizeFull();
    					ta.setReadOnly(true);
    					return ta;
    				}
    			})
    	));
    	
    	DurationView duration = new DurationView();
    	tsl.addTab(new TableLayout(durationContainer, bundle.getString("label.duration"),duration.getNavKey(), 
    			new TableColumnLayout("start",bundle.getString("label.start"),DATE_WIDTH),
    			new TableColumnLayout("end",bundle.getString("label.end"),DATE_WIDTH),
    			new TableColumnLayout("action.name", bundle.getString("label.action"),0.5f),
    			new TableColumnLayout("type.name", bundle.getString("label.type"),0.2f),
    			new TableColumnLayout("object.name", bundle.getString("label.object"),0.3f),
    			new TableColumnLayout("min", bundle.getString("label.min"))
    	));

		
    	WeekendView weekend = new WeekendView();
    	tsl.addTab(new TableLayout(weekendContainer, bundle.getString("label.weekend"),weekend.getNavKey(), 
    			new TableColumnLayout("start",bundle.getString("label.start"),DATE_WIDTH),
    			new TableColumnLayout("end",bundle.getString("label.end"),DATE_WIDTH),
    			new TableColumnLayout("objects", bundle.getString("label.objects"), new Table.ColumnGenerator() {
    				/**
    				 * 
    				 */
    				private static final long serialVersionUID = 3867709237732181054L;

    				@SuppressWarnings("unchecked")
    				@Override
    				public Object generateCell(Table source, Object itemId, Object columnId) {
    					List<ObjectDO> objects = (List<ObjectDO>) source.getContainerProperty(itemId, "objects").getValue();
    					String value = "";
    					for (ObjectDO c : objects) {
    						value += c.getName()+"\n";
    					}
//    					return new Label(value, ContentMode.HTML);
    					TextArea ta = new TextArea();
    					ta.setRows(objects.size() > 10 ? 10 : objects.size());
    					ta.setValue(value);
    					ta.setSizeFull();
    					ta.setReadOnly(true);
    					return ta;
    				}
    			},0.7f),
    			new TableColumnLayout("schedule",bundle.getString("label.schedule"),0.2f)
    	
    	));
    	
        addForInitialization(this);
        addForInitialization(schedule,scheduleContainer);
        addForInitialization(duration,durationContainer);
        addForInitialization(weekend,weekendContainer);
        return tsl;
    }

	
	
	
	@Override
	public String getCaption() {
		return bundle.getString("label.schedule");
	}

	public JPAContainer<ScheduleDO> getScheduleContainer() {
		return scheduleContainer;
	}

	public JPAContainer<DurationDO> getDurationContainer() {
		return durationContainer;
	}

	public JPAContainer<WeekendDO> getWeekendContainer() {
		return weekendContainer;
	}

	@Override
	public SecurityElement getRestriction() {
		return SecurityElement.SCHEDULE_TAB;
	}

	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}
