/*
 * Copyright (c) 2013, Miguel Martins
 * Use is subject to license terms.
 *
 * This source code file is provided under the MIT License. Full licensing
 * terms should be available in the form of text files. The standard source code
 * distribution provides a LICENSE.txt file which can be consulted for licensing
 * details.
 */
package com.googlecode.promnetpp.utilities;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Miguel Martins
 */
public abstract class IndentedWriter extends Writer {
    protected Writer writer;
    //Numbers per indentation
    protected int numberOfSpacesPerIndentation;
    protected int numberOfTabsPerIndentation;
    //Current indentation
    protected int indentationLevel;
    protected int currentNumberOfSpaces;
    protected int currentNumberOfTabs;
    
    protected IndentedWriter() {
        super();
        numberOfSpacesPerIndentation = 4;
        numberOfTabsPerIndentation = 0;
        currentNumberOfSpaces = 0;
        currentNumberOfTabs = 0;
    }
    
    public void indent() {
        currentNumberOfSpaces += numberOfSpacesPerIndentation;
        currentNumberOfTabs += numberOfTabsPerIndentation;
        ++indentationLevel;   
    }
    
    public void dedent() {
        currentNumberOfSpaces -= numberOfSpacesPerIndentation;
        currentNumberOfTabs -= numberOfTabsPerIndentation;
        --indentationLevel;
    }

    public int getIndentationLevel() {
        return indentationLevel;
    }

    @Override
    public void write(String str) throws IOException {
        String indentationString = null;
        if (isSpacesOnly()) {
            indentationString = StringUtils.repeat(" ", currentNumberOfSpaces);
        }
        writer.write(indentationString);
        writer.write(str);
    }
    
    public boolean isSpacesOnly() {
        return numberOfSpacesPerIndentation > 0 &&
                numberOfTabsPerIndentation == 0;
    }
    
    public boolean isTabsOnly() {
        return numberOfTabsPerIndentation > 0 &&
                numberOfSpacesPerIndentation == 0;
    }
}
