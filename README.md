# kria

An asynchronous Clojure client for Riak 2.0 built on top of the [Riak Protocol
Buffer interface][Riak-PB] using Java 7's NIO.2.

Kria only only has four dependencies:

* Java 7 or later (for NIO.2)
* Clojure 1.6
* Protocol Buffers
* [Basho's Riak Protocol Buffer Files][riak-pb-maven]

[riak-pb-maven]: https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22riak-pb%22

I intentionally avoided Netty to reduce complexity. At present, Kria has about
2K lines of Clojure: 1,414 for implementation and 584 for tests. As a very
rough point of comparison, Basho's [official Riak 2 Java client][rjc] has
about 28K lines of code: 17K for code, 11K for tests. It is more complete, of
course, so this isn't an apples-to-apples comparison. Still, I spent
considerable effort reducing duplication in Kria, and I believe this makes
Kria code easier to read and reason about. I hope this translates to better
reliability, too. If and when my team and/or the community wants additional
features, I'm confident they can be added with dozens or hundreds of LOC, not
thousands.

[rjc]: https://github.com/basho/riak-java-client

## Usage

Add this to your `project.clj`:

    [kria "0.1.15"]

For the following examples, I recommend using these namespace aliases:

    [kria.client :as client]
    [kria.server :as server]

To create a connection:

    (defn conn-cb [asc e a] (println (if e e "connected")))
    (def conn (client/connect "127.0.0.1" 8087 conn-cb))

The first line sets up the callback that runs after `connect` succeeds or
fails. On failure, it returns the exception `e`. On success, it prints
"connected".

The second line connects to Riak with the specified host, port, and callback.

Try a ping with:

    (defn ping-cb [asc e a] (println (if e e "pong")))
    (server/ping conn ping-cb)

Get server info with:

    (defn info-cb [asc e a] (prn (or e a)))
    (server/info conn info-cb)

All of the above calls are asynchronous. The callback functions are for
demonstration purposes only. In practice, write your callback functions for
your application's needs.

You can find more examples on how to use the API in `doc/examples`.

## Connect Details

The callback for `connect` takes these arguments:

  * `asc` : [`AsynchronousSocketChannel`][ASC], a Java 7 NIO.2 class that Kria
    uses for asynchronous IO. Each Kria connection wraps exactly one
    `AsynchronousSocketChannel`. The Javadoc is fairly readable, but if you
    don't want to read it, perhaps the most important thing to know is that
    each `AsynchronousSocketChannel` can only support one read or one write at
    a time. So, if you need to make concurrent calls, you'll need more than
    one of them.
  * `e` : exception or `nil` if no exception.
  * `a` : attachment, a term used in the NIO.2 documentation. Generally
    speaking, the attachment is just a placeholder for an object to get passed
    along if a callback succeeds. In Kria, the attachment varies based on the
    API call. In the case of `connect`, it will be `true` on success or `nil`
    on failure.

If you want to pool connections, use the 4 argument version of `connect` that
takes these inputs: `[host port ^AsynchronousChannelGroup group cb]`. To learn
how, read about the [AsynchronousChannelGroup][ACG] class.

[ASC]: http://docs.oracle.com/javase/7/docs/api/java/nio/channels/AsynchronousSocketChannel.html
[ACG]: http://docs.oracle.com/javase/7/docs/api/java/nio/channels/AsynchronousChannelGroup.html

## Using in Applications

Even though Kria is asynchronous with callbacks, it is easy to wrap it as you
like. You might try callback functions, Clojure atoms, and promises and see
what works best.

We have found that [core.async] works great as a layer on top. Just create a
core.async channel in advance and have the callback put the desired return
value in the core.async channel.

When [we] write applications, we tend to create a namespace that wraps all
calls to Kria in a domain-specific way. That namespace provides an API that the
rest of your application relies on. Kria is a thin wrapper over the Riak API,
so it does not handle siblings for you; that is something your domain-specific
logic must decide.

[core.async]: https://github.com/clojure/core.async
[we]: http://bluemontlabs.com

## History

When I started, my goals were to:

* write a simple asynchronous Clojure client
* stay relatively close the Riak API
* use NIO.2, available in Java 7 and later, instead of Netty

Many projects use Netty, but as I learned more about it, I found that NIO.2
provided all I wanted without the complexity of another dependency. The
tradeoff is that Kria requires Java 7 or later.

[Netty]: http://netty.io/
[NIO.2]: http://en.wikipedia.org/wiki/NIO.2#JDK_7_and_NIO.2

Other drivers I saw written in Clojure or Java added complexity that I didn't
need. The goal was to have a simple layer to abstract away the low-level
protocol buffer interface. Other drivers seem to have different objectives.

I started this project before the Java client for Riak 2 was ready. Sean from
Basho [mentioned on the Riak mailing list][ml-1] that "the new official Java
client is designed so that it would be easier to write wrapper libraries in
other JVM languages, and to give the user the option of async or sync at the
API level." I'd be interested to see what people think of the newer client.

[ml-1]: http://lists.basho.com/pipermail/riak-users_lists.basho.com/2014-July/015578.html

I used the [lein-protobuf] plugin at first, but stopped using it for these
reasons:

  1. The protobuf files change rarely, so the plugin seemed less necessary.
  2. The plugin added complexity that did not seem necessary.
  3. I wanted to understand the `protoc` command, including how the Java
     classes were getting created and where they were being stored.
  4. The plugin seemed to slow the REPL start-up time.

[lein-protobuf]: https://github.com/flatland/lein-protobuf
[riak_pb]: https://github.com/basho/riak_pb

As of September 2014, thanks to a [recommendation from Basho][ml-1], Kria uses
the Riak protobuf files via Maven, instead of keeping its own copy (which was
prone to get stale).

## Before Running the Tests

This section is intended for developers who want to run the included tests
with `lein test`.

1. Since the tests use Riak Search, please ensure that search
   is enabled. Check that your `riak.conf` file has `search = on`. If you
   would like to test secondary indexes (2i), ensure that `storage_backend`
   is set to `leveldb` or `memory`, ex. `storage_backend = leveldb`. Run tests
   with `KRIA_TEST_2I=true` to test 2i.

2. Also, run `test/riak-setup.sh` as root *once*, to create and activate the
   necessary bucket types.

## Message Terminology

This section is intended for developers who want to dig into the internals of
Kria. (It may also be helpful in understanding how the Riak protocol buffer
interface works.)

Here, I go into some detail about the message components because Basho's
[terminology of a Riak Protocol Buffer message][Riak-PB] is somewhat unclear.
That documentation uses the term 'message' for both the protobuf part and the
entire data structure. This is confusing, especially when you need to be clear
about the lengths. That's why I use a separate name, 'payload', for the
protobuf component.

So, let me introduce some definitions. The entire data structure is called the
*message*. A message consists of three pieces:

  * *body length* : Bytes 0 to 3. Encodes the length of the body (e.g. the
    rest of the message).
  * *message code* : Byte 4. See `kria.core/message-codes`.
  * *payload* : Optional; Bytes 5+. Encoded using Protocol Buffers.

These three pieces can be aggregated in various ways:

  1. body length + message code = header
  2. message code + payload = body
  3. header + payload = message
  4. body length + body = message
  5. body length + message code + payload = message

Hopefully my terminology is clear. If the above five points make sense to you,
then you are in good shape. I have nothing new to say, but I do want to make
sure that some things are clear.

The body length is simply the length of the body. This sounds simple, but if
you read the Riak documentation, you may get confused. It is *not* the length
of the entire message; it does not include itself in the calculation. Put
another way:

  * If there is a payload, the body length will be the length of the payload
    plus 1.
  * If there is no payload, the length is 1.

In both cases, the 1 comes from the message code, which is one byte long.
Another related term is the *payload length*, which is (hopefully clear by now)
one less than the body length.

[Riak-PB]: http://docs.basho.com/riak/latest/dev/references/protocol-buffers/

## Running Tests in the REPL

It may not be widely known that it is easy to run tests in the REPL.

For example:

```clj
(refresh) (run-tests 'kria.search-test)
```

## License

Copyright 2014 Bluemont Labs LLC

Distributed under the Eclipse Public License, the same as Clojure.
