package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import java.util.UUID;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.player.SpectateMessage;

public class SpectateCodecTest extends CodecTest<SpectateMessage> {

    @Override
    protected Codec<SpectateMessage> createCodec() {
        return new SpectateCodec();
    }

    @Override
    protected SpectateMessage createMessage() {
        return new SpectateMessage(UUID.randomUUID());
    }
}
