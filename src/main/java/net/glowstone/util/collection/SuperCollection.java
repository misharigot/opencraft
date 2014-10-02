package net.glowstone.util.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Generic super collection.
 */
public abstract class SuperCollection<E> implements Collection<E> {

    private final List<? extends Collection<E>> parents;
    private ResultMode resultMode = ResultMode.ANY;
    private AdditionMode additionMode;

    public SuperCollection(AdditionMode additionMode) {
        this(new ArrayList<Collection<E>>(), additionMode);
    }

    public SuperCollection(List<? extends Collection<E>> parents, AdditionMode additionMode) {
        this.parents = parents;
        this.additionMode = additionMode;
    }

    /**
     * Returns the list of parents.
     * @return Parent list.
     */
    public List<? extends Collection<E>> getParents() {
        return parents;
    }

    /**
     * Sets what will this collection returns for certain operations.
     * If mode is set to ANY, operations will return "true" as long as the parents returned "true" at least once.
     * If mode is set to ALL, operations will only return "true" if all parents also returned "true".
     */
    public void setResultMode(ResultMode resultMode) {
        this.resultMode = resultMode;
    }

    /**
     * Returns current result mode.
     * @return Result mode.
     */
    public ResultMode getResultMode() {
        return resultMode;
    }

    /**
     * Sets how this collection will behave to additions.
     * If mode is set to ALL, the addition will be performed on every parent. Default for sets.
     * If mode is set to LAST, the operation will be performed on the last parent only. Default for lists.
     */
    public void setAdditionMode(AdditionMode additionMode) {
        this.additionMode = additionMode;
    }

    /**
     * Returns current addition mode.
     * @return Addition mode.
     */
    public AdditionMode getAdditionMode() {
        return additionMode;
    }

    /**
     * Returns a new collection with the same contents as the parents.
     * @return New mutable collection.
     */
    public abstract Collection<E> asClone();

    /**
     * Returns the class this SuperCollection implements.
     * @return Collection class.
     */
    protected abstract Class<? extends Collection> getCollectionClass();

    protected boolean resultBoolean(int modified) {
        switch (resultMode) {
            case ANY:
                return modified > 0;

            case ALL:
                return modified >= parents.size();
        }

        return false;
    }
    
    @Override
    public boolean add(E object) {
        switch (additionMode) {

            case ALL: {
                int modified = 0;
                for (Collection<E> parent : parents) {
                    if (parent.add(object)) {
                        modified++;
                    }
                }

                return resultBoolean(modified);
            }

            case LAST: {
                return parents.get(parents.size() - 1).add(object);
            }
        }

        throw new IllegalStateException("This SuperCollection has an invalid addition mode!");
    }

    @Override
    public boolean addAll(Collection<? extends E> objects) {
        switch (additionMode) {

            case ALL:
                int modified = 0;

                for (Collection<E> parent : parents) {
                    if (parent.addAll(objects)) {
                        modified++;
                    }
                }
                break;

            case LAST:
                return parents.get(parents.size() - 1).addAll(objects);
        }

        throw new IllegalStateException("This SuperCollection has an invalid addition mode!");
    }

    @Override
    public void clear() {
        for (Collection<E> parent : parents) {
            parent.clear();
        }
    }

    @Override
    public boolean contains(Object object) {
        for (Collection<E> parent : parents) {
            if (parent.contains(object)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean containsAll(Collection<?> objects) {
        for (Object object : objects) {
            if (!contains(object)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object object) {
        // Avoid cloning if possible
        if (object == null) {
            return false;
        }

        if (object == this) {
            return true;
        }

        if (!(getCollectionClass().isInstance(object))) {
            return false;
        }

        // If trivial comparisons didn't work, fall back to clone (to remove duplicates) and compare
        return asClone().equals(object);
    }

    @Override
    public int hashCode() {
        int code = 0;

        for (Collection<E> parent : parents) {
            code += parent.hashCode();
        }

        return code;
    }

    @Override
    public boolean isEmpty() {
        for (Collection<E> parent : parents) {
            if (!parent.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Iterator<E> iterator() {
        // Override if possible, because this is *really* slow
        return asClone().iterator();
    }

    @Override
    public boolean remove(Object object) {
        int modified = 0;

        for (Collection<E> parent : parents) {
            if (parent.remove(object)) {
                modified++;
            }
        }

        return resultBoolean(modified);
    }

    @Override
    public boolean removeAll(Collection<?> objects) {
        int modified = 0;

        for (Collection<E> parent : parents) {
            if (parent.removeAll(objects)) {
                modified++;
            }
        }

        return resultBoolean(modified);
    }

    @Override
    public boolean retainAll(Collection<?> objects) {
        int modified = 0;

        for (Collection<E> parent : parents) {
            if (parent.retainAll(objects)) {
                modified++;
            }
        }

        return resultBoolean(modified);
    }

    @Override
    public int size() {
        // Override if possible
        return asClone().size();
    }

    @Override
    public Object[] toArray() {
        // Override if possible
        return asClone().toArray();
    }

    @Override
    public <T> T[] toArray(T[] array) {
        // Override if possible
        return asClone().<T>toArray(array);
    }

    public static enum ResultMode {
        NEVER,
        ALL,
        ANY,
        ;
    }

    public static enum AdditionMode {
        ALL,
        LAST,
        ;
    }
}
