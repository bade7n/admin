package pro.deta.detatrak.controls.schedule;

import com.vaadin.addon.jpacontainer.JPAContainer;

import pro.deta.detatrak.MyUI;
import pro.deta.detatrak.presenter.LayoutEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.view.layout.DetaFormLayout;
import pro.deta.detatrak.view.layout.FieldLayout;
import pro.deta.detatrak.view.layout.FieldLayout.FieldType;
import pro.deta.detatrak.view.layout.Layout;
import pro.deta.detatrak.view.layout.SaveCancelLayout;
import pro.deta.detatrak.view.layout.TabSheetLayout;
import pro.deta.detatrak.view.layout.ValuesContainer;
import ru.yar.vi.rm.data.ActionDO;
import ru.yar.vi.rm.data.CriteriaDO;
import ru.yar.vi.rm.data.CustomerDO;
import ru.yar.vi.rm.data.ScheduleDO;

public class ScheduleView extends LayoutEntityViewBase<ScheduleDO> {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2309345860681089162L;

	public static final String NAV_KEY = "scheduleView";

    public ScheduleView() {
    	super(ScheduleDO.class);
    }

	@Override
	public String getNavKey() {
		return NAV_KEY;
	}


	@Override
	public Layout getFormDefinition() {
		JPAContainer<CustomerDO> customerDataSource =  JPAUtils.createCachingJPAContainer(CustomerDO.class);
		JPAContainer<CriteriaDO> criteriaDataSource =  JPAUtils.createCachingJPAContainer(CriteriaDO.class);
		JPAContainer<ActionDO> actionDataSource =   JPAUtils.createCachingJPAContainer(ActionDO.class);

		TabSheetLayout l = new TabSheetLayout();
		l.addTab(new DetaFormLayout("Основные настройки",
				new FieldLayout("Объект", "object", FieldLayout.FieldType.COMBOBOX,new ValuesContainer<>(MyUI.getCurrentUI().getObjectContainer())),
				new FieldLayout("Тип клиента", "customer", FieldLayout.FieldType.COMBOBOX_WITHNULL,new ValuesContainer<>(customerDataSource)),
				new FieldLayout("Доступ", "security", FieldType.ACCESSCOMBOBOX),
				new FieldLayout("Расписание", "schedule", FieldLayout.FieldType.SCHEDULE),
				new SaveCancelLayout(this)
		));

		l.addTab(new DetaFormLayout("Дополнительно",
				new FieldLayout("Услуги", "actions", FieldLayout.FieldType.TWINCOLSELECT,new ValuesContainer<>(actionDataSource)),
				new FieldLayout("Дополнительные критерии", "criterias", FieldLayout.FieldType.TWINCOLSELECT,new ValuesContainer<>(criteriaDataSource)),
				new FieldLayout("Дата начала действия", "start", FieldLayout.FieldType.DATEFIELD),
				new FieldLayout("Дата окончания действия", "end", FieldLayout.FieldType.DATEFIELD),
				new SaveCancelLayout(this)
				));

		return l;
	}
	    // "10:00-13:00+14:00-15:00|6;10:00-13:00+14:00-17:00|245&39,41;10:00-12:00|135&1-23,3,2"
	
}
