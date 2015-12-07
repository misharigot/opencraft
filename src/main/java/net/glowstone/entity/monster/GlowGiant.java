package net.glowstone.entity.monster;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;

public class GlowGiant extends GlowMonster implements Giant {

    public GlowGiant(Location loc) {
        super(loc, EntityType.GIANT);
        setSize(3.6F, 10.8F);
    }
}
