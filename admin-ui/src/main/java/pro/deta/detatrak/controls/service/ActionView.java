package pro.deta.detatrak.controls.service;

import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import pro.deta.detatrak.presenter.NewEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import ru.yar.vi.rm.data.ActionDO;
import ru.yar.vi.rm.data.CustomFieldDO;
import ru.yar.vi.rm.data.NotificationDO;
import ru.yar.vi.rm.data.ObjectTypeItemDO;
import ru.yar.vi.rm.data.ValidatorDO;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;

public class ActionView extends NewEntityViewBase<ActionDO> {

    public static final String NAV_KEY = "actionView";
	private JPAContainer<CustomFieldDO> fieldContainer = JPAUtils.createCachingJPAContainer(CustomFieldDO.class);
	private JPAContainer<ValidatorDO> validatorContainer = JPAUtils.createCachingJPAContainer(ValidatorDO.class);
	private JPAContainer<ObjectTypeItemDO> objectTypeContainer = JPAUtils.createCachingJPAContainer(ObjectTypeItemDO.class);
	private JPAContainer<NotificationDO> notificationContainer = JPAUtils.createCachingJPAContainer(NotificationDO.class);
	
    public ActionView() {
    	super(ActionDO.class);
    }

	@Override
	protected void initForm(FieldGroup binder,ActionDO action) {

//        root.addComponent(ComponentsBuilder.createSaveCancelButtons(this));
        FormLayout f1 = new FormLayout();
        FormLayout f2 = new FormLayout();
        FormLayout f3 = new FormLayout();
        FormLayout f4 = new FormLayout();
		tabSheet.addTab(f1 ,"Основные настройки");
		tabSheet.addTab(f2 ,"Электронная очередь");
		tabSheet.addTab(f3 ,"Информация");
		tabSheet.addTab(f4 ,"Дополнительно");
		f1.addComponent(ComponentsBuilder.createTextField("Название", binder, "name"));
		/* Common settings */
		f1.addComponent(ComponentsBuilder.createAccessComboBox(binder,"security"));
		f1.addComponent(ComponentsBuilder.createTwinColSelect("Типы объектов", objectTypeContainer, binder, "type"));
		f1.addComponent(ComponentsBuilder.createTwinColSelect("Дополнительные поля", fieldContainer, binder, "field"));
		f4.addComponent(ComponentsBuilder.createTwinColSelect("Проверки", validatorContainer, binder, "validator"));
		f4.addComponent(ComponentsBuilder.createTwinColSelect("Оповещения", notificationContainer, binder, "notifications"));
		
		/* Settings for pre-entry mode */
		f1.addComponent(ComponentsBuilder.createTextField("Количество дней для записи", binder, "queueLength"));
		f1.addComponent(ComponentsBuilder.createTextField("Мин. время восстановления отменённой записи (в мин.)", binder, "minimumRestore"));

		f3.addComponent(ComponentsBuilder.createCKEditorTextField("Описание", binder, "description"));
		f3.addComponent(ComponentsBuilder.createCKEditorTextField("Финальный текст", binder, "finalText"));
		f3.addComponent(ComponentsBuilder.createCKEditorTextField("Предупреждающий текст", binder, "warningText"));

		/* Settings for cfm mode */
		f2.addComponent(ComponentsBuilder.createTextField("Код талонов очереди", binder, "code"));
		f2.addComponent(ComponentsBuilder.createTextField("Максимальная длина очереди для выдачи талонов", binder, "cfmQueueLength"));
		f2.addComponent(ComponentsBuilder.createTextField("Сообщение при превышении лимита талонов", binder, "cfmQueueLengthMsg"));
		f2.addComponent(ComponentsBuilder.createTextField("Сообщение при окончании обслуживания", binder, "cfmOfficeClosedMsg"));

		f1.addComponent(ComponentsBuilder.createSaveCancelButtons(this));
		f2.addComponent(ComponentsBuilder.createSaveCancelButtons(this));
		f3.addComponent(ComponentsBuilder.createSaveCancelButtons(this));
		f4.addComponent(ComponentsBuilder.createSaveCancelButtons(this));
	}
	

	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}
