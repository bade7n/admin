package pro.deta.detatrak.view;

import java.util.HashMap;

import pro.deta.detatrak.common.TableBuilder;
import pro.deta.detatrak.controls.service.ActionView;
import pro.deta.detatrak.controls.service.CriteriaView;
import pro.deta.detatrak.controls.service.FieldView;
import pro.deta.detatrak.controls.service.ValidatorView;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.util.RightPaneTabsView;
import pro.deta.detatrak.util.TopLevelMenuView;
import pro.deta.security.SecurityElement;
import ru.yar.vi.rm.data.ActionDO;
import ru.yar.vi.rm.data.CriteriaDO;
import ru.yar.vi.rm.data.CustomFieldDO;
import ru.yar.vi.rm.data.ValidatorDO;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.TabSheet;

@TopLevelMenuView(icon="icon-service")
public class ServiceTabsView extends RightPaneTabsView  implements Captioned,Initializable,Restrictable{

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
	public void initTabs(TabSheet tabs) {
    	actionContainer = JPAUtils.createCachingJPAContainer(ActionDO.class);
    	fieldContainer = JPAUtils.createCachingJPAContainer(CustomFieldDO.class);
    	criteriaContainer = JPAUtils.createCachingJPAContainer(CriteriaDO.class);
    	validatorContainer = JPAUtils.createCachingJPAContainer(ValidatorDO.class);
    	
        addForInitialization(this);
        
    	addTab(createActionsTable());
    	addTab(createFieldsTable());
    	addTab(createCriteriaTable());
    	addTab(createValidatorTable());
    }


    private TableBuilder createActionsTable() {
    	ActionView view = new ActionView();
    	TableBuilder panel =
                new TableBuilder()
                        .addColumn("name", bundle.getString("label.name"))
                        .addColumn("security", "Доступ")
                        .setEditItemKey(view.getNavKey())
                        .setBeanContainer(actionContainer)
                        ;
        panel.setCaption(bundle.getString("label.services"));
        addForInitialization(view);
        return panel;
    }

    private TableBuilder createFieldsTable() {
    	FieldView view = new FieldView();
    	TableBuilder panel =
                new TableBuilder()
                        .addColumn("name", bundle.getString("label.name"))
                        .addColumn("type", "Тип клиента")
                        .addColumn("required", "Обязательный")
                        .addColumn("field", "Поле")
                        .setEditItemKey(view.getNavKey())
                        .setBeanContainer(fieldContainer);
        panel.setCaption(bundle.getString("label.fields"));
        addForInitialization(view);
        return panel;
    }

    private TableBuilder createCriteriaTable() {
    	CriteriaView view = new CriteriaView();
    	TableBuilder panel =
                new TableBuilder()
                        .addColumn("name", "Опция")
                        .setEditItemKey(view.getNavKey())
                        .setBeanContainer(criteriaContainer);
        panel.setCaption("Опция");
        addForInitialization(view);
        return panel;
    }

    private TableBuilder createValidatorTable() {
    	ValidatorView view = new ValidatorView();
    	TableBuilder panel =
                new TableBuilder()
                        .addColumn("name", bundle.getString("label.name"))
                        .addColumn("clazz", "Класс")
                        .setEditItemKey(view.getNavKey())
                        .setBeanContainer(validatorContainer);
        panel.setCaption("Проверки");
        addForInitialization(view);
        return panel;
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
