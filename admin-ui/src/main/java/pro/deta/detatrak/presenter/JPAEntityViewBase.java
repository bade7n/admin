package pro.deta.detatrak.presenter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.addon.jpacontainer.EntityContainer;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.PopupView;

import pro.deta.detatrak.event.EventBase;
import pro.deta.detatrak.util.EntityContainerHandler;
import pro.deta.detatrak.util.JPAUtils;
import ru.yar.vi.rm.model.NumberWrapper;


public abstract class JPAEntityViewBase<E> extends EditViewBase implements EntityContainerHandler<E> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8600864478538693787L;
	public static final Logger logger = LoggerFactory.getLogger(JPAEntityViewBase.class);
	protected Item item = null;
	protected Container container = null;
	protected Object itemId = null;
//	protected BeanItem<E> addedBean = null;
	protected Class<E> type = null;
	protected FieldGroup binder;
	E bean = null;

	public JPAEntityViewBase(Class<E> e) {
		type = e;
	}

	public void setItem(EntityItem<E> item,EntityContainer<E> container,PopupView view) {
		
	}

	@Override
	protected void buildUI(String parameter) {
		item = null;
		itemId = getItemId(parameter);
		form.removeAllComponents();
		
		binder = new FieldGroup();
		binder.setBuffered(false);
		if(itemId == null) {
			// если создаём новый объект - не надо его делать через JPA, в режиме Bean
			bean = createBean();
			binder.setItemDataSource(new BeanItem<E>(bean));
			initForm(binder,bean);
		} else {
			item = container.getItem(itemId);
			binder.setItemDataSource(item);
			bean = JPAUtils.getBeanByItem(item);
			initForm(binder,bean);
		}
		
	}
	
	
	public E createBean() {
		try {
			return type.newInstance();
		} catch (InstantiationException e) {
			logger.error("Error while creating class instance " + type,e);
		} catch (IllegalAccessException e) {
			logger.error("Error while creating class instance " + type,e);
		}
		return null;
	}

	public Object getItemId(String parameter) {
		if(parameter != null && parameter.matches("^\\d+$")) {
			Integer currentId = !"".equals(parameter) ? new NumberWrapper(parameter).getInteger() : null;
			return currentId;
		} else if (!"".equalsIgnoreCase(parameter))
			return parameter;
		else
			return null;
	}

	abstract protected void initForm(FieldGroup binder,E bean);
	
	public void save() {
		try {
			binder.commit();
		} catch (CommitException e1) {
			logger.error("Error while binder.commit",e1);
		}
		saveEntity(bean);
		save(bean);
	}
	
	private final void save(Object o) {
		JPAUtils.save(o);
		dispatchEvent(new EventBase("save"));
	}

	public void saveEntity(E obj) {
	}
	
	public void cancel() {
		binder.discard();
		super.cancel();
		postCancel();
	}

	public void postCancel() {
		
	}

	public void setContainer(Container container) {
		this.container = container;
	}
}
