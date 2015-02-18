package pro.deta.detatrak.controls.service;

import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import ru.yar.vi.rm.data.CriteriaDO;

import com.vaadin.data.fieldgroup.FieldGroup;

public class CriteriaView extends JPAEntityViewBase<CriteriaDO> {

    public static final String NAV_KEY = "criteriaView";

    public CriteriaView() {
    	super(CriteriaDO.class);
    }

	@Override
	protected void initForm(FieldGroup binder,CriteriaDO crit) {
        form.addComponent(ComponentsBuilder.createTextField("Опция", binder, "name"));
        form.addComponent(ComponentsBuilder.createSaveCancelButtons(this));
	}
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}
