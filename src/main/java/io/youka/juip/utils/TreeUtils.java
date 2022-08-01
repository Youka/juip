package io.youka.juip.utils;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;

import java.util.Optional;

public class TreeUtils {
    private final CompilationUnitTree compilationUnit;

    public TreeUtils(CompilationUnitTree compilationUnit) {
        this.compilationUnit = compilationUnit;
    }

    public boolean isImported(Class<?> clazz) {
        return this.isImported(clazz, false);
    }
    public boolean isImported(Class<?> clazz, boolean isStatic) {
        final ClassName className = new ClassName(clazz);
        // Lookup imports
        for (ImportTree import_ : this.compilationUnit.getImports()) {
            if (import_.isStatic() == isStatic) {
                final ClassName importClassName = new ClassName(import_.getQualifiedIdentifier().toString());
                // Check fully qualified import
                if (importClassName.equals(className)) {
                    return true;
                }
                // Check wildcard import
                if (className.getPackageName().equals(importClassName.getPackageName()) && importClassName.getSimpleName().equals("*")) {
                    return true;
                }
            }
        }
        // Check default scope
        if (this.compilationUnit.getPackageName().toString().equals(className.getPackageName())) {
            return true;
        }
        // All checks failed
        return false;
    }

    public Optional<? extends AnnotationTree> getAnnotation(MethodTree method, Class<?> annotationClass) {
        return method.getModifiers()
                .getAnnotations()
                .stream()
                .filter(annotation -> {
                    final Tree annotationType = annotation.getAnnotationType();
                    final Tree.Kind treeKind = annotationType.getKind();
                    final String annotationTypeName = annotationType.toString();
                    return treeKind == Tree.Kind.MEMBER_SELECT && annotationTypeName.equals(annotationClass.getName()) ||
                            treeKind == Tree.Kind.IDENTIFIER && annotationTypeName.equals(annotationClass.getSimpleName()) && this.isImported(annotationClass);
                })
                .findFirst();
    }
}
