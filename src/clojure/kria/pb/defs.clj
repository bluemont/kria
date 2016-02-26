(ns kria.pb.defs
  (:require [flatland.protobuf.core :as pb]
            [kria.pb.util :as putil])
  (:import [com.basho.riak.protobuf

            RiakPB$RpbErrorResp
            RiakPB$RpbSetBucketReq
            RiakPB$RpbGetBucketReq
            RiakPB$RpbGetBucketResp
            RiakKvPB$RpbListKeysReq
            RiakKvPB$RpbListKeysResp
            RiakPB$RpbGetBucketTypeReq
            RiakPB$RpbSetBucketTypeReq
            RiakDtPB$DtFetchReq
            RiakDtPB$DtFetchResp
            RiakDtPB$DtUpdateReq
            RiakDtPB$DtUpdateResp
            RiakKvPB$RpbIndexReq
            RiakKvPB$RpbIndexResp
            RiakYokozunaPB$RpbYokozunaIndexDeleteReq
            RiakYokozunaPB$RpbYokozunaIndexGetReq
            RiakYokozunaPB$RpbYokozunaIndexGetResp
            RiakYokozunaPB$RpbYokozunaIndexPutReq
            RiakKvPB$RpbMapRedReq
            RiakKvPB$RpbMapRedResp
            RiakKvPB$RpbDelReq
            RiakKvPB$RpbGetReq
            RiakKvPB$RpbGetResp
            RiakKvPB$RpbPutReq
            RiakKvPB$RpbPutResp
            RiakYokozunaPB$RpbYokozunaSchemaGetReq
            RiakYokozunaPB$RpbYokozunaSchemaGetResp
            RiakYokozunaPB$RpbYokozunaSchemaPutReq
            RiakSearchPB$RpbSearchQueryReq
            RiakSearchPB$RpbSearchQueryResp
            RiakPB$RpbGetServerInfoResp
            RiakKvPB$RpbListBucketsReq
            RiakKvPB$RpbListBucketsResp
            RiakPB$RpbResetBucketReq
            RiakKvPB$RpbGetClientIdResp
            RiakKvPB$RpbSetClientIdReq]))

(def message-defs
  {
   :error-resp (pb/protodef RiakPB$RpbErrorResp)
   ;; :ping-req 1
   ;; :ping-resp 2
   ;; :get-client-id-req 3
   :get-client-id-resp (pb/protodef RiakKvPB$RpbGetClientIdResp)
   :set-client-id-req (pb/protodef RiakKvPB$RpbSetClientIdReq)
   ;; :set-client-id-resp 6
   ;; :get-server-info-req 7
   :get-server-info-resp (pb/protodef RiakPB$RpbGetServerInfoResp)
   :get-req (pb/protodef RiakKvPB$RpbGetReq)
   :get-resp (pb/protodef RiakKvPB$RpbGetResp)
   :put-req (pb/protodef RiakKvPB$RpbPutReq)
   :put-resp (pb/protodef RiakKvPB$RpbPutResp)
   :del-req (pb/protodef RiakKvPB$RpbDelReq)
   ;; :del-resp 14
   :list-buckets-req (pb/protodef RiakKvPB$RpbListBucketsReq)
   :list-buckets-resp (pb/protodef RiakKvPB$RpbListBucketsResp)
   :list-keys-req (pb/protodef RiakKvPB$RpbListKeysReq)
   :list-keys-resp (pb/protodef RiakKvPB$RpbListKeysResp)
   :get-bucket-req (pb/protodef RiakPB$RpbGetBucketReq)
   :get-bucket-resp (pb/protodef RiakPB$RpbGetBucketResp)
   :set-bucket-req (pb/protodef RiakPB$RpbSetBucketReq)
   ;; :set-bucket-resp 22
   :map-red-req (pb/protodef RiakKvPB$RpbMapRedReq)
   :map-red-resp (pb/protodef RiakKvPB$RpbMapRedResp)
   :index-req (pb/protodef RiakKvPB$RpbIndexReq)
   :index-resp (pb/protodef RiakKvPB$RpbIndexResp)
   :search-query-req (pb/protodef RiakSearchPB$RpbSearchQueryReq)
   :search-query-resp (pb/protodef RiakSearchPB$RpbSearchQueryResp)
   :reset-bucket-req (pb/protodef RiakPB$RpbResetBucketReq)
   ;; :reset-bucket-resp 30
   :get-bucket-type-req (pb/protodef RiakPB$RpbGetBucketTypeReq)
   :set-bucket-type-req (pb/protodef RiakPB$RpbSetBucketTypeReq)
   :counter-update-req (pb/protodef RiakDtPB$DtUpdateReq)
   :counter-update-resp (pb/protodef RiakDtPB$DtUpdateResp)
   :counter-get-req (pb/protodef RiakDtPB$DtFetchReq)
   :counter-get-resp (pb/protodef RiakDtPB$DtFetchResp)
   :yz-index-get-req (pb/protodef RiakYokozunaPB$RpbYokozunaIndexGetReq)
   :yz-index-get-resp (pb/protodef RiakYokozunaPB$RpbYokozunaIndexGetResp)
   :yz-index-put-req (pb/protodef RiakYokozunaPB$RpbYokozunaIndexPutReq)
   ;; :yz-index-put-resp 12 ; *
   :yz-index-del-req (pb/protodef RiakYokozunaPB$RpbYokozunaIndexDeleteReq)
   ;; :yz-index-del-resp 14 ; *
   :yz-schema-get-req (pb/protodef RiakYokozunaPB$RpbYokozunaSchemaGetReq)
   :yz-schema-get-resp (pb/protodef RiakYokozunaPB$RpbYokozunaSchemaGetResp)
   :yz-schema-put-req (pb/protodef RiakYokozunaPB$RpbYokozunaSchemaPutReq)
   ;; :yz-schema-put-resp 12 ; *
   :dt-fetch-req (pb/protodef RiakDtPB$DtFetchReq)
   :dt-fetch-resp (pb/protodef RiakDtPB$DtFetchResp)
   :dt-update-req (pb/protodef RiakDtPB$DtUpdateReq)
   ;; :dt-update-resp (pb/protodef RiakDtPB$DtUpdateResp)
   })
