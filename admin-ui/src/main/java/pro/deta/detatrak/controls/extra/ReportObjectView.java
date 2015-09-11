package pro.deta.detatrak.controls.extra;

import pro.deta.detatrak.MyUI;
import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.presenter.LayoutEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.view.layout.DetaFormLayout;
import pro.deta.detatrak.view.layout.FieldLayout;
import pro.deta.detatrak.view.layout.FieldLayout.FieldType;
import pro.deta.detatrak.view.layout.Layout;
import pro.deta.detatrak.view.layout.SaveCancelLayout;
import pro.deta.detatrak.view.layout.TabSheetLayout;
import pro.deta.detatrak.view.layout.ValuesContainer;
import ru.yar.vi.rm.data.AdvertisementDO;
import ru.yar.vi.rm.data.ReportObjectDO;
import ru.yar.vi.rm.data.ReportObjectType;

import com.vaadin.addon.jpacontainer.JPAContainer;

public class ReportObjectView extends LayoutEntityViewBase<ReportObjectDO> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4320175137995934514L;

	public static final String NAV_KEY = "reportObjectView";


    public ReportObjectView() {
    	super(ReportObjectDO.class);
    }

	public Layout getFormDefinition() {
		JPAContainer<AdvertisementDO> advertContainer = JPAUtils.createCachingJPAContainer(AdvertisementDO.class);
		TabSheetLayout l = new TabSheetLayout();
		l.addTab(new DetaFormLayout("Основные настройки",
				new FieldLayout("Тип", "type", FieldLayout.FieldType.COMBOBOX, new ValuesContainer<>(ComponentsBuilder.createContainerFromEnumClass(ReportObjectType.class)) ),
				new FieldLayout("Офис", "office", FieldType.COMBOBOX,new ValuesContainer<>(MyUI.getCurrentUI().getOfficeContainer())),
				new FieldLayout("Объект", "object", FieldType.COMBOBOX,new ValuesContainer<>(MyUI.getCurrentUI().getObjectContainer())),
				new FieldLayout("Объявление", "adv", FieldType.COMBOBOX,new ValuesContainer<>(advertContainer)),
				new FieldLayout("Ширина (в еденицах bootstrap)", "width", FieldLayout.FieldType.TEXTFIELD),
				new SaveCancelLayout(this)
		));
		return l;
	}
	@Override
	public void preSaveEntity(ReportObjectDO obj) {
		// TODO Добавить валидацию что выбран только один объект - либо офис либо объект либо объявление.
		super.preSaveEntity(obj);
	}

	@Override
	public String getNavKey() {
		return NAV_KEY;
	}
	
}
