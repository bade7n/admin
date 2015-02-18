package pro.deta.detatrak.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import ru.yar.vi.rm.data.ObjectIdentifiable;

import com.vaadin.addon.jpacontainer.EntityContainer;
import com.vaadin.data.util.converter.Converter;


public class MyTwinColSelectStringConverter<T> implements Converter<Object,List> {
	
	private EntityContainer<T> container;

	public MyTwinColSelectStringConverter(EntityContainer<T> container) {
		this.container = container;
		
	}

	@Override
	public List convertToModel(Object value,
			Class<? extends List> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		Iterator<Object> it =  ((Set<Object>) value).iterator();
		ArrayList<T> list = new ArrayList<T>();
		while(it.hasNext()) {
			Object k = it.next();
			T item = container.getItem(k).getEntity();
			list.add(item);
		}
		return list;
	}

	@Override
	public Object convertToPresentation(List value,
			Class<? extends Object> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		List<ObjectIdentifiable> objectList = value;
		Set<Object> hset = new LinkedHashSet<Object>();
		if(objectList != null) {
			for(ObjectIdentifiable o: objectList) {
				if(o != null)
				hset.add(o.getObjectId());
			}
		}
		return hset;
	}

	@Override
	public Class<List> getModelType() {
		return List.class;
	}

	@Override
	public Class<Object> getPresentationType() {
		return Object.class;
	}
}