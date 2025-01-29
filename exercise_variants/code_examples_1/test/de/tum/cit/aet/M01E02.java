package de.tum.cit.aet;
import de.tum.in.test.api.*;
import de.tum.in.test.api.jupiter.Public;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Public
@StrictTimeout(10)
@Retention(RUNTIME)
@Target({TYPE, ANNOTATION_TYPE})
@WhitelistPath("target") // mainly for Artemis
@WhitelistClass(AttributeHelper.class)
@BlacklistPath("target/test-classes") // prevent access to test-related classes and resources
@MirrorOutput
public @interface M01E02 {
}
