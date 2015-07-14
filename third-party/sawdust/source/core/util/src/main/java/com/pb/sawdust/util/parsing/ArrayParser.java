package com.pb.sawdust.util.parsing;

import java.util.Arrays;

import static com.pb.sawdust.util.Range.*;
import com.pb.sawdust.util.JavaType;

/**
 * The {@code ArrayParser} class provides a framework for writing a {@code Parser} which parses an array of a given type
 * into an output. Each array parser holds a series of subarray widths which indicate how to split a given input array.
 * For example, an array parser with subarray specified as <code>[2,1,3]</code> will split an array (<code>a</code>)
 * with six elements (<code>a.length == 6</code>) into three arrays: <code>[a[0],a[1]]</code, <code>[a[2]]</code, and
 * <code>[a[3],a[4],a[5]]</code>, each of which will be parsed into an output. It is possible to specify an "open"
 * (variable-width) tail array by making the final subarray width{@link #OPEN_FINAL_SUBARRAY_WIDTH}.  Any array of the
 * specified input type is considred parsable as long as its length is equal to or greater than the sum of the positive
 * subarray widths. For a parser using a non-open final subarray, an input array which is lonmger than the sum of the
 * positive widths will be truncated.
 * <p>
 * Because this class parses arrays , the generic type {@code I} must be an array.  Though this is not (and cannot be)
 * explicitly enforced at construction, most methods in this class make that assumption and will throw an error if a
 * non-array input is specified.
 *
 * @param <I>
 *        The type of input array to be parsed; must be an array class.
 *
 * @param <O>
 *        The component type of the array output by this parser.
 *
 * @author crf <br/>
 *         Started: Jul 8, 2008 1:48:47 PM
 */
public abstract class ArrayParser<I,O> implements Parser<I,O> {
    public static final int OPEN_FINAL_SUBARRAY_WIDTH = -1;

    private final int[] widths;
    private final int minLength;
    private final JavaType type;
    private final boolean openFinalSubarray;

    /**
     * Constructor specifying the widths of the subarrays used when parsing inputs. All widths must be greater than 0,
     * except the final width, which may also be {@link #OPEN_FINAL_SUBARRAY_WIDTH}, indicating an "open" (variable width) final subarray.
     * An open subarray allows the final subarray to be empty if the input array's length is equal to the sum of positive
     * widths.
     *
     * @param widths
     *        The widths of the subarrays resulting from the parsing of an input array.
     *
     * @throws IllegalArgumentException if element in {@code widths} is less than one (final element is allowed to be
     *                                  equal to {@code OPEN_FINAL_SUBARRAY_WIDTH}).
     */
    public ArrayParser(int[] widths) {
        int minLength = 0;
        int widthsLength = widths.length-1;
        boolean openFinalSubarray = false;
        for (int i : range(widthsLength+1)) {
            int width = widths[i];
            if (i == widthsLength) {
                openFinalSubarray = width == OPEN_FINAL_SUBARRAY_WIDTH;
                if (openFinalSubarray) {
                    widths = Arrays.copyOf(widths,widthsLength);
                    break;
                }
            }
            if (width < 1)
                throw new IllegalArgumentException("Array parser widths must be greater than 0: " + width);
            minLength += width;
        }
        this.openFinalSubarray = openFinalSubarray;
        this.widths = widths;
        this.minLength = minLength;
        type = getJavaType();
    }

    /**
     * Constructor specifying breakpoint positions and the input array length. Widths (as used in {@code ArrayParser(int[])}
     * constructor) are determined implicitly by the positions. If {@code positions[0] == 0}, then the size of the output
     * array will be {@code positions.length}, otherwise the size of the output array will be {@code positions.length + 1}
     * (the parsing algorithm assumes a start at input element 0). A positive {@code arrayLength} parameter determines the
     * width of the final element as {@code arrayLength - positions[positions.length-1]}. Alternatively, the {@code arrayLength}
     * parameter may be set to {@link #OPEN_FINAL_SUBARRAY_WIDTH}, which sets the final subarray to be open (equivalent
     * to setting the final width to {@code OPEN_FINAL_SUBARRAY_WIDTH}).
     *
     * @param positions
     *        The breakpoint positions for the subarrays resulting from the parsing of an input array.
     *
     * @param arrayLength
     *        The minimum length of an input array, or {@code OPEN_FINAL_SUBARRAY_WIDTH} for an open final subarray.
     *
     * @throws IllegalArgumentException if <code>arrayLength != OPEN_FINAL_SUBARRAY_WIDTH</code> and arrayLength is less
     *                                  than the sum of the elements in {@code positions}.
     */
     public ArrayParser(int[] positions, int arrayLength) {
         this(getWidthsFromPositions(positions,arrayLength));
     }

