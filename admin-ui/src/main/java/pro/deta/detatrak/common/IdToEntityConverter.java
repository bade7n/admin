package pro.deta.detatrak.common;

import com.vaadin.data.util.converter.Converter;
import ru.yar.vi.rm.data.BaseDO;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class IdToEntityConverter<T> implements Converter<BaseDO, Integer> {

    private Map<Integer, BaseDO> dataSet = new HashMap<Integer, BaseDO>();

    public IdToEntityConverter(Collection<? extends BaseDO> collection) {
        for(BaseDO d : collection) {
            if(!dataSet.containsKey(d.getId())) {
                dataSet.put(d.getId(), d);
            } else {
                //TODO: ошибка в базе - повторяющиеся значения айдишники не уникальны
                continue;
                //throw new RuntimeException("Повторяющиеся значения в источнике данных для комбо-бокса!");
            }
        }
    }

    @Override
    public Integer convertToModel(BaseDO baseDO, Class<? extends Integer> aClass, Locale locale) throws ConversionException {
        return baseDO == null ? null : baseDO.getId();
    }

    @Override
    public BaseDO convertToPresentation(Integer integer, Class<? extends BaseDO> aClass, Locale locale) throws ConversionException {
        return dataSet.get(integer);
    }

    @Override
    public Class<Integer> getModelType() {
        return Integer.class;
    }

    @Override
    public Class<BaseDO> getPresentationType() {
        return BaseDO.class;
    }
}
