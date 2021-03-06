/*
 * Copyright (c) 2013, Miguel Martins
 * Use is subject to license terms.
 *
 * This source code file is provided under the MIT License. Full licensing
 * terms should be available in the form of text files. The standard source code
 * distribution provides a LICENSE.txt file which can be consulted for licensing
 * details.
 */
package com.googlecode.promnetpp.parsing;

/**
 * The
 * <code>AbstractSyntaxTree</code> class represents an abstract syntax tree,
 * constructed with the aid of the parser generated by JavaCC.
 *
 * @author Miguel Martins
 */
public class AbstractSyntaxTree {

    /**
     * Root node of the abstract syntax tree
     */
    private ASTNode rootNode;

    /**
     * Constructs the AbstractSyntaxTree object from the root node.
     *
     * @param rootNode the root node of the abstract syntax tree.
     */
    public AbstractSyntaxTree(ASTNode rootNode) {
        this.rootNode = rootNode;
    }

    /**
     * Returns this abstract syntax tree's root node.
     *
     * @return this abstract syntax tree's root node.
     */
    public ASTNode getRootNode() {
        return rootNode;
    }

    /**
     * Prints a textual representation of this abstract syntax tree to the
     * standard output (System.out).
     *
     * @see SimpleNode#dump(java.lang.String)
     */
    public void dump() {
        rootNode.dump("");
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AbstractSyntaxTree)) {
            return false;
        } else {
            if (obj == this) {
                return true;
            }
            AbstractSyntaxTree otherTree = (AbstractSyntaxTree) obj;
            ASTNode thisTreeNode = this.rootNode;
            ASTNode otherTreeNode = otherTree.rootNode;
            return thisTreeNode.equals(otherTreeNode);
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + (this.rootNode != null ? this.rootNode.hashCode() :
                0);
        return hash;
    }
}
