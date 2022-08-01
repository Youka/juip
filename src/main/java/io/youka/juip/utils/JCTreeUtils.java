package io.youka.juip.utils;

import com.sun.source.tree.BlockTree;
import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

public class JCTreeUtils {
    private final TreeMaker treeMaker;
    private final Names names;

    public JCTreeUtils(JavacTask javacTask) {
        if (!(javacTask instanceof BasicJavacTask)) {
            throw new IllegalArgumentException("JavacTask isn't an instance of BasicJavacTask!");
        }
        final Context context = ((BasicJavacTask) javacTask).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }

    public void addPrint(BlockTree block, String message) {
        this.addPrint(block, message, false);
    }
    public void addPrint(BlockTree block, String message, boolean prepend) {
        // Build new statement
        final JCTree.JCExpression methodSelect = this.treeMaker.Select(
                this.treeMaker.Select(
                        this.treeMaker.Ident(this.names.fromString("System")),
                        this.names.fromString("out")),
                this.names.fromString("println")
        );
        final List<JCTree.JCExpression> methodArguments = List.of(this.treeMaker.Literal(message));
        final JCTree.JCMethodInvocation methodInvocation = this.treeMaker.Apply(List.nil(), methodSelect, methodArguments);
        final JCTree.JCExpressionStatement expressionStatement = this.treeMaker.Exec(methodInvocation);

        // Insert statement into block
        if (!(block instanceof JCTree.JCBlock)) {
            throw new IllegalArgumentException("BlockTree isn't an instance of JCBlock!");
        }
        final JCTree.JCBlock jcBlock = (JCTree.JCBlock) block;
        jcBlock.stats = prepend ? jcBlock.stats.prepend(expressionStatement) : jcBlock.stats.append(expressionStatement);
    }
}
