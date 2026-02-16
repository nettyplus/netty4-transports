# netty4-transports

A unified API for selecting and managing Netty transport layers across different platforms. This library simplifies the process of choosing the optimal transport implementation for your Netty-based application by providing automatic platform detection and a consistent interface.

## Features

- **Unified Transport API**: Single interface to work with all Netty transport implementations
- **Automatic Platform Detection**: Automatically detects which transports are available on the current platform
- **Multiple Transport Support**: 
  - **NIO**: Java NIO transport (available on all platforms)
  - **Epoll**: High-performance Linux-specific transport (x86_64 and aarch64)
  - **io_uring**: Modern Linux I/O interface for better performance (x86_64 and aarch64)
  - **KQueue**: macOS and BSD-specific transport (x86_64 and aarch64)
- **Channel Class Access**: Easy access to ServerSocketChannel, SocketChannel, and DatagramChannel implementations
- **IoHandlerFactory Support**: Create appropriate IoHandler instances for each transport

## Requirements

- Java 11 or higher
- Netty 4.2.x

## Installation

Add the following dependency to your Maven `pom.xml`:

```xml
<dependency>
    <groupId>io.github.nettyplus</groupId>
    <artifactId>netty4-transports</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

**Note**: This project is currently in development. Check the [releases page](https://github.com/nettyplus/netty4-transports/releases) for the latest stable version.

## Usage

### Basic Example - Check Available Transports

```java
import io.github.nettyplus.netty4.transports.NettyTransport;

public class Example {

  public static void main(String[] args) {
    for (NettyTransport transport : NettyTransport.values()) {
      System.out.println(transport.name() + " isAvailable=" + transport.isAvailable());
    }
  }
}
```

### Get Available Transports

```java
import io.github.nettyplus.netty4.transports.NettyTransport;
import java.util.Collection;

// Get only available transports on the current platform
Collection<NettyTransport> available = NettyTransport.availableTransports();
```

### Use Specific Channel Classes

```java
import io.github.nettyplus.netty4.transports.NettyTransport;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;

// Select the best available transport
NettyTransport transport = NettyTransport.availableTransports()
    .stream()
    .findFirst()
    .orElse(NettyTransport.NIO);

// Create EventLoopGroup with the appropriate IoHandler
EventLoopGroup bossGroup = new MultiThreadIoEventLoopGroup(1, transport.createIoHandlerFactory());
EventLoopGroup workerGroup = new MultiThreadIoEventLoopGroup(transport.createIoHandlerFactory());

// Use the transport's channel classes
ServerBootstrap bootstrap = new ServerBootstrap()
    .group(bossGroup, workerGroup)
    .channel(transport.getServerSocketChannelClass());
```

## Transport Compatibility

| Transport | Platform | Architecture | Performance |
|-----------|----------|--------------|-------------|
| NIO | All | All | Good |
| Epoll | Linux | x86_64, aarch64 | Excellent |
| io_uring | Linux 5.9+ | x86_64, aarch64 | Excellent |
| KQueue | macOS, BSD | x86_64, aarch64 | Excellent |

**Note**: Native transports (Epoll, io_uring, KQueue) offer better performance than NIO but are platform-specific.

## Building from Source

```bash
# Clone the repository
git clone https://github.com/nettyplus/netty4-transports.git
cd netty4-transports

# Build with Maven
mvn clean install
```

## Running Tests

```bash
mvn test
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.
