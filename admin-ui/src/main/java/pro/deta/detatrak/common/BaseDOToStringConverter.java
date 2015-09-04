package pro.deta.detatrak.common;

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

import ru.yar.vi.rm.data.BaseDO;

public class BaseDOToStringConverter implements Converter<String, BaseDO> {

	@Override
	public BaseDO convertToModel(String value, Class<? extends BaseDO> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		return null;
	}

	@Override
	public String convertToPresentation(BaseDO value, Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if(value != null)
			return value.getName();
		return null;
	}

	@Override
	public Class<BaseDO> getModelType() {
		return BaseDO.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
