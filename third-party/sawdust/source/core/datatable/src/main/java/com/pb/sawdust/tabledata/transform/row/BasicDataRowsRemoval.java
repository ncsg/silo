package com.pb.sawdust.tabledata.transform.row;

import com.pb.sawdust.tabledata.DataRow;
import com.pb.sawdust.tabledata.DataTable;
import com.pb.sawdust.util.Filter;

/**
 * The {@code BasicDataRowsRemoval} is a row-removal data table transformation which only uses the row's data to determine
 * if it should be removed or not. That is, it is a non-contextual row-removal transformation.
 *
 * @author crf
 *         Started 1/30/12 7:36 AM
 */
public class BasicDataRowsRemoval extends DataRowsRemoval<DataRow> {
    /**
     * Constructor specifying the row filter used to determine which rows to remove from the table. If the row filter returns
     * {@code true} for a certain data row, then that row will be removed from the table.
     *
     * @param dataRowFilter
     *        The filter used to determine which rows to remove.
     */
    public BasicDataRowsRemoval(Filter<DataRow> dataRowFilter) {
        super(contextlessFilter(dataRowFilter));
    }

    private static Filter<ContextDataRow<DataRow>> contextlessFilter(final Filter<DataRow> filter) {
        return new Filter<ContextDataRow<DataRow>>() {

            @Override
            public boolean filter(ContextDataRow<DataRow> input) {
                return filter.filter(input);
            }
        };
    }

    @Override
    protected ContextDataRow<DataRow> getContextDataRow(DataRow row, DataTable table, int rowIndex) {
        return new DegenerateContextDataRow(row);
    }
}