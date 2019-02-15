package com.example.restful.util;

import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.util.stream.Stream;

/**
 * @author: shudj
 * @time: 2019/2/15 16:43
 * @description:
 */
public class Util {

    public static String[] getNullPropertyNames(Object source) {

        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(source);

        return Stream.of(beanWrapper.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> beanWrapper.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }
}
