(ns kria.client
  (:import
    [java.net InetSocketAddress]
    [java.nio.channels
     AsynchronousSocketChannel
     AsynchronousChannelGroup
     CompletionHandler]))

(set! *warn-on-reflection* true)

(defn socket-address
  [^String host ^Integer port]
  (InetSocketAddress. host port))

(defn connect-handler
  [asc cb]
  (proxy [CompletionHandler] []
    (completed [_ a] (cb asc nil true))
    (failed [e a] (cb e asc nil))))

(defn connect
  "Connects to Riak via the Protocol Buffer interface. Returns a
  AsynchronousSocketChannelImpl or a subclass, such as
  UnixAsynchronousSocketChannelImpl.

  The `group` parameter is an AsynchronousChannelGroup; see
  http://docs.oracle.com/javase/7/docs/api/java/nio/channels/
  AsynchronousSocketChannel.html for more information. If the
  `group` parameter is `nil` then the resulting channel is created
  by the system-wide default provider, and bound to the default
  group."
  [^AsynchronousChannelGroup group host port cb]
  (let [sa (socket-address host port)
        asc (AsynchronousSocketChannel/open group)]
    (.connect asc sa nil (connect-handler asc cb))
    asc))

(defn disconnect
  "Disconnect client."
  [^AsynchronousSocketChannel asc]
  (.close asc))
