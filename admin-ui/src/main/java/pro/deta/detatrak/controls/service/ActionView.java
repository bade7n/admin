package pro.deta.detatrak.controls.service;

import java.util.ArrayList;
import java.util.List;

import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.listbuilder.ListBuilder;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import ru.yar.vi.rm.data.ActionDO;
import ru.yar.vi.rm.data.CustomFieldDO;
import ru.yar.vi.rm.data.NotificationDO;
import ru.yar.vi.rm.data.ObjectTypeItemDO;
import ru.yar.vi.rm.data.ValidatorDO;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.fieldgroup.FieldGroup;

public class ActionView extends JPAEntityViewBase<ActionDO> {

    public static final String NAV_KEY = "actionView";
	private JPAContainer<CustomFieldDO> fieldContainer = JPAUtils.createCachingJPAContainer(CustomFieldDO.class);
	private JPAContainer<ValidatorDO> validatorContainer = JPAUtils.createCachingJPAContainer(ValidatorDO.class);
	private JPAContainer<ObjectTypeItemDO> objectTypeContainer = JPAUtils.createCachingJPAContainer(ObjectTypeItemDO.class);
	private JPAContainer<NotificationDO> notificationContainer = JPAUtils.createCachingJPAContainer(NotificationDO.class);
	
	ListBuilder listBuilder = null;
	
    public ActionView() {
    	super(ActionDO.class);
    }

	@Override
	protected void initForm(FieldGroup binder,ActionDO action) {

		form.addComponent(ComponentsBuilder.createTextField("Название", binder, "name"));
		
		/* Settings for pre-entry mode */
		form.addComponent(ComponentsBuilder.createTextField("Количество дней для записи", binder, "queueLength"));
		form.addComponent(ComponentsBuilder.createCKEditorTextField("Описание", binder, "description"));
		form.addComponent(ComponentsBuilder.createCKEditorTextField("Финальный текст", binder, "finalText"));
		form.addComponent(ComponentsBuilder.createCKEditorTextField("Предупреждающий текст", binder, "warningText"));
		form.addComponent(ComponentsBuilder.createTextField("Минимальное время восстановления отменённой записи (мин.)", binder, "minimumRestore"));

		/* Settings for cfm mode */
		form.addComponent(ComponentsBuilder.createTextField("Код талонов очереди", binder, "code"));
		form.addComponent(ComponentsBuilder.createTextField("Максимальная длина очереди для выдачи талонов", binder, "cfmQueueLength"));
		form.addComponent(ComponentsBuilder.createTextField("Сообщение при превышении лимита талонов", binder, "cfmQueueLengthMsg"));
		form.addComponent(ComponentsBuilder.createTextField("Сообщение при окончании обслуживания", binder, "cfmOfficeClosedMsg"));
		
		/* Common settings */
		form.addComponent(ComponentsBuilder.createAccessComboBox(binder,"security"));
        form.addComponent(ComponentsBuilder.createTwinColSelect("Типы объектов", objectTypeContainer, binder, "type"));
        form.addComponent(ComponentsBuilder.createTwinColSelect("Дополнительные поля", fieldContainer, binder, "field"));
        form.addComponent(ComponentsBuilder.createTwinColSelect("Проверки", validatorContainer, binder, "validator"));
        form.addComponent(ComponentsBuilder.createTwinColSelect("Оповещения", notificationContainer, binder, "notifications"));
        form.addComponent(ComponentsBuilder.createSaveCancelButtons(this));

	}
	
	@Override
	public void saveEntity(ActionDO obj) {
		listBuilder.commit();
		obj.getType().clear();
		for(Integer id: (List<Integer>)listBuilder.getValue()) {
			obj.getType().add(objectTypeContainer.getItem(id).getEntity());
		}
	}
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}
