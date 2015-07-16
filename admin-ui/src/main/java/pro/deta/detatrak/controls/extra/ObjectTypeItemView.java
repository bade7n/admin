package pro.deta.detatrak.controls.extra;

import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.presenter.LayoutEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.view.layout.DetaFormLayout;
import pro.deta.detatrak.view.layout.FieldLayout;
import pro.deta.detatrak.view.layout.LabelResourceLayout;
import pro.deta.detatrak.view.layout.Layout;
import pro.deta.detatrak.view.layout.SaveCancelLayout;
import pro.deta.detatrak.view.layout.TabSheetLayout;
import pro.deta.detatrak.view.layout.ValuesContainer;
import ru.yar.vi.rm.data.ObjectTypeDO;
import ru.yar.vi.rm.data.ObjectTypeItemDO;

import com.vaadin.addon.jpacontainer.EntityContainer;
import com.vaadin.data.fieldgroup.FieldGroup;

public class ObjectTypeItemView extends LayoutEntityViewBase<ObjectTypeItemDO> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4320175137995934514L;

	public static final String NAV_KEY = "objectTypeItemView";


    public ObjectTypeItemView() {
    	super(ObjectTypeItemDO.class);
    }

	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

	public Layout getFormDefinition() {
		EntityContainer<ObjectTypeDO> objectItemContainer = JPAUtils.createCachingJPAContainer(ObjectTypeDO.class);
		TabSheetLayout l = new TabSheetLayout();
		l.addTab(new DetaFormLayout("Основные настройки",
				new FieldLayout("Тип", "type", FieldLayout.FieldType.COMBOBOX, new ValuesContainer<>(objectItemContainer)),
				new FieldLayout("Обязательный", "required", FieldLayout.FieldType.CHECKBOX),
				new SaveCancelLayout(this)
		));
		return l;
	}
}
