package io.github.nettyplus.netty4.transports;

import io.netty.channel.IoHandler;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.uring.IoUring;
import io.netty.util.concurrent.ImmediateEventExecutor;
import io.netty.util.concurrent.ThreadAwareExecutor;
import java.util.Collection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class NettyTransportTest {
    @Test
    public void nioIsAvailable() {
        assertTrue(NettyTransport.NIO.isAvailable());
        assertThat(NettyTransport.availableTransports())
            .contains(NettyTransport.NIO);
    }

    @Test
    @EnabledOnOs(value = { OS.LINUX } )
    public void epollIsAvailableOnLinux() {
        assertTrue(Epoll.isAvailable());
        assertTrue(NettyTransport.EPOLL.isAvailable());
    }

    @Test
    @EnabledOnOs(value = { OS.LINUX } )
    public void ioUringIsAvailableOnLinux() {
        assertTrue(IoUring.isAvailable());
        assertTrue(NettyTransport.IO_URING.isAvailable());
    }

    @Test
    @EnabledOnOs(value = { OS.LINUX } )
    public void linuxTransports() {
        assertThat(NettyTransport.availableTransports())
            .containsExactlyInAnyOrder(
                NettyTransport.NIO,
                NettyTransport.IO_URING,
                NettyTransport.EPOLL);
    }

    @Test
    @EnabledOnOs(value = { OS.WINDOWS } )
    public void windowsTransports() {
        assertThat(NettyTransport.availableTransports())
            .containsExactlyInAnyOrder(NettyTransport.NIO);
    }

    @Test
    @EnabledOnOs(value = { OS.MAC } )
    public void macTransports() {
        assertThat(NettyTransport.availableTransports())
            .containsExactlyInAnyOrder(
                NettyTransport.NIO,
                NettyTransport.KQUEUE);
    }

    @ParameterizedTest
    @EnumSource(NettyTransport.class)
    public void checkTransport(final NettyTransport transport) {
        ThreadAwareExecutor executor = ImmediateEventExecutor.INSTANCE;
        assertNotNull(transport.getDatagramChannelClass());
        assertNotNull(transport.getServerSocketChannelClass());
        assertNotNull(transport.getSocketChannelClass());
        if (transport.isAvailable()) {
            IoHandler handler = transport.createIoHandlerFactory().newHandler(executor);
            handler.initialize();
            handler.destroy();
        }
    }
}