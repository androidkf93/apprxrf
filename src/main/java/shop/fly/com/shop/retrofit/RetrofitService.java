package shop.fly.com.shop.retrofit;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import shop.fly.com.shop.bean.BaseBean;
import shop.fly.com.shop.bean.CategoryBean;
import shop.fly.com.shop.bean.CategoryUpdate;
import shop.fly.com.shop.bean.Login;
import shop.fly.com.shop.bean.OrderBean;
import shop.fly.com.shop.bean.OrderCount;
import shop.fly.com.shop.bean.OrderSumBean;
import shop.fly.com.shop.bean.ProductBean;
import shop.fly.com.shop.bean.ProductUpdate;
import shop.fly.com.shop.bean.SpecBean;
import shop.fly.com.shop.bean.UserBean;


/**
 * Created by Administrator on 2017/8/7.
 */

public interface RetrofitService {

    @POST("Account/Login")
    Observable<BaseBean<UserBean>> login(@Body Login login);

    @GET("handler/GetVersion")
    Observable<BaseBean<String>> getVersion();

    @GET("Order/QueryOrderCount")
    Observable<BaseBean<OrderCount>> queryOrderCount(@Query("date") String date);

    @GET("Order/Query")
    Observable<BaseBean<ArrayList<OrderBean>>> queryOrder(@Query("page") int page, @Query("pageSize") int pageSize,
                                                          @Query("status") int status, @Query("time") String time,
                                                          @Query("date") String date, @Query("key") String key,
                                                          @Query("token") String token);

    @GET("Order/ConfirmOrder")
    Observable<BaseBean<String>> confirmOrder(@Query("orderNo") String orderNo);

    @GET("Order/ConfirmRefund")
    Observable<BaseBean<String>> confirmRefund(@Query("orderNo") String orderNo);

    @GET("Order/OrderSum")
    Observable<BaseBean<ArrayList<OrderSumBean>>> getOrderSum();

    @GET("Order/RejectRefundOrder")
    Observable<BaseBean<String>> getRejectRefunOrder();

    @POST("Product/Add")
    Observable<BaseBean<String>> productAdd(@Body ProductUpdate productUpdate);

    @POST("Product/UpShelf")
    Observable<BaseBean<String>> productUpShelf(@Query("productId") int productId);

    @POST("Product/DownShelf")
    Observable<BaseBean<String>> productDownShelf(@Query("productId") int productId);

    @POST("Product/Delete")
    Observable<BaseBean<String>> productDelete(@Query("productId") int productId);

    @GET("Product/Query")
    Observable<BaseBean<ArrayList<ProductBean>>> getProductQuery(@Query("categoryId") int id);

    @GET("Category/Query")
    Observable<BaseBean<List<CategoryBean>>> getCategoryQuery();

    @POST("Category/Add")
    Observable<BaseBean<String>> categoryAdd(@Body CategoryUpdate categoryUpdate);

    @POST("Category/Update")
    Observable<BaseBean<String>> categoryUpdate(@Body CategoryUpdate categoryUpdate);

    @POST("CategoryDelete")
    Observable<BaseBean<String>> categoryDelete(@Query("id") int id);

    @GET("Category/Sort")
    Observable<BaseBean<String>> getCategorySort(@Query("categoryId") int categoryId, @Query("up") boolean up);

    @GET("ProductPro/Query")
    Observable<BaseBean<ArrayList<SpecBean>>> getProductProQuery(@Query("productId") int productId);

    @POST("ProductPro/Add")
    Observable<BaseBean<String>> productProAdd(@Body SpecBean specBean);

    @POST("ProductPro/Update")
    Observable<BaseBean<String>> productProUpdate(@Body SpecBean specBean);

    @POST("ProductPro/Delete")
    Observable<BaseBean<String>> getProductProDelete(@Query("id") int id);

    @POST("Store/DownShelf")
    Observable<BaseBean<Boolean>> getStoreDownShelf(@Query("enable") boolean enable);
}
