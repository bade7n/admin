package pro.deta.detatrak.common;

import com.vaadin.data.util.converter.Converter;

import java.util.Locale;


public class StringToBooleanConverter<T> implements Converter<Boolean, String> {

    @Override
    public String convertToModel(Boolean value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        return value ? "on" : "off";
    }

    @Override
    public Boolean convertToPresentation(String value, Class<? extends Boolean> targetType, Locale locale) throws ConversionException {
        return "on".equals(value) || "true".equals(value);
    }

    @Override
    public Class<String> getModelType() {
        return String.class;
    }

    @Override
    public Class<Boolean> getPresentationType() {
        return Boolean.class;
    }
}
