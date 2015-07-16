package pro.deta.detatrak.controls.extra;

import pro.deta.detatrak.presenter.LayoutEntityViewBase;
import pro.deta.detatrak.view.layout.DetaFormLayout;
import pro.deta.detatrak.view.layout.FieldLayout;
import pro.deta.detatrak.view.layout.FieldLayout.FieldType;
import pro.deta.detatrak.view.layout.Layout;
import pro.deta.detatrak.view.layout.SaveCancelLayout;
import pro.deta.detatrak.view.layout.TabSheetLayout;
import ru.yar.vi.rm.data.AdvertisementDO;

public class AdvertView extends LayoutEntityViewBase<AdvertisementDO> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8149684523796848250L;
	public static final String NAV_KEY = "advertView";

    public AdvertView() {
    	super(AdvertisementDO.class);
    }

	@Override
	public String getNavKey() {
		return NAV_KEY;
	}
	
	public Layout getFormDefinition() {
		TabSheetLayout l = new TabSheetLayout();
		l.addTab(new DetaFormLayout("Основные настройки",
				new FieldLayout("Название", "name", FieldLayout.FieldType.TEXTFIELD),
				new FieldLayout("Описание", "description", FieldLayout.FieldType.TEXTFIELD),
				new FieldLayout("HTML", "html", FieldType.CKEDITOR),
				new SaveCancelLayout(this)
		));
		return l;
	}


}
