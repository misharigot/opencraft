package science.atlarge.opencraft.opencraft.io.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NonNls;
import science.atlarge.opencraft.opencraft.GlowWorld;
import science.atlarge.opencraft.opencraft.entity.GlowEntity;
import science.atlarge.opencraft.opencraft.entity.monster.GlowBlaze;
import science.atlarge.opencraft.opencraft.entity.monster.GlowCaveSpider;
import science.atlarge.opencraft.opencraft.entity.monster.GlowGiant;
import science.atlarge.opencraft.opencraft.entity.monster.GlowHusk;
import science.atlarge.opencraft.opencraft.entity.monster.GlowMagmaCube;
import science.atlarge.opencraft.opencraft.entity.monster.GlowSilverfish;
import science.atlarge.opencraft.opencraft.entity.monster.GlowSkeleton;
import science.atlarge.opencraft.opencraft.entity.monster.GlowSlime;
import science.atlarge.opencraft.opencraft.entity.monster.GlowSnowman;
import science.atlarge.opencraft.opencraft.entity.monster.GlowSpider;
import science.atlarge.opencraft.opencraft.entity.monster.GlowStray;
import science.atlarge.opencraft.opencraft.entity.monster.GlowWitch;
import science.atlarge.opencraft.opencraft.entity.monster.GlowWitherSkeleton;
import science.atlarge.opencraft.opencraft.entity.objects.GlowMinecart;
import science.atlarge.opencraft.opencraft.entity.passive.GlowCow;
import science.atlarge.opencraft.opencraft.entity.passive.GlowDonkey;
import science.atlarge.opencraft.opencraft.entity.passive.GlowLlama;
import science.atlarge.opencraft.opencraft.entity.passive.GlowMooshroom;
import science.atlarge.opencraft.opencraft.entity.passive.GlowMule;
import science.atlarge.opencraft.opencraft.entity.passive.GlowPolarBear;
import science.atlarge.opencraft.opencraft.entity.passive.GlowSkeletonHorse;
import science.atlarge.opencraft.opencraft.entity.passive.GlowSquid;
import science.atlarge.opencraft.opencraft.entity.passive.GlowZombieHorse;
import science.atlarge.opencraft.opencraft.entity.projectile.GlowEgg;
import science.atlarge.opencraft.opencraft.entity.projectile.GlowEnderPearl;
import science.atlarge.opencraft.opencraft.entity.projectile.GlowFireball;
import science.atlarge.opencraft.opencraft.entity.projectile.GlowLingeringPotion;
import science.atlarge.opencraft.opencraft.entity.projectile.GlowSnowball;
import science.atlarge.opencraft.opencraft.entity.projectile.GlowSpectralArrow;
import science.atlarge.opencraft.opencraft.entity.projectile.GlowSplashPotion;
import science.atlarge.opencraft.opencraft.entity.projectile.GlowThrownExpBottle;
import science.atlarge.opencraft.opencraft.entity.projectile.GlowTippedArrow;
import science.atlarge.opencraft.opencraft.entity.projectile.GlowWitherSkull;
import science.atlarge.opencraft.opencraft.io.nbt.NbtSerialization;
import science.atlarge.opencraft.opencraft.util.nbt.CompoundTag;

/**
 * The class responsible for mapping entity types to their storage methods and reading and writing
 * entity data using those storage methods.
 */
public final class EntityStorage {

    /**
     * A table which maps entity ids to compound readers. This is generally used to map stored
     * entities to actual entities.
     */
    private static final Map<String, EntityStore<?>> idTable = new HashMap<>();

    /**
     * A table which maps entities to stores. This is generally used to map entities being stored.
     */
    private static final Map<Class<? extends GlowEntity>, EntityStore<?>> classTable
            = new HashMap<>();
    private static final CompoundTag EMPTY_TAG = new CompoundTag();

