package pro.deta.detatrak.controls.extra;

import pro.deta.detatrak.MyUI;
import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import ru.yar.vi.rm.data.AdvertisementDO;
import ru.yar.vi.rm.data.ReportObjectDO;
import ru.yar.vi.rm.data.ReportObjectType;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.fieldgroup.FieldGroup;

public class ReportObjectView extends JPAEntityViewBase<ReportObjectDO> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4320175137995934514L;

	public static final String NAV_KEY = "reportObjectView";
	private JPAContainer<AdvertisementDO> advertContainer = JPAUtils.createCachingJPAContainer(AdvertisementDO.class);
	

    public ReportObjectView() {
    	super(ReportObjectDO.class);
    }

	@Override
	protected void initForm(FieldGroup binder,ReportObjectDO bean) {
		form.addComponent(ComponentsBuilder.createComboBox("Тип", binder, "type",ReportObjectType.class));
		form.addComponent(ComponentsBuilder.createComboBoxWithDataSource("Офис", MyUI.getCurrentUI().getOfficeContainer(), binder, "office","name"));
		form.addComponent(ComponentsBuilder.createComboBoxWithDataSource("Объект", MyUI.getCurrentUI().getObjectContainer(), binder, "object","name"));
        form.addComponent(ComponentsBuilder.createComboBoxWithDataSource("Объявление", advertContainer, binder, "adv","name"));
        form.addComponent(ComponentsBuilder.createTextField("Ширина (в еденицах bootstrap)", binder, "width"));
		form.addComponent(ComponentsBuilder.createSaveCancelButtons(this));
	}

	
	@Override
	public void saveEntity(ReportObjectDO obj) {
		// TODO Добавить валидацию что выбран только один объект - либо офис либо объект либо объявление.
		super.saveEntity(obj);
	}

	@Override
	public String getNavKey() {
		return NAV_KEY;
	}
	
}
