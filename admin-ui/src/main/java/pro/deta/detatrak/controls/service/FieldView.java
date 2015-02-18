package pro.deta.detatrak.controls.service;

import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import ru.yar.vi.rm.data.CriteriaDO;
import ru.yar.vi.rm.data.CustomFieldDO;
import ru.yar.vi.rm.data.CustomerDO;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.fieldgroup.FieldGroup;

public class FieldView extends JPAEntityViewBase<CustomFieldDO> {

    public static final String NAV_KEY = "fieldView";

	private JPAContainer<CustomerDO> customerDataSource = JPAUtils.createCachingJPAContainer(CustomerDO.class);
	private JPAContainer<CriteriaDO> criteriaDataSource = JPAUtils.createCachingJPAContainer(CriteriaDO.class);

    public FieldView() {
    	super(CustomFieldDO.class);
    	
    }


	@Override
	protected void initForm(FieldGroup binder,CustomFieldDO custom) {
        form.addComponent(ComponentsBuilder.createTextField("Название параметра", binder, "name"));
        form.addComponent(ComponentsBuilder.createComboBoxWithDataSource("Тип клиента", customerDataSource, binder, "customerId","name"));

        form.addComponent(ComponentsBuilder.createCheckBox("Обязательный",binder,"required"));
        form.addComponent(ComponentsBuilder.createTextField("Сообщение при ошибке заполнения",binder,"requiredMsg"));
        form.addComponent(ComponentsBuilder.createTextField("Ограничение",binder,"mask"));
        form.addComponent(ComponentsBuilder.createTextField("Сообщение при несовпадении",binder,"maskMsg"));
        form.addComponent(ComponentsBuilder.createTextField("Пример",binder,"example"));
        form.addComponent(ComponentsBuilder.createTextField("Поле",binder,"field"));

        form.addComponent(ComponentsBuilder.createCheckBox("Показывать в отчётах",binder,"reporting"));
        form.addComponent(ComponentsBuilder.createCheckBox("Показывать на терминале",binder,"onTerminal"));

        form.addComponent(ComponentsBuilder.createTwinColSelect("Варианты",criteriaDataSource,binder,"criteria"));

        addComponent(form);
        addComponent(ComponentsBuilder.createSaveCancelButtons(this));

	}
	
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}
