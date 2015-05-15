package pro.deta.detatrak.view;

import pro.deta.detatrak.common.TableBuilder;
import pro.deta.detatrak.controls.admin.NotificationEmailConnectorView;
import pro.deta.detatrak.controls.admin.NotificationSMSConnectorView;
import pro.deta.detatrak.controls.admin.NotificationView;
import pro.deta.detatrak.controls.admin.RoleView;
import pro.deta.detatrak.controls.admin.UserView;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.util.RightPaneTabsView;
import pro.deta.detatrak.util.TopLevelMenuView;
import pro.deta.security.SecurityElement;
import ru.yar.vi.rm.data.NotificationDO;
import ru.yar.vi.rm.data.NotificationEmailConnectorDO;
import ru.yar.vi.rm.data.NotificationSMSConnectorDO;
import ru.yar.vi.rm.data.RoleDO;
import ru.yar.vi.rm.data.UserDO;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.TabSheet;

@TopLevelMenuView(icon="icon-cog")
public class AdminTabsView extends RightPaneTabsView  implements Captioned,Initializable, Restrictable {

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
	public void initTabs(TabSheet tabs) {
        tabs = new TabSheet();
		rolesDataSource = JPAUtils.createCachingJPAContainer(RoleDO.class);
		usersDataSource = JPAUtils.createCachingJPAContainer(UserDO.class);
		notificationDataSource = JPAUtils.createCachingJPAContainer(NotificationDO.class);
		notificationSMSConnectorDataSource = JPAUtils.createCachingJPAContainer(NotificationSMSConnectorDO.class);
		notificationEmailConnectorDataSource = JPAUtils.createCachingJPAContainer(NotificationEmailConnectorDO.class);

		addForInitialization(this);
        addTab(createUserView());
        addTab(createRoleView());
        addTab(createNotificationView());
        addTab(createNotificationConnectorSMS());
        addTab(createNotificationConnectorEmail());
    }

    private TableBuilder createNotificationConnectorEmail() {
    	NotificationEmailConnectorView view = new NotificationEmailConnectorView();
    	TableBuilder tb = 
    	createTableBuilder(bundle.getString("label.notificationconnector.email"), view.getNavKey(), notificationEmailConnectorDataSource)
        .addColumn("name", bundle.getString("label.name"))
        .addColumn("type", bundle.getString("label.notification.type"));
    	addForInitialization(view,notificationEmailConnectorDataSource);
        return tb;
	}

	private TableBuilder createNotificationConnectorSMS() {
    	NotificationSMSConnectorView view = new NotificationSMSConnectorView();
    	TableBuilder tb = createTableBuilder(bundle.getString("label.notificationconnector.sms"), view.getNavKey(), notificationSMSConnectorDataSource)
        .addColumn("name", bundle.getString("label.name"))
        .addColumn("type", bundle.getString("label.notification.type"));
    	addForInitialization(view,notificationSMSConnectorDataSource);
    	return tb;
    }

	private TableBuilder createNotificationView() {
    	NotificationView view = new NotificationView();
    	TableBuilder tb = 
    	createTableBuilder(bundle.getString("label.notifications"), NotificationView.NAV_KEY, notificationDataSource)
        .addColumn("name", bundle.getString("label.name"))
        .addColumn("event", bundle.getString("label.notification.event"))
        .addColumn("template", bundle.getString("label.notification.template"));
    	addForInitialization(view,notificationDataSource);
        return tb;
    }

	private TableBuilder createRoleView() {
    	RoleView view = new RoleView();
    	TableBuilder tb =
    	createTableBuilder(bundle.getString("label.roles"), view.getNavKey(), rolesDataSource)
        .addColumn("name", bundle.getString("label.name"))
        .addColumn("description", bundle.getString("label.description"));
    	addForInitialization(view,rolesDataSource);
    	return tb;
    }

	private TableBuilder createUserView() {
    	UserView view = new UserView();
    	TableBuilder tb = 
		createTableBuilder(bundle.getString("label.users"), view.getNavKey(), usersDataSource)
        .addColumn("name", bundle.getString("label.name"))
        .addColumn("description", bundle.getString("label.description"));
    	addForInitialization(view,usersDataSource);
        return tb;
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
