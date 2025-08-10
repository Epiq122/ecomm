package ca.robertgleason.ecommbe.utilties;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MappingUtils {

    private final ModelMapper modelMapper;

    public MappingUtils(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source.stream()
                .map(item -> modelMapper.map(item, targetClass))
                .toList();
    }
}
