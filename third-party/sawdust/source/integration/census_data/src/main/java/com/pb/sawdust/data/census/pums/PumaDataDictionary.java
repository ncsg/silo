package com.pb.sawdust.data.census.pums;

/**
 * The {@code PumsDataDictionary} interface provides a structure which contains the metadata for a particular PUMS file.
 * This includes information about the names and types of the household and person fields, as well as which fields correspond
 * to commonly used data items (such as weights, serial ids, and puma identifiers).
 *
 * @param <H>
 *        The type of the household field. This field must be an {@code enum}.
 *
 * @param <P>
 *        The type of the person field. This field must be an {@code enum}.
 *
 * @author crf
 *         Started 10/13/11 7:45 AM
 */
public interface PumaDataDictionary<H extends Enum<H> & PumaDataField.PumaDataHouseholdField ,P extends Enum<P> & PumaDataField.PumaDataPersonField> {
    /**
     * Get an array holding all of the household fields, in the order they are used in the file. This method should be
     * implemented by calling the {@code values()} method on the specific {@code H} class/enum.
     *
     * @return an array holding all of the household fields, in order.
     */
    H[] getAllHouseholdFields();

    /**
     * Get an array holding all of the person fields, in the order they are used in the file. This method should be
     * implemented by calling the {@code values()} method on the specific {@code P} class/enum.
     *
     * @return an array holding all of the person fields, in order.
     */
    P[] getAllPersonFields();

    /**
     * Get the household field class.
     *
     * @return the class of {@code H}.
     */
    Class<H> getHouseholdFieldClass();

    /**
     * Get the person field class.
     *
     * @return the class of {@code P}.
     */
    Class<P> getPersonFieldClass();

    /**
     * Get the household field holding the state FIPS code.
     *
     * @return the state FIPS household field.
     */
    H getStateFipsField();

    /**
     * Get the household field holding the puma code.
     *
     * @return the puma household field.
     */
    H getPumaField();

    /**
     * Get the household field holding the serial id (unique identifier).
     *
     * @return the serial id household field.
     */
    H getHouseholdSerialIdField();

    /**
     * Get the person field holding the serial id (unique identifier).
     *
     * @return the serial id person field.
     */
    P getPersonSerialIdField();

    /**
     * Get the household field holding the household weight.
     *
     * @return the household weight household field.
     */
    H getHouseholdWeightField();

    /**
     * Get the person field holding the person weight.
     *
     * @return the person weight person field.
     */
    P getPersonWeightField();

    /**
     * Get the household field holding the number of persons in the household.
     *
     * @return the number of persons in the household field.
     */
    H getPersonsField();
}