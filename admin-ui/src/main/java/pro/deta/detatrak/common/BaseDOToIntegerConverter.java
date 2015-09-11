package pro.deta.detatrak.common;

import java.util.Locale;

import com.vaadin.data.Container;
import com.vaadin.data.util.converter.Converter;

import pro.deta.detatrak.util.JPAUtils;
import ru.yar.vi.rm.data.BaseDO;

public class BaseDOToIntegerConverter implements Converter<Object, BaseDO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7795326454644256178L;
	private Container container = null;
	
	public BaseDOToIntegerConverter(Container container) {
		this.container = container;
	}

	@Override
	public BaseDO convertToModel(Object value, Class<? extends BaseDO> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		return JPAUtils.getBeanByItem(container.getItem(value));
	}

	@Override
	public Object convertToPresentation(BaseDO value, Class<? extends Object> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if(value != null)
			return value.getId();
		return null;
	}

	@Override
	public Class<BaseDO> getModelType() {
		return BaseDO.class;
	}

	@Override
	public Class<Object> getPresentationType() {
		return Object.class;
	}

}
