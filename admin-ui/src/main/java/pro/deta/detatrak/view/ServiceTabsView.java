package pro.deta.detatrak.view;

import pro.deta.detatrak.controls.service.ActionView;
import pro.deta.detatrak.controls.service.CriteriaView;
import pro.deta.detatrak.controls.service.FieldView;
import pro.deta.detatrak.controls.service.ValidatorView;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.util.NewRightPaneTabsView;
import pro.deta.detatrak.util.TopLevelMenuView;
import pro.deta.detatrak.view.layout.Layout;
import pro.deta.detatrak.view.layout.TabSheetLayout;
import pro.deta.detatrak.view.layout.TableColumnLayout;
import pro.deta.detatrak.view.layout.TableLayout;
import pro.deta.security.SecurityElement;
import ru.yar.vi.rm.data.ActionDO;
import ru.yar.vi.rm.data.CriteriaDO;
import ru.yar.vi.rm.data.CustomFieldDO;
import ru.yar.vi.rm.data.ValidatorDO;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

@TopLevelMenuView(icon="icon-service")
public class ServiceTabsView extends NewRightPaneTabsView  implements Captioned,Initializable,Restrictable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1033444553842199273L;
	public static final String NAV_KEY = "/service";
    private JPAContainer<ActionDO> actionContainer = new JPAContainer<>(ActionDO.class);
    private JPAContainer<CustomFieldDO> fieldContainer = new JPAContainer<>(CustomFieldDO.class);
    private JPAContainer<CriteriaDO> criteriaContainer = new JPAContainer<>(CriteriaDO.class);
    private JPAContainer<ValidatorDO> validatorContainer = new JPAContainer<>(ValidatorDO.class);

    @Override
	public Layout getLayoutDefinition() {
    	actionContainer = JPAUtils.createCachingJPAContainer(ActionDO.class);
    	fieldContainer = JPAUtils.createCachingJPAContainer(CustomFieldDO.class);
    	criteriaContainer = JPAUtils.createCachingJPAContainer(CriteriaDO.class);
    	validatorContainer = JPAUtils.createCachingJPAContainer(ValidatorDO.class);
    	
    	
    	TabSheetLayout tsl = new TabSheetLayout();
    	ActionView action = new ActionView();
    	tsl.addTab(new TableLayout(actionContainer,bundle.getString("label.services"), action.getNavKey(), 
    			new TableColumnLayout("name", bundle.getString("label.name")),
    			new TableColumnLayout("security", "Доступ")
    	));
    	FieldView field = new FieldView();
    	tsl.addTab(new TableLayout(fieldContainer, bundle.getString("label.fields"),field.getNavKey(), 
    			new TableColumnLayout("name", bundle.getString("label.name")),
    			new TableColumnLayout("type", "Тип клиента"),
    			new TableColumnLayout("required", "Обязательный"),
    			new TableColumnLayout("field", "Поле")
    	));
    	CriteriaView criteria = new CriteriaView();
    	tsl.addTab(new TableLayout(criteriaContainer, "Опция",criteria.getNavKey(), 
    			new TableColumnLayout("name", "Опция")
    	));
    	ValidatorView validator = new ValidatorView();
    	tsl.addTab(new TableLayout(validatorContainer, "Проверки",validator.getNavKey(), 
    			new TableColumnLayout("name", bundle.getString("label.name")),
    			new TableColumnLayout("clazz", "Класс")
    	));

        addForInitialization(this);
        addForInitialization(action,actionContainer);
        addForInitialization(field,fieldContainer);
        addForInitialization(criteria,criteriaContainer);
        addForInitialization(validator,validatorContainer);
        return tsl;
    }

	@Override
	public String getCaption() {
		return bundle.getString("label.services");
	}


	@Override
	public void changeView(ViewChangeEvent viewChangeEvent) {
		/**
		 *  @TODO надо реализовать фильтр контейнеров в зависимости от офиса
		 */
	}

	@Override
	public SecurityElement getRestriction() {
		return SecurityElement.SERVICES_TAB;
	}
	
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}
