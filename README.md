# kria

A Clojure client for Riak 2.0.

## Usage

To create a connection:

    (defn conn-cb [asc e a] (println (if e e "connected")))
    (def conn (client/connect nil "127.0.0.1" 8087 conn-cb))

The first line sets up the callback. Here is what the callback arguments mean:

  * `asc` : [`AsynchronousSocketChannel`][ASC], a Java 7 NIO.2 class that
    Kria uses for asynchronous IO.
  * `e` : exception; `nil` if no exception.
  * `a` : attachment. Generally speaking, the attachment varies based on the
    API call. In this case, it will be `true` on success or `nil` if not.

Note that `nil` argument to `client/connect` creates the connection with the
system-wide default provider. If you want to pool connections, see
[`AsynchronousChannelGroup`][ACG].

[ASC]: http://docs.oracle.com/javase/7/docs/api/java/nio/channels/AsynchronousSocketChannel.html
[ACG]: http://docs.oracle.com/javase/7/docs/api/java/nio/channels/AsynchronousChannelGroup.html

Try a ping with:

    (defn ping-cb [asc e a] (println (if e e "pong")))
    (server/ping conn ping-cb)

Get server info with:

    (defn info-cb [asc e a] (prn (if e e a)))
    (server/info conn info-cb)

All of the above calls are asynchronous. The callback functions are for
demonstration purposes only. In practice, they will behave in application
specific ways.

## Protocol Buffer Setup

These steps only need to be run once, or each time the underlying [Riak
Protocol Buffer][riak_pb] files are updated.

Generate `resources/com/basho/riak/protobuf/*.java` files from the
files in `resources/proto/*.proto` using these commands:

    mkdir -p src/java
    cd resources/proto/
    protoc --java_out=../../src/java riak.proto
    protoc --java_out=../../src/java riak_dt.proto
    protoc --java_out=../../src/java riak_kv.proto
    protoc --java_out=../../src/java riak_search.proto
    protoc --java_out=../../src/java riak_yokozuna.proto
    cd ../..

## Compile Java Sources

Before you can run `lein repl` for the first time, run this:

    lein with-profile base javac

This will compile the `src/java/**/*.java` files, making them available to the
Clojure files.

The above command only uses the `base` profile, not the `dev` profile. This
prevents `dev/user.clj` from being loaded, which requires the Java compilation
step as a prerequisite.

If you don't do this, you will likely see this error:

    Caused by: java.lang.ClassNotFoundException: com.basho.riak.protobuf.RiakPB$RpbGetServerInfoResp
      at java.net.URLClassLoader$1.run(URLClassLoader.java:366)
      at java.net.URLClassLoader$1.run(URLClassLoader.java:355)
      at java.security.AccessController.doPrivileged(Native Method)

## History

I used the [lein-protobuf] plugin at first, but stopped using it. Why?

  1. The protobuf files change rarely, so the plugin seemed less necessary.
  2. The plugin added complexity that I did not think I needed.
  3. I wanted to understand how the the `protoc` command worked, including how
     the Java classes were getting created and where they were being stored.
  4. It seemed like the plugin slow down REPL start-up.

[lein-protobuf]: https://github.com/flatland/lein-protobuf
[riak_pb]: https://github.com/basho/riak_pb

## Message Terms

This section is intended for developers.

Let me introduce some definitions. The entire data structure is called the *message*. A message consists of three pieces:

  * *body length* : Bytes 0 to 3. Encodes the length of the body (e.g. the rest
    of the message).
  * *message code* : Byte 4. See `kria.core/message-codes`.
  * *payload* : Optional; Bytes 5+. Encoded using Protocol Buffers.

These three pieces can be aggregated in various ways:

  1. body length + message code = header
  2. message code + payload = body
  3. header + payload = message
  4. body length + body = message
  5. body length + message code + payload = message

I go into some detail about the message components because Basho's
[terminology of a Riak Protocol Buffer message][Riak-PB] is somewhat unclear.
That documentation uses the term 'message' for both the protobuf part and the
entire data structure. This is confusing, especially when you need to be clear
about the lengths. That's why I use a separate name, 'payload', for the
protobuf component.

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
Another related term is the *payload length*, which is (obviously, as I have
beat the horse to death) one less than the body length.

[Riak-PB]: http://docs.basho.com/riak/latest/dev/references/protocol-buffers/
