package pro.deta.detatrak.controls.schedule;

import pro.deta.detatrak.MyUI;
import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.controls.schedule.converter.ScheduleBlock;
import pro.deta.detatrak.controls.schedule.converter.ScheduleBuilder;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.view.ScheduleTabsView;
import ru.yar.vi.rm.data.ActionDO;
import ru.yar.vi.rm.data.CriteriaDO;
import ru.yar.vi.rm.data.CustomerDO;
import ru.yar.vi.rm.data.ScheduleDO;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.fieldgroup.FieldGroup;

public class ScheduleView extends JPAEntityViewBase<ScheduleDO> {
    public static final String NAV_KEY = "scheduleView";

    private JPAContainer<CustomerDO> customerDataSource =  JPAUtils.createCachingJPAContainer(CustomerDO.class);
	private JPAContainer<CriteriaDO> criteriaDataSource =  JPAUtils.createCachingJPAContainer(CriteriaDO.class);
	private JPAContainer<ActionDO> actionDataSource =   JPAUtils.createCachingJPAContainer(ActionDO.class);

	private ScheduleBlock sb;

	private ScheduleTabsView scheduleTabsView;


    public ScheduleView(ScheduleTabsView scheduleTabsView) {
    	super(ScheduleDO.class);
    	this.scheduleTabsView = scheduleTabsView;
    }

    
    @Override
    public void saveEntity(ScheduleDO sch) {
    	sch.setSchedule(ScheduleBuilder.getScheduleString(sb));
    }
    
    @Override
    public void postSaveEntity(ScheduleDO obj) {
    	scheduleTabsView.getScheduleContainer().refreshItem(itemId);
    };

	@Override
	protected void initForm(FieldGroup binder,ScheduleDO schedule) {
        form.addComponent(ComponentsBuilder.createComboBoxWithDataSource("Объект",MyUI.getCurrentUI().getObjectContainer(),binder,"object","name"));
        form.addComponent(ComponentsBuilder.createComboBoxWithDataSource("Тип клиента", customerDataSource,binder,"customer","name"));

        form.addComponent(ComponentsBuilder.createAccessComboBox(binder,"security"));

        form.addComponent(ComponentsBuilder.createTwinColSelect("Дополнительные критерии", criteriaDataSource,binder,"criterias"));
        form.addComponent(ComponentsBuilder.createTwinColSelect("Услуги", actionDataSource,binder,"actions"));
        
        sb = ScheduleBuilder.getScheduleByString(schedule.getSchedule());
        form.addComponent(sb.getScheduleElementsLayout());

		form.addComponent(ComponentsBuilder.createDateField("Дата начала действия",binder,"start"));
		form.addComponent(ComponentsBuilder.createDateField("Дата окончания действия",binder,"end"));

        form.addComponent(ComponentsBuilder.createSaveCancelButtons(this));
	}

	@Override
	public String getNavKey() {
		return NAV_KEY;
	}


	    // "10:00-13:00+14:00-15:00|6;10:00-13:00+14:00-17:00|245&39,41;10:00-12:00|135&1-23,3,2"
	
}
