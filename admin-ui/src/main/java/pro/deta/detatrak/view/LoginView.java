package pro.deta.detatrak.view;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.addon.jpacontainer.EntityContainer;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Or;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import pro.deta.detatrak.MyUI;
import pro.deta.detatrak.util.DataUtil;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.security.SecurityElement;
import ru.yar.vi.rm.data.OfficeDO;
import ru.yar.vi.rm.data.RoleDO;
import ru.yar.vi.rm.data.SiteDO;
import ru.yar.vi.rm.data.UserDO;

public class LoginView extends VerticalLayout {
	/**
	 * 
	 */
	private static final long serialVersionUID = -327615421348942529L;

	private TextField username = new TextField();
	private PasswordField password = new PasswordField();
	private PasswordField passwordConfirm = new PasswordField();
	private CssLayout loginPanel = new CssLayout();

	public LoginView() {
		setSizeFull();
		addStyleName("login-bg");

		loginPanel.setSizeUndefined();
		loginPanel.addStyleName("login-panel");
		addComponent(loginPanel);
		setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
		setExpandRatio(loginPanel, 1);


		Label welcome = new Label(MyUI.getCurrentUI().getBundle().getString("label.welcome"));
		welcome.addStyleName("welcome");
		welcome.setSizeUndefined();
		loginPanel.addComponent(welcome);

		HorizontalLayout fields = new HorizontalLayout();
		loginPanel.addComponent(fields);

		username.setInputPrompt(MyUI.getCurrentUI().getBundle().getString("label.user"));
		username.addStyleName("login");
		username.focus();
		fields.addComponent(username);

		password.addStyleName("login");
		password.setInputPrompt(MyUI.getCurrentUI().getBundle().getString("label.password"));
		fields.addComponent(password);
		
		EntityContainer<UserDO> userContainer = MyUI.getCurrentUI().getUserContainer();
		if(userContainer.size() == 0) {
			passwordConfirm.addStyleName("login");
			password.setInputPrompt(MyUI.getCurrentUI().getBundle().getString("label.passwordConfirm"));
			fields.addComponent(passwordConfirm);
			addError("error.administrationModeEnabled");
		}

		final Button signin = new Button(MyUI.getCurrentUI().getBundle().getString("label.enter"));
		fields.addComponent(signin);

		final ShortcutListener enter = new ShortcutListener(MyUI.getCurrentUI().getBundle().getString("label.enter"),
				ShortcutAction.KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				signin.click();
			}
		};

		signin.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				if(StringUtils.isEmpty(username.getValue()) ||
						StringUtils.isEmpty(password.getValue())
						) {
					addError("error.emptyUsernameOrPassword");
					return;
				}

				try {
					JPAUtils.getEntityManager().getTransaction().begin();
					JPAUtils.getEntityManager().clear();
					JPAUtils.getEntityManager().getTransaction().commit();
				} catch (Exception e) {
					addError("error.dbSyncFailed");
				}
				