    /*
     * Populates the maps with stores.
     */
    static {
        bind(new PlayerStore());

        // LivingEntities - Passive Entities
        bind(new BatStore());
        bind(new ChickenStore());
        bind(new PigStore());
        bind(new RabbitStore());
        bind(new SheepStore());
        bind(new OcelotStore());
        bind(new WolfStore());
        bind(new VillagerStore());
        bind(new AgeableStore<>(GlowCow.class, EntityType.COW, GlowCow::new));
        bind(new AgeableStore<>(GlowMooshroom.class, EntityType.MUSHROOM_COW, GlowMooshroom::new));
        bind(new WaterMobStore<>(GlowSquid.class, EntityType.SQUID, GlowSquid::new));
        bind(new AgeableStore<>(GlowPolarBear.class, EntityType.POLAR_BEAR, GlowPolarBear::new));
        bind(new AbstractHorseStore<>(GlowZombieHorse.class, EntityType.ZOMBIE_HORSE,
                GlowZombieHorse::new));
        bind(new AbstractHorseStore<>(GlowSkeletonHorse.class, EntityType.SKELETON_HORSE,
                GlowSkeletonHorse::new));
        bind(new ChestedHorseStore<>(GlowLlama.class, EntityType.LLAMA, GlowLlama::new));
        bind(new ChestedHorseStore<>(GlowMule.class, EntityType.MULE, GlowMule::new));
        bind(new ChestedHorseStore<>(GlowDonkey.class, EntityType.DONKEY, GlowDonkey::new));
        bind(new HorseStore());
        bind(new ParrotStore());

        // LivingEntities - Hostile Entities
        bind(new CreeperStore());
        bind(new EndermanStore());
        bind(new EndermiteStore());
        bind(new GhastStore());
        bind(new GuardianStore());
        bind(new ElderGuardianStore());
        bind(new IronGolemStore());
        bind(new SlimeStore<>(GlowSlime.class, EntityType.SLIME, GlowSlime::new));
        bind(new SlimeStore<>(GlowMagmaCube.class, EntityType.MAGMA_CUBE, GlowMagmaCube::new));
        bind(new ZombieStore<>());
        bind(new ZombieStore<>(GlowHusk.class, EntityType.HUSK, GlowHusk::new));
        bind(new PigZombieStore());
        bind(new MonsterStore<>(GlowSkeleton.class, EntityType.SKELETON, GlowSkeleton::new));
        bind(new MonsterStore<>(GlowStray.class, EntityType.STRAY, GlowStray::new));
        bind(new MonsterStore<>(GlowWitherSkeleton.class, EntityType.WITHER_SKELETON,
                GlowWitherSkeleton::new));
        bind(new MonsterStore<>(GlowBlaze.class, EntityType.BLAZE, GlowBlaze::new));
        bind(new MonsterStore<>(GlowCaveSpider.class, EntityType.CAVE_SPIDER, GlowCaveSpider::new));
        bind(new MonsterStore<>(GlowSpider.class, EntityType.SPIDER, GlowSpider::new));
        bind(new MonsterStore<>(GlowSnowman.class, EntityType.SNOWMAN, GlowSnowman::new));
        bind(new MonsterStore<>(GlowGiant.class, EntityType.GIANT, GlowGiant::new));
        bind(new MonsterStore<>(GlowSilverfish.class, EntityType.SILVERFISH, GlowSilverfish::new));
        bind(new MonsterStore<>(GlowWitch.class, EntityType.WITCH, GlowWitch::new));
        bind(new ShulkerStore());
        bind(new WitherStore());
        bind(new VexStore());
        bind(new VindicatorStore());
        bind(new EvokerStore());
        bind(new EnderDragonStore());
        bind(new ZombieVillagerStore());

        bind(new AreaEffectCloudStore());
        bind(new ArmorStandStore());
        bind(new FallingBlockStore());
        bind(new ItemFrameStore());
        bind(new ItemStore());
        bind(new TntPrimedStorage());
        bind(new EnderCrystalStore());
        bind(new BoatStore());
        for (GlowMinecart.MinecartType type : GlowMinecart.MinecartType.values()) {
            if (type != null) {
                bind(new MinecartStore(type));
            }
        }
        bind(new ProjectileStore<>(GlowSnowball.class, "snowball", GlowSnowball::new));
        bind(new ProjectileStore<>(GlowEgg.class, "egg", GlowEgg::new));
        bind(new ProjectileStore<>(GlowEnderPearl.class, "ender_pearl", GlowEnderPearl::new));
        bind(new ProjectileStore<>(GlowThrownExpBottle.class, "xp_bottle",
                GlowThrownExpBottle::new));
        bind(new SplashPotionStore<>(GlowSplashPotion.class, "splash_potion",
                GlowSplashPotion::new));
        bind(new SplashPotionStore<>(GlowLingeringPotion.class, "lingering_potion",
                GlowLingeringPotion::new));
        final FireballStore<GlowFireball> fireballStore
                = new FireballStore<>(GlowFireball.class, "fireball", GlowFireball::new);
        bind(fireballStore);
        idTable.put("small_fireball", fireballStore); // NON-NLS
        bind(new FireballStore<>(GlowWitherSkull.class, "wither_skull", GlowWitherSkull::new));
        bind(new ArrowStore<>(GlowSpectralArrow.class, "spectral_arrow", GlowSpectralArrow::new));
        bind(new PaintingStore());
        bind(new ExperienceOrbStore());
        bind(new FireworkStore());

        // Normal and tipped arrows use same storage
        final NormalTippedArrowStore arrowStore = new NormalTippedArrowStore();
        bind(arrowStore);
        classTable.put(GlowTippedArrow.class, arrowStore);
    }

