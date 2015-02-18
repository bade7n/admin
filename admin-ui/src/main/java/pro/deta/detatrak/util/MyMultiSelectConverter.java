package pro.deta.detatrak.util;

import com.vaadin.addon.jpacontainer.EntityContainer;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.EntityItemProperty;
import com.vaadin.addon.jpacontainer.EntityProvider;
import com.vaadin.addon.jpacontainer.metadata.EntityClassMetadata;
import com.vaadin.addon.jpacontainer.metadata.MetadataFactory;
import com.vaadin.addon.jpacontainer.metadata.PropertyMetadata;
import com.vaadin.data.Property;
import com.vaadin.data.util.TransactionalPropertyWrapper;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.ui.AbstractSelect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.ManyToMany;


public class MyMultiSelectConverter<T> implements
		Converter<Collection<Object>, Collection<T>> {
	private final AbstractSelect select;
	private Boolean owningSide;
	private String mappedBy;

	public MyMultiSelectConverter(AbstractSelect select) {
		this.select = select;
	}

	private EntityContainer<T> getContainer() {
		return ((EntityContainer) this.select.getContainerDataSource());
	}

	public Collection<Object> convertToPresentation(Collection<T> value,
			Class<? extends Collection<Object>> targetType, Locale locale)
			throws Converter.ConversionException {
		if ((value == null) || (value.isEmpty())) {
			try {
				return createNewCollectionForType(getPropertyDataSource()
						.getType());
			} catch (Exception e) {
				throw new Converter.ConversionException(e);
			}
		}

		HashSet identifiers = new HashSet();
		for (Iterator<T> i$ = value.iterator(); i$.hasNext();) {
			T entity = i$.next();
			Object identifier = getContainer().getEntityProvider()
					.getIdentifier(entity);

			identifiers.add(identifier);
		}
		return identifiers;
	}

	public Collection<T> convertToModel(Collection<Object> value,
			Class<? extends Collection<T>> targetType, Locale locale)
			throws Converter.ConversionException {
		Collection idset = value;

		Collection modelValue = (Collection) getPropertyDataSource().getValue();

		if (modelValue == null) {
			try {
				modelValue = createNewCollectionForType(getPropertyDataSource()
						.getType());
			} catch (Exception e) {
				throw new Converter.ConversionException(e);
			}
		}

		if ((idset == null) || (idset.isEmpty())) {
			modelValue.clear();
			return modelValue;
		}

		HashSet<T> orphaned = new HashSet<T>(modelValue);

		for (Iterator<T> i$ = idset.iterator(); i$.hasNext();) {
			Object id = i$.next();
			EntityItem<T> item = getContainer().getItem(id);
			T entity = item.getEntity();
			if (!(modelValue.contains(entity))) {
				modelValue.add(entity);
				addBackReference(entity);
			}
			orphaned.remove(entity);
		}

		for (Iterator<T> i$ = orphaned.iterator(); i$.hasNext();) {
			T entity = i$.next();
			modelValue.remove(entity);
			removeBackReference(entity);
		}

		if (!(isOwningSide()))
			;
		return modelValue;
	}

	private EntityItemProperty getPropertyDataSource() {
		Property<?> property = select.getPropertyDataSource();
    	if (property instanceof EntityItemProperty)
    		return (EntityItemProperty) property;
    	else if (property instanceof TransactionalPropertyWrapper<?>) {
    		TransactionalPropertyWrapper<?> propertyWrapper = (TransactionalPropertyWrapper<?>) property;
    		return (EntityItemProperty) propertyWrapper.getWrappedProperty();
    	}
    	return null;
	}

	private void removeBackReference(T entity) {
		if (!(isOwningSide())) {
			EntityItemProperty itemProperty = getBackReferenceItemProperty(entity);
			Collection c = (Collection) itemProperty.getValue();
			c.remove(getPropertyDataSource().getItem().getEntity());
			itemProperty.setValue(c);
		}
	}

	private EntityItemProperty getBackReferenceItemProperty(T entity) {
		EntityItem item = getContainer().getItem(
				getContainer().getEntityProvider().getIdentifier(entity));

		EntityItemProperty itemProperty = item.getItemProperty(this.mappedBy);
		return itemProperty;
	}

	private void addBackReference(T entity) {
		if (!(isOwningSide())) {
			EntityItemProperty itemProperty = getBackReferenceItemProperty(entity);
			Collection c = (Collection) itemProperty.getValue();
			c.add(getPropertyDataSource().getItem().getEntity());
			itemProperty.setValue(c);
		}
	}

	private boolean isOwningSide() {
		if (this.owningSide == null) {
			Class entityClass = getPropertyDataSource().getItem()
					.getContainer().getEntityClass();

			EntityClassMetadata entityClassMetadata = MetadataFactory
					.getInstance().getEntityClassMetadata(entityClass);

			PropertyMetadata property = entityClassMetadata
					.getProperty(getPropertyDataSource().getPropertyId());

			ManyToMany annotation = (ManyToMany) property
					.getAnnotation(ManyToMany.class);
			if ((annotation.mappedBy() != null)
					&& (!(annotation.mappedBy().isEmpty()))) {
				this.owningSide = Boolean.FALSE;
				this.mappedBy = annotation.mappedBy();
				return this.owningSide.booleanValue();
			}
			this.owningSide = Boolean.TRUE;
		}
		return this.owningSide.booleanValue();
	}

	static Collection createNewCollectionForType(Class<?> type)
			throws InstantiationException, IllegalAccessException {
		if (type.isInterface()) {
			if (type == Set.class)
				return new HashSet();
			if (type == List.class) {
				return new ArrayList();
			}
			throw new RuntimeException(
					"Couldn't instantiate a collection for property.");
		}

		return ((Collection) type.newInstance());
	}

	public Class<Collection<T>> getModelType() {
		return getPropertyDataSource().getType();
	}

	public Class<Collection<Object>> getPresentationType() {
		return getPropertyDataSource().getType();
	}
}