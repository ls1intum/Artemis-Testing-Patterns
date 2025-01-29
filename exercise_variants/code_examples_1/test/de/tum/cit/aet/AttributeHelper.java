package de.tum.cit.aet;

import de.tum.in.test.api.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;

import static org.junit.jupiter.api.Assertions.fail;

@M01E02
public class AttributeHelper {

    @SuppressWarnings("unchecked")
    public static <T> T checkAttributeType(String clazzName, String attributeName, Class<T> objectDatatype, Object object) {
        if (object == null) {
            return null;
        }
        if (!objectDatatype.isInstance(object))
            fail("Attribute " + attributeName + " of class " + clazzName + " must be from type " + objectDatatype.getSimpleName());
        return (T) object;
    }

    // Markus: Why don't we use de.tum.in.test.api.util.ReflectionTestUtils.valueForAttribute(java.lang.Object, java.lang.String) or de.tum.in.test.api.util.ReflectionTestUtils.valueForNonPublicAttribute instead of getAttribute?
    public static <T> T getAttribute(Class<?> clazz, String attributeName, Object instance, Class<T> expectedType) {
        try {
            Field field = clazz.getDeclaredField(attributeName);
            field.setAccessible(true);
            Object value = field.get(instance);
            field.setAccessible(false);
            return checkAttributeType(clazz.getSimpleName(), attributeName, expectedType, value);
        } catch (NoSuchFieldException e) {
            fail("Expected field " + attributeName + " of class " + clazz.getSimpleName() + " does not exist.");
        } catch (InaccessibleObjectException e) {
            fail("Attribute " + attributeName + " of class " + clazz.getSimpleName() + " is not accessible.");
        } catch (IllegalAccessException e) {
            fail("Internal Test Error: ", e);
        }
        return null;
    }

    // Markus: Why don't we use de.tum.in.test.api.util.ReflectionTestUtils.setValueOfAttribute(java.lang.Object, java.lang.String, java.lang.Object) / de.tum.in.test.api.util.ReflectionTestUtils.setValueOfNonPublicAttribute instead of setAttribute?
    public static void setAttribute(Class<?> clazz, String attributeName, Object instance, Object value) {
        try {
            Field field = clazz.getDeclaredField(attributeName);
            field.setAccessible(true);
            field.set(instance, value);
            field.setAccessible(false);
        } catch (NoSuchFieldException e) {
            fail("Expected field " + attributeName + " of class " + clazz.getSimpleName() + " does not exist.");
        } catch (InaccessibleObjectException e) {
            fail("Attribute " + attributeName + " of class " + clazz.getSimpleName() + " is not accessible.");
        } catch (IllegalAccessException e) {
            fail("Internal Test Error: ", e);
        }
    }
}
