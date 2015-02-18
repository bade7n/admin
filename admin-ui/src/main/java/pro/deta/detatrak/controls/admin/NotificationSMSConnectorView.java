package pro.deta.detatrak.controls.admin;

import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import ru.yar.vi.rm.data.NotificationSMSConnectorDO;
import ru.yar.vi.rm.data.SMSEncoding;
import ru.yar.vi.rm.data.SMSType;

import com.vaadin.data.fieldgroup.FieldGroup;

public class NotificationSMSConnectorView extends JPAEntityViewBase<NotificationSMSConnectorDO> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7228187847180905895L;

	public static final String NAV_KEY = "notificationSMSConnectorView";
    
    
    public NotificationSMSConnectorView() {
    	super(NotificationSMSConnectorDO.class);
    }

	@Override
	protected void initForm(FieldGroup binder,NotificationSMSConnectorDO user) {
		form.addComponent(ComponentsBuilder.createTextField("Название", binder, "name"));
		form.addComponent(ComponentsBuilder.createTextField("SMS C ID", binder, "smscId"));
		form.addComponent(ComponentsBuilder.createComboBox("SMS Type", binder, "smsType", SMSType.class));
		form.addComponent(ComponentsBuilder.createComboBox("SMS Encoding", binder, "smsEncoding", SMSEncoding.class));
		form.addComponent(ComponentsBuilder.createTextField("Charset", binder, "smsCharset"));
		form.addComponent(ComponentsBuilder.createTextField("MOMT", binder, "momt"));
		form.addComponent(ComponentsBuilder.createTextField("Sender", binder, "sender"));
		
        form.addComponent(ComponentsBuilder.createSaveCancelButtons(this));
	}
	
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}
