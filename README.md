# netty4-transports

## Transports
- nio
- epoll
- io_uring
- kqueue

## Usage
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
