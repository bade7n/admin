package pro.deta.detatrak.controls.schedule;

import com.vaadin.addon.jpacontainer.JPAContainer;

import pro.deta.detatrak.MyUI;
import pro.deta.detatrak.presenter.LayoutEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.view.layout.DetaFormLayout;
import pro.deta.detatrak.view.layout.FieldLayout;
import pro.deta.detatrak.view.layout.Layout;
import pro.deta.detatrak.view.layout.SaveCancelLayout;
import pro.deta.detatrak.view.layout.TabSheetLayout;
import pro.deta.detatrak.view.layout.ValuesContainer;
import ru.yar.vi.rm.data.ActionDO;
import ru.yar.vi.rm.data.CriteriaDO;
import ru.yar.vi.rm.data.CustomerDO;
import ru.yar.vi.rm.data.DurationDO;
import ru.yar.vi.rm.data.ObjectTypeDO;

public class DurationView extends LayoutEntityViewBase<DurationDO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1355273678063569861L;
	public static final String NAV_KEY = "durationView";

	public DurationView() {
		super(DurationDO.class);
	}

		
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}


	@Override
	public Layout getFormDefinition() {
		JPAContainer<CustomerDO> customerDataSource = JPAUtils.createCachingJPAContainer(CustomerDO.class);
		JPAContainer<CriteriaDO> criteriaDataSource = JPAUtils.createCachingJPAContainer(CriteriaDO.class);
		JPAContainer<ActionDO> actionDataSource = JPAUtils.createCachingJPAContainer(ActionDO.class);
		JPAContainer<ObjectTypeDO> objectTypeDataSource = JPAUtils.createCachingJPAContainer(ObjectTypeDO.class);

		TabSheetLayout l = new TabSheetLayout();
		l.addTab(new DetaFormLayout("Основные настройки",
				new FieldLayout("Тип объекта", "type", FieldLayout.FieldType.COMBOBOX,new ValuesContainer<>(objectTypeDataSource)),
				new FieldLayout("Время", "min", FieldLayout.FieldType.TEXTFIELD),
				new FieldLayout("Услуги", "action", FieldLayout.FieldType.COMBOBOX,new ValuesContainer<>(actionDataSource)),
				new SaveCancelLayout(this)
		));

		l.addTab(new DetaFormLayout("Дополнительно",
				new FieldLayout("Объект", "object", FieldLayout.FieldType.COMBOBOX,new ValuesContainer<>(MyUI.getCurrentUI().getObjectContainer())),
				new FieldLayout("Тип клиента", "customer", FieldLayout.FieldType.COMBOBOX,new ValuesContainer<>(customerDataSource)),
				new FieldLayout("Дополнительные критерии", "criteria", FieldLayout.FieldType.COMBOBOX,new ValuesContainer<>(criteriaDataSource)),
				new FieldLayout("Дата начала действия", "start", FieldLayout.FieldType.DATEFIELD),
				new FieldLayout("Дата окончания действия", "end", FieldLayout.FieldType.DATEFIELD),
				new SaveCancelLayout(this)
				));

		return l;
	}

}
