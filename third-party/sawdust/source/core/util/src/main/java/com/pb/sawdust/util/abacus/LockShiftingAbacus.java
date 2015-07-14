package com.pb.sawdust.util.abacus;

import com.pb.sawdust.util.Range;
import com.pb.sawdust.util.array.ArrayUtil;

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.NoSuchElementException;

/**
 * The {@code LockShiftingAbacus} class provides an abacus in which the dimension cycling order can be modified. The dimensions
 * whose iteration are placed higher than their normal precedence are called "lock-shifted" because during the iteration
 * cycle the dimension looks as if it was held by a series of dimensional locks, each lock value incremented (shifted) by
 * one. For example, if a lock-shifting abacus is created with the dimensions 2,5,2 and the second dimension is set as a
 * lock-shifting dimension, then the following states will start the iteration sequence of the abacus:
 * <pre>
 *     <code>
 *         [0,0,0]
 *     </code>
 *     <code>
 *         [0,0,1]
 *     </code>
 *     <code>
 *         [1,0,0]
 *     </code>
 *     <code>
 *         [1,0,1]
 *     </code>
 *     <code>
 *         [0,1,0]
 *     </code>
 *     <code>
 *         ...
 *     </code>
 * </pre>
 *
 * @author crf <br/>
 *         Started: Jun 5, 2009 9:50:04 AM
 */
public class LockShiftingAbacus extends Abacus {
    private LockableAbacus abacus;
    private final Abacus lockShiftAbacus;
    private final int[] lockShiftingDimensions;
    private final Set<Integer> lockShiftingDimensionsSet;
    private final Range lockShiftRange;

    private boolean incremented;
    private boolean hasNext;

    /**
     * Constructor specifying the abacus dimensions and the dimensions which will be lock-shifted. The order of the
     * lock-shifted dimensions defines their precedence: dimensions listed earlier will be incremented before those listed
     * later.
     *
     * @param dimensions
     *        The dimensions of the abacus. Each entry specifies the value at which a "rollover" occurs.
     *
     * @param lockShiftingDimensions
     *        The dimensions to lock-shift. These dimensions will be incremented out of normal abacus order, after the
     *        non-lock shifted dimensions have completed a full (sub) cycle, and in the order they are listed here.
     *
     * @throws IllegalArgumentException if {@code dimensions.length == 0}, if any entry in {@code dimensions} is less
     *                                  than one, if any dimension in {@code lockShiftingDimensions} is repeated, or if
     *                                  any dimension in {@code lockShiftingDimensions} is not on the interval
     *                                  <code>[0,dimensions.length)</code>.
     */
    public LockShiftingAbacus(int[] dimensions, int ... lockShiftingDimensions) {
        //the super abacus becomes an equivalent abacus
        //an internal locking abacus is used for this abacus
        super(getEquivalentAbacusDimensions(dimensions,lockShiftingDimensions));
        abacus = new LockableAbacus(dimensions);
        this.lockShiftingDimensions = ArrayUtil.copyArray(lockShiftingDimensions);
        int[] lockShiftDimensions = new int[lockShiftingDimensions.length];
        lockShiftingDimensionsSet = new HashSet<Integer>();
        int counter = 0;
        for (int d : lockShiftingDimensions) {
            lockShiftDimensions[counter++] = dimensions[d];
            lockShiftingDimensionsSet.add(d);
        }
        if (lockShiftDimensions.length == 0) {
            lockShiftAbacus = placeholderAbacus;
            lockShiftRange = new Range(0); 
        } else {
            lockShiftAbacus = new Abacus(true,lockShiftDimensions);
            lockShiftRange = lockShiftAbacus.range;
        }
        setNextLockShift();
        incremented = true;
        hasNext = true;
    }

