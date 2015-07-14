package com.pb.sawdust.tabledata.metadata;

import java.util.*;

/**
 * The {@code Typer} class is used to infer data types from string inputs. It determines the types by trying a test
 * specific to each data type sequentially on the input string. The tests used for each type are as follows:
 * <p>
 * <table border="1" cellpadding="3" style="border-collapse: collapse;">
 *   <tr>
 *     <th>DataType</td>
 *     <th>Test ({@code value} is the string input)</td>
 *   </tr>
 *   <tr>
 *     <td>{@code BOOLEAN}</td>
 *     <td>{@code value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")}</td>
 *   </tr>
 *   <tr>
 *     <td>{@code BYTE}</td>
 *     <td>{@code java.lang.Byte.parseByte(value)} successfully executes</td>
 *   </tr>
 *   <tr>
 *     <td>{@code SHORT}</td>
 *     <td>{@code java.lang.Short.parseShort(value)} successfully executes</td>
 *   </tr>
 *   <tr>
 *     <td>{@code INT}</td>
 *     <td>{@code java.lang.Integer.parseInt(value)} successfully executes</td>
 *   </tr>
 *   <tr>
 *     <td>{@code LONG}</td>
 *     <td>{@code java.lang.Long.parseLong(value)} successfully executes</td>
 *   </tr>
 *   <tr>
 *     <td>{@code FLOAT}</td>
 *     <td>{@code java.lang.Float.valueOf(value)} successfully executes, and
 *         {@code java.lang.Float.valueOf(value)}.toString().equals(java.lang.Float.valueOf(value)}.toString())}<br/>
 *         This test checks to see if any precision is lost when converting to a double versus a float.
 *     </td>
 *   </tr>
 *   <tr>
 *     <td>{@code DOUBLE}</td>
 *     <td>{@code java.lang.Double.parseDouble(value)} successfully executes</td>
 *   </tr>
 *   <tr>
 *     <td>{@code STRING}</td>
 *     <td>Default data type, always succeeds</td>
 *   </tr>
 * </table>
 * <p>
 * These tests can be changed by extending this class and overriding the appropriate {@code testFor*} methods. The order
 * of the data types listed above is the order that the tests execute, until one succeeds. If none succeeds, the
 * {@code DataType.STRING} type is selected. Also, if desired, the user can specify a subset of the data types available
 * to select from when "typing" an input string. This class also contains methods to "type" an array of string data
 * (organized as "rows").  Essentially, the methods checks as deep into the data as specified, and picks the appropriate
 * data type for each "column."
 * <p>
 * In addition to the type determination functionality, this class contains methods which will cast a string input
 * into the appropriate object as defined by its inferred (or stated) type.
 * <p>
 * Finally, for user convenience, a number of the class methods appear in static form.
 *
 * @author crf <br/>
 *         Started: Jun 18, 2008 4:52:42 PM
 */
public class Typer {

    private static final Map<DataType,Integer> DATA_TYPE_PRECEDENCE; // = new LinkedHashMap<DataType,Integer>();
    private static final Set<DataType> DATA_TYPE_PRECEDENCE_SET;

    static {
        Map<DataType,Integer> dtp = new LinkedHashMap<>();
        dtp.put(DataType.BOOLEAN,0);
        dtp.put(DataType.BYTE,1);
        dtp.put(DataType.SHORT,2);
        dtp.put(DataType.INT,3);
        dtp.put(DataType.LONG,4);
        dtp.put(DataType.FLOAT,5);
        dtp.put(DataType.DOUBLE,6);
        dtp.put(DataType.STRING,7);
        DATA_TYPE_PRECEDENCE = Collections.unmodifiableMap(dtp);
        DATA_TYPE_PRECEDENCE_SET = DATA_TYPE_PRECEDENCE.keySet();
    }

    /**
     * Determine the data type for a given string input. The data types available to test the input against are
     * specified as an argument. If the string cannot be matched to any of the available data types, then
     * {@code DataType.STRING} will be returned (whether it is in {@code includedTypes} or not).
     *
     * @param value
     *        The string to infer a data type for.
     *
     * @param includeTypes
     *        The types available to test {@code value} against.
     *
     * @return the data type that {@code value} matches to.
     */
    public DataType inferType(String value, Collection<DataType> includeTypes) {
        for (DataType type : DATA_TYPE_PRECEDENCE_SET) {
            if (!includeTypes.contains(type))
                continue;
            switch (type) {
                case BOOLEAN : if (testForBoolean(value)) return type; break;
                case BYTE : if (testForByte(value)) return type; break;
                case SHORT : if (testForShort(value)) return type; break;
                case INT : if (testForInt(value)) return type; break;
                case LONG : if (testForLong(value)) return type; break;
                case FLOAT : if (testForFloat(value)) return type; break;
                case DOUBLE : if (testForDouble(value)) return type; break;
                case STRING : if (testForString(value)) return type; break;
            }
        }
        return DataType.STRING;
    }

