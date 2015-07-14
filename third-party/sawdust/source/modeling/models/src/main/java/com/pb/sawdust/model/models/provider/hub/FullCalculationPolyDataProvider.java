package com.pb.sawdust.model.models.provider.hub;

import com.pb.sawdust.model.models.provider.VariableCalculation;
import com.pb.sawdust.tensor.alias.matrix.id.IdDoubleMatrix;
import com.pb.sawdust.tensor.factory.TensorFactory;

/**
 * The {@code FullCalculationPolyDataProvider} ...
 *
 * @author crf <br/>
 *         Started 2/17/11 7:38 AM
 */
public class FullCalculationPolyDataProvider<K> extends SimpleCalculationPolyDataProvider<K> {
    public FullCalculationPolyDataProvider(int dataId, PolyDataProvider<K> baseProvider, TensorFactory factory) {
        super(dataId,baseProvider,factory);
    }

    public FullCalculationPolyDataProvider(PolyDataProvider<K> baseProvider, TensorFactory factory) {
        super(baseProvider,factory);
    }

    @Override
    protected IdDoubleMatrix<? super K> getCalculatedPolyData(VariableCalculation calculation) {
        return super.getCalculatedPolyData(getResolvedCalculation(calculation.getName()));
    }

    @Override
    public CalculationPolyDataProvider<K> getSubDataHub(int start, int end) {
        return new FullCalculationSubPolyDataProvider(start,end);
    }

    private class FullCalculationSubPolyDataProvider extends WrappedCalculationSubPolyDataProvider {

        private FullCalculationSubPolyDataProvider(int start, int end) {
            super(start,end);
        }

        @Override
        protected IdDoubleMatrix<? super K> getCalculatedPolyData(VariableCalculation calculation) {
            return super.getCalculatedPolyData(getResolvedCalculation(calculation.getName()));
        }
    }
}