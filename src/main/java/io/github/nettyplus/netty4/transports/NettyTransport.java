package io.github.nettyplus.netty4.transports;

import io.netty.channel.IoHandlerFactory;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollIoHandler;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.uring.IoUring;
import io.netty.channel.uring.IoUringIoHandler;
import io.netty.channel.uring.IoUringServerSocketChannel;
import io.netty.channel.uring.IoUringSocketChannel;
import io.netty.channel.uring.IoUringDatagramChannel;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public enum NettyTransport {
    NIO(true, NioIoHandler::newFactory, NioServerSocketChannel.class, NioSocketChannel.class, NioDatagramChannel.class),
    EPOLL(Epoll.isAvailable(), EpollIoHandler::newFactory, EpollServerSocketChannel.class, EpollSocketChannel.class, EpollDatagramChannel.class),
    IO_URING(IoUring.isAvailable(), IoUringIoHandler::newFactory, IoUringServerSocketChannel.class, IoUringSocketChannel.class, IoUringDatagramChannel.class);
    /* TODO uncomment this line after Netty 4.2.0 Alpha 2 is released
    KQUEUE(KQueue.isAvailable(), KQueueIoHandler::newFactory, KQueueServerSocketChannel.class);
     */

    private static final Collection<NettyTransport> AVAILABLE = Arrays.stream(values())
        .filter(t -> t.isAvailable())
        .collect(Collectors.toList());

    private final boolean available;
    private final Supplier<IoHandlerFactory> ioHandlerFactorySupplier;
    private final Class<? extends ServerSocketChannel> serverSocketChannelClass;
    private final Class<? extends SocketChannel> socketChannelClass;
    private final Class<? extends DatagramChannel> datagramChannelClass;

    NettyTransport(boolean available,
        Supplier<IoHandlerFactory> ioHandlerFactory,
        Class<? extends ServerSocketChannel> serverSocketChannelClass,
        Class<? extends SocketChannel> socketChannelClass,
        Class<? extends DatagramChannel> datagramChannelClass) {
        this.available = available;
        this.ioHandlerFactorySupplier= ioHandlerFactory;
        this.serverSocketChannelClass = serverSocketChannelClass;
        this.socketChannelClass = socketChannelClass;
        this.datagramChannelClass = datagramChannelClass;
    }

    public boolean isAvailable() {
        return available;
    }

    public Class<? extends ServerSocketChannel> getServerSocketChannelClass() {
        return this.serverSocketChannelClass;
    }

    public Class<? extends SocketChannel> getSocketChannelClass() {
        return this.socketChannelClass;
    }

    public Class<? extends DatagramChannel> getDatagramChannelClass() {
        return this.datagramChannelClass;
    }

    public IoHandlerFactory createIoHandlerFactory() {
        return this.ioHandlerFactorySupplier.get();
    }

    public static Collection<NettyTransport> availableTransports() {
        return AVAILABLE;
    }
}