				EntityContainer<UserDO> userContainer = MyUI.getCurrentUI().getUserContainer();
				userContainer.removeAllContainerFilters();
				if(userContainer.size() == 0) {
					// В режиме инсталляции добавляем пользователя
					if(!password.getValue().equalsIgnoreCase(passwordConfirm.getValue())) {
						addError("error.passwordsMismatched");
						return;
					}
					
					UserDO user = new UserDO();
					user.setName(username.getValue());
					user.setName(DataUtil.md5(password.getValue()));
					EntityManager em = JPAUtils.getEntityManager();
					RoleDO role = em.find(RoleDO.class, "rm-admin");
					if(role == null) {
						role = new RoleDO();
						role.setName("rm-admin");
						role.setSecurityElements(new ArrayList<SecurityElement>() {{add(SecurityElement.ADMIN_ACCESS);add(SecurityElement.ADMINISTRATION_TAB);}});
					}
					user.setRole(new ArrayList<RoleDO>());
					user.getRole().add(role);
					try {
						em.getTransaction().begin();
						em.persist(role);
						em.persist(user);
						RoleDO operRole = em.find(RoleDO.class, "rm-oper");
						if(operRole == null) {
							operRole = new RoleDO();
							operRole.setName("rm-oper");
							operRole.setSecurityElements(new ArrayList<SecurityElement>() {{add(SecurityElement.OPER_ACCESS);}});
							em.persist(operRole);
						}
						em.getTransaction().commit();
					} catch(Exception e) {
						addError("error.administrationModeCommit",e.getMessage());
					}
				}
				userContainer.addContainerFilter(new Compare.Equal("name", username.getValue()));
				userContainer.addContainerFilter(new Compare.Equal("pass", DataUtil.md5(password.getValue())));
				if(userContainer.size() == 0) {
					addError("error.wrongPasswordOrUserNotFound");
					return;
				} else if(userContainer.size() > 1) {
					addError("error.manyUsersFound");
					return;
				}
				Object userId = userContainer.firstItemId();
				EntityItem<UserDO> userEntity = userContainer.getItem(userId);
//				JPAUtils.getEntityManager().refresh(userEntity.getEntity());
//				for (RoleDO r : userEntity.getEntity().getRole()) {
//					if(r!=null)
//						JPAUtils.getEntityManager().refresh(r);	
//				}
				List<SecurityElement> restr = DataUtil.getRestrictions(userEntity.getEntity());
				if(!DataUtil.matchElement(restr, SecurityElement.ADMIN_ACCESS)) {
					addError("error.userAccessDenied");
					return;
				} else {
					signin.removeShortcutListener(enter);
					MyUI.getCurrentUI().setUser(userEntity.getEntity());
					
					JPAContainer<SiteDO> siteContainer = MyUI.getCurrentUI().getSiteContainer();
					Filter[] filterSite = new Filter[userEntity.getEntity().getSite().size()];
					if(filterSite.length == 0 ) {
						addError("error.noSitePermissionFound");
						return;
					}
					int j =0;
					for (SiteDO o : userEntity.getEntity().getSite()) {
						filterSite[j++] = new Compare.Equal("id", o.getId());
					}
					siteContainer.addContainerFilter(new Or(filterSite));
					SiteDO currentSite = siteContainer.getItem(siteContainer.firstItemId()).getEntity();
					MyUI.getCurrentUI().setSite(currentSite );
					BeanContainer<Integer,OfficeDO> officeContainer = MyUI.getCurrentUI().createOfficeContainer();
					
					if(!DataUtil.matchElement(restr, SecurityElement.OFFICE_UNRESTRICTED)) {
						// if user has office restrictions
						Filter[] filters = new Filter[userEntity.getEntity().getOffice().size()];
						int i =0;
						for (OfficeDO o : userEntity.getEntity().getOffice()) {
							filters[i++] = new Compare.Equal("id", o.getId());
						}
						officeContainer.addContainerFilter(new Or(filters));
					}
					MyUI.getCurrentUI().setOfficeContainer(officeContainer);
					MyUI.getCurrentUI().setOffice(officeContainer.getItem(officeContainer.firstItemId()).getBean());
					MyUI.getCurrentUI().updateObjectContainer();
					MyUI.getCurrentUI().buildOfficeChooser();
				}
			}
		});

		signin.addShortcutListener(enter);
	}

	public void addError(String errorMessage, Object... string) {
		if (loginPanel.getComponentCount() > 2) {
			// Remove the previous error message
			loginPanel.removeComponent(loginPanel.getComponent(2));
		}
		//		 Add new error message
		String message = MyUI.getCurrentUI().getBundle().getString(errorMessage);
		if(string != null && string.length> 0)
			message = MessageFormat.format(message,string);
		
		Label error = new Label(message);
		error.addStyleName("error");
		error.setSizeUndefined();
		// Add animation
		error.addStyleName("v-animate-reveal");
		loginPanel.addComponent(error);
		username.focus();
	}
}
