package com.augmentaa.sparkev.retrofit;


import com.augmentaa.sparkev.model.signup.RequestSignUp;
import com.augmentaa.sparkev.model.signup.ResponseRegistration;
import com.augmentaa.sparkev.model.signup.Vehicle.ConnectorTypeDetails;
import com.augmentaa.sparkev.model.signup.Vehicle.GetAllVehicle;
import com.augmentaa.sparkev.model.signup.Vehicle.RequestAddVehicle;
import com.augmentaa.sparkev.model.signup.Vehicle.ResponseAddVehicle;
import com.augmentaa.sparkev.model.signup.Vehicle.ResponseUpdateVehicle;
import com.augmentaa.sparkev.model.signup.Vehicle.Vehicle;
import com.augmentaa.sparkev.model.signup.Vehicle.VehicleBrand;
import com.augmentaa.sparkev.model.signup.Vehicle.VehicleModel;
import com.augmentaa.sparkev.model.signup.add_charger.RequestAddCharger;
import com.augmentaa.sparkev.model.signup.add_charger.RequestAddChargerSyncBLE;
import com.augmentaa.sparkev.model.signup.add_charger.ResponseAddCharger;
import com.augmentaa.sparkev.model.signup.add_charger.ResponseAddChargerSyncBLE;
import com.augmentaa.sparkev.model.signup.billing_address.RequestBillingAddress;
import com.augmentaa.sparkev.model.signup.billing_address.ResponseBillingAddress;
import com.augmentaa.sparkev.model.signup.call_request.CallRequest;
import com.augmentaa.sparkev.model.signup.call_request.RequestCallBackRequest;
import com.augmentaa.sparkev.model.signup.call_request.ResponseCallBackRequest;
import com.augmentaa.sparkev.model.signup.charger_details.ChargerDetails;
import com.augmentaa.sparkev.model.signup.charger_list.ResponseChargerList;
import com.augmentaa.sparkev.model.signup.charger_status.RequestChargerStatus;
import com.augmentaa.sparkev.model.signup.charger_status.ResponseChargerStatus;
import com.augmentaa.sparkev.model.signup.charger_summry.RequestGetSummary;
import com.augmentaa.sparkev.model.signup.charger_summry.ResponseGetAllSummary;
import com.augmentaa.sparkev.model.signup.country.ResponseCountry;
import com.augmentaa.sparkev.model.signup.data_transfer.RequestCurrent;
import com.augmentaa.sparkev.model.signup.data_transfer.ResponseCurrent;
import com.augmentaa.sparkev.model.signup.error_log.ErrorMessage_Email;
import com.augmentaa.sparkev.model.signup.get_chargerlist_for_warranty.ResponseGetChargerList;
import com.augmentaa.sparkev.model.signup.get_payment_mode.PaymentMode;
import com.augmentaa.sparkev.model.signup.get_warrenty.RespponseGetWarranty;
import com.augmentaa.sparkev.model.signup.guest_access_list.RequestDenyGuestAccess;
import com.augmentaa.sparkev.model.signup.guest_access_list.RequestGrantGuestAccess;
import com.augmentaa.sparkev.model.signup.guest_access_list.RequestGuestAccessList;
import com.augmentaa.sparkev.model.signup.guest_access_list.RequestRevokeCharger;
import com.augmentaa.sparkev.model.signup.guest_access_list.ResponseGuestAccessList;
import com.augmentaa.sparkev.model.signup.guest_access_list.ResponseRevokeCharger;
import com.augmentaa.sparkev.model.signup.initiate_upgrade_request.RequestUpgradeInitiate;
import com.augmentaa.sparkev.model.signup.initiate_upgrade_request.ResponseInitiateRequest;
import com.augmentaa.sparkev.model.signup.login.RequestForLogin;
import com.augmentaa.sparkev.model.signup.login.ResponseForLogin;
import com.augmentaa.sparkev.model.signup.login.ResponseForLoginBLE;
import com.augmentaa.sparkev.model.signup.mobile_static_text.TextStaticData;
import com.augmentaa.sparkev.model.signup.mobile_validation.RequestMobileNumber;
import com.augmentaa.sparkev.model.signup.mobile_validation.ResponseMobileNumber;
import com.augmentaa.sparkev.model.signup.notification.ResponseNotification;
import com.augmentaa.sparkev.model.signup.one_time_schedule.ResponseCreateSchedule;
import com.augmentaa.sparkev.model.signup.one_time_schedule.ResponseGetAllSchedule;
import com.augmentaa.sparkev.model.signup.otp.RequestForOTPVefifyUser;
import com.augmentaa.sparkev.model.signup.otp.RequestForResendOTP;
import com.augmentaa.sparkev.model.signup.otp.ResponseForResendOTP;
import com.augmentaa.sparkev.model.signup.otp.ResponseForVerifyOTP;
import com.augmentaa.sparkev.model.signup.otp.ResponseForgotVerifyOTP;
import com.augmentaa.sparkev.model.signup.payment.PaymentRequest;
import com.augmentaa.sparkev.model.signup.payment.PaymentResponse;
import com.augmentaa.sparkev.model.signup.payment_status.RequestToPaymentStatus;
import com.augmentaa.sparkev.model.signup.payment_status.ResponseToPayment;
import com.augmentaa.sparkev.model.signup.profile.ResponseForUpdateProfile;
import com.augmentaa.sparkev.model.signup.profile.ResponseGetProfile;
import com.augmentaa.sparkev.model.signup.project_update.ProjectUpdate;
import com.augmentaa.sparkev.model.signup.question_list.Question;
import com.augmentaa.sparkev.model.signup.razorpay.CreateOrderResponse;
import com.augmentaa.sparkev.model.signup.receipt_history.ResponseReceiptHistory;
import com.augmentaa.sparkev.model.signup.remote_start_stop.RequestChargerStartStop;
import com.augmentaa.sparkev.model.signup.remote_start_stop.ResponseChargerStartStop;
import com.augmentaa.sparkev.model.signup.request_access.RequestAccess;
import com.augmentaa.sparkev.model.signup.request_access.ResponseRequestAccess;
import com.augmentaa.sparkev.model.signup.schedule.RequestActiveDeactiveSchecule;
import com.augmentaa.sparkev.model.signup.schedule.RequestCreateSchedule;
import com.augmentaa.sparkev.model.signup.schedule.RequestGetSchedule;
import com.augmentaa.sparkev.model.signup.schedule.RequestUpdateSchedule;
import com.augmentaa.sparkev.model.signup.schedule.ResponseGetSchedule;
import com.augmentaa.sparkev.model.signup.schedule.ResponseUpdateSchedule;
import com.augmentaa.sparkev.model.signup.session_history.RequestSessionHistory;
import com.augmentaa.sparkev.model.signup.session_history.ResponseSessionHistory;
import com.augmentaa.sparkev.model.signup.session_logs_ble.InsertSessionLogsBLE;
import com.augmentaa.sparkev.model.signup.session_logs_ble.ResponseSessionLogBLE;
import com.augmentaa.sparkev.model.signup.sessionmeter_value.ChargerSessionValue;
import com.augmentaa.sparkev.model.signup.state.ResponseState;
import com.augmentaa.sparkev.model.signup.tracking.ResponseUpgradeTracking;
import com.augmentaa.sparkev.model.signup.transaction_details.RequestTransactionDetails;
import com.augmentaa.sparkev.model.signup.transaction_details.ResponseTransactionDetails;
import com.augmentaa.sparkev.model.signup.update_password.RequestForUpdatePassword;
import com.augmentaa.sparkev.model.signup.update_password.ResponseForUpdatePassword;
import com.augmentaa.sparkev.model.signup.upgrage_charger_list.ResponseUpgradeChargerList;
import com.augmentaa.sparkev.model.signup.warranty_history.ResponseWarrantyHistory;
import com.augmentaa.sparkev.model.signup.warranty_renewal_request.ResponseWarrantyRenewal;
import com.augmentaa.sparkev.model.signup.warranty_status_details.ResponseCheckWarranty;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APIInterface {
    // 1. Signup Registration
    @POST("registerNewBLE")
    Call<ResponseRegistration> PostSignUp(@Body RequestSignUp loginResponse);

    // 2. getChargingStations
    @GET("master/getAllCountries")
    Call<ResponseCountry> getAllCountryList();

    // 2. getChargingStations
    @GET("master/getStateByCountry/{country_id}")
    Call<List<ResponseState>> getAllStateList(@Path("country_id") int country_id);

    //3. OTP Verify at the time of registration
   @POST("verifyUserNewBLE")
    Call<ResponseForVerifyOTP> veryfyUserRegistration(@Body RequestForOTPVefifyUser otpRequest);

    //4. OTP Verify at the time of registration
    @POST("getotpNew")
    Call<ResponseForResendOTP> reSendOTP(@Body RequestForResendOTP otpRequest);

    //5. Login
    @POST("api/v1/loginNew")
    Call<ResponseForLogin> PostSignIn(@Body RequestForLogin loginResponse);

    //6. Login
    @POST("loginBLE")
    Call<ResponseForLoginBLE> loginBLE(@Body RequestForLogin loginResponse);

    //6. Login
    @POST("user/getUserByMobileAndEmail")
    Call<ResponseMobileNumber> checkMobileEmail(@Body RequestMobileNumber loginResponse);


    //7. OTP Verify at the time of registration
    @POST("verifyOTPNew")
    Call<ResponseForgotVerifyOTP> verifyOTPNew(@Body RequestForOTPVefifyUser otpRequest);

    //8. Update Password input
    @POST("updatepasswordNewBLE")
    Call<ResponseForUpdatePassword> updatePassword(@Body RequestForUpdatePassword loginResponse);


    // 9. Get Vehicle Brand
    @GET("brands")
    Call<List<VehicleBrand>> getVehicleBrands();

    // 10. Get Vehicle Model
    @GET("vehicleModels/{brand_id}")
    Call<List<VehicleModel>> getVehicleModel(@Path("brand_id") int brand_id);

    // 11. Get All connector
    @GET("connectorTypes/{model_id}")
    Call<List<ConnectorTypeDetails>> getConnectorType(@Path("model_id") int model_id);

    //12. Get Vehicle Model
    @POST("vehicles")
    Call<ResponseAddVehicle> addVehicleDetails(@HeaderMap Map<String, String> headers, @Body RequestAddVehicle addVehicleDetails);

    // 13 delete Vehicle
    @DELETE("deleteRegisteredVehicle/{vehicle_id}/{created_by}")
    Call<Vehicle> deleteVehicleDetails(@HeaderMap Map<String, String> headers, @Path("vehicle_id") int vehicle_id, @Path("created_by") int created_by);


    // 14. Get All vehicle added by user
    @GET("vehicles/getVehiclesByUserId/{created_by}")
    Call<GetAllVehicle> getVehicleDetailsByUserId(@HeaderMap Map<String, String> headers, @Path("created_by") int created_by);


    //15. Update Vehicle details
    @POST("updateRegisteredVehicle")
    Call<ResponseUpdateVehicle> updateVehicleDetails(@HeaderMap Map<String, String> headers, @Body RequestAddVehicle addVehicleDetails);

    //16. Update Profile details
    @Multipart
    @POST("users/profileUpdate")
    Call<ResponseForUpdateProfile> profileUpdate(@HeaderMap Map<String, String> headers,
                                                 @Part MultipartBody.Part file,
                                                 @Part("id") RequestBody id,
                                                 @Part("f_Name") RequestBody f_Name,
                                                 @Part("m_Name") RequestBody m_Name,
                                                 @Part("l_Name") RequestBody l_Name,
                                                 @Part("name") RequestBody name,
                                                 @Part("address1") RequestBody address1,
                                                 @Part("PIN") RequestBody PIN,
                                                 @Part("city_id") RequestBody city_id,
                                                 @Part("state_id") RequestBody state_id,
                                                 @Part("country_id") RequestBody country_id,
                                                 @Part("email") RequestBody email,
                                                 @Part("gender") RequestBody gender,
                                                 @Part("mobile") RequestBody mobile


    );


    //16. Get Profile details
    @GET("users/getUserProfile/{userid}")
    Call<ResponseGetProfile> getProfile(@HeaderMap Map<String, String> headers, @Path("userid") int userid);


    // 14. Get All ChargerList added by user
    @GET("charger/getAllChargersByUserIdBLE/{user_id}")
    Call<ResponseChargerList> getChargerList(@HeaderMap Map<String, String> headers, @Path("user_id") int user_id);


    // 14. Get All ChargerList added by user
    @GET("users/getactvityhistory/{user_id}")
    Call<ResponseWarrantyHistory> getWarrantyHistory(@HeaderMap Map<String, String> headers, @Path("user_id") int user_id);

    //15. Update Vehicle details
    @POST("users/getTransactionDetails")
    Call<ResponseTransactionDetails> getTransactionDetails(@HeaderMap Map<String, String> headers, @Body RequestTransactionDetails addVehicleDetails);


    // 14. Get All ChargerList added by user
    @GET("warranty/getwarrenty/{serialNo}")
    Call<RespponseGetWarranty> getWarranty(@HeaderMap Map<String, String> headers, @Path("serialNo") String serialNo);

    // 14. Get All ChargerList added by user
    @GET("users/checkwarrantystatus/{user_id}/{serialNo}")
    Call<ResponseCheckWarranty> checkWarrantyStatus(@HeaderMap Map<String, String> headers, @Path("user_id") int user_id, @Path("serialNo") String serialNo);

    //15. Update Vehicle details
    @POST("charger/updateChargerAddressBLE")
    Call<ResponseBillingAddress> updateBillingAddress(@HeaderMap Map<String, String> headers, @Body RequestBillingAddress addVehicleDetails);

    //15. Update Vehicle details
    @POST("api/v1/createdevice-new")
    Call<ResponseAddCharger> createDevice(@HeaderMap Map<String, String> headers, @Body RequestAddCharger addVehicleDetails);

    //15. Update Vehicle details
    @POST("user/userChargerMappingBLESync")
    Call<ResponseAddChargerSyncBLE> userChargerMappingBLESync(@HeaderMap Map<String, String> headers, @Body RequestAddChargerSyncBLE addVehicleDetails);

    //15. Update Vehicle details
    @POST("api/v1/requestAccess")
    Call<ResponseRequestAccess> requestAccess(@HeaderMap Map<String, String> headers, @Body RequestAccess addVehicleDetails);


    // 14. Get All ChargerList added by user
    @POST("api/v1/guestAccessList")
    Call<ResponseGuestAccessList> guestAccessList(@HeaderMap Map<String, String> headers, @Body RequestGuestAccessList user_id);


    //15. Update Vehicle details
    @POST("api/v1/grantGuestAccess")
    Call<ResponseGuestAccessList> grantGuestAccess(@HeaderMap Map<String, String> headers, @Body RequestGrantGuestAccess addVehicleDetails);

    //15. Update Vehicle details
    @POST("api/v1/denyGuestAccess")
    Call<ResponseGuestAccessList> denyGuestAccess(@HeaderMap Map<String, String> headers, @Body RequestDenyGuestAccess addVehicleDetails);

    // 14. Get All ChargerList added by user
    @POST("api/v1/requestSentList")
    Call<ResponseGuestAccessList> requestSentList(@HeaderMap Map<String, String> headers, @Body RequestGuestAccessList user_id);


    // 14. Get All ChargerList added by user
    @POST("payment/initiatetransaction")
    Call<PaymentResponse> initiatetransaction(@HeaderMap Map<String, String> headers, @Body PaymentRequest user_id);

    //3.Checl Payment Status
    @POST("payment/checkstatus")
    Call<ResponseToPayment> getCheckPaymentStatus(@HeaderMap Map<String, String> headers, @Body RequestToPaymentStatus loginResponse);
    //3.Checl Payment Status
    @POST("payment/checkorderstatus")
    Call<ResponseToPayment> getCheckorderstatus(@HeaderMap Map<String, String> headers, @Body RequestToPaymentStatus loginResponse);


    // 8. Get Vehicle Brand
    @GET("master/getQuestions")
    Call<Question> getQuestionList();

    //3.Call Request
    @POST("cbr/createRequest")
    Call<CallRequest> postCallRequest(@HeaderMap Map<String, String> headers, @Body CallRequest loginResponse);

    //3.Call History
    @POST("cbr/getCallHistory")
    Call<ResponseCallBackRequest> getCallHistory(@HeaderMap Map<String, String> headers, @Body RequestCallBackRequest loginResponse);


    @Multipart
    @POST("user/chargerRenewalRequestBle")
    Call<ResponseWarrantyRenewal> uploadImage(@HeaderMap Map<String, String> headers,
                                              @Part MultipartBody.Part[] gallery,
                                              @Part("user_id") RequestBody user_id,
                                              @Part("charger_id") RequestBody charger_id,
                                              @Part("mobile") RequestBody mobile,
                                              @Part("address1") RequestBody address1,
                                              @Part("address2") RequestBody address2,
                                              @Part("pin") RequestBody pin,
                                              @Part("city_id") RequestBody city_id,
                                              @Part("state_id") RequestBody state_id,
                                              @Part("country_id") RequestBody country_id,
                                              @Part("request_type") RequestBody request_type
    );


    @Multipart
    @POST("user/chargerRenewalRequestBle")
    Call<ResponseWarrantyRenewal> uploadImageNew(@HeaderMap Map<String, String> headers,
                                                 @Part MultipartBody.Part gallery,
                                                 @Part("user_id") RequestBody user_id,
                                                 @Part("charger_id") RequestBody charger_id,
                                                 @Part("mobile") RequestBody mobile,
                                                 @Part("address1") RequestBody address1,
                                                 @Part("address2") RequestBody address2,
                                                 @Part("pin") RequestBody pin,
                                                 @Part("city_id") RequestBody city_id,
                                                 @Part("state_id") RequestBody state_id,
                                                 @Part("country_id") RequestBody country_id,
                                                 @Part("request_type") RequestBody request_type,
                                                 @Part("landmark") RequestBody landmark);


    // Remote Start stop
    @POST("/remoteTransaction")
    Call<ResponseChargerStartStop> chargerStart(@HeaderMap Map<String, String> headers, @Body RequestChargerStartStop loginResponse);


    // Charger Status
    @POST("status")
    Call<ResponseChargerStatus> getChargerStatus(@Body RequestChargerStatus chargerStatus);

    // Charger meter value
    @GET("charger/getActiveSessionMeterValues/{ChargerID}/{connId}")
    Call<ChargerSessionValue> getActiveSessionMeterValues(@HeaderMap Map<String, String> headers, @Path("ChargerID") String ChargerID, @Path("connId") String connId);

    // Get Charger by DisplayId
    @GET("charger/getChargerByDisplayId/{chargerId}")
    Call<List<ChargerDetails>> getChargerByDisplayId(@HeaderMap Map<String, String> headers, @Path("chargerId") String chargerId);


    //Create One time Schedule

    @POST("charger/createScheduleBLE")
    Call<ResponseCreateSchedule> createScheduleBLE(@HeaderMap Map<String, String> headers, @Body RequestCreateSchedule loginResponse);


    // Get One time Schedule by charger id
    @GET("charger/getScheduleBLEByChargerSerialNo/{chargerId}")
    Call<ResponseGetAllSchedule> getScheduleBLEByChargerSerialNo(@HeaderMap Map<String, String> headers, @Path("chargerId") String chargerId);


    // Get One time Schedule by User id
    @GET("charger/getScheduleBLEByUserId/{user_id}")
    Call<List<ResponseGetAllSchedule>> getScheduleBLEByUserId(@HeaderMap Map<String, String> headers, @Path("user_id") String user_id);


    // Delete One time Schedule
    @DELETE("charger/deleteScheduleBLE/{schedule_id}/{user_id}")
    Call<List<ResponseCreateSchedule>> deleteScheduleBLE(@HeaderMap Map<String, String> headers, @Path("schedule_id") int schedule_id, @Path("user_id") int user_id);


    // Get One time Schedule by User id
    @GET("users/getChargerListforWarranty/{user_id}")
    Call<ResponseGetChargerList> getChargerListforWarranty(@HeaderMap Map<String, String> headers, @Path("user_id") int user_id);

    @POST("charger/ScheduleBLEByChargerSerialNoAndUserId")
    Call<ResponseGetSchedule> getAllSchedule(@HeaderMap Map<String, String> headers, @Body RequestGetSchedule loginResponse);

    @POST("charger/updateScheduleStatusBLE")
    Call<ResponseCreateSchedule> setActiveDeactiveSchedule(@HeaderMap Map<String, String> headers, @Body RequestActiveDeactiveSchecule loginResponse);


    @POST("charger/updateEnableDisableScheduleBLE")
    Call<ResponseCreateSchedule> updateEnableDisableScheduleBLE(@HeaderMap Map<String, String> headers, @Body RequestActiveDeactiveSchecule loginResponse);


    // Get All Transaction
    @GET("users/getTransactionAll/{user_id}")
    Call<ResponseReceiptHistory> getTransactionAll(@HeaderMap Map<String, String> headers, @Path("user_id") int user_id);


    @POST("user/userChargingHistoryBle")
    Call<ResponseSessionHistory> userChargingHistoryBle(@HeaderMap Map<String, String> headers, @Body RequestSessionHistory loginResponse);


    @POST("user/userChargingHistoryBleMode")
    Call<ResponseSessionHistory> userChargingHistoryBleMode(@HeaderMap Map<String, String> headers, @Body RequestSessionHistory loginResponse);


    @POST("charger/updateScheduleBLE")
    Call<ResponseUpdateSchedule> updateScheduleBLE(@HeaderMap Map<String, String> headers, @Body RequestUpdateSchedule loginResponse);


    @POST("user/userChargingSummaryBle")
    Call<ResponseGetAllSummary> userChargingSummaryBle(@HeaderMap Map<String, String> headers, @Body RequestGetSummary loginResponse);


    @POST("user/userChargingSummaryBleMode")
    Call<ResponseGetAllSummary> userChargingSummaryBleMode(@HeaderMap Map<String, String> headers, @Body RequestGetSummary loginResponse);


    @POST("user/revokeChargerAccessBLE")
    Call<ResponseRevokeCharger> revokeCharger(@HeaderMap Map<String, String> headers, @Body RequestRevokeCharger loginResponse);

    // Set Current
    @POST("dataTransfer")
    Call<ResponseCurrent> setCurrent(@Body RequestCurrent loginResponse);


    // Get All Transaction
    @GET("user/getUserNotificationList/{user_id}")
    Call<ResponseNotification> getUserNotificationList(@HeaderMap Map<String, String> headers, @Path("user_id") int user_id);


    // Get All Transaction
    @GET("user/updateUSerNotificationStatus/{user_id}")
    Call<ResponseNotification> updateUSerNotificationStatus(@HeaderMap Map<String, String> headers, @Path("user_id") int user_id);


    @POST("user/insertUserChargingLogs_BLE")
    Call<ResponseSessionLogBLE> insertUserChargingLogs_BLE(@HeaderMap Map<String, String> headers, @Body InsertSessionLogsBLE loginResponse);

    //3.Ask Question ExiTreee
    @POST("error/errorLog")
    Call<ErrorMessage_Email> postErrorLog(@Body ErrorMessage_Email loginResponse);


    // Get All Transaction
    @GET("version/getLatestProjectVersion/{project_id}/{platform}")
    Call<ProjectUpdate> getLatestProjectVersion( @Path("project_id") int project_id, @Path("platform") String platform);


    // Get All Transaction
    @GET("payment/getwaylist/{project_id}")
    Call<PaymentMode> getWayPaymentList(@HeaderMap Map<String, String> headers,@Path("project_id") int project_id);



//    //Razor Pay API
//    @POST("payment/create")
//    Call<CreateOrderResponse> createPayment(@HeaderMap Map<String, String> headers, @Body PaymentRequest user_id);

    //Razor Pay API
    @POST("payment/createorder")
    Call<CreateOrderResponse> createPayment(@HeaderMap Map<String, String> headers, @Body PaymentRequest user_id);

    // Upgrade Charger
    @GET("plan/getOCPPUpgradePlans/{serialNo}")
    Call<RespponseGetWarranty> getOCPPUpgradePlans(@HeaderMap Map<String, String> headers, @Path("serialNo") String serialNo);

    // Upgrade Charger List
    @GET("charger/getBleChargersByUser/{user_id}")
    Call<ResponseUpgradeChargerList> getBleChargersByUser(@HeaderMap Map<String, String> headers, @Path("user_id") int serialNo);


    //Initiate upgrade request
    @POST("request/requestIniatedByCustomer")
    Call<ResponseInitiateRequest> requestIniatedByCustomer(@HeaderMap Map<String, String> headers, @Body RequestUpgradeInitiate data);


    // Upgrade Charger List
    @GET("request/getBleupgradedetailsByUser/{user_id}")
    Call<ResponseUpgradeTracking> getBleupgradedetailsByUser(@HeaderMap Map<String, String> headers, @Path("user_id") int user_id);

    // Upgrade Charger List
    @GET("master/getMobileAppStaticData/{module}")
    Call<TextStaticData> getMobileAppStaticData(@HeaderMap Map<String, String> headers, @Path("module") String module);


}
