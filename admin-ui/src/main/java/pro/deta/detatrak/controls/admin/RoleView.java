package pro.deta.detatrak.controls.admin;

import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import pro.deta.security.SecurityElement;
import ru.yar.vi.rm.data.RoleDO;

import com.vaadin.data.fieldgroup.FieldGroup;

public class RoleView extends JPAEntityViewBase<RoleDO> {

	public static final String NAV_KEY = "roleView";

	public RoleView() {
		super(RoleDO.class);
	}

	@Override
	protected void initForm(FieldGroup binder, RoleDO bean) {
		form.addComponent(ComponentsBuilder.createTextField("Название", binder, "name"));
		form.addComponent(ComponentsBuilder.createTextField("Описание", binder, "description"));
		form.addComponent(ComponentsBuilder.createOptionGroup("GUI Security", binder, "securityElements",SecurityElement.class));
		form.addComponent(ComponentsBuilder.createSaveCancelButtons(this));
	}
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}
