package pro.deta.detatrak.controls.admin;

import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import ru.yar.vi.rm.data.NotificationConnectorDO;
import ru.yar.vi.rm.data.NotificationDO;
import ru.yar.vi.rm.data.NotificationEvent;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.fieldgroup.FieldGroup;

public class NotificationView extends JPAEntityViewBase<NotificationDO> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7228187847180905895L;

	public static final String NAV_KEY = "notificationView";

    private JPAContainer<NotificationConnectorDO> notificationConnectorContainer = JPAUtils.createCachingJPAContainer(NotificationConnectorDO.class);

    
    public NotificationView() {
    	super(NotificationDO.class);
    }

	@Override
	protected void initForm(FieldGroup binder,NotificationDO user) {
		form.addComponent(ComponentsBuilder.createTextField("Название", binder, "name"));
		form.addComponent(ComponentsBuilder.createComboBox("Тип оповещения", notificationConnectorContainer,binder, "connector"));
		form.addComponent(ComponentsBuilder.createComboBox("Событие оповещения", binder, "event", NotificationEvent.class));
		form.addComponent(ComponentsBuilder.createTextArea("Шаблон оповещения", binder, "template"));
        form.addComponent(ComponentsBuilder.createSaveCancelButtons(this));
	}
	
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}
