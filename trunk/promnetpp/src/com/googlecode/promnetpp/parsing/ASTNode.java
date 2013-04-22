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

import java.util.HashMap;
import java.util.Map;

/**
 * Instances of the <code>ASTNode</code> class represent a node in the abstract
 * syntax tree, obtained by the parser generated by JavaCC. The
 * <code>ASTNode</code> class overrides (and extends from) the SimpleNode class
 * for better maintainability and convenience.
 *
 * @author Miguel Martins
 */
public class ASTNode extends SimpleNode {

    /**
     * A name associated with this node, <strong>not to be confused with the
     * node's name</strong>
     */
    protected String name;
    /**
     * Name of a type associated with this node; typically used when the node
     * is a variable declaration node, where this field corresponds to the
     * variable's type (such as "byte" or "int")
     */
    protected String typeName;
    /**
     * Values for this node; contains additional information about the node, and
     * is dependant on the node's type
     */
    protected Map<String, Object> values;

    /**
     * Single-argument constructor, as per the specification of the
     * <code>SimpleNode</code> class.
     * 
     * @param i the id of the node
     * @see SimpleNode#SimpleNode(int) 
     */
    public ASTNode(int i) {
        super(i);
        values = new HashMap<String, Object>();
    }

    /**
     * Double-argument constructor, as per the specification of the
     * <code>SimpleNode</code> class.
     * 
     * @param p the parser associated with this node
     * @param i the id of the node
     * @see SimpleNode#SimpleNode(com.googlecode.promnetpp.parsing.PROMELAParser, int) 
     */
    public ASTNode(PROMELAParser p, int i) {
        super(p, i);
        values = new HashMap<String, Object>();
    }

    /**
     * Gets this node's name. Useful to distinguish between the various nodes
     * in the abstract syntax tree.
     * 
     * @return this node's name.
     */
    public String getNodeName() {
        return PROMELAParserTreeConstants.jjtNodeName[id];
    }

    /**
     * Gets the name associated with this node. <strong>Not to be confused with
     * the name of the node itself.</strong>
     * 
     * @return the name associated with this node.
     * @see ASTNode#name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets this node's type name.
     * 
     * @return this node's type name.
     * @see ASTNode#typeName
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Gets this node's value. <code>value</code> refers to a field in the
     * <code>SimpleNode<code> class, commonly used to store additional
     * information about a node.
     * 
     * @return this node's value.
     * @see SimpleNode#value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Same as <code>getValue()</code>, but casts the value to a string. If the
     * value cannot be cast to a string, an AssertionError is implicitly thrown.
     * 
     * @return this node's value, as a string.
     * @see SimpleNode#value
     */
    public String getValueAsString() {
        assert value instanceof String;
        return (String) value;
    }

    /**
     * Gets this node's value map.
     * 
     * @return this node's value map.
     */
    public Map<String, Object> getValues() {
        return values;
    }

    /**
     * Retrieves a particular value from this node, given a specific key.
     * 
     * @param key the intended value's key
     * @return the intended value, or {@code null} if there is no value
     * associated with the specified key.
     */
    public Object getValue(String key) {
        return values.get(key);
    }

    /**
     * Same as <code>getValue(String key)</code>, but the value is returned as
     * a <code>Boolean</code> object. Null values are treated as
     * <code>Boolean</code> objects whose boolean value is <code>false</code>.
     * 
     * @param key the intended value's key
     * @return the intended value, as a <code>Boolean</code> object.
     */
    public Boolean getValueAsBoolean(String key) {
        Object value = values.get(key);
        assert value == null || value instanceof Boolean;
        if (value == null) {
            return false;
        }
        return (Boolean) value;
    }
    
    /**
     * Same as <code>getValue(String key)</code>, but the value is returned as
     * a string. If the value cannot be cast to a <code>String</code> object,
     * an implicit AssertionError is thrown.
     * 
     * @param key the intended value's key
     * @return the intended value, as a string, or null if there is no value
     * associated with the given key.
     */
    public String getValueAsString(String key) {
        Object value = values.get(key);
        if (value == null) {
            return null;
        }
        assert value instanceof String;
        return (String) value;
    }

    /**
     * Inserts (puts) a value into this node's value map.
     * 
     * @param key a unique string, identifying the value
     * @param value the value to be inserted, along with its key
     * @see ASTNode#values
     */
    public void putValue(String key, Object value) {
        values.put(key, value);
    }

