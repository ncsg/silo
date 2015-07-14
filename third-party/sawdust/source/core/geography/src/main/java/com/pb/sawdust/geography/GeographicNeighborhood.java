package com.pb.sawdust.geography;

import java.util.*;

/**
 * The {@code GeographicNeighborhood} class is used to specify a collection of geographic elements from a single geography.
 * This class can be viewed a sub-geography, though it does not have an explicit connection to a parent geography and does
 * not extend {@code Geography} (the reason for this is that it does not maintain information about the identifiers for
 * the elements it contains, nor any specific ordering).
 *
 * @param <G>
 *        The type of the geographic elements this neighborhood contains.
 *
 * @author crf
 *         Started 10/24/11 10:30 AM
 */
public class GeographicNeighborhood<G extends GeographyElement<?>> {
    private final Set<G> neighborhood;

    public GeographicNeighborhood(Collection<G> neighborhood) {
        this.neighborhood = Collections.unmodifiableSet(new HashSet<>(neighborhood));
    }

    /**
     * Get the elements this neighborhood contains.
     *
     * @return this neighborhood's elements.
     */
    public Set<G> getNeighborhood() {
        return neighborhood;
    }

    /**
     * Get a geographic neighborhood that contains all of the elements from this neighborhood and other neighborhoods.
     *
     * @param otherNeighborhoods
     *        The other neighborhoods which will be merged with this one.
     *
     * @return a geographic neighborhood which merges this neighborhood with those in {@code otherNeighborhoods}.
     */
    public GeographicNeighborhood<G> mergeWith(Collection<GeographicNeighborhood<G>> otherNeighborhoods) {
        Set<G> newNeighborhood = new HashSet<>(neighborhood);
        for (GeographicNeighborhood<G> g : otherNeighborhoods)
            newNeighborhood.addAll(g.getNeighborhood());
        return new GeographicNeighborhood<>(newNeighborhood);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return neighborhood.equals(((GeographicNeighborhood) o).neighborhood);
    }

    @Override
    public int hashCode() {
        return neighborhood.hashCode();
    }

    @Override
    public String toString() {
        return "<GeographicNeighborhood: " + neighborhood + ">";
    }


}