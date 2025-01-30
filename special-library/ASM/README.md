# **ASM Test Directory**

## **Purpose of This Directory**

This directory contains test cases designed to analyze Java class bytecode using the ASM library. The primary objective is to ensure that specific programming constructs are used (or avoided) in the implementation of the project. The tests focus on detecting the usage of Java **Streams API methods (**``**, **``**)** and **Iterable's **``** method** while enforcing constraints such as prohibiting the usage of explicit loops (`for`, `while`).

## **Purpose of the **ASMTest** File**

The `ASMTest` file contains unit tests that:

- Validate the correct implementation of methods related to **baggage control and flight management**.
- Ensure compliance with coding constraints, such as avoiding `forEach`, `peek`, and explicit loops.
- Utilize **reflection** and **bytecode inspection** to enforce constraints at a deeper level than regular Java analysis tools.

## **How ASM is Used in This File**

### **What is ASM?**

ASM is a Java bytecode manipulation and analysis framework. It allows reading, modifying, and analyzing compiled Java class files (`.class` files) to extract method calls and verify compliance with expected behaviors.

### **How ASM Works in this specific file**

1. **Loading the Class Bytecode**:

   ```java
   String classFileName = "/" + baggageControlUnitClass.getName().replace('.', '/') + ".class";
   InputStream classStream = baggageControlUnitClass.getResourceAsStream(classFileName);
   ```

    - Retrieves the compiled class file of `BaggageControlUnit`.
    - Converts its name to match the standard classpath format.


2. **Reading and Inspecting the Bytecode**:

   ```java
   ClassReader classReader = new ClassReader(classStream);
   classReader.accept(new ClassVisitor(Opcodes.ASM9) {
   ```

    - Uses `ClassReader` to parse the `.class` file and inspect the bytecode.


3. **Detecting Forbidden Method Calls (**``**, **``**)**:

   ```java
   public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
       if ((owner.contains("java/util/stream/Stream") && (name.equals("forEach") || name.equals("peek"))) ||
           (owner.contains("java/lang/Iterable") && name.equals("forEach"))) {
           throw new AssertionError("Forbidden usage of " + name + " detected in method: " + name);
       }
       super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
   }
   ```

    - Iterates through method calls in the bytecode.
    - If a method call to `Stream.forEach()`, `Stream.peek()`, or `Iterable.forEach()` is found, the test **fails**.

4. **Assertions to Enforce Constraints**:

   ```java
   UnwantedNodesAssert.assertThatProjectSources().withinPackage("de.tum.cit.ase")
       .withLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17)
       .hasNo(LoopType.ANY_WHILE);
   ```

    - Uses AST (Abstract Syntax Tree) assertions to verify that no explicit `while` or `for` loops are used.

## **Conclusion**

This test setup ensures that Java Streams API is used correctly while enforcing coding constraints. By leveraging **ASM for bytecode analysis** and **AST assertions**, the `ASMTest` file provides a rigorous mechanism to enforce Java best practices and project-specific rules.

