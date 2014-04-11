package net.glowstone.util.nbt;

import org.apache.commons.lang.Validate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code TAG_Compound} tag.
 * @author Graham Edgecombe
 */
public final class CompoundTag extends Tag<Map<String, Tag>> {

    /**
     * The value.
     */
    private final Map<String, Tag> value = new LinkedHashMap<>();

    /**
     * Creates a new, empty CompoundTag.
     */
    public CompoundTag() {
        super(TagType.COMPOUND);
    }

    @Override
    public Map<String, Tag> getValue() {
        return value;
    }

    @Override
    protected void valueToString(StringBuilder bldr) {
        bldr.append(value.size()).append(" entries\n{\n");
        for (Map.Entry<String, Tag> entry : value.entrySet()) {
            bldr.append("    ").append(entry.getValue().toString().replaceAll("\n", "\n    ")).append("\n");
        }
        bldr.append("}");
    }

    ////////////////////////////////////////////////////////////////////////////
    // Helper stuff

    public boolean isEmpty() {
        return value.isEmpty();
    }

    /**
     * Check if the compound contains the given key.
     * @param key The key.
     * @return True if the key is in the map.
     */
    public boolean containsKey(String key) {
        return value.containsKey(key);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Simple gets

    public byte getByte(String key) {
        return get(key, ByteTag.class);
    }

    public short getShort(String key) {
        return get(key, ShortTag.class);
    }

    public int getInt(String key) {
        return get(key, IntTag.class);
    }

    public long getLong(String key) {
        return get(key, LongTag.class);
    }

    public float getFloat(String key) {
        return get(key, FloatTag.class);
    }

    public double getDouble(String key) {
        return get(key, DoubleTag.class);
    }

    public byte[] getByteArray(String key) {
        return get(key, ByteArrayTag.class);
    }

    public String getString(String key) {
        return get(key, StringTag.class);
    }

    public int[] getIntArray(String key) {
        return get(key, IntArrayTag.class);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Fancy gets

    @SuppressWarnings("unchecked")
    public <V> List<V> getList(String key, TagType type) {
        ListTag tag = getTag(key, ListTag.class);
        if (tag.getChildType() != type) {
            throw new IllegalArgumentException("List \"" + key + "\" contains " + tag.getChildType() + ", not " + type);
        }
        return tag.getValue();
    }

    public CompoundTag getCompound(String key) {
        return getTag(key, CompoundTag.class);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Simple is

    public boolean isByte(String key) {
        return is(key, ByteTag.class);
    }

    public boolean isShort(String key) {
        return is(key, ShortTag.class);
    }

    public boolean isInt(String key) {
        return is(key, IntTag.class);
    }

    public boolean isLong(String key) {
        return is(key, LongTag.class);
    }

    public boolean isFloat(String key) {
        return is(key, FloatTag.class);
    }

    public boolean isDouble(String key) {
        return is(key, DoubleTag.class);
    }

    public boolean isByteArray(String key) {
        return is(key, ByteArrayTag.class);
    }

    public boolean isString(String key) {
        return is(key, StringTag.class);
    }

    public boolean isIntArray(String key) {
        return is(key, IntArrayTag.class);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Fancy is

    public boolean isList(String key, TagType type) {
        if (!is(key, ListTag.class)) return false;
        ListTag tag = getTag(key, ListTag.class);
        return tag.getChildType() == type;
    }

    public boolean isCompound(String key) {
        return is(key, CompoundTag.class);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Simple sets

    public void putByte(String key, int value) {
        put(key, new ByteTag((byte) value));
    }

    public void putShort(String key, int value) {
        put(key, new ShortTag((short) value));
    }

    public void putInt(String key, int value) {
        put(key, new IntTag(value));
    }

    public void putLong(String key, long value) {
        put(key, new LongTag(value));
    }

    public void putFloat(String key, double value) {
        put(key, new FloatTag((float) value));
    }

    public void putDouble(String key, double value) {
        put(key, new DoubleTag(value));
    }

    public void putByteArray(String key, byte[] value) {
        put(key, new ByteArrayTag(value));
    }

    public void putString(String key, String value) {
        put(key, new StringTag(value));
    }

    public void putIntArray(String key, int[] value) {
        put(key, new IntArrayTag(value));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Fancy sets

    public <V> void putList(String key, TagType type, List<V> value) {
        put(key, new ListTag<>(type, value));
    }

    public void putCompound(String key, CompoundTag tag) {
        put(key, tag);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor helpers

    private <T extends Tag<?>> boolean is(String key, Class<T> clazz) {
        if (!containsKey(key)) return false;
        final Tag tag = value.get(key);
        return tag != null && clazz == tag.getClass();
    }

    void put(String key, Tag tag) {
        Validate.notNull(key, "Key cannot be null");
        Validate.notNull(tag, "Tag cannot be null");
        value.put(key, tag);
    }

    private <V, T extends Tag<V>> V get(String key, Class<T> clazz) {
        return getTag(key, clazz).getValue();
    }

    @SuppressWarnings("unchecked")
    private <T extends Tag<?>> T getTag(String key, Class<T> clazz) {
        if (!is(key, clazz)) {
            throw new IllegalArgumentException("Compound does not contain " + clazz.getSimpleName() + " \"" + key + "\"");
        }
        return (T) value.get(key);
    }
}

