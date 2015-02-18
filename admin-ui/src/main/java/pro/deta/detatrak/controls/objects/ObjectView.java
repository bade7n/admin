package pro.deta.detatrak.controls.objects;

import pro.deta.detatrak.MyUI;
import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import ru.yar.vi.rm.data.ObjectDO;
import ru.yar.vi.rm.data.ObjectTypeDO;
import ru.yar.vi.rm.data.OfficeDO;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.fieldgroup.FieldGroup;

public class ObjectView extends JPAEntityViewBase<ObjectDO> {

    public static final String NAV_KEY = "objectView";
    
	private JPAContainer<ObjectTypeDO> objectTypeContainer = JPAUtils.createCachingJPAContainer(ObjectTypeDO.class);
	private JPAContainer<OfficeDO> officeContainer = JPAUtils.createCachingJPAContainer(OfficeDO.class);


    public ObjectView() {
    	super(ObjectDO.class);
    }


    @Override
    public void saveEntity(ObjectDO obj) {
    	obj.setOffice(MyUI.getCurrentUI().getOffice());
    }

	@Override
	protected void initForm(FieldGroup binder,ObjectDO object) {
		form.addComponent(ComponentsBuilder.createTextField("Название объекта", binder, "name"));
        form.addComponent(ComponentsBuilder.createComboBoxWithDataSource("Тип",objectTypeContainer,binder,"type","name"));
        form.addComponent(ComponentsBuilder.createComboBoxWithDataSource("Офис",officeContainer,binder,"office","name"));
        form.addComponent(ComponentsBuilder.createTwinColSelect("Дочерние объекты", MyUI.getCurrentUI().getObjectContainer(), binder, "childs"));
        addComponent(ComponentsBuilder.createSaveCancelButtons(this));
	}
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}
