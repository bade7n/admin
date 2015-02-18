package pro.deta.detatrak.controls.objects;

import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import ru.yar.vi.rm.data.RegionDO;

import com.vaadin.data.fieldgroup.FieldGroup;

public class RegionView extends JPAEntityViewBase<RegionDO> {
    public static final String NAV_KEY = "regionView";

    public RegionView() {
    	super(RegionDO.class);
    }

	@Override
	protected void initForm(FieldGroup binder,RegionDO region) {
        form.addComponent(ComponentsBuilder.createTextField("Название", binder, "name"));
        form.addComponent(ComponentsBuilder.createSaveCancelButtons(this));
	}
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}
