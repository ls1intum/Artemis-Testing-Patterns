package de.tum.cit.aet;

import de.tum.in.test.api.util.ReflectionTestUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static de.tum.cit.aet.EnvironmentBehaviorTest.INITIAL_LEVEL;

public class Utils {
    static Class<?> environmentClass = ReflectionTestUtils.getClazz(Constants.environmentClassPath());
    static Class<?> environmentAggregationClass = ReflectionTestUtils.getClazz(Constants.environmentAggregationClassPath());
    static Constructor<?> environmentConstructor = ReflectionTestUtils.getConstructor(environmentClass, String.class,
            List.class);
    static Constructor<?> environmentAggregationConstructor = ReflectionTestUtils.getConstructor(environmentAggregationClass, Set.class);
    static Class<?> componentClass = ReflectionTestUtils.getClazz(Constants.componentClassPath());
    static Constructor<?> componentConstructor = ReflectionTestUtils.getConstructor(componentClass, double.class);
    public static Tuple<Object, Double> createEnvironmentObjectsWithMax(){
        int numEnvironments = 2;
        int numComponents = 2;
        double max = Double.MIN_VALUE;
        Set<Object> environmentSet = new HashSet<>();
        ArrayList<Object> componentList = new ArrayList<>();

        for(int i = 0; i < numEnvironments; ++i) {
            double sum = 0.0;
            for(int j = 0; j < numComponents; ++j) {
                double sensorValue = ((Math.random() * 10) + 33);
                sum += sensorValue;
                componentList.add(createComponent(sensorValue));
            }
            max = Math.max(max, sum / numComponents);
        }

        for(int i = 0; i < numEnvironments; ++i){
            Object house = ReflectionTestUtils.newInstance(environmentConstructor,  INITIAL_LEVEL, new ArrayList<>(List.of(componentList.get(2 * i),
                    componentList.get(2 * i + 1))));

            environmentSet.add(house);
        }

        var environmentAggregation = ReflectionTestUtils.newInstance(environmentAggregationConstructor, environmentSet);
        return new Tuple<>(environmentAggregation, max);
    }

    public static Object createComponent(double componentValue){
        return ReflectionTestUtils.newInstance(componentConstructor ,componentValue);
    }

    public static class Tuple<T, U>{
        public T first;
        public U second;

        public Tuple(T t, U u){
            first = t;
            second = u;
        }
    }
}
