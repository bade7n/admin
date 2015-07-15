package pro.deta.detatrak.controls.objects;

import pro.deta.detatrak.MyUI;
import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.presenter.LayoutEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.view.layout.DetaFormLayout;
import pro.deta.detatrak.view.layout.FieldLayout;
import pro.deta.detatrak.view.layout.Layout;
import pro.deta.detatrak.view.layout.SaveCancelLayout;
import pro.deta.detatrak.view.layout.TabSheetLayout;
import pro.deta.detatrak.view.layout.ValuesContainer;
import ru.yar.vi.rm.data.ObjectDO;
import ru.yar.vi.rm.data.ObjectTypeDO;
import ru.yar.vi.rm.data.OfficeDO;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.fieldgroup.FieldGroup;

public class ObjectView extends LayoutEntityViewBase<ObjectDO> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8771872994858121206L;
	public static final String NAV_KEY = "objectView";
    
    public ObjectView() {
    	super(ObjectDO.class);
    }

    public Layout getFormDefinition() {
    	JPAContainer<ObjectTypeDO> objectTypeContainer = JPAUtils.createCachingJPAContainer(ObjectTypeDO.class);
    	JPAContainer<OfficeDO> officeContainer = JPAUtils.createCachingJPAContainer(OfficeDO.class);

		TabSheetLayout l = new TabSheetLayout();
		l.addTab(new DetaFormLayout("Основные настройки",
				new FieldLayout("Название", "name", FieldLayout.FieldType.TEXTFIELD),
				new FieldLayout("Тип", "type", FieldLayout.FieldType.COMBOBOX,new ValuesContainer<>(objectTypeContainer)),
				new FieldLayout("Офис", "office", FieldLayout.FieldType.COMBOBOX,new ValuesContainer<>(officeContainer)),
				new SaveCancelLayout(this)
		));

		l.addTab(new DetaFormLayout("Дополнительно",
				new FieldLayout("Дочерние объекты", "childs", FieldLayout.FieldType.TWINCOLSELECT,new ValuesContainer<>(MyUI.getCurrentUI().getObjectContainer())),
				new SaveCancelLayout(this)
				));

		return l;
	}

    @Override
    public void saveEntity(ObjectDO obj) {
    	obj.setOffice(MyUI.getCurrentUI().getOffice());
    }

	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}
