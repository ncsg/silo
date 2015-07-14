package com.pb.sawdust.model.models.provider.hub;

import com.pb.sawdust.model.models.provider.SimpleDataProvider;
import com.pb.sawdust.tensor.ArrayTensor;
import com.pb.sawdust.util.test.TestBase;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.pb.sawdust.util.Range.range;

/**
 * The {@code SubDataProviderHubTest} ...
 *
 * @author crf <br/>
 *         Started Oct 5, 2010 12:12:33 AM
 */
public class SubDataProviderHubTest extends DataProviderHubTest<String> {
    protected SubDataProviderHub<String> subProviderHub;
    protected DataProviderHub<String> parent;

    public static void main(String ... args) {
        TestBase.main();
//        DataProviderHubTest.switchSubDataTest(SubDataProviderHubTest.class,true);
//        TestBase.main();
//        DataProviderHubTest.switchSubDataTest(SubDataProviderHubTest.class,false);
    }

    @Override
    protected DataProviderHub<String> getProviderHub(int id, int dataLength, Map<String, double[]> sharedData, Map<String, Map<String, double[]>> data) {
        SimpleDataProviderHub<String> hub = new SimpleDataProviderHub<String>(id,data.keySet(), ArrayTensor.getFactory());
        for (String key : data.keySet())
            hub.addKeyedProvider(key,new SimpleDataProvider(data.get(key),ArrayTensor.getFactory()));
        hub.addProvider(new SimpleDataProvider(sharedData,ArrayTensor.getFactory()));
        parent = hub;
        return new SubDataProviderHub<String>(parent,0,dataLength);
    }

    @Override
    protected DataProviderHub<String> getUninitializedProvider(int id, Set<String> keys) {
        return null; //no uninitialized sub provider
    }

    @Override
    protected Set<String> getKeys() {
        Set<String> keys = new HashSet<String>();
        for (int i : range(random.nextInt(3,15)))
            keys.add(random.nextAsciiString(10));
        return keys;
    }

    @Override
    @Before
    public void beforeTest() {
        super.beforeTest();
        subProviderHub = (SubDataProviderHub<String>) providerHub;
    }

    @Override
    @Ignore
    @Test
    public void testGetId() {
        //no way to set id
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructorStartTooSmall() {
         new SubDataProviderHub<String>(parent,-1,subDataEnd);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructorEndTooBig() {
         new SubDataProviderHub<String>(parent,subDataStart,dataLength+1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructorStartEndSwapped() {
         new SubDataProviderHub<String>(parent,subDataEnd,subDataStart);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructorZeroLength() {
         new SubDataProviderHub<String>(parent,subDataStart,subDataStart);
    }
}