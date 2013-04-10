/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package promnetpp.tests;

import java.io.StringReader;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.googlecode.promnetpp.parsing.AbstractSyntaxTree;
import com.googlecode.promnetpp.parsing.PROMELAParser;
import com.googlecode.promnetpp.parsing.ParseException;

/**
 *
 * @author Miguel Martins
 */
public class AbstractSyntaxTreeTest {
    private final PROMELAParser firstParser;
    private final PROMELAParser secondParser;
    
    public AbstractSyntaxTreeTest() {
        String firstProgram = "typedef message {\n"
                + "byte first_byte;\n"
                + "byte second_byte;\n" + "};";
        
        String secondProgram = "typedef message {\n"
                + "byte first_byte;\n"
                + "byte second_byte\n" + "}";
        
        this.firstParser = new PROMELAParser(new StringReader(firstProgram));
        this.secondParser = new PROMELAParser(new StringReader(secondProgram));
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void doEqualityTest() throws ParseException {
        AbstractSyntaxTree firstTree = new AbstractSyntaxTree(
                firstParser.Start());
        
        AbstractSyntaxTree secondTree = new AbstractSyntaxTree(
                secondParser.Start());
        
        assertTrue(firstTree.equals(firstTree)); //Self-equality
        assertTrue(firstTree.equals(secondTree));
    }
}
