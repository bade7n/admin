package pro.deta.detatrak.controls.extra;

import pro.deta.detatrak.presenter.LayoutEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.view.layout.DetaFormLayout;
import pro.deta.detatrak.view.layout.FieldLayout;
import pro.deta.detatrak.view.layout.Layout;
import pro.deta.detatrak.view.layout.SaveCancelLayout;
import pro.deta.detatrak.view.layout.TabSheetLayout;
import ru.yar.vi.rm.data.ObjectTypeDO;

import com.vaadin.addon.jpacontainer.EntityContainer;

public class ObjectTypeView extends LayoutEntityViewBase<ObjectTypeDO> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2909980719200560830L;
	public static final String NAV_KEY = "objectTypeView";

    public ObjectTypeView() {
    	super(ObjectTypeDO.class);
    }

	@Override
	public String getNavKey() {
		return NAV_KEY;
	}
	public Layout getFormDefinition() {
		TabSheetLayout l = new TabSheetLayout();
		l.addTab(new DetaFormLayout("Основные настройки",
				new FieldLayout("Наименование типа", "name", FieldLayout.FieldType.TEXTFIELD),
				new FieldLayout("Тип", "type", FieldLayout.FieldType.TEXTFIELD),
				new FieldLayout("Описание", "description", FieldLayout.FieldType.TEXTFIELD),
				new SaveCancelLayout(this)
		));
		return l;
	}
}
