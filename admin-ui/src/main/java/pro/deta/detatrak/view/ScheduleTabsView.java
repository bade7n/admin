package pro.deta.detatrak.view;


import java.util.List;

import pro.deta.detatrak.MyUI;
import pro.deta.detatrak.common.TableBuilder;
import pro.deta.detatrak.controls.schedule.DurationView;
import pro.deta.detatrak.controls.schedule.ScheduleView;
import pro.deta.detatrak.controls.schedule.WeekendOfficeQueryModifierDelegate;
import pro.deta.detatrak.controls.schedule.WeekendView;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.util.RightPaneTabsView;
import pro.deta.detatrak.util.TopLevelMenuView;
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
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;

@TopLevelMenuView(icon="icon-schedule")
public class ScheduleTabsView extends RightPaneTabsView implements Captioned,Initializable,Restrictable {

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
	public void initTabs(TabSheet tabs) {
		scheduleContainer = JPAUtils.createCachingJPAContainer(ScheduleDO.class);
		durationContainer = JPAUtils.createCachingJPAContainer(DurationDO.class);
		weekendContainer = JPAUtils.createCachingJPAContainer(WeekendDO.class);
		scheduleContainer.addNestedContainerProperty("object.name");
		durationContainer.addNestedContainerProperty("type.name");
		durationContainer.addNestedContainerProperty("object.name");
		durationContainer.addNestedContainerProperty("action.name");

		weekendContainer.getEntityProvider().setQueryModifierDelegate(new WeekendOfficeQueryModifierDelegate() );

		addForInitialization(this);
		addTab(createScheduleView());
		addTab(createDurationView());
		addTab(createWeekendView());
	}

	private TableBuilder createWeekendView() {
		WeekendView view = new WeekendView();
		TableBuilder weekendTabBuilder = new TableBuilder()
		.addColumn("start", bundle.getString("label.start"),DATE_WIDTH)
		.addColumn("end", bundle.getString("label.end"),DATE_WIDTH)
		.addColumn("objects", "Объекты", 0.7f, new Table.ColumnGenerator() {
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
//				return new Label(value, ContentMode.HTML);
				TextArea ta = new TextArea();
				ta.setRows(objects.size() > 10 ? 10 : objects.size());
				ta.setValue(value);
				ta.setSizeFull();
				ta.setReadOnly(true);
				return ta;
			}
		})
		.addColumn("schedule", "График",0.2f)
		.setEditItemKey(view.getNavKey())
		.setBeanContainer(weekendContainer)
		.setCaption(bundle.getString("label.weekend"));
		addForInitialization(view,weekendContainer);
		return weekendTabBuilder;
	}

	private TableBuilder createDurationView() {
		DurationView view = new DurationView();
		TableBuilder durationTabBuilder = new TableBuilder()
		.addColumn("start", bundle.getString("label.start"),DATE_WIDTH)
		.addColumn("end", bundle.getString("label.end"),DATE_WIDTH)
		.addColumn("action.name", "Услуга",0.5f)
		.addColumn("type.name", "Тип объекта",0.2f)
		.addColumn("object.name", "Объект",0.3f)
		.addColumn("min", "Время",40)
		.setEditItemKey(view.getNavKey())
		.setBeanContainer(durationContainer)
		.setCaption(bundle.getString("label.duration"));
		addForInitialization(view,durationContainer);
		return durationTabBuilder;
	}

	private TableBuilder createScheduleView() {
		ScheduleView view = new ScheduleView();
		TableBuilder scheduleTabBuilder = new TableBuilder()
		.addColumn("start", bundle.getString("label.start"),DATE_WIDTH)
		.addColumn("end", bundle.getString("label.end"),DATE_WIDTH)
		.addColumn("object.name", "Объект",0.3f)
		.addColumn("schedule", "График", 0.7f, new Table.ColumnGenerator() {
			/**
			 * 
			 */
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
		//        .addColumn("criteria", "Дополнительные критерии")
		//        .addColumn("action", "Услуги")
		.setEditItemKey(view.getNavKey())
		.setBeanContainer(scheduleContainer)
		.setCaption(bundle.getString("label.schedule"));
		addForInitialization(view,scheduleContainer);
		return scheduleTabBuilder;
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
