package pro.deta.detatrak.common;

import com.vaadin.data.util.converter.Converter;

import java.util.*;

public class ListToSetConverter implements Converter<Set, List> {
    @Override
    public List convertToModel(Set set, Class<? extends List> aClass, Locale locale) throws ConversionException {
        return new ArrayList(set);
    }

    @Override
    public Set convertToPresentation(List list, Class<? extends Set> aClass, Locale locale) throws ConversionException {
    	if(list == null)
    		return new HashSet();
        return new HashSet(list);
    }

    @Override
    public Class<List> getModelType() {
        return List.class;
    }

    @Override
    public Class<Set> getPresentationType() {
        return Set.class;
    }
}
