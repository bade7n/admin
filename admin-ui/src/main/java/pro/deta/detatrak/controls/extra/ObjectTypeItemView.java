package pro.deta.detatrak.controls.extra;

import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.view.ExtraTabsView;
import ru.yar.vi.rm.data.ObjectTypeDO;
import ru.yar.vi.rm.data.ObjectTypeItemDO;

import com.vaadin.addon.jpacontainer.EntityContainer;
import com.vaadin.data.fieldgroup.FieldGroup;

public class ObjectTypeItemView extends JPAEntityViewBase<ObjectTypeItemDO> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4320175137995934514L;

	public static final String NAV_KEY = "objectTypeItemView";

    private EntityContainer<ObjectTypeDO> objectItemContainer = JPAUtils.createCachingJPAContainer(ObjectTypeDO.class);

    public ObjectTypeItemView(ExtraTabsView extraTabsView) {
    	super(ObjectTypeItemDO.class,extraTabsView.getObjectTypeItemContainer());
    }

	@Override
	protected void initForm(FieldGroup binder,ObjectTypeItemDO bean) {
        form.addComponent(ComponentsBuilder.createComboBoxWithDataSource("Тип", objectItemContainer, binder, "type","name"));
        form.addComponent(ComponentsBuilder.createCheckBox("Обязательный", binder, "required"));
		form.addComponent(ComponentsBuilder.createSaveCancelButtons(this));
	}

	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}
