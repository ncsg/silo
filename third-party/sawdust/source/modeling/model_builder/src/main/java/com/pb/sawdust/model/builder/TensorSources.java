package com.pb.sawdust.model.builder;

import com.pb.sawdust.model.integration.transcad.TranscadNativeMatrixReader;
import com.pb.sawdust.model.integration.transcad.TranscadNativeMatrixReader64Bit;
import com.pb.sawdust.tensor.TensorUtil;
import com.pb.sawdust.tensor.alias.matrix.Matrix;
import com.pb.sawdust.tensor.alias.matrix.id.IdDoubleMatrix;
import com.pb.sawdust.tensor.alias.matrix.primitive.*;
import com.pb.sawdust.tensor.decorators.primitive.DoubleTensor;
import com.pb.sawdust.tensor.factory.TensorFactory;
import com.pb.sawdust.tensor.read.CsvTensorReader;
import com.pb.sawdust.tensor.read.TensorReader;
import com.pb.sawdust.tensor.read.ZipMatrixReader;
import com.pb.sawdust.util.JavaType;
import com.pb.sawdust.util.exceptions.RuntimeWrappingException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Path;

/**
 * The {@code TensorSources} ...
 *
 * @author crf
 *         Started 6/5/12 9:15 AM
 */
public class TensorSources {

    public static abstract class CachedTensorSource implements TensorSource {
        private volatile DoubleTensor tensor = null;

        protected abstract DoubleTensor getTensorInstance();

        public synchronized DoubleTensor getTensor() {
            if (tensor == null)
                tensor = getTensorInstance();
            return tensor;
        }
    }

    private static abstract class AbstractTensorSource extends CachedTensorSource {
        private final String tensorName;

        protected AbstractTensorSource(String tensorName) {
            this.tensorName = tensorName;
        }

        protected String getTensorName() {
            return tensorName;
        }
    }

    private static abstract class FileTensorSource extends AbstractTensorSource {
        private final Path tensorFile;
        private final TensorFactory factory;

        protected FileTensorSource(Path tensorFile, String tensorName, TensorFactory factory) {
            super(tensorName);
            this.tensorFile = tensorFile;
            this.factory = factory;
        }

        protected Path getTensorFile() {
            return tensorFile;
        }

        protected abstract TensorReader<Double,?> getTensorReader();

        @Override
        public DoubleTensor getTensorInstance() {
            return (DoubleTensor) factory.tensor(getTensorReader());
        }
    }

    public static class CsvTensorSource extends FileTensorSource {
        private final int[] dimensions;
        private final String tensorColumn;

        public CsvTensorSource(Path tensorFile, String tensorName, TensorFactory factory, int[] dimensions, String tensorColumn) {
            super(tensorFile,tensorName,factory);
            this.dimensions = dimensions;
            this.tensorColumn = tensorColumn;
        }

        public CsvTensorSource(Path tensorFile, String tensorName, TensorFactory factory, int[] dimensions) {
            this(tensorFile,tensorName,factory,dimensions,tensorName);
        }

        @Override
        public TensorReader<Double,?> getTensorReader() {
            CsvTensorReader<Double,?> reader = new CsvTensorReader<Double,Object>(getTensorFile().toString(),JavaType.DOUBLE,dimensions);
            reader.setCurrentTensor(tensorColumn);
            return reader;
        }
    }

//    public static class TranscadNativeTensorSource implements TensorSource {
//        private final TensorSource tensorSource;
//
//        public TranscadNativeTensorSource(Path tensorFile, String tensorName, TensorFactory factory, String tensorNameInFile, int rowIndex, int columnIndex) {
//            boolean is64 = System.getProperty("os.arch").contains("64");
//            if (is64)
//                tensorSource = new TranscadNativeTensorSource64Bit(tensorFile,tensorName,factory,tensorNameInFile,rowIndex,columnIndex);
//            else
//                tensorSource = new TranscadNativeTensorSource32Bit(tensorFile,tensorName,factory,tensorNameInFile,rowIndex,columnIndex);
//        }
//
//        public TranscadNativeTensorSource(Path tensorFile, String tensorName, TensorFactory factory, String tensorNameInFile) {
//            this(tensorFile,tensorName,factory,tensorNameInFile,-1,-1);
//        }
//
//        public TranscadNativeTensorSource(Path tensorFile, String tensorName, TensorFactory factory) {
//            this(tensorFile,tensorName,factory,tensorName);
//        }
//
//        @Override
//        public DoubleTensor getTensor() {
//            return tensorSource.getTensor();
//        }
//    }

    public static class TranscadNativeTensorSource extends AbstractTensorSource {
        private final Path tensorFile;
        private final TensorFactory factory;
        private final String tensorNameInFile;
        private final int rowIndex;
        private final int columnIndex;

        public TranscadNativeTensorSource(Path tensorFile, String tensorName, TensorFactory factory, String tensorNameInFile, int rowIndex, int columnIndex) {
            super(tensorName);
            this.tensorFile = tensorFile;
            this.factory = factory;
            this.tensorNameInFile = tensorNameInFile;
            this.rowIndex = rowIndex;
            this.columnIndex = columnIndex;
        }

