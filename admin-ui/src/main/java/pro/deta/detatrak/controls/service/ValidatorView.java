package pro.deta.detatrak.controls.service;

import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import ru.yar.vi.rm.data.ValidatorDO;

import com.vaadin.data.fieldgroup.FieldGroup;

public class ValidatorView extends JPAEntityViewBase<ValidatorDO> {

    public static final String NAV_KEY = "validatorView";

    public ValidatorView() {
    	super(ValidatorDO.class);
    }

	@Override
	protected void initForm(FieldGroup binder,ValidatorDO valid) {
		
        form.addComponent(ComponentsBuilder.createTextField("Название",binder, "name"));
        form.addComponent(ComponentsBuilder.createTextField("Класс", binder,"clazz"));
        form.addComponent(ComponentsBuilder.createTextArea("Параметры", binder,"parameter"));
        form.addComponent(ComponentsBuilder.createTextArea("Ошибка", binder, "error"));
        form.addComponent(ComponentsBuilder.createSaveCancelButtons(this));
	}
	
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}
