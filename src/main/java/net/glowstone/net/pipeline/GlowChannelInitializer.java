package net.glowstone.net.pipeline;

import com.flowpowered.networking.ConnectionManager;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import net.glowstone.net.protocol.ProtocolType;

/**
 * Used to initialize the channels.
 */
public class GlowChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final ConnectionManager connectionManager;

    public GlowChannelInitializer(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    protected final void initChannel(SocketChannel c) {
        MessageHandler handler = new MessageHandler(connectionManager);
        CodecsHandler codecs = new CodecsHandler(ProtocolType.HANDSHAKE.getProtocol());
        FramingHandler framing = new FramingHandler();

        c.pipeline()
                .addLast("encryption", NoopHandler.instance)
                .addLast("framing", framing)
                .addLast("compression", NoopHandler.instance)
                .addLast("codecs", codecs)
                .addLast("handler", handler);
    }
}
