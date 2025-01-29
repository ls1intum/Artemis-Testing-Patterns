package de.tum.cit.aet;

import de.tum.in.test.api.jupiter.PublicTest;
import de.tum.in.test.api.util.ReflectionTestUtils;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static de.tum.in.test.api.util.ReflectionTestUtils.getMethod;
import static org.junit.jupiter.api.Assertions.*;
import static de.tum.cit.aet.Utils.*;

/**
 * @author Stephan Krusche (krusche@in.tum.de)
 * @version 5.1 (11.06.2021)
 */
@M01E02
class EnvironmentBehaviorTest {
    static final double EPSILON = 0.00001;
    static final String INITIAL_LEVEL = Constants.initialControlMode(Constants.variant);

    private final Object environmentObject = ReflectionTestUtils.newInstance(environmentConstructor, INITIAL_LEVEL, new ArrayList<>());

    private final Method changeControlMode = getMethod(environmentObject, Constants.environmentChangeControlMode(Constants.variant), String.class);

    private final Method computeAverageCondition = getMethod(environmentObject, Constants.environmentComputeAverageCondition(Constants.variant));

    private int firstInitialLevel = -1;

    private int secondInitialLevel = -1;

    private ArrayList<Integer> firstSet = new ArrayList<>(List.of(1,2,3));
    private ArrayList<Integer> secondSet = new ArrayList<>(List.of(1,2,3));

    private int numberLastClassObjects = -1;
    private ArrayList<Integer> lastClassSet = new ArrayList<>(List.of(2,3,4));

    @PublicTest
    void changeControlModeTest(){
        int initialLevel = 0;
        int changeLevelTo = 0;
        if(firstInitialLevel == -1){
            initialLevel = (int) (Math.random() * 3) + 1;
            do{
                changeLevelTo = (int) (Math.random() * 3) + 1;
            } while(changeLevelTo == initialLevel);
            firstInitialLevel = initialLevel;
            secondInitialLevel = changeLevelTo;
        } else{
            firstSet.remove(Integer.valueOf(firstInitialLevel));
            secondSet.remove(Integer.valueOf(secondInitialLevel));
            Random random = new Random();
            int firstIndex = random.nextInt(firstSet.size());
            initialLevel = firstSet.get(firstIndex);

            do {
                int secondIndex = random.nextInt(secondSet.size());
                changeLevelTo = secondSet.get(secondIndex);
            } while(initialLevel == changeLevelTo);
        }


        Object environmentObject = ReflectionTestUtils.newInstance(environmentConstructor,  Constants.intToLevel(Constants.variant, initialLevel), new ArrayList<>());
        var result = AttributeHelper.getAttribute(environmentClass, Constants.environmentAttributeName(Constants.variant), environmentObject, String.class);

        if(!result.equals(Constants.intToLevel(Constants.variant, initialLevel))){
            fail("The constructor does not set the initial " + Constants.environmentAttributeName(Constants.variant) + " correctly");
        }

        try{
            ReflectionTestUtils.invokeMethod(environmentObject, changeControlMode, Constants.intToLevel(Constants.variant, changeLevelTo));
        } catch (Exception e){
            fail(Constants.environmentChangeControlMode(Constants.variant) + " throws an exception when it should not have.");
        }
        result = AttributeHelper.getAttribute(environmentClass, Constants.environmentAttributeName(Constants.variant), environmentObject, String.class);

        if(!result.equals(Constants.intToLevel(Constants.variant, changeLevelTo))){
            fail("The method " + Constants.environmentChangeControlMode(Constants.variant) + " does not set the correct " + Constants.environmentAttributeName(Constants.variant));
        }
    }

    @PublicTest
    void changeControlModeSecondTest(){
        int initialLevel = 0;
        int changeLevelTo = 0;
        if(firstInitialLevel == -1){
            initialLevel = (int) (Math.random() * 3);
            do{
                changeLevelTo = (int) (Math.random() * 3);
            } while(changeLevelTo == initialLevel);
            firstInitialLevel = initialLevel;
            secondInitialLevel = changeLevelTo;
        } else{
            firstSet.remove(Integer.valueOf(firstInitialLevel));
            secondSet.remove(Integer.valueOf(secondInitialLevel));
            Random random = new Random();
            int firstIndex = random.nextInt(firstSet.size());
            initialLevel = firstSet.get(firstIndex);

            do {
                int secondIndex = random.nextInt(secondSet.size());
                changeLevelTo = secondSet.get(secondIndex);
            } while(initialLevel == changeLevelTo);
        }

        Object environmentObject = ReflectionTestUtils.newInstance(environmentConstructor,  Constants.intToLevel(Constants.variant, initialLevel), new ArrayList<>());
        var result = AttributeHelper.getAttribute(environmentClass, Constants.environmentAttributeName(Constants.variant), environmentObject, String.class);

        if(!result.equals(Constants.intToLevel(Constants.variant, initialLevel))){
            fail("The constructor does not set the initial " + Constants.environmentAttributeName(Constants.variant) + " correctly");
        }

        try{
            ReflectionTestUtils.invokeMethod(environmentObject, changeControlMode, Constants.intToLevel(Constants.variant, changeLevelTo));
        } catch (Exception e){
            fail(Constants.environmentChangeControlMode(Constants.variant) + " throws an exception when it should not have.");
        }
        result = AttributeHelper.getAttribute(environmentClass, Constants.environmentAttributeName(Constants.variant), environmentObject, String.class);

        if(!result.equals(Constants.intToLevel(Constants.variant, changeLevelTo))){
            fail("The method " + Constants.environmentChangeControlMode(Constants.variant) + " does not set the correct " + Constants.environmentAttributeName(Constants.variant));
        }
    }

