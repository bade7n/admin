package pro.deta.detatrak.view;

import pro.deta.detatrak.controls.admin.NotificationEmailConnectorView;
import pro.deta.detatrak.controls.admin.NotificationSMSConnectorView;
import pro.deta.detatrak.controls.admin.NotificationView;
import pro.deta.detatrak.controls.admin.RoleView;
import pro.deta.detatrak.controls.admin.UserView;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.util.NewRightPaneTabsView;
import pro.deta.detatrak.util.TopLevelMenuView;
import pro.deta.detatrak.view.layout.Layout;
import pro.deta.detatrak.view.layout.TabSheetLayout;
import pro.deta.detatrak.view.layout.TableColumnLayout;
import pro.deta.detatrak.view.layout.TableLayout;
import pro.deta.security.SecurityElement;
import ru.yar.vi.rm.data.NotificationDO;
import ru.yar.vi.rm.data.NotificationEmailConnectorDO;
import ru.yar.vi.rm.data.NotificationSMSConnectorDO;
import ru.yar.vi.rm.data.RoleDO;
import ru.yar.vi.rm.data.UserDO;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.navigator.ViewChangeListener;

@TopLevelMenuView(icon="icon-cog")
public class AdminTabsView extends NewRightPaneTabsView  implements Captioned,Initializable, Restrictable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8104402120172078527L;
	public static final String NAV_KEY = "/admin";
    private JPAContainer<RoleDO> rolesDataSource = null;
    private JPAContainer<UserDO> usersDataSource = null;
    private JPAContainer<NotificationDO> notificationDataSource = null;
    private JPAContainer<NotificationSMSConnectorDO> notificationSMSConnectorDataSource = null;
    private JPAContainer<NotificationEmailConnectorDO> notificationEmailConnectorDataSource = null;

    @Override
	public Layout getLayoutDefinition() {
    	rolesDataSource = JPAUtils.createCachingJPAContainer(RoleDO.class);
		usersDataSource = JPAUtils.createCachingJPAContainer(UserDO.class);
		notificationDataSource = JPAUtils.createCachingJPAContainer(NotificationDO.class);
		notificationSMSConnectorDataSource = JPAUtils.createCachingJPAContainer(NotificationSMSConnectorDO.class);
		notificationEmailConnectorDataSource = JPAUtils.createCachingJPAContainer(NotificationEmailConnectorDO.class);

		
    	TabSheetLayout tsl = new TabSheetLayout();
    	UserView user = new UserView();
    	tsl.addTab(new TableLayout(usersDataSource,bundle.getString("label.users"), user.getNavKey(),
    			new TableColumnLayout("name",bundle.getString("label.name")),
    			new TableColumnLayout("description",bundle.getString("label.description"))
    	));
    	
    	RoleView role = new RoleView();
    	tsl.addTab(new TableLayout(rolesDataSource, bundle.getString("label.roles"),role.getNavKey(), 
    			new TableColumnLayout("name",bundle.getString("label.name")),
    			new TableColumnLayout("description",bundle.getString("label.description"))
    	));

        
        NotificationView notification = new NotificationView();
    	tsl.addTab(new TableLayout(notificationDataSource, bundle.getString("label.notifications"),notification.getNavKey(), 
    			new TableColumnLayout("name",bundle.getString("label.name")),
    			new TableColumnLayout("event",bundle.getString("label.notification.event")),
    			new TableColumnLayout("template",bundle.getString("label.notification.template"))
    	));
    	NotificationSMSConnectorView notificationSms = new NotificationSMSConnectorView();
    	tsl.addTab(new TableLayout(notificationSMSConnectorDataSource, bundle.getString("label.notificationconnector.sms"),notificationSms.getNavKey(), 
    			new TableColumnLayout("name",bundle.getString("label.name")),
    			new TableColumnLayout("event",bundle.getString("label.notification.type"))
    	));
    	NotificationEmailConnectorView notificationEmail = new NotificationEmailConnectorView();
    	tsl.addTab(new TableLayout(notificationEmailConnectorDataSource, bundle.getString("label.notificationconnector.email"),notificationEmail.getNavKey(), 
    			new TableColumnLayout("name",bundle.getString("label.name")),
    			new TableColumnLayout("event",bundle.getString("label.notification.type"))
    	));
    	
        addForInitialization(this);
        addForInitialization(user,usersDataSource);
        addForInitialization(role,rolesDataSource);
        addForInitialization(notification,notificationDataSource);
        addForInitialization(notificationSms,notificationSMSConnectorDataSource);
        addForInitialization(notificationEmail,notificationEmailConnectorDataSource);
        return tsl;
    }

	@Override
    public void changeView(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }

	@Override
	public String getCaption() {
		return bundle.getString("label.admin");
	}

	@Override
	public SecurityElement getRestriction() {
		return SecurityElement.ADMINISTRATION_TAB;
	}
	
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}
