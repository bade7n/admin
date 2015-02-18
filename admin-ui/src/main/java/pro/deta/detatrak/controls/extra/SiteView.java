package pro.deta.detatrak.controls.extra;

import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import ru.yar.vi.rm.data.ActionDO;
import ru.yar.vi.rm.data.CustomerDO;
import ru.yar.vi.rm.data.OfficeDO;
import ru.yar.vi.rm.data.RegionDO;
import ru.yar.vi.rm.data.SiteDO;

import com.vaadin.addon.jpacontainer.EntityContainer;
import com.vaadin.data.fieldgroup.FieldGroup;

public class SiteView extends JPAEntityViewBase<SiteDO> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3012193455613754605L;
	public static final String NAV_KEY = "siteView";
	private EntityContainer<ActionDO> actionContainer = JPAUtils.createCachingJPAContainer(ActionDO.class);
	private EntityContainer<CustomerDO> customerContainer = JPAUtils.createCachingJPAContainer(CustomerDO.class);
	private EntityContainer<OfficeDO> officeContainer = JPAUtils.createCachingJPAContainer(OfficeDO.class);
	private EntityContainer<RegionDO> regionContainer = JPAUtils.createCachingJPAContainer(RegionDO.class);
	
    public SiteView() {
    	super(SiteDO.class);
    }


	@Override
	protected void initForm(FieldGroup binder,SiteDO site) {

		
        form.addComponent(ComponentsBuilder.createTextField("Название", binder, "name"));
        form.addComponent(ComponentsBuilder.createCheckBox("Главный", binder, "main"));

        form.addComponent(ComponentsBuilder.createCKEditorTextField("Описание", binder, "description"));
        form.addComponent(ComponentsBuilder.createCKEditorTextField("Информация", binder, "info"));

        form.addComponent(ComponentsBuilder.createCKEditorTextField("Вводный текст", binder, "intro"));
        form.addComponent(ComponentsBuilder.createCKEditorTextField("Текст по окончании процедуры записи", binder, "finalText"));
        form.addComponent(ComponentsBuilder.createTextArea("HTTP Title", binder, "httpTitle"));
        form.addComponent(ComponentsBuilder.createCKEditorTextField("HTTP Description", binder, "httpDescription"));
        form.addComponent(ComponentsBuilder.createTextArea("HTTP Keywords", binder, "httpKeywords"));
        form.addComponent(ComponentsBuilder.createTextArea("HTTP Template", binder, "httpTemplate"));

        form.addComponent(ComponentsBuilder.createTextArea("Стиль терминала", binder, "selfCss"));
        form.addComponent(ComponentsBuilder.createTextArea("Стиль основной", binder, "styleCss"));
        form.addComponent(ComponentsBuilder.createTextArea("Стиль живая очередь", binder, "cfmCss"));

        form.addComponent(ComponentsBuilder.createTwinColSelect("Услуги", actionContainer, binder, "actions"));
        form.addComponent(ComponentsBuilder.createTwinColSelect("Типы клиентов", customerContainer, binder, "customers"));
        form.addComponent(ComponentsBuilder.createTwinColSelect("Офисы", officeContainer, binder, "offices"));
        form.addComponent(ComponentsBuilder.createTwinColSelect("Регионы", regionContainer, binder, "regions"));

        form.addComponent(ComponentsBuilder.createSaveCancelButtons(this));
		
	}
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}
