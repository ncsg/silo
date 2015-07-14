package com.pb.sawdust.data.census.pums;

import com.pb.sawdust.tabledata.read.TableReader;
import com.pb.sawdust.util.Filter;

import java.util.Collection;
import java.util.Set;

/**
 * The {@code PumaDataReader} class is used to build household and person {@code DataTableReader}s for data from PUMA/PUMS
 * files. It allows specific household and person fields/columns to be specified for inclusion, as well as providing the
 * functionality for filtering (skipping) records based on PUMA, household size, or any other arbitrary data characteristic.
 * <p>
 * Because some PUMA/PUMS data files contain both household and person records which are structurally different, and thus
 * some of these records (person records for household tables, or household records for person tables) must be filtered
 * before the data is fully parsed. This class allows for this by providing line filters which act on the type {@code S},
 * which is specific to the type of table reader built by instances of this class. These filters will be applied before
 * data is parsed into the specific structure defined for a particular household or person table.
 * <p>
 * These line filters may technically also be used to filter out records based on the data they contain, but this is more
 * appropriately carried out through data filters this class provides which act upon parsed {@code Object[]} row data.
 * <p>
 * By default, all records, regardless of their PUMA, will be included in the tables read by the readers this class builds.
 * The method {@link #addAllowedPuma(Puma)} method can be used to explicitly specify which PUMA's may be included. Once
 * this method is called, then records will only be included if their PUMA matches one of those explicitly included by calling
 * that method. Additionally, a PUMA filter may be added to actively exclude or include records based on their PUMA, but
 * this filter acts <i>with</i> (<i>not in place of</i>) the list of allowed PUMAs: if a record's PUMA passes the filter
 * but is not in the list of allowed PUMAs, it will be excluded from the table.
 *
 * @param <S>
 *       The type the line filters will act upon. This will usually be {@code String} or {@code String[]}, depending on
 *       the type of table reader used to build the tables.
 *
 * @param <H>
 *        The type of the household field this reader reads. This field must be an {@code enum}.
 *
 * @param <P>
 *        The type of the person field this reader reads. This field must be an {@code enum}.
 *
 * @author crf
 *         Started 10/13/11 8:03 AM
 */
public interface PumaDataReader<S,H extends PumaDataField.PumaDataHouseholdField,P extends PumaDataField.PumaDataPersonField> {
    /**
     * Specify the set of columns that will be included in household tables returned by this class. If this method is called
     * more than once, only the set of columns from the most recent call will be included in the household tables. It is
     * possible that returned tables may include additional required fields, if they are not specified in {@code columns}.
     *
     * @param columns
     *        The columns to include in household tables.
     */
    void specifyHouseholdColumns(Set<H> columns);

    /**
     * Specify the set of columns that will be included in person tables returned by this class. If this method is called
     * more than once, only the set of columns from the most recent call will be included in the person tables. It is
     * possible that returned tables may include additional required fields, if they are not specified in {@code columns}.
     *
     * @param columns
     *        The columns to include in person tables.
     */
    void specifyPersonColumns(Set<P> columns);

    /**
     * Add a line filter for the household table reader built by this reader. If household line filters have already been
     * defined, this filter will be applied <i>with</i> them, <i>not</i> in place of them.
     *
     * @param filter
     *        The line filter for household table readers.
     */
    void addHouseholdLineFilter(Filter<S> filter);

    /**
     * Add a data filter for the household table reader built by this reader. If household data filters have already been
     * defined, this filter will be applied <i>with</i> them, <i>not</i> in place of them.
     *
     * @param filter
     *        The data filter for household table readers.
     */
    void addHouseholdDataFilter(Filter<Object[]> filter);

    /**
     * Clear all of the household line and data filters currently being applied by this reader.
     */
    void clearHouseholdFilters();

    /**
     * Add a line filter for the person table reader built by this reader. If person line filters have already been
     * defined, this filter will be applied <i>with</i> them, <i>not</i> in place of them.
     *
     * @param filter
     *        The line filter for person table readers.
     */
    void addPersonLineFilter(Filter<S> filter);

    /**
     * Add a data filter for the person table reader built by this reader. If person data filters have already been
     * defined, this filter will be applied <i>with</i> them, <i>not</i> in place of them.
     *
     * @param filter
     *        The data filter for person table readers.
     */
    void addPersonDataFilter(Filter<Object[]> filter);

    /**
     * Clear all of the person line and data filters currently being applied by this reader.
     */
    void clearPersonFilters();

    /**
     * Specify a PUMA to allow for table readers built by this reader.
     *
     * @param puma
     *        The PUMA to allow.
     */
    void addAllowedPuma(Puma puma);

    /**
     * Specify a number of PUMAs to allow for table readers built by this reader.
     *
     * @param pumas
     *        The PUMAs to allow.
     */
    void addAllowedPumas(Collection<Puma> pumas);

    /**
     * Add a PUMA filter for the table readers built by this reader. If PUMA filters have already been defined, this filter
     * will be applied <i>with</i> them, <i>not</i> in place of them.
     *
     * @param filter
     *        The PUMA filter for this reader.
     */
    void addPumaFilter(Filter<Puma> filter);

    /**
     * Clear all of the PUMA filters and allowed PUMAs from this reader. This action means that all PUMAs will be allowed
     * by readers built by this class.
     */
    void clearPumaFilters();

    /**
     * Specify if zero person households should be skipped and not included in household tables.
     *
     * @param skipZeroPersonHouseholds
     *        If {@code true}, then zero person households will be skipped.
     */
    void setSkipZeroPersonHouseholds(boolean skipZeroPersonHouseholds);

    /**
     * Get a table reader for reading the household data.
     *
     * @return the household table reader.
     */
    TableReader getHouseholdTableReader();

    /**
     * Get a table reader for reading the person data.
     *
     * @return the person table reader.
     */
    TableReader getPersonTableReader();
}