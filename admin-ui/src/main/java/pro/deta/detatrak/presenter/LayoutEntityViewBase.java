package pro.deta.detatrak.presenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Component;

import pro.deta.detatrak.event.EventBase;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.view.layout.BuildLayoutParameter;
import pro.deta.detatrak.view.layout.FormParameter;
import pro.deta.detatrak.view.layout.Layout;
import pro.deta.detatrak.view.layout.LayoutDefinitionException;
import pro.deta.detatrak.view.layout.LayoutRuntimeException;

public abstract class LayoutEntityViewBase<E> extends JPAEntityViewBase<E>{
	public static final Logger logger = LoggerFactory.getLogger(LayoutEntityViewBase.class);
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
			binder.setBuffered(true);
			if(itemId == null) {
				// если создаём новый объект - не надо его делать через JPA, в режиме Bean
				bean = createBean();
				binder.setItemDataSource(new BeanItem<E>(bean));
				addComponent(buildDefinition(binder,bean));
			} else {
				item = container.getItem(itemId);//container.getItem(10)
				binder.setItemDataSource(item);
				bean = JPAUtils.getBeanByItem(item);
				addComponent(buildDefinition(binder,bean));
			}
		} catch (LayoutDefinitionException e) {
			logger.error("Error while building form definition for bean " + item +" by " + itemId + e.getMessage());
		}

	}

	private Component buildDefinition(final FieldGroup binder, final E bean) throws LayoutDefinitionException {
		Component c;
		c = formDefinition.build(getLayoutParameters(bean));
		return c;
	}


	private BuildLayoutParameter<FormParameter<E>> getLayoutParameters(E e) {
		final FormParameter<E> fp = new FormParameter<E>(e, binder);
		return new BuildLayoutParameter<FormParameter<E>>() {
			@Override
			public FormParameter<E> getData() {
				return fp;
			}
		};
	}



	public abstract Layout getFormDefinition();

	protected void initForm(FieldGroup binder,E bean) {
		throw new RuntimeException("Not should be called. Call getFormDefinition/buildDefinition instead.");
	}



	public void save() {
		try {
			binder.commit();
		} catch (CommitException e1) {
			logger.error("Error while binder.commit",e1);
		}

		try {
			BuildLayoutParameter<FormParameter<E>> layoutParameters = getLayoutParameters(bean);
			formDefinition.save(layoutParameters);
		} catch (LayoutRuntimeException e1) {
			logger.error("Error while propagating save event to layout.", e1);
		}
		saveEntity(bean);
		save(bean);
	}

	private final void save(Object o) {
		JPAUtils.save(o);
		dispatchEvent(new EventBase("save"));
		//		if(tableBuilder != null && tableBuilder.getTable()!= null) {
		//			tableBuilder.getTable().containerItemSetChange(event);
		//		}
	}

	public void saveEntity(E obj) {

	}



	public void cancel() {
		binder.discard();
		try {
			formDefinition.cancel(getLayoutParameters(bean));
		} catch (LayoutRuntimeException e1) {
			logger.error("Error while propagating save event to layout.", e1);
		}
		super.cancel();
		postCancel();
	}

	public void postCancel() {

	}



	/**
	 * 
	 */
	private static final long serialVersionUID = -8215896366049268923L;

}
