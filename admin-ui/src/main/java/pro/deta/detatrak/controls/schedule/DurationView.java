package pro.deta.detatrak.controls.schedule;

import pro.deta.detatrak.MyUI;
import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import ru.yar.vi.rm.data.ActionDO;
import ru.yar.vi.rm.data.CriteriaDO;
import ru.yar.vi.rm.data.CustomerDO;
import ru.yar.vi.rm.data.DurationDO;
import ru.yar.vi.rm.data.ObjectDO;
import ru.yar.vi.rm.data.ObjectTypeDO;
import ru.yar.vi.rm.data.OfficeDO;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.filter.Compare;

public class DurationView extends JPAEntityViewBase<DurationDO> {

	public static final String NAV_KEY = "durationView";

	private JPAContainer<CustomerDO> customerDataSource = JPAUtils.createCachingJPAContainer(CustomerDO.class);
	private JPAContainer<CriteriaDO> criteriaDataSource = JPAUtils.createCachingJPAContainer(CriteriaDO.class);
	private JPAContainer<ActionDO> actionDataSource = JPAUtils.createCachingJPAContainer(ActionDO.class);
	private JPAContainer<ObjectTypeDO> objectTypeDataSource = JPAUtils.createCachingJPAContainer(ObjectTypeDO.class);

	public DurationView() {
		super(DurationDO.class);
	}

		
	@Override
	public void initForm(FieldGroup binder,DurationDO duration) {
		form.addComponent(ComponentsBuilder.createComboBoxWithDataSource("Услуги", actionDataSource,binder, "action","name"));
		form.addComponent(ComponentsBuilder.createComboBoxWithDataSource("Тип клиента", customerDataSource,binder,"customer","name"));
		form.addComponent(ComponentsBuilder.createTextField("Время",binder,"min"));
		form.addComponent(ComponentsBuilder.createComboBoxWithDataSource("Тип объекта",objectTypeDataSource,binder,"type","name"));
		form.addComponent(ComponentsBuilder.createComboBoxWithDataSource("Объект", MyUI.getCurrentUI().getObjectContainer(),binder,"object","name"));
		form.addComponent(ComponentsBuilder.createComboBoxWithDataSource("Дополнительные критерии", criteriaDataSource,binder,"criteria","name"));
		form.addComponent(ComponentsBuilder.createDateField("Дата начала действия",binder,"start"));
		form.addComponent(ComponentsBuilder.createDateField("Дата окончания действия",binder,"end"));
		form.addComponent(ComponentsBuilder.createSaveCancelButtons(this));
	}
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}
