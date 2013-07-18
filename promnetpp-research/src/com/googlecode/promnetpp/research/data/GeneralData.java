/*
 * Copyright (c) 2013, Miguel Martins
 * Use is subject to license terms.
 *
 * This source code file is provided under the MIT License. Full licensing
 * terms should be available in the form of text files. The standard source code
 * distribution provides a LICENSE.txt file which can be consulted for licensing
 * details.
 */
package com.googlecode.promnetpp.research.data;

/**
 *
 * @author Miguel Martins
 */
public class GeneralData {
    public static final int[] seeds = {1234, 71337, 749464, -252392, -355723,
        960103, 905902, 634195, -807626, 458852, -438956, 521259, -231442,
        615387, 392039, -456988, 144748, 685910, 115335, -481879, -145600,
        -20244, 569789, 980987, 916986, 560451, 868386, 568700, -165345,
        -47588};
    
    public static String[] fileNames = {"NewOneThirdRule.pml", "1-of-n.pml"};
    public static String spinHome = "C:/spin";
    public static String PROMNeTppHome = "C:/promnetpp";
    public static String OMNeTppHome = "C:/omnetpp-4.3";

    public static String getJARFilePath() {
        return PROMNeTppHome + "/promnetpp.jar";
    }
}
