package com.browser2app.pse.psenativedemo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AutomatonServerInterface {

	@FormUrlEncoded
	@POST("automatonRequest/{id}")
	Call<AutomatonRequestResponse> automatonRequest(@Path("id") String id
			, @Field("userType") String userType
			, @Field("authorizerId") String authorizerId
			, @Field("cus") String cus
			, @Field("amount") String amount
			, @Field("subject") String subject
			, @Field("merchant") String merchant
			, @Field("paymentId") String paymentId
			, @Field("returnURL") String returnURL
			, @Field("cancelURL") String cancelURL
			, @Field("payerEmail") String payerEmail
	);

}
