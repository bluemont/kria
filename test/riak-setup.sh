#! /bin/sh
set -e

setup_riak_bucket_type_set () {
  riak-admin bucket-type create kria-set '{"props":{"datatype":"set"}}'
  riak-admin bucket-type activate kria-set
}

setup_riak_bucket_type_counter () {
  riak-admin bucket-type create kria-counter '{"props":{"datatype":"counter"}}'
  riak-admin bucket-type activate kria-counter
}

setup () {
  setup_riak_bucket_type_counter
  setup_riak_bucket_type_set
}

setup
