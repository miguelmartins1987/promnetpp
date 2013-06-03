/*
 * Copyright (c) 2013, Miguel Martins
 * Use is subject to license terms.
 *
 * This source code file is provided under the MIT License. Full licensing
 * terms should be available in the form of text files. The standard source code
 * distribution provides a LICENSE.txt file which can be consulted for licensing
 * details.
 */
package com.googlecode.promnetpp.verification;

/**
 *
 * @author Miguel Martins
 */
public abstract class Verifier {
    public abstract boolean containsErrors();
    public abstract void doVerification();
    public abstract void finish();
    
    public boolean isErrorFree() {
        return !containsErrors();
    }
}
