/*
 * Copyright 2014 OpenMarket Ltd
 * Copyright 2017 Vector Creations Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package im.vector.matrix.android.internal.crypto.api

import im.vector.matrix.android.internal.crypto.model.rest.DeleteDeviceParams
import im.vector.matrix.android.internal.crypto.model.rest.DeviceInfo
import im.vector.matrix.android.internal.crypto.model.rest.DevicesListResponse
import im.vector.matrix.android.internal.crypto.model.rest.KeyChangesResponse
import im.vector.matrix.android.internal.crypto.model.rest.KeysClaimBody
import im.vector.matrix.android.internal.crypto.model.rest.KeysClaimResponse
import im.vector.matrix.android.internal.crypto.model.rest.KeysQueryBody
import im.vector.matrix.android.internal.crypto.model.rest.KeysQueryResponse
import im.vector.matrix.android.internal.crypto.model.rest.KeysUploadBody
import im.vector.matrix.android.internal.crypto.model.rest.KeysUploadResponse
import im.vector.matrix.android.internal.crypto.model.rest.SendToDeviceBody
import im.vector.matrix.android.internal.crypto.model.rest.SignatureUploadResponse
import im.vector.matrix.android.internal.crypto.model.rest.UpdateDeviceInfoBody
import im.vector.matrix.android.internal.crypto.model.rest.UploadSigningKeysBody
import im.vector.matrix.android.internal.network.NetworkConstants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

internal interface CryptoApi {

    /**
     * Get the devices list
     * Doc: https://matrix.org/docs/spec/client_server/latest#get-matrix-client-r0-devices
     */
    @GET(NetworkConstants.URI_API_PREFIX_PATH_R0 + "devices")
    fun getDevices(): Call<DevicesListResponse>

    /**
     * Get the device info by id
     * Doc: https://matrix.org/docs/spec/client_server/latest#get-matrix-client-r0-devices-deviceid
     */
    @GET(NetworkConstants.URI_API_PREFIX_PATH_R0 + "devices/{deviceId}")
    fun getDeviceInfo(@Path("deviceId") deviceId: String): Call<DeviceInfo>

    /**
     * Upload device and/or one-time keys.
     * Doc: https://matrix.org/docs/spec/client_server/r0.4.0.html#post-matrix-client-r0-keys-upload
     *
     * @param params the params.
     */
    @POST(NetworkConstants.URI_API_PREFIX_PATH_R0 + "keys/upload")
    fun uploadKeys(@Body body: KeysUploadBody): Call<KeysUploadResponse>

    /**
     * Upload device and/or one-time keys.
     * Doc: not documented
     *
     * @param deviceId the deviceId
     * @param params   the params.
     */
    @POST(NetworkConstants.URI_API_PREFIX_PATH_R0 + "keys/upload/{deviceId}")
    fun uploadKeys(@Path("deviceId") deviceId: String, @Body body: KeysUploadBody): Call<KeysUploadResponse>

    /**
     * Download device keys.
     * Doc: https://matrix.org/docs/spec/client_server/r0.4.0.html#post-matrix-client-r0-keys-query
     *
     * @param params the params.
     */
    @POST(NetworkConstants.URI_API_PREFIX_PATH_R0 + "keys/query")
    fun downloadKeysForUsers(@Body params: KeysQueryBody): Call<KeysQueryResponse>

    /**
     * CrossSigning - Uploading signing keys
     * Public keys for the cross-signing keys are uploaded to the servers using /keys/device_signing/upload.
     * This endpoint requires UI Auth.
     */
    @POST(NetworkConstants.URI_API_PREFIX_PATH_UNSTABLE + "keys/device_signing/upload")
    fun uploadSigningKeys(@Body params: UploadSigningKeysBody): Call<KeysQueryResponse>

    /**
     *  CrossSigning - Uploading signatures
     *  Signatures of device keys can be up
     *  loaded using /keys/signatures/upload.
     *  For example, Alice signs one of her devices (HIJKLMN) (using her self-signing key),
     *  her own master key (using her HIJKLMN device), Bob's master key (using her user-signing key).
     *
     * The response contains a failures property, which is a map of user ID to device ID to failure reason, if any of the uploaded keys failed.
     * The homeserver should verify that the signatures on the uploaded keys are valid.
     * If a signature is not valid, the homeserver should set the corresponding entry in failures to a JSON object
     * with the errcode property set to M_INVALID_SIGNATURE.
     *
     * After Alice uploads a signature for her own devices or master key,
     * her signature will be included in the results of the /keys/query request when anyone requests her keys.
     * However, signatures made for other users' keys, made by her user-signing key, will not be included.
     */
    @POST(NetworkConstants.URI_API_PREFIX_PATH_UNSTABLE + "keys/signatures/upload")
    fun uploadSignatures(@Body params: Map<String, @JvmSuppressWildcards Any>?): Call<SignatureUploadResponse>

    /**
     * Claim one-time keys.
     * Doc: https://matrix.org/docs/spec/client_server/r0.4.0.html#post-matrix-client-r0-keys-claim
     *
     * @param params the params.
     */
    @POST(NetworkConstants.URI_API_PREFIX_PATH_R0 + "keys/claim")
    fun claimOneTimeKeysForUsersDevices(@Body body: KeysClaimBody): Call<KeysClaimResponse>

    /**
     * Send an event to a specific list of devices
     * Doc: https://matrix.org/docs/spec/client_server/r0.4.0.html#put-matrix-client-r0-sendtodevice-eventtype-txnid
     *
     * @param eventType     the type of event to send
     * @param transactionId the transaction ID for this event
     * @param body          the body
     */
    @PUT(NetworkConstants.URI_API_PREFIX_PATH_R0 + "sendToDevice/{eventType}/{txnId}")
    fun sendToDevice(@Path("eventType") eventType: String, @Path("txnId") transactionId: String, @Body body: SendToDeviceBody): Call<Unit>

    /**
     * Delete a device.
     * Doc: https://matrix.org/docs/spec/client_server/r0.4.0.html#delete-matrix-client-r0-devices-deviceid
     *
     * @param deviceId the device id
     * @param params   the deletion parameters
     */
    @HTTP(path = NetworkConstants.URI_API_PREFIX_PATH_R0 + "devices/{device_id}", method = "DELETE", hasBody = true)
    fun deleteDevice(@Path("device_id") deviceId: String, @Body params: DeleteDeviceParams): Call<Unit>

    /**
     * Update the device information.
     * Doc: https://matrix.org/docs/spec/client_server/r0.4.0.html#put-matrix-client-r0-devices-deviceid
     *
     * @param deviceId the device id
     * @param params   the params
     */
    @PUT(NetworkConstants.URI_API_PREFIX_PATH_R0 + "devices/{device_id}")
    fun updateDeviceInfo(@Path("device_id") deviceId: String, @Body params: UpdateDeviceInfoBody): Call<Unit>

    /**
     * Get the update devices list from two sync token.
     * Doc: https://matrix.org/docs/spec/client_server/r0.4.0.html#get-matrix-client-r0-keys-changes
     *
     * @param oldToken the start token.
     * @param newToken the up-to token.
     */
    @GET(NetworkConstants.URI_API_PREFIX_PATH_R0 + "keys/changes")
    fun getKeyChanges(@Query("from") oldToken: String, @Query("to") newToken: String): Call<KeyChangesResponse>
}
