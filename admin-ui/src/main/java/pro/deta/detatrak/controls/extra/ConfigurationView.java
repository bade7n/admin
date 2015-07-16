package pro.deta.detatrak.controls.extra;

import pro.deta.detatrak.presenter.LayoutEntityViewBase;
import pro.deta.detatrak.view.layout.DetaFormLayout;
import pro.deta.detatrak.view.layout.FieldLayout;
import pro.deta.detatrak.view.layout.LabelResourceLayout;
import pro.deta.detatrak.view.layout.Layout;
import pro.deta.detatrak.view.layout.SaveCancelLayout;
import pro.deta.detatrak.view.layout.TabSheetLayout;
import ru.yar.vi.rm.data.ConfigDO;

public class ConfigurationView extends LayoutEntityViewBase<ConfigDO> {
    public static final String NAV_KEY = "configurationView";
    
    public ConfigurationView() {
    	super(ConfigDO.class);
    }

	public Layout getFormDefinition() {
		TabSheetLayout l = new TabSheetLayout();
		l.addTab(new DetaFormLayout("Основные настройки",
				new FieldLayout("Название", "name", FieldLayout.FieldType.TEXTFIELD),
				new LabelResourceLayout("name"),
				new FieldLayout("Значение", "value", FieldLayout.FieldType.TEXTFIELD),
				new SaveCancelLayout(this)
		));
		return l;
	}
	
	public String getNavKey() {
		return NAV_KEY;
	}
}
