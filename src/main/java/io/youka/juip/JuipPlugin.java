package io.youka.juip;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.Plugin;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;
import com.sun.source.util.TreeScanner;
import io.youka.juip.utils.JCTreeUtils;
import io.youka.juip.utils.TreeUtils;

public class JuipPlugin implements Plugin {
    @Override
    public String getName() {
        return "juip";
    }

    @Override
    public void init(JavacTask task, String... args) {
        final JCTreeUtils jcTreeUtils = new JCTreeUtils(task);
        task.addTaskListener(new TaskListener() {
            @Override
            public void started(TaskEvent e) { }
            @Override
            public void finished(TaskEvent e) {
                if (e.getKind() == TaskEvent.Kind.PARSE) {
                    final CompilationUnitTree compilationUnit = e.getCompilationUnit();
                    final TreeUtils treeUtils = new TreeUtils(compilationUnit);
                    final String[] lastClassName = new String[1];
                    compilationUnit.accept(new TreeScanner<Void,Void>() {
                        @Override
                        public Void visitClass(ClassTree clazz, Void additionalData) {
                            lastClassName[0] = clazz.getSimpleName().toString();
                            return super.visitClass(clazz, additionalData);
                        }
                        @Override
                        public Void visitMethod(MethodTree method, Void additionalData) {
                            processMethod(method);
                            return super.visitMethod(method, additionalData);
                        }

                        private void processMethod(MethodTree method) {
                            treeUtils.getAnnotation(method, PrintEnterLeave.class).ifPresent(printEnterLeave -> {
                                final String qualifiedName = String.join(".", compilationUnit.getPackageName().toString(), lastClassName[0], method.getName());
                                jcTreeUtils.addPrint(method.getBody(), "Enter " + qualifiedName, true);
                                jcTreeUtils.addPrint(method.getBody(), "Leave " + qualifiedName);
                            });
                        }
                    }, null);
                }
            }
        });
    }
}
