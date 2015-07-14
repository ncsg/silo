package com.pb.sawdust.tensor.alias.scalar.id;

import com.pb.sawdust.tensor.TensorFactoryTests;
import com.pb.sawdust.tensor.Tensor;
import com.pb.sawdust.tensor.decorators.id.TensorIdFactoryTests;
import com.pb.sawdust.tensor.factory.TensorFactoryTest;
import com.pb.sawdust.tensor.decorators.id.primitive.size.*;
import com.pb.sawdust.tensor.decorators.id.size.IdD0Tensor;
import com.pb.sawdust.tensor.factory.TensorFactory;
import com.pb.sawdust.util.JavaType;
import com.pb.sawdust.util.array.*;
import com.pb.sawdust.util.test.TestBase;
import org.junit.Before;

import java.util.LinkedList;
import java.util.List;

/**
 * The {@code ScalarIdPackageFactoryTests} ...
 *
 * @author crf <br/>
 *         Started Dec 24, 2010 11:08:38 AM
 */
public class TensorScalarIdFactoryTests {
    public static List<Class<? extends TestBase>> TEST_CLASSES = new LinkedList<Class<? extends TestBase>>();
    static {
        TEST_CLASSES.add(TensorFactoryIdScalarTest.class);
        TEST_CLASSES.add(TensorFactoryIdBooleanScalarTest.class);
        TEST_CLASSES.add(TensorFactoryIdCharScalarTest.class);
        TEST_CLASSES.add(TensorFactoryIdByteScalarTest.class);
        TEST_CLASSES.add(TensorFactoryIdShortScalarTest.class);
        TEST_CLASSES.add(TensorFactoryIdIntScalarTest.class);
        TEST_CLASSES.add(TensorFactoryIdLongScalarTest.class);
        TEST_CLASSES.add(TensorFactoryIdFloatScalarTest.class);
        TEST_CLASSES.add(TensorFactoryIdDoubleScalarTest.class);
    }
    

    public static class TensorFactoryIdScalarTest extends IdScalarTest<Double,String> {
        protected TensorFactory factory;
        
        public static void main(String ... args) {
            TestBase.main();
        }
        
        @Before
        public void beforeTest() {
            factory = (TensorFactory) getTestData(getCallingContextInstance().getClass(),TensorFactoryTest.TENSOR_FACTORY_KEY);
            super.beforeTest();
        }

        protected List<List<String>> getIds(int ... dimensions) {
            return TensorIdFactoryTests.randomIds(random,dimensions);
        }

        protected Tensor<Double> getTensor(TypeSafeArray<Double> data) {
            ids = getIds();
            return getIdTensor(data,ids);
        }

        protected IdD0Tensor<Double,String> getIdTensor(TypeSafeArray<Double> data, List<List<String>> ids) {
            IdD0Tensor<Double,String> tensor = (IdD0Tensor<Double,String>) factory.<Double,String>tensor(ids);
            tensor.setTensorValues(data);
            return tensor;
        }

        protected TypeSafeArray<Double> getData() {
            return TensorFactoryTests.doubleObjectData(new int[] {1},random);
        }

        protected JavaType getJavaType() {
            return JavaType.OBJECT;
        }

        protected Double getRandomElement() {
            return TensorFactoryTests.randomDouble(random);
        }
    }  

    public static class TensorFactoryIdBooleanScalarTest extends IdBooleanScalarTest<String> {

        protected TensorFactory factory;
        
        public static void main(String ... args) {
            TestBase.main();
        }
        
        @Before
        public void beforeTest() {
            factory = (TensorFactory) getTestData(getCallingContextInstance().getClass(),TensorFactoryTest.TENSOR_FACTORY_KEY);
            super.beforeTest();
        }

        protected List<List<String>> getIds(int ... dimensions) {
            return TensorIdFactoryTests.randomIds(random,dimensions);
        }

        protected Tensor<Boolean> getTensor(TypeSafeArray<Boolean> data) {
            ids = getIds();
            return getIdTensor(data,ids);
        }

        protected BooleanTypeSafeArray getData() {
            return TensorFactoryTests.booleanData(new int[] {1},random);
        }

        protected Boolean getRandomElement() {
            return TensorFactoryTests.randomBoolean(random);
        }

