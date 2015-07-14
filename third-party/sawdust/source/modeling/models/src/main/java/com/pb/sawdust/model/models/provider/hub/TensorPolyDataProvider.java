package com.pb.sawdust.model.models.provider.hub;

import com.pb.sawdust.model.models.provider.AbstractIdData;
import com.pb.sawdust.model.models.provider.DataProvider;
import com.pb.sawdust.model.models.provider.EmptyDataProvider;
import com.pb.sawdust.model.models.provider.tensor.PolyDataIndexProvider;
import com.pb.sawdust.model.models.provider.tensor.TensorDataProvider;
import com.pb.sawdust.tensor.alias.matrix.id.IdDoubleMatrix;
import com.pb.sawdust.tensor.alias.matrix.primitive.DoubleMatrix;
import com.pb.sawdust.tensor.decorators.primitive.AbstractDoubleTensor;
import com.pb.sawdust.tensor.decorators.primitive.DoubleTensor;
import com.pb.sawdust.tensor.decorators.primitive.size.DoubleD2TensorShell;
import com.pb.sawdust.tensor.factory.TensorFactory;
import com.pb.sawdust.util.array.ArrayUtil;
import com.pb.sawdust.util.collections.SetList;

import java.util.*;

/**
 * The {@code TensorPolyDataProvider} ...
 *
 * @author crf
 *         Started 4/9/12 9:56 AM
 */
public class TensorPolyDataProvider<K> {//extends AbstractIdData implements PolyDataProvider<K> {
//    private String variable;
//    private Set<String> variableSet;
//    private final PolyDataIndexProvider indexProvider;
//    private final DoubleTensor tensor;
//    private final DataProvider generalProvider;
//    private final SetList<K> dataKeys;
//    private final TensorFactory factory;
//
//    public TensorPolyDataProvider(String variable, DoubleTensor tensor, PolyDataIndexProvider indexProvider, SetList<K> dataKeys, TensorFactory factory) {
//        super();
//        this.tensor = tensor;
//        this.indexProvider = indexProvider;
//        generalProvider = new EmptyDataProvider(factory,indexProvider.getIndexLength());
//        this.variable = variable;
//        variableSet = new HashSet<>();
//        variableSet.add(variable);
//        this.dataKeys = dataKeys;
//        this.factory = factory;
//    }
//
//    @Override
//    public Set<String> getPolyDataVariables() {
//        return variableSet;
//    }
//
//    @Override
//    public IdDoubleMatrix<? super K> getPolyData(String variable) {
//
//    }
//
//    @Override
//    public DataProvider getProvider(K key) {
//        return generalProvider;
//    }
//
//    @Override
//    public PolyDataProvider<K> getSubDataHub(int start, int end) {
//        return new SubPolyDataProvider<>(this,start,end);
//    }
//
//    @Override
//    public int getAbsoluteStartIndex() {
//        return 0;
//    }
//
//    @Override
//    public int getDataLength() {
//        return indexProvider.getIndexLength();
//    }
//
//    private int getKeyLocation(K key) {
//        return dataKeys.indexOf(key);
//    }
//
//    @Override
//    public DataProvider getFullProvider(K key) {
//        if (!dataKeys.contains(key))
//            throw new IllegalArgumentException("Poly data key not found: " + key);
//        return new TensorDataProvider(variable,tensor,indexProvider.getSpecifiedProvider(getKeyLocation(key)),factory);
//    }
//
//    @Override
//    public SetList<K> getDataKeys() {
//        return dataKeys;
//    }
//
//    @Override
//    public DataProvider getSharedProvider() {
//        return generalProvider;
//    }
}