package com.pb.sawdust.model.models.provider;

import com.pb.sawdust.tensor.ArrayTensor;
import com.pb.sawdust.util.test.TestBase;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The {@code SimpleDataProviderTest} ...
 *
 * @author crf <br/>
 *         Started Sep 26, 2010 6:37:11 AM
 */
public class SimpleDataProviderTest extends DataProviderTest {
    
    public static void main(String ... args) {
        TestBase.main();
    }

    @Override
    protected DataProvider getProvider(int id, int length, Map<String, double[]> data) {
        return new SimpleDataProvider(id,data,ArrayTensor.getFactory());
    }

    @Override
    protected DataProvider getUninitializedProvider(Set<String> variables) {
        return null; //cannot be uninitialized
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBadConstructorNonMatchingDataLengths() {
        Map<String,double[]> newData = new HashMap<String,double[]>(data);
        String variable;
        while (newData.containsKey(variable = random.nextAsciiString(10)));
        newData.put(variable,random.nextDoubles(dataLength-random.nextInt(1,dataLength/2)));
        getProvider(id,dataLength,newData);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBadConstructorEmptyData() {
        Map<String,double[]> newData = new HashMap<String,double[]>();
        for (String variable : data.keySet())
            newData.put(variable,new double[0]);
        getProvider(id,dataLength,newData);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBadConstructorEmptyDataMap() {
        getProvider(id,dataLength,new HashMap<String,double[]>());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructorBadId() {
        //assumes haven't done Integer.MAX_VALUE data ids in between
        getProvider(id+Integer.MAX_VALUE,provider.getDataLength(),data);
    }
}