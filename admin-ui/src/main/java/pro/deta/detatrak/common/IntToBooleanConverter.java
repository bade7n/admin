package pro.deta.detatrak.common;

import com.vaadin.data.util.converter.Converter;

import java.util.Locale;


public class IntToBooleanConverter<T> implements Converter<Boolean, Integer> {

    @Override
    public Integer convertToModel(Boolean value, Class<? extends Integer> targetType, Locale locale) throws ConversionException {
        return value ? 1 : 0;
    }

    @Override
    public Boolean convertToPresentation(Integer value, Class<? extends Boolean> targetType, Locale locale) throws ConversionException {
        return value == 1;
    }

    @Override
    public Class<Integer> getModelType() {
        return Integer.class;
    }

    @Override
    public Class<Boolean> getPresentationType() {
        return Boolean.class;
    }
}
