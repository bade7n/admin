package pro.deta.detatrak.controls.extra;

import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import ru.yar.vi.rm.data.ObjectTypeDO;

import com.vaadin.data.fieldgroup.FieldGroup;

public class ObjectTypeView extends JPAEntityViewBase<ObjectTypeDO> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2909980719200560830L;
	public static final String NAV_KEY = "objectTypeView";

    public ObjectTypeView() {
    	super(ObjectTypeDO.class);
    }

    @Override
	protected void initForm(FieldGroup binder,ObjectTypeDO type) {
    	form.addComponent(ComponentsBuilder.createTextField("Наименование типа",binder, "name"));
        form.addComponent(ComponentsBuilder.createTextField("Тип",binder, "type"));
        form.addComponent(ComponentsBuilder.createTextField("Описание",binder, "description"));
        form.addComponent(ComponentsBuilder.createSaveCancelButtons(this));
	}
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}
