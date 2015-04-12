package net.glowstone.generator.populators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.glowstone.generator.decorators.nether.FireDecorator;
import net.glowstone.generator.decorators.nether.MushroomDecorator;
import net.glowstone.generator.populators.nether.OrePopulator;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class NetherPopulator extends BlockPopulator {

    private final List<BlockPopulator> inGroundPopulators = new ArrayList<>();
    private final List<BlockPopulator> onGroundPopulators = new ArrayList<>();

    private final OrePopulator orePopulator = new OrePopulator();

    private final FireDecorator fireDecorator = new FireDecorator();
    private final MushroomDecorator brownMushroomDecorator = new MushroomDecorator(Material.BROWN_MUSHROOM);
    private final MushroomDecorator redMushroomDecorator = new MushroomDecorator(Material.RED_MUSHROOM);

    public NetherPopulator() {
        inGroundPopulators.add(orePopulator);

        onGroundPopulators.add(fireDecorator);
        onGroundPopulators.add(brownMushroomDecorator);
        onGroundPopulators.add(redMushroomDecorator);

        fireDecorator.setAmount(1);
        brownMushroomDecorator.setAmount(1);
        redMushroomDecorator.setAmount(1);
    }

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        populateInGround(world, random, chunk);
        populateOnGround(world, random, chunk);
    }

    private void populateInGround(World world, Random random, Chunk chunk) {
        for (BlockPopulator populator : inGroundPopulators) {
            populator.populate(world, random, chunk);
        }
    }

    private void populateOnGround(World world, Random random, Chunk chunk) {
        for (BlockPopulator populator : onGroundPopulators) {
            populator.populate(world, random, chunk);
        }
    }
}
