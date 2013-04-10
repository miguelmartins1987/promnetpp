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

public class ASTNode extends SimpleNode {

    protected String name;
    protected String typeName;
    protected Map<String, Object> values;

    public ASTNode(int i) {
        super(i);
        values = new HashMap<String, Object>();
    }

    public ASTNode(PROMELAParser p, int i) {
        super(p, i);
        values = new HashMap<String, Object>();
    }

    public String getNodeName() {
        return PROMELAParserTreeConstants.jjtNodeName[id];
    }

    public String getName() {
        return name;
    }

    public String getTypeName() {
        return typeName;
    }

    public Object getValue() {
        return value;
    }

    public String getValueAsString() {
        assert value instanceof String;
        return (String) value;
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public Object getValue(String key) {
        return values.get(key);
    }

    public Boolean getValueAsBoolean(String key) {
        Object value = values.get(key);
        assert value == null || value instanceof Boolean;
        if (value == null) {
            return false;
        }
        return (Boolean) value;
    }
    
    public String getValueAsString(String key) {
        Object value = values.get(key);
        assert value instanceof String;
        return (String) value;
    }

    public void putValue(String key, Object value) {
        values.put(key, value);
    }

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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 29 * hash + (this.values != null ? this.values.hashCode() : 0);
        return hash;
    }
}