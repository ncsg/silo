package com.pb.sawdust.geography;

/**
 * The {@code GeographyElement} interface specifies a single component of a {@code Geography}. Each element has an associated
 * size (whose units depend on the geography) and an identifier which should be unique within each geography. Two elements
 * are equal if they have equal sizes, identifiers, and descriptions.
 *
 * @param <I>
 *        The type of the element's identifier.
 *
 * @author crf
 *         Started 10/17/11 11:01 AM
 */
public interface GeographyElement<I> {
    /**
     * Get the size of this element.
     *
     * @return this element's size.
     */
    double getSize();

    /**
     * Get a description for this element.
     *
     * @return this element's description.
     */
    String getDescription();

    /**
     * Get the identifier for this element.
     *
     * @return this element's identifier.
     */
    I getIdentifier();
}