    /**
     * Tests the equality between this node and any other node. For two nodes to
     * be considered "equal", they must, first and foremost, be instances of the
     * same class (ASTNode). Secondly, they must be equal in regard to their
     * names, their id's, and their values. Finally, they must contain the exact
     * same number of child nodes, and those child nodes must be equal as well.
     * 
     * @param obj any given object; ideally, an instance of this class
     * @return {@code false} if at least one of the equality conditions are not
     * met; {@code true} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ASTNode)) {
            return false;
        } else {
            if (obj == this) {
                return true;
            }
            ASTNode otherNode = (ASTNode) obj;
            //Check for name equality
            boolean nameEquals = this.name == null ? otherNode.name == null
                    : this.name.equals(otherNode.name);
            if (!nameEquals) {
                return false;
            }
            //Check for number of values
            boolean haveSameNumberOfValues = this.getValues().size()
                    == otherNode.getValues().size();
            if (!haveSameNumberOfValues) {
                return false;
            }
            //Check for same id and value
            if (this.id != otherNode.id) {
                return false;
            }
            boolean haveSameValue = this.value == null ? otherNode.value == null
                    : this.value.equals(otherNode.value);
            if (!haveSameValue) {
                return false;
            }
            /*
             * Check for children equality (number of children, and the
             * children themselves)
             */
            boolean haveSameNumberOfChildren = this.jjtGetNumChildren()
                    == otherNode.jjtGetNumChildren();
            if (!haveSameNumberOfChildren) {
                return false;
            }

            for (int i = 0; i < this.jjtGetNumChildren(); ++i) {
                ASTNode thisNodeChild = (ASTNode) this.jjtGetChild(i);
                ASTNode otherNodeChild = (ASTNode) otherNode.jjtGetChild(i);
                if (!thisNodeChild.equals(otherNodeChild)) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 29 * hash + (this.values != null ? this.values.hashCode() : 0);
        return hash;
    }
    
    public boolean hasNoChildren() {
        return jjtGetNumChildren() == 0;
    }
    
    public boolean hasSingleChild() {
        return jjtGetNumChildren() == 1;
    }
    
    public boolean hasMultipleChildren() {
        return jjtGetNumChildren() > 1;
    }
    
    public ASTNode getFirstChild() {
        return (ASTNode) jjtGetChild(0);
    }
    
    public ASTNode getSecondChild() {
        return (ASTNode) jjtGetChild(1);
    }
    
    public String toCppVariableName() {
        String result = name;
        for (int i = 0; i < jjtGetNumChildren(); ++i) {
            ASTNode child = (ASTNode) jjtGetChild(i);
            if (child.getNodeName().equals("Variable")) {
                result += "." + child.name;
            }
        }
        return result;
    }

    public String toCppExpression() {
        String result = getExpressionAsString(this);
        return result;
    }

    private String getExpressionAsString(ASTNode expression) {
        ASTNode firstTerm = expression.getFirstChild();
        String result = getTermAsString(firstTerm);
        int numberOfTerms = (Integer) expression.getValue("numberOfTerms");
        if (numberOfTerms == 2) {
            ASTNode secondTerm = expression.getSecondChild();
            String operator = expression.getValueAsString("operator");
            result += " " + operator + " " + getTermAsString(secondTerm);
        }
        return result;
    }

    private String getTermAsString(ASTNode term) {
        ASTNode firstFactor = term.getFirstChild();
        String result = getFactorAsString(firstFactor);
        return result;
    }

    private String getFactorAsString(ASTNode factor) {
        String factorType = factor.getValueAsString("factorType");
        String factorValue = factor.getValueAsString("factorValue");
        if (factorType.equals("expressionParentheses")) {
            ASTNode expression = factor.getFirstChild();
            return "(" + getFactorAsString(expression) + ")";
        } else if (factorType.equals("integerLiteral")) {
            return factorValue;
        } else if (factorType.equals("stringLiteral")) {
            return factorValue;
        } else if (factorType.equals("booleanConstant")) {
            return factorValue;
        } else if (factorType.equals("timeout")) {
            return factorValue;
        } else if (factorType.equals("functionCall")) {
            //TODO Handle function calls
            return "function()";
        } else if (factorType.equals("variable")) {
            ASTNode variable = factor.getFirstChild();
            return variable.toCppVariableName();
        } else {
            return "";
        }
    }
}