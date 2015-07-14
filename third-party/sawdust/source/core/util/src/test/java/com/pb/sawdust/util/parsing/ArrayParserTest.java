package com.pb.sawdust.util.parsing;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import java.util.Random;

import com.pb.sawdust.util.test.TestBase;

/**
 * @author crf <br/>
 *         Started: Sep 3, 2008 11:48:24 AM
 */
public abstract class ArrayParserTest<I,O> extends TestBase {
    
    protected Random randomGenerator = new Random();
    protected int[] widths;
    protected ArrayParser<I,O> arrayParser;

    abstract ArrayParser<I,O> getArrayParser(int[] widths);
    abstract ArrayParser<I,O> getArrayParser(int[] positions, int length);
    abstract I getSampleInput(int length);

    @Before
    public void beforeTest() {
        int randomLength = randomGenerator.nextInt(20)+5;
        widths = new int[randomLength];
        for (int i = 0; i < randomLength; i++)
            widths[i] = randomGenerator.nextInt(25)+1;
        arrayParser = getArrayParser(widths);
    }

    @Test(expected= IllegalArgumentException.class)
    public void testInvalidWidth() {
        getArrayParser(new int[] {3,5,-6,9});
    }

    @Test(expected= IllegalArgumentException.class)
    public void testInvalidPositions() {
        getArrayParser(new int[] {0,3,8,5},20);
    }

    @Test(expected= IllegalArgumentException.class)
    public void testInvalidLength() {
        getArrayParser(new int[] {3,5,9},8);
    }

    @Test
    public void testInputIsArray() {
        assertTrue(getSampleInput(0).getClass().isArray());
    }

    @Test
    public void testJavaType() {
        Class arrayClass;
        switch(arrayParser.getJavaType()) {
            case BYTE : arrayClass = byte[].class; break;
            case SHORT : arrayClass = short[].class; break;
            case INT : arrayClass = int[].class; break;
            case LONG : arrayClass = long[].class; break;
            case FLOAT : arrayClass = float[].class; break;
            case DOUBLE : arrayClass = double[].class; break;
            case BOOLEAN : arrayClass = boolean[].class; break;
            case CHAR : arrayClass = char[].class; break;
            case OBJECT : arrayClass = Object[].class; break;
            default : throw new IllegalStateException("Shouldn't get here");
        }
        assertTrue(arrayClass.isInstance(getSampleInput(0)));
    }

    @Test
    public void testGetOutputArray() {
        int size = randomGenerator.nextInt(10);
        assertEquals(size,arrayParser.getOutputArray(size,getSampleInput(0)).length);
    }

    @Test
    public void testGetMinLength() {
        int sum= 0;
        for (int i : widths)
            sum += i;
        assertEquals(sum,arrayParser.getMinLength());
    }

    @Test
    public void testIsParsableEqual() {
        assertTrue(arrayParser.isParsable(getSampleInput(arrayParser.getMinLength())));
    }

    @Test
    public void testIsParsableBigger() {
        assertTrue(arrayParser.isParsable(getSampleInput(arrayParser.getMinLength()+1)));
    }

    @Test
    public void testIsParsableSmaller() {
        assertFalse(arrayParser.isParsable(getSampleInput(arrayParser.getMinLength()-1)));
    }

    @Test(expected= IllegalArgumentException.class)
    public void testParseFailure() {
        arrayParser.parse(getSampleInput(arrayParser.getMinLength()-1));
    }

    @Test
    public void testWidthsLengthWith0() {
        int[] positions = new int[] {0,3,5,9};
        int minSize = positions[positions.length-1] + randomGenerator.nextInt(20)+1;
        arrayParser = getArrayParser(positions,minSize);
        assertEquals(minSize,arrayParser.getMinLength());
    }

    @Test
    public void testWidthsLength() {
        int[] positions = new int[] {0,3,5,9};
        int minSize = positions[positions.length-1] + randomGenerator.nextInt(20)+1;
        arrayParser = getArrayParser(positions,minSize);
        assertEquals(minSize,arrayParser.getMinLength());
    }

    @Test
    public void testWidthsOpenLength() {
        int[] positions = new int[] {3,5,9};
        arrayParser = getArrayParser(positions, ArrayParser.OPEN_FINAL_SUBARRAY_WIDTH);
        assertEquals(positions[positions.length-1],arrayParser.getMinLength());
    }

    @Test
    public void testParseOutputLength() {
        assertEquals(widths.length,arrayParser.parse(getSampleInput(arrayParser.getMinLength())).length);
    }

    protected void setOpenParser() {
        widths[widths.length-1] = ArrayParser.OPEN_FINAL_SUBARRAY_WIDTH;
        arrayParser = getArrayParser(widths);
    }

    @Test
    public void testOpenParseLength() {
        setOpenParser();
        assertEquals(widths.length,arrayParser.parse(getSampleInput(arrayParser.getMinLength())).length);
    }

    @Test
    public void testOpenParse() {
        setOpenParser();
        assertEquals(widths.length,arrayParser.parse(getSampleInput(arrayParser.getMinLength() + randomGenerator.nextInt(10) + 1)).length);
    }


}