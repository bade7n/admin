package pro.deta.detatrak.controls.admin;

import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import pro.deta.detatrak.util.DataUtil;
import pro.deta.detatrak.util.JPAUtils;
import ru.yar.vi.rm.data.OfficeDO;
import ru.yar.vi.rm.data.RoleDO;
import ru.yar.vi.rm.data.SiteDO;
import ru.yar.vi.rm.data.UserDO;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.TextField;

public class UserView extends JPAEntityViewBase<UserDO> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7228187847180905895L;

	public static final String NAV_KEY = "userView";
    
    private JPAContainer<RoleDO> roles = JPAUtils.createCachingJPAContainer(RoleDO.class);
    private JPAContainer<SiteDO> sites = JPAUtils.createCachingJPAContainer(SiteDO.class);
    private JPAContainer<OfficeDO> offices = JPAUtils.createCachingJPAContainer(OfficeDO.class);
    private TextField password;
    
    
    public UserView() {
    	super(UserDO.class);
    }

	@Override
	protected void initForm(FieldGroup binder,UserDO user) {
		form.addComponent(binder.buildAndBind("Логин", "name"));
		password = new TextField("Пароль");
        form.addComponent(password);
        form.addComponent(binder.buildAndBind("Описание", "description"));
        form.addComponent(ComponentsBuilder.createTwinColSelect("Роли", roles, binder, "role"));
        form.addComponent(ComponentsBuilder.createTwinColSelect("Офисы", offices, binder, "office"));
        form.addComponent(ComponentsBuilder.createTwinColSelect("Площадки", sites, binder, "site"));
        form.addComponent(ComponentsBuilder.createSaveCancelButtons(this));
	}
	@Override
	public void saveEntity(UserDO obj) {
		if(password.getValue() != null && !"".equalsIgnoreCase(password.getValue())) {
			String passw = DataUtil.md5(password.getValue());
			obj.setPass(passw);
		}
	};
	
	@Override
	public Object getItemId(String parameter) {
		if("".equalsIgnoreCase(parameter))
			return null;
		return parameter;
	}
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}
