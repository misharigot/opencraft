package net.glowstone.net.message.play.player;

import com.flowpowered.network.Message;
import lombok.Data;

@Data
public final class PlayerSwingArmMessage implements Message {

    private final int hand;
}
