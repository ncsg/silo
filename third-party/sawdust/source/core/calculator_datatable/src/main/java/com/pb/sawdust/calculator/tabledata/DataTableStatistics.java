package com.pb.sawdust.calculator.tabledata;

import com.pb.sawdust.tabledata.DataRow;
import com.pb.sawdust.tabledata.DataTable;
import com.pb.sawdust.tabledata.metadata.DataType;
import com.pb.sawdust.util.concurrent.DnCRecursiveAction;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The {@code DataTableStatistics} ...
 *
 * @author crf
 *         Started 9/25/12 6:51 AM
 */
public class DataTableStatistics {

    public static final int DEFAULT_CONCURRENT_SPLIT_LIMIT = 10000;

    public static <T> Map<String,T> getStatistics(DataTable table, StatisticFactory<?,T> statistic) {
        return getStatistics(table,statistic,new HashSet<>(Arrays.asList(table.getColumnLabels())));
    }

    @SuppressWarnings("unchecked") //will be correct
    public static <T> Map<String,T> getStatistics(DataTable table, StatisticFactory<?,T> statistic, Set<String> columns) {
        Map<StatisticFactory<?,?>,Collection<String>> statisticToColumnMap = new HashMap<>();
        statisticToColumnMap.put(statistic,columns);
        return (Map<String,T>) getStatistics(table,statisticToColumnMap).get(statistic);
    }

    public static Map<StatisticFactory<?,?>,Map<String,?>> getStatistics(DataTable table, Set<StatisticFactory<?,?>> statistics) {
        return getStatistics(table,statistics,new HashSet<>(Arrays.asList(table.getColumnLabels())));
    }

    public static Map<StatisticFactory<?,?>,Map<String,?>> getStatistics(DataTable table, Set<StatisticFactory<?,?>> statistics, Set<String> columns) {
        Map<StatisticFactory<?,?>,Collection<String>> statisticToColumnMap = new HashMap<>();
        for (StatisticFactory<?,?> factory : statistics)
            statisticToColumnMap.put(factory,columns);
        return getStatistics(table,statisticToColumnMap);
    }

    public static Map<StatisticFactory<?,?>,Map<String,?>> getStatistics(DataTable table, Map<StatisticFactory<?,?>,? extends Collection<String>> statisticToColumnMap) {
        Map<StatisticFactory<?,?>,Map<String,?>> statistics = new HashMap<>();
        for (StatisticFactory<?,?> factory : statisticToColumnMap.keySet()) {
            Map<String,Object> smap = new HashMap<>();
            for (String column : statisticToColumnMap.get(factory))
                smap.put(column,getStatistic(table,factory.getStatisticFunction(table.getColumnDataType(column)),column));
            statistics.put(factory,smap);
        }
        return statistics;
    }


    private static <T,O> O getStatistic(DataTable table, StatisticFunction<T,O> statisticFunction, String column) {
        return getStatistic(table,statisticFunction,column,DEFAULT_CONCURRENT_SPLIT_LIMIT);
    }

    private static <T,O> O getStatistic(DataTable table, StatisticFunction<T,O> statisticFunction, String column, int concurrentRowLimit) {
        if (statisticFunction instanceof ConcurrentStatisticFunction)
            return getStatisticConcurrent(table,(ConcurrentStatisticFunction<T,O>) statisticFunction,column,concurrentRowLimit);
        else
            return getStatisticDirect(table,statisticFunction,column);
    }

    @SuppressWarnings("unchecked") //pollution shouldn't be a problem, as the functions will check against problematic values
    private static <T,O> O getStatisticDirect(DataTable table, StatisticFunction<T,O> statisticFunction, String column) {
        for (DataRow row : table)
            statisticFunction.processNextValue((T) row.getCell(column));
        return statisticFunction.getStatistic();
    }

    private static <T,O> O getStatisticConcurrent(DataTable table, ConcurrentStatisticFunction<T,O> statisticFunction, String column, int concurrentRowLimit) {
        StatisticCalculator<T,O> calculator = new StatisticCalculator<>(table,statisticFunction,column,concurrentRowLimit);
        new ForkJoinPool().execute(calculator);
        calculator.getResult();
        return statisticFunction.getStatistic();
    }

    private static class StatisticCalculator<T,O> extends DnCRecursiveAction {
        private static final long serialVersionUID = -1614690183983760836L;

        private final DataTable table;
        private final ConcurrentStatisticFunction<T,O> statisticFunction;
        private final String column;
        private final long minCalcLength;

        protected StatisticCalculator(DataTable table, ConcurrentStatisticFunction<T,O> statisticFunction, String column, long minCalcLength, long start, long length, DnCRecursiveAction next) {
            super(start,length,next);
            this.table = table;
            this.statisticFunction = statisticFunction;
            this.column = column;
            this.minCalcLength = minCalcLength;
        }

        protected StatisticCalculator(DataTable table, ConcurrentStatisticFunction<T,O> statisticFunction, String column, long minCalcLength) {
            super(0,table.getRowCount());
            this.table = table;
            this.statisticFunction = statisticFunction;
            this.column = column;
            this.minCalcLength = minCalcLength;
        }

        @Override
        @SuppressWarnings("unchecked") //pollution shouldn't be a problem, as the functions will check against problematic values
        protected void computeAction(long start, long length) {
            int point = (int) start;
            while (length-- > 0)
                statisticFunction.processNextValue((T) table.getRow(point++).getCell(column));
        }

