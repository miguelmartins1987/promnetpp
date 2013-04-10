/*
 * Copyright (c) 2013, Miguel Martins
 * Use is subject to license terms.
 *
 * This source code file is provided under the MIT License. Full licensing
 * terms should be available in the form of text files. The standard source code
 * distribution provides a LICENSE.txt file which can be consulted for licensing
 * details.
 */
package com.googlecode.promnetpp.translation;

import com.googlecode.promnetpp.parsing.AbstractSyntaxTree;

/**
 * The
 * <code>Translator</code> interface represents all PROMELA translator objects
 *
 * @author Miguel Martins
 */
public interface Translator {

    /**
     * Performs all the necessary translation operations on PROMELA's abstract
     * syntax tree. The target language for this project is C++, but it might
     * be possible to extend this to other programming languages in the future.
     *
     * @param abstractSyntaxTree The abstract syntax tree (PROMELA) that serves
     * as the basis for translation.
     */
    public void translate(AbstractSyntaxTree abstractSyntaxTree);

    public void init();

    public void finish();
}
