package pro.deta.detatrak.controls.objects;

import pro.deta.detatrak.presenter.LayoutEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.view.layout.DetaFormLayout;
import pro.deta.detatrak.view.layout.FieldLayout;
import pro.deta.detatrak.view.layout.Layout;
import pro.deta.detatrak.view.layout.SaveCancelLayout;
import pro.deta.detatrak.view.layout.TabSheetLayout;
import pro.deta.detatrak.view.layout.ValuesContainer;
import ru.yar.vi.rm.data.OfficeDO;
import ru.yar.vi.rm.data.RegionDO;

import com.vaadin.addon.jpacontainer.JPAContainer;

public class RegionView extends LayoutEntityViewBase<RegionDO> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5059842746725329939L;
	public static final String NAV_KEY = "regionView";

    public RegionView() {
    	super(RegionDO.class);
    }

    
    public Layout getFormDefinition() {
    	JPAContainer<OfficeDO> officeContainer = JPAUtils.createCachingJPAContainer(OfficeDO.class);

		TabSheetLayout l = new TabSheetLayout();
		l.addTab(new DetaFormLayout("Основные настройки",
				new FieldLayout("Название", "name", FieldLayout.FieldType.TEXTFIELD),
				new FieldLayout("Офис по умолчанию", "defaultOffice", FieldLayout.FieldType.COMBOBOX,new ValuesContainer<>(officeContainer)),
				new SaveCancelLayout(this)
		));

		return l;
	}
    
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}
