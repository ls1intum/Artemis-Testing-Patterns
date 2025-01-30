package de.tum.cit.aet;

import de.tum.in.test.api.jupiter.PublicTest;
import de.tum.in.test.api.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static de.tum.cit.aet.EnvironmentBehaviorTest.*;
import static de.tum.cit.aet.Utils.*;
import static de.tum.in.test.api.util.ReflectionTestUtils.getMethod;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class EnvironmentAggregationBehaviorTest {

    private final Object environmentAggregationObject = ReflectionTestUtils.newInstance(environmentAggregationConstructor,
            new HashSet<>());

    private final Method calculateMaxAvgMethod = getMethod(environmentAggregationObject, Constants.environmentAggregationComputeMaxAverage());

    @PublicTest
    void calculateMaxAvgEnvironmentAggregationWithZeroComponents(){
        System.out.println(ReflectionTestUtils.invokeMethod(environmentAggregationObject, calculateMaxAvgMethod));
        assertEquals(ReflectionTestUtils.invokeMethod(environmentAggregationObject, calculateMaxAvgMethod), 0.0,
                "Case of zero " +  Constants.environmentObject() + " not handled correctly");
    }

    @PublicTest
    void calculateMaxAvgEnvironmentAggregationTest(){
        Tuple<Object, Double> tuple = createEnvironmentObjectsWithMax();
        var res = Math.abs(tuple.second - (double) ReflectionTestUtils.invokeMethod(tuple.first, calculateMaxAvgMethod));

        if(res > EPSILON){
            fail("The " + Constants.environmentAggregationComputeMaxAverage() + " method is not implemented correctly or the values are not equal"
                    + " within the specified epsilon" );
        }
    }

    @PublicTest
    void calculateMaxAvgEnvironmentAggregationComponentBeingZeroTest(){
        var environmentObject = ReflectionTestUtils.newInstance(environmentConstructor, INITIAL_LEVEL, new ArrayList<>(List.of(createComponent(-0.25))));

        var environmentSecondObject = ReflectionTestUtils.newInstance(environmentConstructor, INITIAL_LEVEL, new ArrayList<>(List.of(createComponent(-1),
                createComponent(1))));

        var environmentAggregation = ReflectionTestUtils.newInstance(environmentAggregationConstructor, new HashSet<>(List.of(
                environmentObject, environmentSecondObject
        )));

        if( (double) ReflectionTestUtils.invokeMethod(environmentAggregation, calculateMaxAvgMethod) != 0){
            fail("The " + Constants.environmentAggregationComputeMaxAverage() + " method is not implemented correctly when the max is 0");
        }
    }
}
