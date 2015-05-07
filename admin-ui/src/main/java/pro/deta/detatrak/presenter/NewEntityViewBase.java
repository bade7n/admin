package pro.deta.detatrak.presenter;

import pro.deta.detatrak.common.ComponentsBuilder;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.TabSheet;

public abstract class NewEntityViewBase<E> extends JPAEntityViewBase<E>{
	protected TabSheet tabSheet;

	public NewEntityViewBase(Class<E> e) {
		super(e);
	}

	
	@Override
	protected void buildUI(String parameter) {
		removeAllComponents();
		tabSheet = new TabSheet();
		root.addComponent(tabSheet);

		item = null;
		itemId = getItemId(parameter);
		
		binder = new FieldGroup();
		binder.setBuffered(false);
		if(itemId == null) {
			// если создаём новый объект - не надо его делать через JPA, в режиме Bean
			E bean = createBean();
			addedBean = new BeanItem<E>(bean);
			binder.setItemDataSource(addedBean);
			initForm(binder,bean);
		} else {
			item = container.getItem(itemId);
			binder.setItemDataSource(item);
			initForm(binder,item.getEntity());
		}
		
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -8215896366049268923L;

}