        public TranscadNativeTensorSource(Path tensorFile, String tensorName, TensorFactory factory, String tensorNameInFile) {
            this(tensorFile,tensorName,factory,tensorNameInFile,-1,-1);
        }

        public TranscadNativeTensorSource(Path tensorFile, String tensorName, TensorFactory factory) {
            this(tensorFile,tensorName,factory,tensorName);
        }

        @Override
        public DoubleTensor getTensorInstance() {
            Matrix<? extends Number> m =  (Matrix<? extends Number>) factory.tensor(getTensorReader());
            switch (m.getType()) {
                case BYTE : return TensorUtil.copyOfAsDouble((ByteMatrix) m,factory);
                case SHORT : return TensorUtil.copyOfAsDouble((ShortMatrix) m,factory);
                case INT : return TensorUtil.copyOfAsDouble((IntMatrix) m,factory);
                case LONG : return TensorUtil.copyOfAsDouble((LongMatrix) m,factory);
                case FLOAT : return TensorUtil.copyOfAsDouble((FloatMatrix) m,factory);
                case DOUBLE : return TensorUtil.copyOfAsDouble((DoubleMatrix) m,factory);
                default : throw new IllegalStateException("Shouldn't be here: " + m.getType());
            }
        }


        private TensorReader<? extends Number,?> getTensorReader() {
            if (rowIndex < 0)
                return new TranscadNativeMatrixReader<>(tensorFile,tensorNameInFile);
            else
                return new TranscadNativeMatrixReader<>(tensorFile,tensorNameInFile,rowIndex,columnIndex);
        }
    }

    public static class ZipMatrixTensorSource extends FileTensorSource {

        public ZipMatrixTensorSource(Path tensorFile, String tensorName, TensorFactory factory) {
            super(tensorFile,tensorName,factory);
        }

        @Override
        public TensorReader<Double,?> getTensorReader() {
            return ZipMatrixReader.getDoubleZipMatrixReader(getTensorFile().toFile());
        }
    }

//    public static class ZipTensorTensorSource extends FileTensorSource {
//        private final int tensorNumber;
//        private final int indexNumber;
//        private String tensorNameInFile;
//        private String indexNameInFile;
//
//        public ZipTensorTensorSource(Path tensorFile, String tensorName, TensorFactory factory, int tensorNumber, int indexNumber) {
//            super(tensorFile,tensorName,factory);
//            this.tensorNumber = tensorNumber;
//            this.indexNumber = indexNumber;
//            tensorNameInFile = null;
//            indexNameInFile = null;
//        }
//
//        public ZipTensorTensorSource(Path tensorFile, String tensorName, TensorFactory factory, int tensorNumber) {
//            this(tensorFile,tensorName,factory,tensorNumber,0);
//        }
//
//        public ZipTensorTensorSource(Path tensorFile, String tensorName, TensorFactory factory) {
//            this(tensorFile,tensorName,factory,0,0);
//        }
//
//        public void setTensorNameInFile(String tensorNameInFile) {
//            this.tensorNameInFile = tensorNameInFile;
//        }
//
//        public void setIndexNameInFile(String indexNameInFile) {
//            this.indexNameInFile = indexNameInFile;
//        }
//
//        private int getTensorNumber(ZipTensorReader<Double,?> reader) {
//            if (tensorNameInFile == null)
//                return tensorNumber;
//            reader.getTensorGroupMetadata()
//            return tensorNameInFile == null ? tensorNumber :;
//        }
//
//        private int getIndexNumber(ZipTensorReader<Double,?> reader) {
//            return indexNameInFile == null ? indexNumber :
//        }
//
//        @Override
//        public TensorReader<Double,?> getTensorReader() {
//            ZipTensorReader<Double,?> reader = new ZipTensorReader<Double,Object>(getTensorFile().toFile());
//            reader.setCurrentTensor(getTensorNumber(reader));
//            reader.setCurrentIndex(reader);
//            return reader;
//        }
//    }

    public static class JavaStaticTensorSource extends AbstractTensorSource {
        private final String className;
        private final String methodName;

        public JavaStaticTensorSource(String tableName, String className, String methodName) {
            super(tableName);
            this.className = className;
            this.methodName = methodName;
        }

        @Override
        public DoubleTensor getTensorInstance() {
            try {
                Class<?> sourceClass = Class.forName(className);
                Method method = sourceClass.getMethod(methodName);
                if (!Modifier.isStatic(method.getModifiers()))
                    throw new IllegalStateException("Method " + methodName + " is not static on " + className);
                if (!DoubleTensor.class.isAssignableFrom(method.getReturnType()))
                    throw new IllegalStateException("Static method " + className + "." + methodName + " does not return a DoubleTensor");
                return (DoubleTensor) method.invoke(null);
            } catch (NoSuchMethodException e) {
                throw new RuntimeWrappingException("Method not found, make sure it is declared public: " + className + "." + methodName,e);
            } catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeWrappingException(e);
            }
        }
    }
}