    /**
     * Determine the data type for a given string input. All {@code DataType}s are available for matching, with
     * {@code DataType.STRING} being the default.
     *
     * @param value
     *        The string to infer a data type for.
     *
     * @return the data type that {@code value} matches to.
     */
    public DataType inferType(String value) {
        return inferType(value,DATA_TYPE_PRECEDENCE_SET);
    }

    /**
     * Test whether the given string value can be represented by {@code DataType.BYTE}.
     *
     * @param value
     *        The string value to check.
     *
     * @return {@code true} if {@code value} can be represented as a byte, {@code false} otherwise.
     */
    protected boolean testForByte(String value) {
        try {
            Byte.parseByte(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Test whether the given string value can be represented by {@code DataType.SHORT}.
     *
     * @param value
     *        The string value to check.
     *
     * @return {@code true} if {@code value} can be represented as a short, {@code false} otherwise.
     */
    protected boolean testForShort(String value) {
        try {
            Short.parseShort(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Test whether the given string value can be represented by {@code DataType.INT}.
     *
     * @param value
     *        The string value to check.
     *
     * @return {@code true} if {@code value} can be represented as an int, {@code false} otherwise.
     */
    protected boolean testForInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Test whether the given string value can be represented by {@code DataType.LONG}.
     *
     * @param value
     *        The string value to check.
     *
     * @return {@code true} if {@code value} can be represented as a long, {@code false} otherwise.
     */
    protected boolean testForLong(String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Test whether the given string value can be represented by {@code DataType.FLOAT}.
     *
     * @param value
     *        The string value to check.
     *
     * @return {@code true} if {@code value} can be represented as a float, {@code false} otherwise.
     */
    protected boolean testForFloat(String value) {
        try {
            Float f = Float.valueOf(value);
            return f.toString().equals(Double.valueOf(value).toString());
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Test whether the given string value can be represented by {@code DataType.DOUBLE}.
     *
     * @param value
     *        The string value to check.
     *
     * @return {@code true} if {@code value} can be represented as a double, {@code false} otherwise.
     */
    protected boolean testForDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Test whether the given string value can be represented by {@code DataType.BOOLEAN}.
     *
     * @param value
     *        The string value to check.
     *
     * @return {@code true} if {@code value} can be represented as a boolean, {@code false} otherwise.
     */
    protected boolean testForBoolean(String value) {
        return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
    }


    /**
     * Test whether the given string value can be represented by {@code DataType.STRING}.
     *
     * @param value
     *        The string value to check.
     *
     * @return {@code true}, since the input is already a string.
     */
    protected boolean testForString(String value) {
        return true;
    }

    /**
     * Infer data types for an array of data. The data is assumed to be like a "row-wise" table, with the first
     * array dimension being rows, and the second dimension being columns. Each row up to the maximium specified depth
     * is checked and the data types inferred for each column, which are then compared to the other rows already checked.
     * For each column, the data type selected will be the one which can encompass all of the data values checked
     * (<i>e.g.</i>, if one value maps to a {@code DataType.FLOAT} and another maps to a {@code DataType.DOUBLE}, then
     * {@code DataType.DOUBLE} is the type which can map them both).
     *
     * @param data
     *        The data whose types are to be inferred.
     *
     * @param maxDepth
     *        The maximum depth to check into the data array.
     *
     * @param includeTypes
     *        The data types to restrict the inferred types to.
     *
     * @return an array of data types, the order matching the order of the columns (2nd dimension) in {@code data}.
     */
    public DataType[] inferTypesOnStringData(String[][] data, int maxDepth, Collection<DataType> includeTypes) {
        if (data.length == 0)
            return new DataType[0];
        DataType[] types = new DataType[data[0].length];
        int typesLength = types.length;
        for (int i=0; i < maxDepth; i++) {
            String[] dataRow = data[i];
            for (int j=0; j<typesLength; j++) {
                DataType type = inferType(dataRow[j],includeTypes);
                if (i == 0) {
                    types[j] = type;
                } else {
                    if (types[j] == DataType.BOOLEAN ^ type == DataType.BOOLEAN)
                        types[j] = DataType.STRING;
                    if (DATA_TYPE_PRECEDENCE.get(type) > DATA_TYPE_PRECEDENCE.get(types[j]))
                        types[j] = type;
                }
            }
        }
        return types;
    }

    /**
     * Infer data types for an array of data. The data is assumed to be like a "row-wise" table, with the first
     * array dimension being rows, and the second dimension being columns. Each row up to the maximium specified depth
     * is checked and the data types inferred for each column, which are then compared to the other rows already checked.
     * For each column, the data type selected will be the one which can encompass all of the data values checked
     * (<i>e.g.</i>, if one value maps to a {@code DataType.FLOAT} and another maps to a {@code DataType.DOUBLE}, then
     * {@code DataType.DOUBLE} is the type which can map them both). All data types are available for mapping in this
     * method.
     *
     * @param data
     *        The data whose types are to be inferred.
     *
     * @param maxDepth
     *        The maximum depth to check into the data array.
     *
     * @return an array of data types, the order matching the order of the columns (2nd dimension) in {@code data}.
     */
    public DataType[] inferTypesOnRowData(String[][] data, int maxDepth) {
        return inferTypesOnStringData(data,maxDepth,DATA_TYPE_PRECEDENCE_SET);
    }

    /**
     * Infer data types for an array of data. The data is assumed to be like a "row-wise" table, with the first
     * array dimension being rows, and the second dimension being columns. Each row in the table
     * is checked and the data types inferred for each column, which are then compared to the other rows already checked.
     * For each column, the data type selected will be the one which can encompass all of the data values checked
     * (<i>e.g.</i>, if one value maps to a {@code DataType.FLOAT} and another maps to a {@code DataType.DOUBLE}, then
     * {@code DataType.DOUBLE} is the type which can map them both).
     *
     * @param data
     *        The data whose types are to be inferred.
     *
     * @param includeTypes
     *        The data types to restrict the inferred types to.
     *
     * @return an array of data types, the order matching the order of the columns (2nd dimension) in {@code data}.
     */
    public DataType[] inferTypesOnRowData(String[][] data,Collection<DataType> includeTypes) {
        return inferTypesOnStringData(data,data.length,includeTypes);
    }

    /**
     * Infer data types for an array of data. The data is assumed to be like a "row-wise" table, with the first
     * array dimension being rows, and the second dimension being columns. Each row in the table
     * is checked and the data types inferred for each column, which are then compared to the other rows already checked.
     * For each column, the data type selected will be the one which can encompass all of the data values checked
     * (<i>e.g.</i>, if one value maps to a {@code DataType.FLOAT} and another maps to a {@code DataType.DOUBLE}, then
     * {@code DataType.DOUBLE} is the type which can map them both). All data types are available for mapping in this
     * method.
     *
     * @param data
     *        The data whose types are to be inferred.
     *
     * @return an array of data types, the order matching the order of the columns (2nd dimension) in {@code data}.
     */
    public DataType[] inferTypesOnStringData(String[][] data) {
        return inferTypesOnStringData(data,data.length,DATA_TYPE_PRECEDENCE_SET);
    }

    /**
     * Cast (or parse) a string input into an object instance corresponding to a given data type. The returned object
     * will be an instance of the class {@code type.getObjectClass()}. An exception will likely occur if the input
     * string cannot be cast to the data type.
     *
     * @param value
     *        The string to be cast.
     *
     * @param type
     *        The data type the string is to be cast to.
     *
     * @return {@code value} represented as an instance object corresponding to {@code type}.
     */
    public Object castToType(String value, DataType type) {
        switch(type) {
            case BOOLEAN : return Boolean.parseBoolean(value);
            case BYTE : return Byte.parseByte(value);
            case SHORT : return Short.parseShort(value);
            case INT : return Integer.parseInt(value);
            case LONG : return Long.parseLong(value);
            case FLOAT : return Float.parseFloat(value);
            case DOUBLE : return Double.parseDouble(value);
            default : return value;
        }
    }

    /**
     * Cast (or parse) a string input into an object instance corresponding to an inferred data type. The returned
     * object will be an instance of the class {@code inferType(value).getObjectClass()}.
     *
     * @param value
     *        The string to be cast.
     *
     * @return {@code value} represented as an instance object corresponding to its inferred type.
     */
    public Object castToType(String value) {
        return castToType(value,inferType(value));
    }

    /**
     * Coerce a given string input to the specified data type. This method is identical to {@code castToType(String,DataType)}
     * except it will cast any numeric string to the specidied data type.  Specifically, if
     * <pre>
     *     <code>
     *         Number.class.isInstance(castToType(value)) == true
     *     </code>
     * </pre>
     * then the appropriate {@code [primitive]value()} method will be called, where {@code [primitive]} is the primitive
     * associated with {@code type}.
     *
     * @param value
     *        The string to be coerced.
     *
     * @param type
     *        The data type the string is to be coerced to.
     *
     * @return {@code value} represented as an instance object corresponding to {@code type}.
     *
     * @throws IllegalArgumentException if {@code value} cannot be coerced into the specified type.
     */
    public Object coerceToType(String value, DataType type) {
        if (type == DataType.STRING)
            return value;
        DataType actualType = inferType(value);
        Object newValue = castToType(value,actualType);
        if (actualType == type)
            return newValue;
        if (Number.class.isInstance(newValue)) {
            Number n = (Number) newValue;
            switch(type) {
                case BYTE : return n.byteValue();
                case SHORT : return n.shortValue();
                case INT : return n.intValue();
                case LONG : return n.longValue();
                case FLOAT : return n.floatValue();
                case DOUBLE : return n.doubleValue();
            }
        }
        throw new IllegalArgumentException("Cannot coerce argument to a " + type + ": " + value);
    }

    /**
     * Determine the data type for a given string input. The data types available to test the input against are
     * specified as an argument. This is just a static convenience wrapper for the instance method
     * {@link #infer(String, java.util.Collection)}.
     *
     * @param value
     *        The string to infer a data type for.
     *
     * @param includeTypes
     *        The types available to test {@code value} against.
     *
     * @return the data type that {@code value} matches to.
     */
    public static DataType infer(String value, Collection<DataType> includeTypes) {
        return new Typer().inferType(value,includeTypes);
    }

    /**
     * Determine the data type for a given string input. This is just a static convenience wrapper for the instance
     * method {@link #inferType(String)}.
     *
     * @param value
     *        The string to infer a data type for.
     *
     * @return the data type that {@code value} matches to.
     */
    public static DataType infer(String value) {
        return new Typer().inferType(value);
    }

    /**
     * Convenience method to infer data types for an array of data. This is just a static convenience wrapper for the
     * instance method {@link #inferTypes(String[][], int)}.
     *
     *  @param data
     *        The data whose types are to be inferred.
     *
     * @param maxDepth
     *        The maximum depth to check into the data array.
     *
     * @return an array of data types, the order matching the order of the columns (2nd dimension) in {@code data}.
     */
    public static DataType[] inferTypes(String[][] data, int maxDepth) {
        return new Typer().inferTypesOnRowData(data,maxDepth);
    }

    /**
     * Convenience method to infer data types for an array of data. This is just a static convenience wrapper for the
     * instance method {@link #inferTypes(String[][])}.
     *
     *  @param data
     *        The data whose types are to be inferred.
     *
     * @return an array of data types, the order matching the order of the columns (2nd dimension) in {@code data}.
     */
    public static DataType[] inferTypes(String[][] data) {
        return new Typer().inferTypesOnStringData(data);
    }

    /**
     * Parse a string input into an object instance corresponding to a given data type. This is just a static
     * convenience wrapper for the instance method {@link #castToType(String, DataType)}.
     *
     * @param value
     *        The string to be cast.
     *
     * @param type
     *        The data type the string is to be cast to.
     *
     * @return {@code value} represented as an instance object corresponding to {@code type}.
     */
    public static Object parse(String value, DataType type) {
        return new Typer().castToType(value,type);
    }


    /**
     * Cast (or parse) a string input into an object instance corresponding to an inferred data type. This is just a static
     * convenience wrapper for the instance method {@link #castToType(String)}.
     *
     * @param value
     *        The string to be cast.
     *
     * @return {@code value} represented as an instance object corresponding to its inferred type.
     */
    public static Object parse(String value) {
        return new Typer().castToType(value);
    }

    /**
     * The {@code StandardTyper} class provides a "standard" typing class for general purpose use. It gurantees that all
     * text values will be parsed to/interpreted as one of the following data types: {@code BOOLEAN}, {@code INT},
     * {@code DOUBLE}, or {@code STRING}.  This makes "guessing" the data types a bit easier, though some care must be
     * taken, as the parsing algorithm may produce unexpected/undesired results. In particular, integer data too large
     * for an {@code int} but valid for a {@code long} will be parsed as a {@code DOUBLE}.
     */
    public static class StandardTyper extends Typer {

        protected boolean testForByte(String value) {
            return false;
        }

        protected boolean testForShort(String value) {
            return false;
        }

        protected boolean testForLong(String value) {
            return false;
        }

        protected boolean testForFloat(String value) {
            return false;
        }

    }

}