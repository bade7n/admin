package pro.deta.detatrak.controls.schedule;

import pro.deta.detatrak.MyUI;
import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import ru.yar.vi.rm.data.WeekendDO;

import com.vaadin.data.fieldgroup.FieldGroup;


public class WeekendView extends JPAEntityViewBase<WeekendDO> {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3594074436756159006L;
	public static final String NAV_KEY = "weekendView";

    public WeekendView() {
    	super(WeekendDO.class);
    }


	@Override
	protected void initForm(FieldGroup binder,WeekendDO weekend) {
        form.addComponent(ComponentsBuilder.createTextField("График", binder, "schedule"));

        form.addComponent(ComponentsBuilder.createTwinColSelect("Объекты", MyUI.getCurrentUI().getObjectContainer(),binder,"objects"));
        form.addComponent(ComponentsBuilder.createDateField("Дата начала действия", binder, "start"));
        form.addComponent(ComponentsBuilder.createDateField("Дата окончания действия", binder, "end"));
        form.addComponent(ComponentsBuilder.createSaveCancelButtons(this));        
	}

    
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}
