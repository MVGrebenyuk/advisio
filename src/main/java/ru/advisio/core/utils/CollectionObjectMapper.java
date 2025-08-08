package ru.advisio.core.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CollectionObjectMapper<E, T> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        objectMapper.registerModule(new JavaTimeModule());
    }

    public E convertValue(Object fromValue, Class<E> toValueType) throws IllegalArgumentException {
        return objectMapper.convertValue(fromValue, toValueType);
    }

    public Collection<E> convertCollection(Collection<T> source, Class<E> target){
        var retCollection = new ArrayList<E>();

        if(source.isEmpty()){
            log.warn("Collection for cast is empty");
            return retCollection;
        }

        source.forEach(src -> {
            var trgt = safeConvert(target, src);
            retCollection.add(trgt);
        });

        return retCollection;
    }

    public Set<E> convertCollection(Set<T> source, Class<E> target){
        Set retCollection = new HashSet<>();

        if(source.isEmpty()){
            log.warn("Collection for cast is empty");
            return retCollection;
        }

        source.forEach(src -> {
            var trgt = safeConvert(target, src);
            retCollection.add(trgt);
        });

        return retCollection;
    }

    private E safeConvert(Class<E> target, T src) {
        E trgt;
        try {
            trgt = objectMapper.convertValue(src, target);
        } catch (Exception e){
            e.printStackTrace();
            log.error("Cannot cast {} to {}", src, target);
            throw new ClassCastException();
        }
        return trgt;
    }

}
