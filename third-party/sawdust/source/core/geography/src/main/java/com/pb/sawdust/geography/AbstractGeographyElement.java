package com.pb.sawdust.geography;

/**
 * The {@code AbstractGeographyElement} is a skeletal implementation of {@code GeographyElement}. Classes extending this
 * class need only specify appropriate constructors to fill in the necessary data.
 *
 * @author crf
 *         Started 10/17/11 11:02 AM
 */
public abstract class AbstractGeographyElement<I> implements GeographyElement<I> {
    private final double size;
    private final I identifier;
    private final String description;

    /**
     * Constructor specifying the identifier, size, and description for the element.
     *
     * @param identifier
     *        The element's identifier.
     *
     * @param size
     *        The element's size.
     *
     * @param description
     *        The element's description.
     */
    public AbstractGeographyElement(I identifier, double size, String description) {
        this.size = size;
        this.identifier = identifier;
        this.description = description;
    }

    public double getSize() {
        return size;
    }

    public I getIdentifier() {
        return identifier;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "<GeographicElement: " + identifier + ">";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AbstractGeographyElement that = (AbstractGeographyElement) o;
        if (Double.compare(that.size,size) != 0)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (identifier != null ? !identifier.equals(that.identifier) : that.identifier != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = size != +0.0d ? Double.doubleToLongBits(size) : 0L;
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + (identifier != null ? identifier.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}