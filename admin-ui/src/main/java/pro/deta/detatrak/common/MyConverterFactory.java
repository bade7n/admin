package pro.deta.detatrak.common;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.DefaultConverterFactory;

import java.util.List;
import java.util.Set;

public class MyConverterFactory extends DefaultConverterFactory {
    @Override
    protected <PRESENTATION, MODEL> Converter<PRESENTATION, MODEL> findConverter(Class<PRESENTATION> presentationType, Class<MODEL> modelType) {
        if (presentationType == Set.class && modelType == List.class) {
            return (Converter<PRESENTATION, MODEL>) new ListToSetConverter();
        }
        if (presentationType == Boolean.class && modelType == Integer.class) {
            return (Converter<PRESENTATION, MODEL>) new IntToBooleanConverter();
        }
        if (presentationType == Boolean.class && modelType == String.class) {
            return (Converter<PRESENTATION, MODEL>) new StringToBooleanConverter();
        }
        // Let default factory handle the rest
        return super.findConverter(presentationType, modelType);
    }
}