        protected IdBooleanD0Tensor<String> getIdTensor(TypeSafeArray<Boolean> data, List<List<String>> ids) {
            IdBooleanD0Tensor<String> tensor = (IdBooleanD0Tensor<String>) factory.booleanTensor(ids);
            tensor.setTensorValues(data);
            return tensor;
        }
    }  

    public static class TensorFactoryIdCharScalarTest extends IdCharScalarTest<String> { 

        protected TensorFactory factory;
        
        public static void main(String ... args) {
            TestBase.main();
        }
        
        @Before
        public void beforeTest() {
            factory = (TensorFactory) getTestData(getCallingContextInstance().getClass(),TensorFactoryTest.TENSOR_FACTORY_KEY);
            super.beforeTest();
        }

        protected List<List<String>> getIds(int ... dimensions) {
            return TensorIdFactoryTests.randomIds(random,dimensions);
        }

        protected Tensor<Character> getTensor(TypeSafeArray<Character> data) {
            ids = getIds();
            return getIdTensor(data,ids);
        }

        protected CharTypeSafeArray getData() {
            return TensorFactoryTests.charData(new int[] {1},random);
        }

        protected Character getRandomElement() {
            return TensorFactoryTests.randomChar(random);
        }

        protected IdCharD0Tensor<String> getIdTensor(TypeSafeArray<Character> data, List<List<String>> ids) {
            IdCharD0Tensor<String> tensor = (IdCharD0Tensor<String>) factory.charTensor(ids);
            tensor.setTensorValues(data);
            return tensor;
        }
    }     

    public static class TensorFactoryIdByteScalarTest extends IdByteScalarTest<String> {

        protected TensorFactory factory;
        
        public static void main(String ... args) {
            TestBase.main();
        }
        
        @Before
        public void beforeTest() {
            factory = (TensorFactory) getTestData(getCallingContextInstance().getClass(),TensorFactoryTest.TENSOR_FACTORY_KEY);
            super.beforeTest();
        }

        protected List<List<String>> getIds(int ... dimensions) {
            return TensorIdFactoryTests.randomIds(random,dimensions);
        }

        protected Tensor<Byte> getTensor(TypeSafeArray<Byte> data) {
            ids = getIds();
            return getIdTensor(data,ids);
        }

        protected ByteTypeSafeArray getData() {
            return TensorFactoryTests.byteData(new int[] {1},random);
        }

        protected Byte getRandomElement() {
            return TensorFactoryTests.randomByte(random);
        }

        protected IdByteD0Tensor<String> getIdTensor(TypeSafeArray<Byte> data, List<List<String>> ids) {
            IdByteD0Tensor<String> tensor = (IdByteD0Tensor<String>) factory.byteTensor(ids);
            tensor.setTensorValues(data);
            return tensor;
        }
    }     

    public static class TensorFactoryIdShortScalarTest extends IdShortScalarTest<String> {

        protected TensorFactory factory;
        
        public static void main(String ... args) {
            TestBase.main();
        }
        
        @Before
        public void beforeTest() {
            factory = (TensorFactory) getTestData(getCallingContextInstance().getClass(),TensorFactoryTest.TENSOR_FACTORY_KEY);
            super.beforeTest();
        }

        protected List<List<String>> getIds(int ... dimensions) {
            return TensorIdFactoryTests.randomIds(random,dimensions);
        }

        protected Tensor<Short> getTensor(TypeSafeArray<Short> data) {
            ids = getIds();
            return getIdTensor(data,ids);
        }

        protected ShortTypeSafeArray getData() {
            return TensorFactoryTests.shortData(new int[] {1},random);
        }

        protected Short getRandomElement() {
            return TensorFactoryTests.randomShort(random);
        }

        protected IdShortD0Tensor<String> getIdTensor(TypeSafeArray<Short> data, List<List<String>> ids) {
            IdShortD0Tensor<String> tensor = (IdShortD0Tensor<String>) factory.shortTensor(ids);
            tensor.setTensorValues(data);
            return tensor;
        }
    }                            

    public static class TensorFactoryIdIntScalarTest extends IdIntScalarTest<String> {

        protected TensorFactory factory;
        
        public static void main(String ... args) {
            TestBase.main();
        }
        
        @Before
        public void beforeTest() {
            factory = (TensorFactory) getTestData(getCallingContextInstance().getClass(),TensorFactoryTest.TENSOR_FACTORY_KEY);
            super.beforeTest();
        }

        protected List<List<String>> getIds(int ... dimensions) {
            return TensorIdFactoryTests.randomIds(random,dimensions);
        }

