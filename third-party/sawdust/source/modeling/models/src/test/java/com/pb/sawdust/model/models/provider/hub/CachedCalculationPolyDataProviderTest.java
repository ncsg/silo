package com.pb.sawdust.model.models.provider.hub;

import com.pb.sawdust.tensor.ArrayTensor;
import com.pb.sawdust.util.test.TestBase;
import org.junit.Before;

/**
 * The {@code CachedCalculationPolyDataProviderTest} ...
 *
 * @author crf <br/>
 *         Started 2/17/11 10:51 AM
 */
public class CachedCalculationPolyDataProviderTest extends SimpleCalculationPolyDataProviderTest {
    protected CachedCalculationPolyDataProvider<String> cachedPolyDataProvider;

    public static void main(String ... args) {
        TestBase.main();
    }
    
    protected SimpleCalculationPolyDataProvider<String> getCalculationProvider(int id, PolyDataProvider<String> baseProvider) {
        return new CachedCalculationPolyDataProvider<String>(id,baseProvider,ArrayTensor.getFactory());
    }
    
    @Before
    public void beforeTest() {
        super.beforeTest();
        cachedPolyDataProvider = (CachedCalculationPolyDataProvider<String>) calculationPolyDataProvider;
    }
}