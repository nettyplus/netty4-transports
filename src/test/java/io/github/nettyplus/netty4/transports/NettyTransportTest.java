package io.github.nettyplus.netty4.transports;

import io.netty.channel.IoHandler;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.uring.IoUring;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class NettyTransportTest {
    @Test
    public void nioIsAvailable() {
        assertTrue(NettyTransport.NIO.isAvailable());
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
        assertEquals(Set.of(NettyTransport.NIO, NettyTransport.IO_URING, NettyTransport.EPOLL),
                NettyTransport.availableTransports().collect(Collectors.toSet()));
    }

    @Test
    @EnabledOnOs(value = { OS.MAC } )
    public void macTransports() {
        assertEquals(Set.of(NettyTransport.NIO),
                NettyTransport.availableTransports().collect(Collectors.toSet()));
    }
    @Test
    public void testCreateIoHandlerFactory() {
        for (NettyTransport transport : NettyTransport.availableTransports().toList()) {
            IoHandler handler = transport.createIoHandlerFactory().newHandler();
            handler.prepareToDestroy();
            handler.destroy();
        }
    }
}