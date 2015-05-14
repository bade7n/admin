package pro.deta.detatrak.controls.service;

import pro.deta.detatrak.presenter.LayoutEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.view.layout.DetaFormLayout;
import pro.deta.detatrak.view.layout.FieldLayout;
import pro.deta.detatrak.view.layout.Layout;
import pro.deta.detatrak.view.layout.SaveCancelLayout;
import pro.deta.detatrak.view.layout.TabSheetLayout;
import pro.deta.detatrak.view.layout.ValuesContainer;
import ru.yar.vi.rm.data.ActionDO;
import ru.yar.vi.rm.data.CustomFieldDO;
import ru.yar.vi.rm.data.NotificationDO;
import ru.yar.vi.rm.data.ObjectTypeItemDO;
import ru.yar.vi.rm.data.ValidatorDO;

import com.vaadin.addon.jpacontainer.JPAContainer;

public class ActionView extends LayoutEntityViewBase<ActionDO> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4364176512490296558L;
	
	public static final String NAV_KEY = "actionView";
	
    public ActionView(JPAContainer<ActionDO> container) {
    	super(ActionDO.class,container);
    }

    
	public Layout getFormDefinition() {
		JPAContainer<CustomFieldDO> fieldContainer = JPAUtils.createCachingJPAContainer(CustomFieldDO.class);
		JPAContainer<ValidatorDO> validatorContainer = JPAUtils.createCachingJPAContainer(ValidatorDO.class);
		JPAContainer<ObjectTypeItemDO> objectTypeContainer = JPAUtils.createCachingJPAContainer(ObjectTypeItemDO.class);
		JPAContainer<NotificationDO> notificationContainer = JPAUtils.createCachingJPAContainer(NotificationDO.class);
		TabSheetLayout l = new TabSheetLayout();
		l.addTab(new DetaFormLayout("Основные настройки",
					new FieldLayout("Название", "name", FieldLayout.FieldType.TEXTFIELD),
					new FieldLayout("Доступ", "security", FieldLayout.FieldType.ACCESSCOMBOBOX),
					new FieldLayout("Типы объектов", "type", FieldLayout.FieldType.TWINCOLSELECT,new ValuesContainer<>(objectTypeContainer)),
					new FieldLayout("Дополнительные поля", "field", FieldLayout.FieldType.TWINCOLSELECT,new ValuesContainer<>(fieldContainer)),
					new FieldLayout("Количество дней для записи", "queueLength", FieldLayout.FieldType.TEXTFIELD),
					new FieldLayout("Мин. время восстановления отменённой записи (в мин.)", "minimumRestore", FieldLayout.FieldType.TEXTFIELD),
					new SaveCancelLayout(this)
				));
		l.addTab(new DetaFormLayout("Электронная очередь",
				new FieldLayout("Код талонов очереди", "code", FieldLayout.FieldType.TEXTFIELD),
				new FieldLayout("Максимальная длина очереди для выдачи талонов", "cfmQueueLength", FieldLayout.FieldType.TEXTFIELD),
				new FieldLayout("Сообщение при превышении лимита талонов", "cfmQueueLengthMsg", FieldLayout.FieldType.TEXTFIELD),
				new FieldLayout("Сообщение при окончании обслуживания", "cfmOfficeClosedMsg", FieldLayout.FieldType.TEXTFIELD),
				new SaveCancelLayout(this)
		));
		l.addTab(new DetaFormLayout("Информация",
				new FieldLayout("Описание", "description", FieldLayout.FieldType.CKEDITOR),
				new FieldLayout("Финальный текст", "finalText", FieldLayout.FieldType.CKEDITOR),
				new FieldLayout("Предупреждающий текст", "warningText", FieldLayout.FieldType.CKEDITOR),
				new SaveCancelLayout(this)
		));
		
		l.addTab(new DetaFormLayout("Дополнительно",
				new FieldLayout("Проверки", "validator", FieldLayout.FieldType.TWINCOLSELECT,new ValuesContainer<>(validatorContainer)),
				new FieldLayout("Оповещения", "notifications", FieldLayout.FieldType.TWINCOLSELECT,new ValuesContainer<>(notificationContainer)),
				new SaveCancelLayout(this)
				));

		return l;
	}

	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}
