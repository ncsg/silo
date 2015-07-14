package com.pb.sawdust.model.builder.parser;

import com.pb.sawdust.calculator.NumericFunctionN;
import com.pb.sawdust.calculator.ParameterizedNumericFunction;

import java.util.*;

/**
 * The {@code ParsedFunctionBuilder} ...
 *
 * @author crf <br/>
 *         Started 4/17/11 7:37 AM
 */
public abstract class ParsedFunctionBuilder implements FunctionBuilder {
    protected final Map<String,NumberParser> numberParseMap;
    protected final Map<String,NumericFunctionN> functionMap;

    abstract protected com.pb.sawdust.calculator.ParameterizedNumericFunction buildFunction(List<String> formula);
    abstract protected List<String> parseToReversePolish(String formula);

    public ParsedFunctionBuilder(Map<String,NumberParser> numberParseMap, Map<String,NumericFunctionN> functionMap) {
        this.functionMap = Collections.unmodifiableMap(new HashMap<String,NumericFunctionN>(functionMap));
        this.numberParseMap = numberParseMap;
    }

    public com.pb.sawdust.calculator.ParameterizedNumericFunction buildFunction(String formula) {
        return buildFunction(parseToReversePolish(formula));
    }

    public static class BasicParameterizedNumericFunction implements ParameterizedNumericFunction {
        private final NumericFunctionN function;
        private final List<String> arguments;

        public BasicParameterizedNumericFunction(NumericFunctionN function, List<String> arguments) {
            this.function = function;
            this.arguments = Collections.unmodifiableList(new LinkedList<String>(arguments));
        }

        @Override
        public NumericFunctionN getFunction() {
            return function;
        }

        @Override
        public List<String> getArgumentNames() {
            return arguments;
        }
    }

    public static long[] buildLongArguments(com.pb.sawdust.calculator.ParameterizedNumericFunction function, Map<String,Long> valueMap) {
        long[] arguments = new long[function.getFunction().getArgumentCount()];
        int counter = 0;
        for (String arg : function.getArgumentNames())
            arguments[counter++] = valueMap.get(arg);
        return arguments;
    }

    public static double[] buildDoubleArguments(com.pb.sawdust.calculator.ParameterizedNumericFunction function, Map<String,Double> valueMap) {
        double[] arguments = new double[function.getFunction().getArgumentCount()];
        int counter = 0;
        for (String arg : function.getArgumentNames())
            arguments[counter++] = valueMap.get(arg);
        return arguments;
    }

    /**
     * The {@code NumberParser} interface provides a framework for parsing strings into numbers.
     */
    public static interface NumberParser {
        /**
         * Parse an input string.
         * 
         * @param number
         *        The input to parse.
         *        
         * @return the number that {@code number} parses to.
         * 
         * @throws NumberFormatException if {@code number} cannot be parsed by this parser.
         */
        Number parse(String number);
    }

    /**
     * The {@code ByteParser} class is a number parser returning {@code Byte}s.
     */
    public static class ByteParser implements NumberParser {
        public Byte parse(String number) {
            return Byte.parseByte(number);
        }
    }                                                                          

    /**
     * The {@code ShortParser} class is a number parser returning {@code Short}s.
     */
    public static class ShortParser implements NumberParser {
        public Short parse(String number) {
            return Short.parseShort(number);
        }
    } 

    /**
     * The {@code IntParser} class is a number parser returning {@code Int}s.
     */
    public static class IntParser implements NumberParser {
        public Integer parse(String number) {
            return Integer.parseInt(number);
        }
    } 

    /**
     * The {@code LongParser} class is a number parser returning {@code Long}s.
     */
    public static class LongParser implements NumberParser {
        public Long parse(String number) {
            return Long.parseLong(number);
        }
    } 

    /**
     * The {@code FloatParser} class is a number parser returning {@code Float}s.
     */
    public static class FloatParser implements NumberParser {
        public Float parse(String number) {
            return Float.parseFloat(number);
        }
    } 

    /**
     * The {@code DoubleParser} class is a number parser returning {@code Double}s.
     */
    public static class DoubleParser implements NumberParser {
        public Double parse(String number) {
            return Double.parseDouble(number);
        }
    }
}