    @PublicTest
    void changeControlModeWithOtherStringTest(){
        try {
            changeControlMode.invoke(environmentObject, "should fail");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (Exception e) {
            if (!e.getCause().getClass().equals(IllegalArgumentException.class)) {
                fail("You did not handle the case correctly when a different String was provided to the method.");
            }
        }
    }

    @PublicTest
    void changeControlModeWithNullTest(){
        try {
            changeControlMode.invoke(environmentObject, (Object) null);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (Exception e) {
            if (!e.getCause().getClass().equals(IllegalArgumentException.class)) {
                fail("You forgot to check whether the " + Constants.environmentAttributeName(Constants.variant)+ " of the argument is null.");
            }
        }
    }

    @PublicTest
    void computeAverageConditionWithZeroComponentsTest(){
        if((double) ReflectionTestUtils.invokeMethod(environmentObject, computeAverageCondition) != 0.0){
            fail("You did not handle the case correctly when there are zero of " + Constants.componentObject(Constants.variant)+ ".");
        }
    }

    @PublicTest
    void calculateAvgEnvironmentComponentTest(){
        int amountComponentObjects = 0;
        ArrayList<Object> componentObjectList = new ArrayList<>();
        if(numberLastClassObjects == -1){
            amountComponentObjects = (int) ((Math.random() * 3) + 2);
        } else{
            lastClassSet.remove(Integer.valueOf(amountComponentObjects));
            Random random = new Random();
            int index = random.nextInt(lastClassSet.size());
            amountComponentObjects = lastClassSet.get(index);
        }

        double sum = 0.0;
        for(int i = 0; i < amountComponentObjects; ++i) {
            double sensorValue = (int) ((Math.random() * 10) + 33);
            sum = sum + sensorValue;
            componentObjectList.add(ReflectionTestUtils.newInstance(componentConstructor, sensorValue));
        }

        Object environmentObject = ReflectionTestUtils.newInstance(environmentConstructor, INITIAL_LEVEL, componentObjectList);

        double resultFromStudent = (double) ReflectionTestUtils.invokeMethod(environmentObject,
                computeAverageCondition);

        if(Math.abs((sum / componentObjectList.size()) - resultFromStudent) > EPSILON){
            fail("The " + Constants.environmentComputeAverageCondition(Constants.variant)+ " is not implemented correctly or the values are not equal within " +
                    "the specified range");
        }
    }

    @PublicTest
    void calculateAvgEnvironmentComponentSecondTest(){
        int amountComponentObjects = 0;
        ArrayList<Object> componentList = new ArrayList<>();
        if(numberLastClassObjects == -1){
            amountComponentObjects = (int) ((Math.random() * 3) + 2);
        } else{
            lastClassSet.remove(Integer.valueOf(amountComponentObjects));
            Random random = new Random();
            int index = random.nextInt(lastClassSet.size());
            amountComponentObjects = lastClassSet.get(index);
        }

        double sum = 0.0;
        for(int i = 0; i < amountComponentObjects; ++i) {
            double sensorValue = (int) ((Math.random() * 20) - 10);
            sum = sum + sensorValue;
            componentList.add(ReflectionTestUtils.newInstance(componentConstructor, sensorValue));
        }

        Object middleClassObject = ReflectionTestUtils.newInstance(environmentConstructor, INITIAL_LEVEL, componentList);

        double resultFromStudent = (double) ReflectionTestUtils.invokeMethod(middleClassObject,
                computeAverageCondition);

        if(Math.abs((sum / componentList.size()) - resultFromStudent) > EPSILON){
            fail("The " + Constants.environmentComputeAverageCondition(Constants.variant)+ " is not implemented correctly or the values are not equal within " +
                    "the specified range");
        }
    }
}
