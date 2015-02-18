package pro.deta.detatrak.common;

import com.vaadin.data.util.converter.Converter;
import ru.yar.vi.rm.data.BaseDO;

import java.util.*;

public class IdsArrayToEntitySetConverter<T> implements Converter<Set, String[]> {

    private Map<Integer, BaseDO> dataSet = new HashMap<Integer, BaseDO>();

    public IdsArrayToEntitySetConverter(Collection<? extends BaseDO> collection) {
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
    public String[] convertToModel(Set value, Class<? extends String[]> targetType, Locale locale) throws ConversionException {
        if(value == null) return new String[0];
        String[] ids = new String[value.size()];
        int i = 0;
        for (Object o : value){
            if(o == null) continue;
            ids[i] = "" + ((BaseDO)o).getId();
            i++;
        }
        return ids;
    }

    @Override
    public Set convertToPresentation(String[] value, Class<? extends Set> targetType, Locale locale) throws ConversionException {
        if(value == null) return new HashSet();
        Set<BaseDO> set = new HashSet<BaseDO>();
        for (int i = 0; i < value.length; i++){
            set.add(dataSet.get(Integer.parseInt(value[i])));
        }
        return set;
    }

    @Override
    public Class<String[]> getModelType() {
        return String[].class;
    }

    @Override
    public Class<Set> getPresentationType() {
        return Set.class;
    }
}