        protected Tensor<Integer> getTensor(TypeSafeArray<Integer> data) {
            ids = getIds();
            return getIdTensor(data,ids);
        }

        protected IntTypeSafeArray getData() {
            return TensorFactoryTests.intData(new int[] {1},random);
        }

        protected Integer getRandomElement() {
            return TensorFactoryTests.randomInt(random);
        }

        protected IdIntD0Tensor<String> getIdTensor(TypeSafeArray<Integer> data, List<List<String>> ids) {
            IdIntD0Tensor<String> tensor = (IdIntD0Tensor<String>) factory.intTensor(ids);
            tensor.setTensorValues(data);
            return tensor;
        }
    }

    public static class TensorFactoryIdLongScalarTest extends IdLongScalarTest<String> {

        protected TensorFactory factory;
        
        public static void main(String ... args) {
            TestBase.main();
        }
        
        @Before
        public void beforeTest() {
            factory = (TensorFactory) getTestData(getCallingContextInstance().getClass(),TensorFactoryTest.TENSOR_FACTORY_KEY);
            super.beforeTest();
        }

        protected List<List<String>> getIds(int ... dimensions) {
            return TensorIdFactoryTests.randomIds(random,dimensions);
        }

        protected Tensor<Long> getTensor(TypeSafeArray<Long> data) {
            ids = getIds();
            return getIdTensor(data,ids);
        }

        protected LongTypeSafeArray getData() {
            return TensorFactoryTests.longData(new int[] {1},random);
        }

        protected Long getRandomElement() {
            return TensorFactoryTests.randomLong(random);
        }

        protected IdLongD0Tensor<String> getIdTensor(TypeSafeArray<Long> data, List<List<String>> ids) {
            IdLongD0Tensor<String> tensor = (IdLongD0Tensor<String>) factory.longTensor(ids);
            tensor.setTensorValues(data);
            return tensor;
        }
    }   

    public static class TensorFactoryIdFloatScalarTest extends IdFloatScalarTest<String> {

        protected TensorFactory factory;
        
        public static void main(String ... args) {
            TestBase.main();
        }
        
        @Before
        public void beforeTest() {
            factory = (TensorFactory) getTestData(getCallingContextInstance().getClass(),TensorFactoryTest.TENSOR_FACTORY_KEY);
            super.beforeTest();
        }

        protected List<List<String>> getIds(int ... dimensions) {
            return TensorIdFactoryTests.randomIds(random,dimensions);
        }

        protected Tensor<Float> getTensor(TypeSafeArray<Float> data) {
            ids = getIds();
            return getIdTensor(data,ids);
        }

        protected FloatTypeSafeArray getData() {
            return TensorFactoryTests.floatData(new int[] {1},random);
        }

        protected Float getRandomElement() {
            return TensorFactoryTests.randomFloat(random);
        }

        protected IdFloatD0Tensor<String> getIdTensor(TypeSafeArray<Float> data, List<List<String>> ids) {
            IdFloatD0Tensor<String> tensor = (IdFloatD0Tensor<String>) factory.floatTensor(ids);
            tensor.setTensorValues(data);
            return tensor;
        }
    }  

    public static class TensorFactoryIdDoubleScalarTest extends IdDoubleScalarTest<String> {

        protected TensorFactory factory;
        
        public static void main(String ... args) {
            TestBase.main();
        }
        
        @Before
        public void beforeTest() {
            factory = (TensorFactory) getTestData(getCallingContextInstance().getClass(), TensorFactoryTest.TENSOR_FACTORY_KEY);
            super.beforeTest();
        }

        protected List<List<String>> getIds(int ... dimensions) {
            return TensorIdFactoryTests.randomIds(random,dimensions);
        }

        protected Tensor<Double> getTensor(TypeSafeArray<Double> data) {
            ids = getIds();
            return getIdTensor(data,ids);
        }

        protected DoubleTypeSafeArray getData() {
            return TensorFactoryTests.doubleData(new int[] {1},random);
        }

        protected Double getRandomElement() {
            return TensorFactoryTests.randomDouble(random);
        }

        protected IdDoubleD0Tensor<String> getIdTensor(TypeSafeArray<Double> data, List<List<String>> ids) {
            IdDoubleD0Tensor<String> tensor = (IdDoubleD0Tensor<String>) factory.doubleTensor(ids);
            tensor.setTensorValues(data);
            return tensor;
        }
    }  
}