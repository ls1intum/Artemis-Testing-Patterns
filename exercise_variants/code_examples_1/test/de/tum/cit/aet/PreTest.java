package de.tum.cit.aet;

import com.github.javaparser.ParserConfiguration;
import de.tum.in.test.api.ast.type.LoopType;
import de.tum.in.test.api.ast.asserting.UnwantedNodesAssert;

import java.util.Optional;

@SuppressWarnings("OptionalAssignedToNull")
// @M01E02
public class PreTest {

    public static Optional<AssertionError> noLoopException = null;


    public static void noLoop() throws AssertionError {
        if(noLoopException == null) {
            try {
                UnwantedNodesAssert
                        .assertThatProjectSources()
                        .withinPackage("de.tum.cit.ase")
                        .withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17)
                        .hasNo(LoopType.ANY);
                noLoopException = Optional.empty();
            } catch (AssertionError ae){
                noLoopException = Optional.of(ae);
                throw noLoopException.get();
            }
        } else {
            if (noLoopException.isPresent()) {
                throw noLoopException.get();
            }
        }
    }

}
