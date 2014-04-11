package net.glowstone.util.nbt;

/**
 * The {@code TAG_End} tag.
 * @author Graham Edgecombe
 */
final class EndTag extends Tag<Void> {

    /**
     * Creates the tag.
     */
    private EndTag() {
        super(TagType.END);
    }

    @Override
    public Void getValue() {
        return null;
    }

    @Override
    public String toString() {
        return "TAG_End";
    }

}