        @Override
        protected DnCRecursiveAction getNextAction(long start, long length, DnCRecursiveAction next) {
            return new StatisticCalculator<>(table,statisticFunction,column,minCalcLength,start,length,next);
        }

        @Override
        protected boolean continueDividing(long length) {
            return length >= minCalcLength;
        }
    }




    public static interface StatisticFunction<T,O> {
        boolean isValidForType(DataType dataType);
        void processNextValue(T value);
        O getStatistic();
        String getName();
    }

    public static abstract class NumericStatisticFunction<T extends Number,O> implements StatisticFunction<T,O> {
        @Override
        public boolean isValidForType(DataType dataType) {
            return dataType.getJavaType().isNumeric();
        }

        @Override
        abstract public O getStatistic();
    }

    public static abstract class NumericStatisticMeasureFunction<T extends Number,O extends Number> extends NumericStatisticFunction<T,O> {}

    public static interface ConcurrentStatisticFunction<T,O> extends StatisticFunction<T,O> {}




    public static interface StatisticFactory<T,O> {
        StatisticFunction<T,O> getStatisticFunction(DataType type);
    }

    public static enum MeasureStatistic implements StatisticFactory<Number,Number> {
        SUM,
        AVERAGE,
        VARIANCE,
        STANDARD_DEVIATION,
        SAMPLE_STANDARD_DEVIATION;

        public StatisticFunction<Number,Number> getStatisticFunction(DataType type) {
            switch (this) {
                case SUM : return new StatisticSum(type);
                case AVERAGE : return new StatisticAverage(type);
                case VARIANCE : return new StatisticVariance(type);
                case STANDARD_DEVIATION : return new StatisticStandardDeviation(type,false);
                case SAMPLE_STANDARD_DEVIATION : return new StatisticStandardDeviation(type,true);
                default : throw new IllegalStateException("Missing function for statistic: " + this);
            }
        }
    }

    private static class DoubleStatisticSum extends NumericStatisticMeasureFunction<Number,Double> {
        private AtomicLong sum = new AtomicLong(Double.doubleToLongBits(0.0));

        public void processNextValue(Number nextValue) {
            long v = sum.get();
            double value = nextValue.doubleValue();
            while (!sum.compareAndSet(v,Double.doubleToLongBits(value + Double.longBitsToDouble(v))))
                v = sum.get();
        }

        public Double getStatistic() {
            return Double.longBitsToDouble(sum.get());
        }

        public String getName() {
            return "sum";
        }
    }

    private static class LongStatisticSum extends NumericStatisticMeasureFunction<Number,Long> {
        private AtomicLong sum = new AtomicLong(0L);

        public void processNextValue(Number nextValue) {
            sum.addAndGet(nextValue.longValue());
        }

        public Long getStatistic() {
            return sum.get();
        }

        public String getName() {
            return "sum";
        }
    }

    private static class StatisticSum implements ConcurrentStatisticFunction<Number,Number> {
        private final NumericStatisticMeasureFunction<Number,?> statisticMeasureFunction;

        public StatisticSum(DataType type) {
            switch (type) {
                case BYTE :
                case SHORT :
                case INT :
                case LONG : statisticMeasureFunction = new LongStatisticSum(); break;
                case FLOAT :
                case DOUBLE : statisticMeasureFunction = new DoubleStatisticSum(); break;
                default : throw new IllegalArgumentException("Non-numeric java type for sum statistic: " + type);
            }
        }

        @Override
        public boolean isValidForType(DataType dataType) {
            return statisticMeasureFunction.isValidForType(dataType);
        }

        @Override
        public void processNextValue(Number value) {
            statisticMeasureFunction.processNextValue(value);
        }

        @Override
        public Number getStatistic() {
            return statisticMeasureFunction.getStatistic();
        }

        public String getName() {
            return statisticMeasureFunction.getName();
        }
    }

    private static class StatisticAverage extends StatisticSum {
        AtomicLong count = new AtomicLong(0L);

        public StatisticAverage(DataType type) {
            super(type);
        }

        public void processNextValue(Number value) {
            super.processNextValue(value);
            count.incrementAndGet();
        }

        public Double getStatistic() {
            return super.getStatistic().doubleValue()/count.get();
        }

        public String getName() {
            return "average";
        }
    }

    private static class StatisticVariance extends StatisticAverage {
        private AtomicLong squareSum = new AtomicLong(Double.doubleToLongBits(0.0));

        public StatisticVariance(DataType type) {
            super(type);
        }

        public void processNextValue(Number nextValue) {
            super.processNextValue(nextValue);
            long v = squareSum.get();
            double value = nextValue.doubleValue();
            value *= value; //square
            while (!squareSum.compareAndSet(v,Double.doubleToLongBits(value + Double.longBitsToDouble(v))))
                v = squareSum.get();
        }

        public Double getStatistic() {
            double average = super.getStatistic();
            double squareAverage =  squareSum.get()/count.get();
            return squareAverage - average*average;
        }

        public String getName() {
            return "variance";
        }
    }

    private static class StatisticStandardDeviation extends StatisticVariance {
        private boolean sample;

        public StatisticStandardDeviation(DataType type, boolean sample) {
            super(type);
            this.sample = sample;
        }

        public StatisticStandardDeviation(DataType type) {
            this(type,false);
        }

        public Double getStatistic() {
            double variance = super.getStatistic();
            return Math.sqrt(sample ? (variance*count.get() / (count.get()-1)) : variance);
        }

        public String getName() {
            return (sample ? "sample_" : "") + "standard_deviation";
        }
    }



}