    /**
     * Get an output array of the specified length.
     *
     * @param length
     *        The length of the output array.
     *
     * @param inputArray
     *        The input array (included for cases where the output array depends on the input array).
     *
     * @return an ouput array of size {@code length}.
     */
    abstract O[] getOutputArray(int length, I inputArray);

    /**
     * Form an output element from an input's subarray.
     *
     * @param inputSegment
     *        The input subarray.
     *
     * @return an output formed from {@code inputSegment}.
     */
    abstract O formOutputElement(I inputSegment);

    /**
     * Get the java type of the component type of the input array type.
     *
     * @return the input component type's java type.
     */
    abstract JavaType getJavaType();

    /**
     * Get the minimum length required for an input to be parsable.
     *
     * @return the minimum length for an input array.
     */
    public int getMinLength() {
        return minLength;
    }

    /**
     * Determine whether the specified input can be parsed by this parser. An input array is considered parsable if
     * <code>input.length >= getMinLength()</code>.
     *
     * @param input
     *        The input array.
     *
     * @return {@code true} if {@code input} can be parsed, {@code false} if it cannot.
     */
    public boolean isParsable(I input) {
        return getInputLength(input) >= minLength;
    }

    private int getInputLength(I input) {
        switch(type) {
            case BYTE : return ((byte[]) input).length;
            case SHORT : return ((short[]) input).length;
            case INT : return ((int[]) input).length;
            case LONG : return ((long[]) input).length;
            case FLOAT : return ((float[]) input).length;
            case DOUBLE : return ((double[]) input).length;
            case BOOLEAN : return ((boolean[]) input).length;
            case CHAR : return ((char[]) input).length;
            case OBJECT : return ((Object[]) input).length;
            default : throw new IllegalStateException("Shouldn't get here; caused by: " + input);
        }
    }

    public O[] parse(I input) {
        if (!isParsable(input))
            throw new IllegalArgumentException("Input not parsable: " + input);
        O[] output;
        if (openFinalSubarray)
            output = getOutputArray(widths.length+1,input);
        else
            output = getOutputArray(widths.length,input);
        int offset = 0;
        for (int i : range(widths.length)) {
            int width = widths[i];
            output[i] = formOutputElement(getRangeCopy(input,offset,width));
            offset += width;
        }
        if (openFinalSubarray)
            output[widths.length] = formOutputElement(getRangeCopy(input,offset,getInputLength(input)-offset));
        return output;
    }

    @SuppressWarnings("unchecked") //cast from Object[] to I is valid due to restrictions class contract
    private I getRangeCopy(I input,int offset, int width) {
        switch(type) {
            case BYTE : return (I) Arrays.copyOfRange((byte[]) input,offset,offset+width);
            case SHORT : return (I) Arrays.copyOfRange((short[]) input,offset,offset+width);
            case INT : return (I) Arrays.copyOfRange((int[]) input,offset,offset+width);
            case LONG : return (I) Arrays.copyOfRange((long[]) input,offset,offset+width);
            case FLOAT : return (I) Arrays.copyOfRange((float[]) input,offset,offset+width);
            case DOUBLE : return (I) Arrays.copyOfRange((double[]) input,offset,offset+width);
            case BOOLEAN : return (I) Arrays.copyOfRange((boolean[]) input,offset,offset+width);
            case CHAR : return (I) Arrays.copyOfRange((char[]) input,offset,offset+width);
            case OBJECT : return (I) Arrays.copyOfRange((Object[]) input,offset,offset+width);
            default : return null; // shouldn't get here
        }
    }

    private static int[] getWidthsFromPositions(int[] positions, int arrayLength) {
        if (positions[0] != 0) {
            int[] new_positions = new int[positions.length+1];
            new_positions[0] = 0;
            System.arraycopy(positions,0,new_positions,1,positions.length);
            positions = new_positions;
        }
        int size = positions.length;
        int[] widths = new int[size];
        int lastPosition = 0;
        for (int i : range(1,size)) {
            int position = positions[i];
            if (lastPosition >= position)
                throw new IllegalArgumentException("Positions must be in accending order: " + position);
            widths[i-1] = position - lastPosition;
            lastPosition = position;
        }
        if (arrayLength == OPEN_FINAL_SUBARRAY_WIDTH) {
            widths[size-1] = OPEN_FINAL_SUBARRAY_WIDTH;
        } else {
            if (lastPosition >= arrayLength)
                throw new IllegalArgumentException("Last position (" + lastPosition + ") must be smaller than array length (" + arrayLength + ").");
            widths[size-1] = arrayLength - lastPosition;
        }
        return widths;
    }
}