    private static int[] getEquivalentAbacusDimensions(int[] dimensions, int[] lockShiftingDimensions) {
        int[] equivalentDimensions = new int[dimensions.length];
        Set<Integer> locks = new HashSet<Integer>();
        int counter = lockShiftingDimensions.length-1;
        for (int d : lockShiftingDimensions) {
            if (!locks.add(d))
                throw new IllegalArgumentException("Lock shifting dimensions must be unique: " + d);
            if (d < 0 || d >= dimensions.length)
                throw new IllegalArgumentException(String.format("Lock shifting dimension (%d) out of bounds for abacus with %d dimensions.",d,dimensions.length));
            equivalentDimensions[counter--] = dimensions[d];
        }
        counter = lockShiftingDimensions.length;
        for (int i : new Range(dimensions.length))
            if (!locks.contains(i))
                equivalentDimensions[counter++] = dimensions[i];
        return equivalentDimensions;
    }

    public int[] next() {
        if (!hasNext())
            throw new NoSuchElementException();
        incremented = false;
        return abacus.next();
    }

    private void setNextLockShift() {
        int[] nextLockShift = lockShiftAbacus.next();
        abacus = abacus.freshClone();  //reset
        for (int i : lockShiftRange)
            abacus.lockDimension(lockShiftingDimensions[i],nextLockShift[i]);
    }

    public boolean hasNext() {
        if (!incremented) {
            hasNext = true;
            if (!abacus.hasNext()) {
                if (lockShiftAbacus.hasNext()) {
                    setNextLockShift();
                } else {
                    //all done
                    hasNext = false;
                }
            }
            incremented = true;
        }
        return hasNext;
    }

    public LockShiftingAbacus freshClone() {
        return new LockShiftingAbacus(abacus.dimensions,lockShiftingDimensions);
    }

    public int[] getAbacusPoint(long iteratorPosition) {
        int[] equivalentPoint = super.getAbacusPoint(iteratorPosition);
        int[] point = new int[equivalentPoint.length];
        int counter = equivalentPoint.length-1;
        for (int i : range)
            if (!lockShiftingDimensionsSet.contains(i))
                point[i] = equivalentPoint[counter--];
        counter = 0;
        for (int d : ArrayUtil.getReverse(lockShiftingDimensions))
            point[d] = equivalentPoint[counter++];
        return point;
    }

    public void setAbacusAtPosition(long iteratorPosition) {
        setLockShiftAbacusPointUnchecked(getAbacusPoint(iteratorPosition));
    }

    public void setAbacusPoint(int[] point) {
        if (point.length != dimensions.length)
            throw new IllegalArgumentException("Abacus point must have an element for each dimension.");
        for (int i : range)
            if (point[i] < 0 || point[i] >= abacus.dimensions[i])
                throw new IllegalArgumentException("Abacus point element (dimension " + i + ") out of bounds:" + point[i]);
        setLockShiftAbacusPointUnchecked(point);
    }

    private void setLockShiftAbacusPointUnchecked(int[] point) {
        abacus = new LockableAbacus(abacus.dimensions);
        abacus.setAbacusPoint(point);
        int[] lockShiftPoint = new int[lockShiftingDimensions.length];
        int counter = 0;
        for (int d : lockShiftingDimensions) {
            abacus.lockDimension(d,point[d]);
            lockShiftPoint[counter++] = point[d];
        }
        lockShiftAbacus.setAbacusPoint(lockShiftPoint);
        lockShiftAbacus.next();
        incremented = true;
        hasNext = true;
    }

    //this is necessary to get an empty lockshift to work correctly
    private static Abacus placeholderAbacus = new Abacus(1) {
        private final int[] nextPlaceholder = new int[0];

        public boolean hasNext() {
            return false;
        }

        public int[] next() {
            return nextPlaceholder;
        }

        void increment() { }

        public long getStateCount() {
            return 0;
        }

        public Abacus freshClone() {
            return this;
        }

        public int[] getAbacusPoint(long iteratorPosition) {
            throw new IllegalArgumentException("Iterator position beyond bounds of abacus: " + iteratorPosition);
        }

        public void setAbacusAtPosition(long iteratorPosition) {}

        public void setAbacusPoint(int[] point) {}

        public long getLength() {
            return 0;
        }
    };

    public static void main(String ... args) {
        LockShiftingAbacus aa = new LockShiftingAbacus(new int[] {2,4,2},1,2);
        for (int[] b : new IterableAbacus(aa)) {
            System.out.println(Arrays.toString(b));
        }
    }
}