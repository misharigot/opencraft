package net.glowstone.messaging;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public abstract class ChannelTest {

    protected abstract Channel<Subscriber, String> createChannel();

    /**
     * Verify that a created channel is initially empty.
     */
    @Test
    void initiallyEmptyTest() {
        Channel<Subscriber, String> channel = createChannel();
        assertTrue(channel.isEmpty());
    }

    /**
     * Verify that subscribing to and unsubscribing from an initially empty channel correctly updates its state.
     */
    @Test
    void emptyTest() {

        Channel<Subscriber, String> channel = createChannel();
        Subscriber alice = new Subscriber("Alice");

        channel.subscribe(alice, alice::onMessage);
        assertFalse(channel.isEmpty());

        channel.unsubscribe(alice);
        assertTrue(channel.isEmpty());
    }

    /**
     * Verify that subscribing and unsubscribing works correctly.
     */
    @Test
    void subscribeAndUnsubscribeTest() {

        Channel<Subscriber, String> channel = createChannel();
        Subscriber alice = new Subscriber("Alice");

        channel.subscribe(alice, alice::onMessage);
        assertTrue(channel.isSubscribed(alice));

        channel.unsubscribe(alice);
        assertFalse(channel.isSubscribed(alice));
    }

    /**
     * Verify that a subscriber receives a publish message.
     */
    @Test
    void subscribeAndPublishTest() {

        Channel<Subscriber, String> channel = createChannel();
        Subscriber alice = new Subscriber("Alice");
        String message = "Message";

        channel.subscribe(alice, alice::onMessage);
        channel.publish(message);

        alice.assertReceived(message);
    }

    /**
     * Verify that multiple subscribers (all) receive a published message.
     */
    @Test
    void broadcastTest() {

        Channel<Subscriber, String> channel = createChannel();
        Subscriber alice = new Subscriber("Alice");
        Subscriber bob = new Subscriber("Bob");
        String message = "Message";

        channel.subscribe(alice, alice::onMessage);
        channel.subscribe(bob, bob::onMessage);
        channel.publish(message);

        alice.assertReceived(message);
        bob.assertReceived(message);
    }

    /**
     * Verify that a message can be published to a channel with no subscribers.
     */
    @Test
    void publishToEmptyTest() {
        Channel<Subscriber, String> channel = createChannel();
        assertDoesNotThrow(() -> channel.publish("Message"));
    }
}