    private EntityStorage() {
    }

    /**
     * Creates an entity of the given Glowstone class, by deserializing an empty tag.
     *
     * @param clazz    the type of entity
     * @param <T>      the type of entity
     * @param location the entity's initial location
     */
    public static <T extends GlowEntity> T create(Class<T> clazz, Location location) {
        return (T) find(clazz, clazz.getSimpleName()).createEntity(location, EMPTY_TAG);
    }

    /**
     * Binds a store by adding entries for it to the tables.
     *
     * @param store The store object.
     * @param <T>   The type of entity.
     */
    public static <T extends GlowEntity> void bind(EntityStore<T> store) {
        idTable.put(store.getEntityType(), store);
        classTable.put(store.getType(), store);
    }

    /**
     * Load a new entity in the given world from the given data tag.
     *
     * @param world    The target world.
     * @param compound The tag to load from.
     * @return The newly constructed entity.
     * @throws IllegalArgumentException if there is an error in the data.
     */
    public static GlowEntity loadEntity(GlowWorld world, CompoundTag compound) {
        // look up the store by the tag's id
        if (!compound.isString("id")) {
            throw new IllegalArgumentException("Entity has no type");
        }
        String id = compound.getString("id");
        if (id.startsWith("minecraft:")) { // NON-NLS
            id = id.substring("minecraft:".length()); // NON-NLS
        }
        EntityStore<?> store = idTable.get(id);
        if (store == null) {
            throw new UnknownEntityTypeException(compound);
        }

        // verify that, if the tag contains a world, it's correct
        World checkWorld = NbtSerialization.readWorld(world.getServer(), compound);
        if (checkWorld != null && checkWorld != world) {
            throw new IllegalArgumentException(
                    "Entity in wrong world: stored in " + world + " but data says " + checkWorld);
        }

        // find out the entity's location
        Location location = NbtSerialization.listTagsToLocation(world, compound);
        if (location == null) {
            throw new IllegalArgumentException("Entity has no location");
        }

        // create the entity instance and read the rest of the data
        return createEntity(store, location, compound);
    }

    /**
     * Helper method to call EntityStore methods for type safety.
     */
    private static <T extends GlowEntity> T createEntity(EntityStore<T> store, Location location,
            CompoundTag compound) {
        Lock lock = ((GlowWorld) location.getWorld()).getEntityManager().getLock().writeLock();
        lock.lock();
        try {
            T entity = store.createEntity(location, compound);
            store.load(entity, compound);
            return entity;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Finds a store by entity class, throwing an exception if not found.
     */
    private static EntityStore<?> find(Class<? extends GlowEntity> clazz, @NonNls String type) {
        EntityStore<?> store = classTable.get(clazz);
        if (store == null) {
            // TODO maybe try to look up a parent class's store if one isn't found
            throw new IllegalArgumentException("Unknown entity type to " + type + ": " + clazz);
        }
        return store;
    }

    /**
     * Unsafe-cast an unknown EntityStore to the base type.
     */
    private static EntityStore<GlowEntity> getBaseStore(EntityStore<?> store) {
        return (EntityStore<GlowEntity>) store;
    }

    /**
     * Save an entity's data to the given compound tag.
     *
     * @param entity   The entity to save.
     * @param compound The target tag.
     */
    public static void save(GlowEntity entity, CompoundTag compound) {
        // look up the store for the entity
        EntityStore<?> store = find(entity.getClass(), "save");

        // EntityStore knows how to save world and location information
        getBaseStore(store).save(entity, compound);
    }

    /**
     * Load an entity's data from the given compound tag.
     *
     * @param entity   The target entity.
     * @param compound The tag to load from.
     */
    public static void load(GlowEntity entity, CompoundTag compound) {
        // look up the store for the entity
        EntityStore<?> store = find(entity.getClass(), "load");

        // work out the entity's location, using its current location if unavailable
        World world = NbtSerialization.readWorld(entity.getServer(), compound);
        if (world == null) {
            world = entity.getWorld();
        }
        Location location = NbtSerialization.listTagsToLocation(world, compound);
        if (location != null) {
            entity.teleport(location);
        }

        // read the rest of the entity's information
        getBaseStore(store).load(entity, compound);
    }

}
