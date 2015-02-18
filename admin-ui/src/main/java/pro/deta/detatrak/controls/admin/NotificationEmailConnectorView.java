package pro.deta.detatrak.controls.admin;

import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import ru.yar.vi.rm.data.NotificationEmailConnectorDO;

import com.vaadin.data.fieldgroup.FieldGroup;

public class NotificationEmailConnectorView extends JPAEntityViewBase<NotificationEmailConnectorDO> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7228187847180905895L;

	public static final String NAV_KEY = "notificationEmailConnectorView";
    
    
    public NotificationEmailConnectorView() {
    	super(NotificationEmailConnectorDO.class);
    }

	@Override
	protected void initForm(FieldGroup binder,NotificationEmailConnectorDO user) {
		form.addComponent(ComponentsBuilder.createTextField("Название", binder, "name"));
		
		form.addComponent(ComponentsBuilder.createTextField("SMTP Host", binder, "smtpHost"));
		form.addComponent(ComponentsBuilder.createTextField("Email От", binder, "fromEmail"));
		form.addComponent(ComponentsBuilder.createTextField("Имя От", binder, "fromPersonal"));
		form.addComponent(ComponentsBuilder.createTextField("Кодировка От", binder, "fromEncoding"));
		form.addComponent(ComponentsBuilder.createTextField("Кодировка", binder, "toEncoding"));
		form.addComponent(ComponentsBuilder.createTextField("Заголовок", binder, "subject"));
		form.addComponent(ComponentsBuilder.createTextField("Content-type", binder, "contentType"));
		form.addComponent(ComponentsBuilder.createCheckBox("Debug", binder, "debug"));
        form.addComponent(ComponentsBuilder.createSaveCancelButtons(this));
	}
	
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}
