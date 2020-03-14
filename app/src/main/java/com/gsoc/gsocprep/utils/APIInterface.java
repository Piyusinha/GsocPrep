package com.gsoc.gsocprep.utils;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import com.gsoc.gsocprep.model.*;

import java.util.List;

public interface APIInterface {
    @GET("manufacturers")
    Observable<List<manufacturers>> getName();
    @GET("manufacturers/{hsn}/vehicles")
    Observable<List<vehicleModels>> getCommercialName(@Path("hsn") String hsn);
    @GET("manufacturers/{hsn}/vehicles/{tsn}")
    Observable<modelinformation> getcategory(@Path("hsn") String hsn,@Path("tsn") String tsn);


}
