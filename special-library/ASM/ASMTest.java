package de.tum.cit.ase;

import com.github.javaparser.ParserConfiguration;
import com.tngtech.archunit.thirdparty.org.objectweb.asm.ClassReader;
import com.tngtech.archunit.thirdparty.org.objectweb.asm.ClassVisitor;
import com.tngtech.archunit.thirdparty.org.objectweb.asm.MethodVisitor;
import com.tngtech.archunit.thirdparty.org.objectweb.asm.Opcodes;
import de.tum.in.test.api.BlacklistPath;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.ast.asserting.UnwantedNodesAssert;
import de.tum.in.test.api.ast.type.LoopType;
import de.tum.in.test.api.ast.type.*;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.jupiter.PublicTest;
import de.tum.in.test.api.util.ReflectionTestUtils;
import org.junit.jupiter.api.BeforeEach;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;

@Public
@T08E02
@WhitelistPath("target") // mainly for Artemis
@BlacklistPath("target/test-classes") // prevent access to test-related classes and resources
public class ASMTest {
    private final Class<?> baggageControlUnitClass = ReflectionTestUtils.getClazz("de.tum.cit.ase.BaggageControlUnit");
    private final Class<?> baggageClass = ReflectionTestUtils.getClazz("de.tum.cit.ase.Baggage");
    private final Class<?> flightClass = ReflectionTestUtils.getClazz("de.tum.cit.ase.Flight");
    private final Class<?> countryClass = ReflectionTestUtils.getClazz("de.tum.cit.ase.Country");

    private final Constructor<?> baggageConstructor = ReflectionTestUtils.getConstructor(baggageClass, double.class, flightClass);
    private final Constructor<?> flightConstructor = ReflectionTestUtils.getConstructor(flightClass, countryClass, countryClass);
    private final Method valueOf = ReflectionTestUtils.getMethod(countryClass, "valueOf", String.class);
    private final Object germany = ReflectionTestUtils.invokeMethod(countryClass, valueOf, "GERMANY");
    private final Object argentina = ReflectionTestUtils.invokeMethod(countryClass, valueOf, "ARGENTINA");
    private final Object france = ReflectionTestUtils.invokeMethod(countryClass, valueOf, "FRANCE");
    private final Object flight0 = ReflectionTestUtils.newInstance(flightConstructor, germany, argentina);
    private final Object flight1 = ReflectionTestUtils.newInstance(flightConstructor, argentina, germany);
    private final Object flight2 = ReflectionTestUtils.newInstance(flightConstructor, france, germany);
    private final UUID flight0Id = (UUID) ReflectionTestUtils.valueForNonPublicAttribute(flight0, "id");
    private final UUID flight1Id = (UUID) ReflectionTestUtils.valueForNonPublicAttribute(flight1, "id");
    private final UUID flight2Id = (UUID) ReflectionTestUtils.valueForNonPublicAttribute(flight2, "id");
    private final Object baggage0 = ReflectionTestUtils.newInstance(baggageConstructor, 50.0, flight0);
    private final Object baggage1 = ReflectionTestUtils.newInstance(baggageConstructor, 120.0, flight1);
    private final Object baggage2 = ReflectionTestUtils.newInstance(baggageConstructor, 80.0, flight2);
    private final Object baggage3 = ReflectionTestUtils.newInstance(baggageConstructor, 90.0, flight2);
    private final ArrayList<Object> arrayBaggage = new ArrayList<>(Arrays.asList(baggage0, baggage1, baggage2, baggage3));
    private final ArrayList<Object> arrayBaggageEmpty = new ArrayList<>();


