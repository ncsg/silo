package com.pb.sawdust.data.census.pums.transform;

import com.pb.sawdust.data.census.pums.PumaDataDictionary;
import com.pb.sawdust.data.census.pums.PumaDataType;
import com.pb.sawdust.data.census.pums.PumaTables;
import com.pb.sawdust.tabledata.DataRow;
import com.pb.sawdust.tabledata.DataTable;
import com.pb.sawdust.tabledata.transform.row.CompositeRowWiseDataTableTransformation;
import com.pb.sawdust.tabledata.transform.row.RowWiseDataTableTransformation;

import java.util.Collection;

/**
 * The {@code CompositePumaDataTransformation} is a composite row-wise data table transformation which contains transformations
 * acting on PUMA data.
 *
 * @author crf
 *         Started 1/20/12 7:49 AM
 */
public class CompositePumaRowWiseDataTransformation extends CompositeRowWiseDataTableTransformation<RowWiseDataTableTransformation> implements PumaDataGroupTransformation {
    private final PumaDataDictionary<?,?> dictionary;
    private final PumaDataType transformationType;

    /**
     * Constructor specifying the transformations to composite.
     *
     * @param transformations
     *        The transformations this transformation will be comprised of, in the order they will be applied.
     */
    public <T extends RowWiseDataTableTransformation & PumaDataGroupTransformation> CompositePumaRowWiseDataTransformation(Collection<T> transformations) {
        super(transformations);
        boolean first = true;
        PumaDataDictionary dictionary = null;
        PumaDataType transformationType = null;
        for (PumaDataTableTransformation transformation : transformations) {
            if (first) {
                dictionary = transformation.getDataDictionary();
                transformationType = transformation.getTransformationType();
                first = false;
            }
            if (transformation.getTransformationType() != transformationType)
                throw new IllegalArgumentException("Composite Puma data transformation must be comprised of only person or only household transformations");
            if (transformation.getDataDictionary() != dictionary)
                throw new IllegalArgumentException("Composite puma data transformation must use the same data dictionary: " + dictionary + " vs. " + transformation.getDataDictionary());
        }
        this.dictionary = dictionary;
        this.transformationType = transformationType;
    }


    @Override
    public PumaDataDictionary<?,?> getDataDictionary() {
        return dictionary;
    }

    @Override
    public PumaDataType getTransformationType() {
        return transformationType;
    }

    @Override
    public PumaTables.PumaDataRow getContextDataRow(DataRow row, DataTable table, int rowIndex) {
        return (PumaTables.PumaDataRow) row;
    }
}