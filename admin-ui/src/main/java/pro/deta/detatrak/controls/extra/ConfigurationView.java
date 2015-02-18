package pro.deta.detatrak.controls.extra;

import java.util.MissingResourceException;

import pro.deta.detatrak.MyUI;
import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import ru.yar.vi.rm.data.ConfigDO;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Label;

public class ConfigurationView extends JPAEntityViewBase<ConfigDO> {
    public static final String NAV_KEY = "configurationView";
    
    public ConfigurationView() {
    	super(ConfigDO.class);
    }

	@Override
	protected void initForm(FieldGroup binder,ConfigDO config) {
        form.addComponent(ComponentsBuilder.createTextField("Название", binder, "name"));
        String description = "";
        try {
            description = config.getName() == null ? "" : MyUI.getCurrentUI().getBundle().getString(config.getName());
        } catch (MissingResourceException e) {
        }
        form.addComponent(new Label(description));
        form.addComponent(ComponentsBuilder.createTextField("Значение", binder, "value"));
        form.addComponent(ComponentsBuilder.createSaveCancelButtons(this));
	}
	
	public String getNavKey() {
		return NAV_KEY;
	}
}