    @BeforeEach
    void checkForLoops() {
        UnwantedNodesAssert.assertThatProjectSources().withinPackage("de.tum.cit.ase").
                withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.ANY_WHILE);
        UnwantedNodesAssert.assertThatProjectSources().withinPackage("de.tum.cit.ase").
                withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17).hasNo(LoopType.FORSTMT);
    }

    @BeforeEach
    void checkForbiddenMethods() {
        String classFileName = "/" + baggageControlUnitClass.getName().replace('.', '/') + ".class";
        InputStream classStream = baggageControlUnitClass.getResourceAsStream(classFileName);
        if (classStream == null) {
            throw new IllegalStateException("Class file not found: " + classFileName);
        }

        try {
            // Analyze bytecode using ASM
            ClassReader classReader = new ClassReader(classStream);
            classReader.accept(new ClassVisitor(Opcodes.ASM9) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                    return new MethodVisitor(Opcodes.ASM9) {
                        @Override
                        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                            if ((owner.contains("java/util/stream/Stream") && (name.equals("forEach") || name.equals("peek"))) ||
                                    (owner.contains("java/lang/Iterable") && name.equals("forEach"))) {
                                throw new AssertionError("Forbidden usage of " + name + " detected in method: " + name);
                            }
                            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                        }
                    };
                }
            }, 0);
        } catch (AssertionError e) {
            throw e; // Fail the test if any forbidden method is used
        } catch (Exception e) {
            fail("An error occurred while analyzing the class for forbidden instructions.", e);
        }

        System.out.println("All methods passed forbidden method check.");
    }


    @PublicTest
    void testCountBaggages() {

        Method countBaggage = ReflectionTestUtils.getMethod(baggageControlUnitClass, "countBaggages", Stream.class);
        Long countBaggageReturn = (Long) ReflectionTestUtils.invokeMethod(baggageControlUnitClass, countBaggage, arrayBaggage.stream());
        if (countBaggageReturn != arrayBaggage.size()) {
            fail("After invoking the method 'countBaggages' on a stream containing " + arrayBaggage.size() + " baggages, " + countBaggageReturn + " was returned as baggages count.");
        }
        countBaggageReturn = (Long) ReflectionTestUtils.invokeMethod(baggageControlUnitClass, countBaggage, arrayBaggageEmpty.stream());
        if (countBaggageReturn != arrayBaggageEmpty.size()) {
            fail("After invoking the method 'countBaggages' on a stream containing " + arrayBaggageEmpty.size() + " baggages, " + countBaggageReturn + " was returned as baggages count.");
        }
    }

    @PublicTest
    void testCountBaggagesForFlight() {
        Method countBaggagesForFlight = ReflectionTestUtils.getMethod(baggageControlUnitClass, "countBaggagesForFlight", Stream.class, UUID.class);
        Long countBaggageForFlightReturn = (Long) ReflectionTestUtils.invokeMethod(baggageControlUnitClass, countBaggagesForFlight, arrayBaggage.stream(), flight2Id);
        long expectedBaggageStreamCountBaggagesFlightId2 = 2;
        if (countBaggageForFlightReturn != expectedBaggageStreamCountBaggagesFlightId2) {
            fail("After invoking the method 'countBaggagesForFlight' on a stream containing " + expectedBaggageStreamCountBaggagesFlightId2 + " baggages for the input flight id, but " + countBaggageForFlightReturn + " was returned as baggages count.");
        }
        countBaggageForFlightReturn = (Long) ReflectionTestUtils.invokeMethod(baggageControlUnitClass, countBaggagesForFlight, arrayBaggageEmpty.stream(), flight2Id);
        if (countBaggageForFlightReturn != 0) {
            fail("After invoking the method 'countBaggagesForFlight' on a stream containing " + arrayBaggageEmpty.size() + " baggages for the input flight id, but " + countBaggageForFlightReturn + " was returned as baggages count.");
        }
    }

    @PublicTest
    void testSumWeight() {
        Method sumWeighMethod = ReflectionTestUtils.getMethod(baggageControlUnitClass, "sumWeight", Stream.class);
        double sumWeighReturn = (double) ReflectionTestUtils.invokeMethod(baggageControlUnitClass, sumWeighMethod, arrayBaggage.stream());
        double expectedSumWeigh = 340.0;
        if (sumWeighReturn != expectedSumWeigh) {
            fail("After invoking the method 'sumWeight' on a stream containing baggages of a total weight of " + expectedSumWeigh + ", " + sumWeighReturn + " was returned as the total weight.");
        }
        sumWeighReturn = (double) ReflectionTestUtils.invokeMethod(baggageControlUnitClass, sumWeighMethod, arrayBaggageEmpty.stream());
        if (sumWeighReturn != 0.0) {
            fail("After invoking the method 'sumWeight' on a stream containing baggages of a total weight of " + 0.0 + ", " + sumWeighReturn + " was returned as the total weight.");
        }
    }

    @PublicTest
    void testAverageWeight() {
        Method averageWeightMethod = ReflectionTestUtils.getMethod(baggageControlUnitClass, "averageWeight", Stream.class);
        double averageWeightReturn = (double) ReflectionTestUtils.invokeMethod(baggageControlUnitClass, averageWeightMethod, arrayBaggage.stream());
        double expectedAverageWeight = 85.0;
        if (averageWeightReturn != expectedAverageWeight) {
            fail("After invoking the method 'averageWeight' on a stream with an average weight of " + expectedAverageWeight + ", " + averageWeightReturn + " was returned as average weight.");
        }
        averageWeightReturn = (double) ReflectionTestUtils.invokeMethod(baggageControlUnitClass, averageWeightMethod, arrayBaggageEmpty.stream());
        if (averageWeightReturn != -1.0) {
            fail("After invoking the method 'averageWeight' on an empty stream " + averageWeightReturn + " was returned as average weight, but should be -1.0.");
        }
    }

    @PublicTest
    void testOverweightBaggages() {
        Method overweightBaggages = ReflectionTestUtils.getMethod(baggageControlUnitClass, "overweightBaggages", Stream.class, double.class);
        double maxWeight = 80.0;
        ReflectionTestUtils.invokeMethod(baggageControlUnitClass, overweightBaggages, arrayBaggage.stream(), maxWeight);
        boolean isOverWeight0 = (boolean) ReflectionTestUtils.valueForNonPublicAttribute(baggage0, "overweight");
        boolean isOverWeight1 = (boolean) ReflectionTestUtils.valueForNonPublicAttribute(baggage1, "overweight");
        boolean isOverWeight2 = (boolean) ReflectionTestUtils.valueForNonPublicAttribute(baggage2, "overweight");
        boolean isOverWeight3 = (boolean) ReflectionTestUtils.valueForNonPublicAttribute(baggage3, "overweight");
        if (isOverWeight0 || !isOverWeight1 || isOverWeight2 || !isOverWeight3) {
            fail("After invoking the 'overweightBaggages' method the 'overweight' attribute was not updated correctly.");
        }

    }

    @PublicTest
    void testBaggageCountPerFlight() {
        Method baggageCountPerFlight = ReflectionTestUtils.getMethod(baggageControlUnitClass, "baggageCountPerFlight", Stream.class);
        Object baggageCountPerFlightReturn = ReflectionTestUtils.invokeMethod(baggageControlUnitClass, baggageCountPerFlight, arrayBaggage.stream());
        if (baggageCountPerFlightReturn instanceof Map<?, ?> map) {
            if (map.size() != 3) {
                fail("After invoking the 'baggageCountPerFlight' the size of the returned was " + map.size() + " but should be 3.");
            }
            if(map.get(flight2Id) == null|| map.get(flight0Id) == null|| map.get(flight1Id) == null){
                fail("After invoking the method 'BaggageCountPerFlight' the UUID keys of the Map won't fit all the flights UUIDs.");
            }
            if(map.get(flight0Id) instanceof Long l0 && map.get(flight1Id) instanceof Long l1 && map.get(flight2Id) instanceof Long l2){
                if(l0 != 1 || l1 != 1|| l2 != 2){
                    fail("After invoking the method 'BaggageCountPerFlight' the number of the baggages per flight was incorrect.");
                }
            }else{
                fail("Something is wrong with the return type Map values of 'baggageCountPerFlight' method.");
            }

        } else {
            fail("Something is wrong with the return type of 'baggageCountPerFlight' method.");
        }
    }

    @PublicTest
    void testGroupByDestination() {
        Method groupByDestination = ReflectionTestUtils.getMethod(baggageControlUnitClass, "groupByDestination", Stream.class);
        Object groupByDestinationReturn = ReflectionTestUtils.invokeMethod(baggageControlUnitClass, groupByDestination, arrayBaggage.stream());
        if (groupByDestinationReturn instanceof Map<?, ?> map) {
            if (map.size() != 2) {
                fail("After invoking the 'groupByDestination' the size of the returned was " + map.size() + " but should be 2.");
            }
            if(map.get(germany) == null|| map.get(argentina) == null){
                fail("After invoking the method 'BaggageCountPerFlight' the Country keys of the Map won't fit all the flights destination countries.");
            }
            if(map.get(germany) instanceof List<?> l0 && map.get(argentina) instanceof List<?> l1){
                if(l0.size() != 3 || l1.size() != 1){
                    fail("After invoking the method 'groupByDestination' the number of the baggages per flight destination was incorrect.");
                }
            }else{
                fail("Something is wrong with the return type Map values of 'groupByDestination' method.");
            }

        } else {
            fail("Something is wrong with the return type of 'groupByDestination' method.");
        }
    }

}
