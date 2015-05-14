package pro.deta.detatrak.controls.extra;

import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.common.IAction;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import ru.yar.vi.rm.data.ReportObjectDO;
import ru.yar.vi.rm.data.TerminalLinkDO;
import ru.yar.vi.rm.data.TerminalPageDO;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.PopupView;

public class TerminalLinkView extends JPAEntityViewBase<TerminalLinkDO> implements IAction {
	/**
	 * 
	 */
	public static final String NAV_KEY = "terminalLinkView";

	private static final long serialVersionUID = -2909980719200560830L;
	private PopupView view;
	private JPAContainer<TerminalPageDO> pageContainer;

	public TerminalLinkView(JPAContainer<TerminalLinkDO> navLinkContainer) {
		super(TerminalLinkDO.class,navLinkContainer);
		pageContainer = JPAUtils.createCachingJPAContainer(TerminalPageDO.class);
	}

	public void setItem(EntityItem<TerminalLinkDO> item,JPAContainer<TerminalLinkDO> container,PopupView view) {
		this.container = container;
		this.view = view;
		
		this.item = item;
		form.removeAllComponents();

		binder = new FieldGroup();
		binder.setItemDataSource(item);
		initForm(binder,item.getEntity());
	}

	protected void initForm(FieldGroup binder,TerminalLinkDO type) {
		binder.setBuffered(true);
		form.addComponent(ComponentsBuilder.createTextField("Заголовок",binder, "name"));
		form.addComponent(ComponentsBuilder.createTextField("Icon",binder, "icon"));
		ComboBox createComboBoxWithDataSource = ComponentsBuilder.createComboBoxWithDataSource("Страница", pageContainer, binder, "content","name");
		createComboBoxWithDataSource.setNullSelectionItemId(null);
		createComboBoxWithDataSource.setImmediate(false);
		form.addComponent(createComboBoxWithDataSource);

		form.addComponent(ComponentsBuilder.createSaveCancelButtons(this));
	}

	@Override
	public void saveEntity(TerminalLinkDO obj) {
		// TODO Добавить валидацию что выбран только один объект - либо офис либо объект либо объявление.
		super.saveEntity(obj);
		view.setPopupVisible(false);
	}

	@Override
	public void postCancel() {
		view.setPopupVisible(false);
	}

	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}
