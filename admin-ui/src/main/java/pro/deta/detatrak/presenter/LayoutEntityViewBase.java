package pro.deta.detatrak.presenter;

import org.apache.log4j.Logger;

import pro.deta.detatrak.view.layout.BuildLayoutParameter;
import pro.deta.detatrak.view.layout.Layout;
import pro.deta.detatrak.view.layout.LayoutDefinitionException;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Component;

public abstract class LayoutEntityViewBase<E> extends JPAEntityViewBase<E>{
	public static final Logger log = Logger.getLogger(LayoutEntityViewBase.class);
	private Layout formDefinition;

	public LayoutEntityViewBase(Class<E> class1) {
		super(class1);
		formDefinition = getFormDefinition();
	}


	@Override
	protected void buildUI(String parameter) {
		removeAllComponents();
		try {

			item = null;
			itemId = getItemId(parameter);

			binder = new FieldGroup();
			binder.setBuffered(false);
			if(itemId == null) {
				// если создаём новый объект - не надо его делать через JPA, в режиме Bean
				E bean = createBean();
				addedBean = new BeanItem<E>(bean);
				binder.setItemDataSource(addedBean);
				addComponent(buildDefinition(binder,bean));
			} else {
				item = container.getItem(itemId);
				binder.setItemDataSource(item);
				addComponent(buildDefinition(binder,item.getEntity()));
			}
		} catch (LayoutDefinitionException e) {
			log.error("Error while building form definition for bean " + item +" by " + itemId + e.getMessage());
		}

	}

	private Component buildDefinition(final FieldGroup binder, final E bean) throws LayoutDefinitionException {
		Component c;
		c = formDefinition.build(new BuildLayoutParameter<E>() {
			@Override
			public FieldGroup getBinder() {
				return binder;
			}

			@Override
			public E getBean() {
				return bean;
			}
		});
		return c;
	}


	public abstract Layout getFormDefinition();

	protected void initForm(FieldGroup binder,E bean) {
		throw new RuntimeException("Not should be called. Call getFormDefinition/buildDefinition instead.");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8215896366049268923L;

}
