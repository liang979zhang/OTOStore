package com.example.administrator.otostore.RxJavaUtils;


import java.math.BigDecimal;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

//    final String BASE_URL = "http://192.168.0.254:8080/chat-face/";

    //    final String BASE_URL = "http://192.168.138.85:8888/";
    final String BASE_URL = "http://115.28.214.108:9081/";

    /*
  *  用户登录
  * */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/UserLogin")
    @FormUrlEncoded
    Observable<String> UserLogin(
            @Field("Json") String Json,
            @Field("ClntType") int ClntType,
            @Field("MobileNum") String MobileNum,
            @Field("EnPwd") String EnPwd

    );

    /*
    *1. 行业大分类获取
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/GetClassList")
    @FormUrlEncoded
    Observable<String> GetClassList(
            @Field("Json") String Json
    );

      /*
    * 2.行业小分类获取
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/GetDevisionList")
    @FormUrlEncoded
    Observable<String> GetDevisionList(
            @Field("Json") String Json,
            @Field("ParCode") int ParCode
    );

    /*
  * 生成验证码
  * */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("CommService.asmx/GenSMSVerifyCode")
    @FormUrlEncoded
    Observable<String> GenSMSVerifyCode(
            @Field("Json") String Json,
            @Field("UserID") long UserID,
            @Field("MobileNum") String MobileNum,
            @Field("VerifyType") int VerifyType
    );

    /*
   * 用户注册
   * */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/UserRegister")
    @FormUrlEncoded
    Observable<String> UserRegister(
            @Field("Json") String Json,
            @Field("ClntType") int ClntType,
            @Field("ParUserID") long ParUserID,
            @Field("Gender") int Gender,
            @Field("NickName") String NickName,
            @Field("Email") String Email,
            @Field("MobileNum") String MobileNum,
            @Field("UserPwd") String UserPwd
    );

     /*
    * 获取推荐人
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/GetUserBaseInfoA")
    @FormUrlEncoded
    Observable<String> GetUserBaseInfoA(
            @Field("Json") String Json,
            @Field("MobileNum") String MobileNum

    );
    /*
    * 3店铺注册
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/ShopRegister")
    @FormUrlEncoded
    Observable<String> ShopRegister(
            @Field("Json") String Json,
            @Field("UserID") long UserID,
            @Field("ShopName") String ShopName,
            @Field("ShopClass") int ShopClass,
            @Field("ShopDivision") int ShopDivision

    );
    /*店铺升级
    */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/PromoteUserType")
    @FormUrlEncoded
    Observable<String> PromoteUserType(
            @Field("Json") String Json,
            @Field("UserID") long UserID
//            @Field("ShopName") String ShopName,
//            @Field("ShopClass") int ShopClass,
//            @Field("ShopDivision") int ShopDivision

    );

    /*
 * 重置密码
 * */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/ResetUserPwd")
    @FormUrlEncoded
    Observable<String> ResetUserPwd(
            @Field("Json") String Json,
            @Field("MobileNum") String MobileNum,
            @Field("NewEnPwd") String NewEnPwd

    );

    /**********************************************************************************************************************************************************************************************/
    /*
    * 4获取店铺用户信息
    * */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/GetShopInfo")
    @FormUrlEncoded
    Observable<String> GetShopInfo(
            @Field("Json") String Json,
            @Field("UserID") long UserID

            /*
            * GetShopInfo
            * */
    );

    /*
    * 5获取店铺信息
    * */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/GetShopInfoEx")
    @FormUrlEncoded
    Observable<String> GetShopInfoEx(
            @Field("Json") String Json,
            @Field("ShopID") long ShopID
    );

    /*
    * 6关闭店铺
    * */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/CloseShop")
    @FormUrlEncoded
    Observable<String> CloseShop(
            @Field("Json") String Json,
            @Field("UserID") long UserID
    );

    /*
   * 8 设置店铺坐标
   * */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/SetShopLonLat")
    @FormUrlEncoded
    Observable<String> SetShopLonLat(
            @Field("Json") String Json,
            @Field("ShopID") long ShopID,
            @Field("Longitude") BigDecimal Longitude,
            @Field("Latitude") BigDecimal Latitude
    );

    /*
    * 10 店铺提交审核
    * */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/ShopSubmit")
    @FormUrlEncoded
    Observable<String> ShopSubmit(
            @Field("Json") String Json,
            @Field("UserID") long UserID,
            @Field("ShopID") long ShopID
    );

    /*
    * 7 编辑店铺信息
    * */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/EditShopInfo")
    @FormUrlEncoded
    Observable<String> EditShopInfo(
            @Field("Json") String Json,
            @Field("ShopID") long ShopID,
            @Field("ShopName") String ShopName,
            @Field("ShopClass") int ShopClass,
            @Field("ShopDivision") int ShopDivision,
            @Field("NickName") String NickName,
            @Field("ContectTel") String ContectTel,
            @Field("Longitude") BigDecimal Longitude,
            @Field("Latitude") BigDecimal Latitude,
            @Field("ProvinceCode") String ProvinceCode,
            @Field("CityCode") String CityCode,
            @Field("DistrictCode") String DistrictCode,
            @Field("Addr") String Addr,
            @Field("LegalRepresentative") String LegalRepresentative,
            @Field("LegalID") String LegalID,
            @Field("RegistrationNo") String RegistrationNo,
            @Field("BusinessLicenceName") String BusinessLicenceName,
            @Field("OpeningTime") String OpeningTime,
            @Field("ClosingTime") String ClosingTime,
            @Field("Remark") String Remark
    );

    /*
     * 9 设置店铺设置
     * */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/SetShopSettings")
    @FormUrlEncoded
    Observable<String> SetShopSettings(
            @Field("Json") String Json,
            @Field("ShopID") long ShopID,
            @Field("OrderType") int OrderType,
            @Field("DeliverFee") BigDecimal DeliverFee,
            @Field("DeliveryNoChargeAmt") BigDecimal DeliveryNoChargeAmt,
            @Field("PackingFee") BigDecimal PackingFee,
            @Field("PackingNoChargeAmt") BigDecimal PackingNoChargeAmt,
            @Field("Discount") BigDecimal Discount,
            @Field("ClosingMode") int ClosingMode,
            @Field("ClosingStartDate") String ClosingStartDate,
            @Field("ClosingEndDate") String ClosingEndDate,
            @Field("ReturnAddrID") long ReturnAddrID,
            @Field("ValidDays") int ValidDays,
            @Field("Remark") String Remark
    );

    /*
  * 11 获取店铺最近的年费信息
  * */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/GetLatestShopFee")
    @FormUrlEncoded
    Observable<String> GetLatestShopFee(
            @Field("Json") String Json,
            @Field("ShopID") long ShopID
    );

    /*
* 12 获取店铺年费缴纳列表
* */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/GetShopFeeList")
    @FormUrlEncoded
    Observable<String> GetShopFeeList(
            @Field("Json") String Json,
            @Field("ShopID") long ShopID
    );

    /*
* 13 店铺年费缴纳
* */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/NewShopFee")
    @FormUrlEncoded
    Observable<String> NewShopFee(
            @Field("Json") String Json,
            @Field("ShopID") long ShopID,
            @Field("FeeType") int FeeType,
            @Field("FeeAmt") BigDecimal FeeAmt
    );

    /*
    * 14.店铺修改年费信息
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/EditShopFee")
    @FormUrlEncoded
    Observable<String> EditShopFee(
            @Field("Json") String Json,
            @Field("ShopFeeID") long ShopFeeID,
            @Field("FeeType") int FeeType,
            @Field("FeeAmt") BigDecimal FeeAmt,
            @Field("Remark") String Remark,
            @Field("ShopFeeStatus") int ShopFeeStatus
    );

      /*
    * 15 保存店铺分类(新增或编辑)
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/SaveShopCategory")
    @FormUrlEncoded
    Observable<String> SaveShopCategory(
            @Field("Json") String Json,
            @Field("CatID") long CatID,
            @Field("ShopID") long ShopID,
            @Field("CatName") String CatName,
            @Field("CatIdx") int CatIdx,
            @Field("Remark") String Remark,
            @Field("ParCatCode") String ParCatCode
    );

      /*
    * 16 删除店铺产品分类
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/DeleteShopCategory")
    @FormUrlEncoded
    Observable<String> DeleteShopCategory(
            @Field("Json") String Json,
            @Field("CatCode") String CatCode

    );

     /*
    * 17 获取店铺分类(及子类)
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/GetShopCategory")
    @FormUrlEncoded
    Observable<String> GetShopCategory(
            @Field("Json") String Json,
            @Field("ShopID") long ShopID,
            @Field("ParCatCode") String ParCatCode

    );

      /*
    * 18 获取店铺分类(及子类)
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/GetShopCategoryInfo")
    @FormUrlEncoded
    Observable<String> GetShopCategoryInfo(
            @Field("Json") String Json,
            @Field("ShopID") long ShopID
    );

        /*
    * 19 保存店铺产品(新增、编辑)
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/SaveShopProduct")
    @FormUrlEncoded
    Observable<String> SaveShopProduct(
            @Field("Json") String Json,
            @Field("ProductID") long ProductID,
            @Field("ShopID") long ShopID,
            @Field("CatID") long CatID,
            @Field("ProductCode") String ProductCode,
            @Field("ProductName") String ProductName,
            @Field("ValidDays") int ValidDays,
            @Field("Unit") String Unit,
            @Field("SupportVoucher") int SupportVoucher,
            @Field("SupportDispatch") int SupportDispatch,
            @Field("Remark") String Remark
    );

       /*
    * 20 产品上架/下架
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/SetProductOnOff")
    @FormUrlEncoded
    Observable<String> SetProductOnOff(
            @Field("Json") String Json,
            @Field("ProductID") long ProductID,
            @Field("ProductStatus") int ProductStatus,
            @Field("Remark") String Remark
    );

      /*
    * 21 获取店铺分类产品列表(当前分类及其子类)
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/GetProductList")
    @FormUrlEncoded
    Observable<String> GetProductList(
            @Field("Json") String Json,
            @Field("ShopID") long ShopID,
            @Field("CatCode") String CatCode
    );

     /*
    * 22 获取店铺分类产品列表(当前分类及其子类)
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/GetProduct")
    @FormUrlEncoded
    Observable<String> GetProduct(
            @Field("Json") String Json,
            @Field("ProductID") long ProductID
    );

      /*
    * 23 保存产品规格(新增、保存)
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/SaveProductSpec")
    @FormUrlEncoded
    Observable<String> SaveProductSpec(
            @Field("Json") String Json,
            @Field("ProductSpecID") long ProductSpecID,
            @Field("ProductID") long ProductID,
            @Field("SpecIdx") int SpecIdx,
            @Field("SpecCode") String SpecCode,
            @Field("ProductSpec") String ProductSpec,
            @Field("Price") BigDecimal Price,
            @Field("Quantity") int Quantity,
            @Field("Remark") String Remark
    );

         /*
    * 24 获取产品规格列表
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/GetProductSpecList")
    @FormUrlEncoded
    Observable<String> GetProductSpecList(
            @Field("Json") String Json,
            @Field("ProductID") long ProductID,
            @Field("SpecStatusLst") String SpecStatusLst
    );

          /*
    * 25 获取店铺分类规格产品列表
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/GetProductSpecListEx")
    @FormUrlEncoded
    Observable<String> GetProductSpecListEx(
            @Field("Json") String Json,
            @Field("ProductID") long ProductID,
            @Field("CatCode") String CatCode,
            @Field("QueryType") int QueryType
    );

    /*
* 26 获取产品规格
* */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/GetProductSpec")
    @FormUrlEncoded
    Observable<String> GetProductSpec(
            @Field("Json") String Json,
            @Field("ProductSpecID") long ProductSpecID
    );

    /*
  * 27  删除产品规格
  * */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/DeleteProductSpec")
    @FormUrlEncoded
    Observable<String> DeleteProductSpec(
            @Field("Json") String Json,
            @Field("ProductSpecID") long ProductSpecID
    );

    /*
   * 28  获取商品规格库存
   * */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/GetProductSpecStock")
    @FormUrlEncoded
    Observable<String> GetProductSpecStock(
            @Field("Json") String Json,
            @Field("ProductSpecID") long ProductSpecID
    );

    /*
 * 29  保存店铺优惠券(新增、编辑) 部分数据列需要结合优惠券类型设置，部分数据列在某优惠券类型不需要
 * */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/SaveShopVoucher")
    @FormUrlEncoded
    Observable<String> SaveShopVoucher(
            @Field("Json") String Json,
            @Field("VoucherID") long VoucherID,
            @Field("ShopID") long ShopID,
            @Field("VoucherType") int VoucherType,
            @Field("VoucherCode") String VoucherCode,
            @Field("VoucherName") String VoucherName,
            @Field("VoucherQty") int VoucherQty,
            @Field("IsAddUp") int IsAddUp,
            @Field("LimitedQty") int LimitedQty,
            @Field("StartDate") String StartDate,
            @Field("EndDate") String EndDate,
            @Field("LimitedAmt") BigDecimal LimitedAmt,
            @Field("DiscountAmt") BigDecimal DiscountAmt,
            @Field("GiftID") long GiftID,
            @Field("GiftAmt") BigDecimal GiftAmt,
            @Field("DiscountRate") BigDecimal DiscountRate,
            @Field("FreeAmt") BigDecimal FreeAmt,
            @Field("Remark") String Remark
    );

    /*
 * 30  获取店铺特定类别代金券券列表
 * */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/GetShopVoucherList")
    @FormUrlEncoded
    Observable<String> GetShopVoucherList(
            @Field("Json") String Json,
            @Field("ShopID") long ShopID,
            @Field("VoucherType") int VoucherType
    );

    /*
 * 31  获取店铺优惠券
 * */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/GetShopVoucher")
    @FormUrlEncoded
    Observable<String> GetShopVoucher(
            @Field("Json") String Json,
            @Field("VoucherID") long VoucherID
    );

    /*
* 32  设置店铺优惠券过期
* */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/SetShopVoucherInvalid")
    @FormUrlEncoded
    Observable<String> SetShopVoucherInvalid(
            @Field("Json") String Json,
            @Field("VoucherID") long VoucherID
    );

    /*
* 33  发布代金券
* */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/PublicShopVoucher")
    @FormUrlEncoded
    Observable<String> PublicShopVoucher(
            @Field("Json") String Json,
            @Field("ShopID") long ShopID,
            @Field("VoucherID") long VoucherID
    );


    /*
* 34  综合搜索店铺
* */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/SearchShops")
    @FormUrlEncoded
    Observable<String> SearchShops(
            @Field("Json") String Json,
            @Field("ShopClass") int ShopClass,
            @Field("ShopDivision") int ShopDivision,
            @Field("ProvinceCode") String ProvinceCode,
            @Field("CityCode") String CityCode,
            @Field("DistrictCode") String DistrictCode,
            @Field("ShopName") String ShopName,
            @Field("Longitude") BigDecimal Longitude,
            @Field("Latitude") BigDecimal Latitude,
            @Field("Radius") int Radius,
            @Field("RowsPerPage") int RowsPerPage,
            @Field("PageNum") int PageNum
    );

    /*
* 35 按店铺名称或昵称搜索店铺
* */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/SearchShopsByName")
    @FormUrlEncoded
    Observable<String> SearchShopsByName(
            @Field("Json") String Json,
            @Field("ShopName") String ShopName,
            @Field("RowsPerPage") int RowsPerPage,
            @Field("PageNum") int PageNum
    );

    /*
* 36 获取店铺相册列表
* */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/GetShopAttachList")
    @FormUrlEncoded
    Observable<String> GetShopAttachList(
            @Field("Json") String Json,
            @Field("ShopID") long ShopID,
            @Field("AttachType") int AttachType,
            @Field("RowsPerPage") int RowsPerPage,
            @Field("PageNum") int PageNum
    );

    /*
* 37 获取产品相册列表
* */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/GetProductAttachList")
    @FormUrlEncoded
    Observable<String> GetProductAttachList
    (
            @Field("Json") String Json,
            @Field("ProductID") long ProductID,
            @Field("AttachType") int AttachType,
            @Field("RowsPerPage") int RowsPerPage,
            @Field("PageNum") int PageNum
    );

    /*
* 38 获取用户地址列表
* */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/GetUserAddress")
    @FormUrlEncoded
    Observable<String> GetUserAddress
    (
            @Field("Json") String Json,
            @Field("UserID") long UserID
    );

    /*
* 39 获取店铺设置
* */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("ShopService.asmx/GetShopSettings")
    @FormUrlEncoded
    Observable<String> GetShopSettings
    (
            @Field("Json") String Json,
            @Field("ShopID") long ShopID
    );
 /*
    * 40上传图片,从Base64格式保存为本地文件
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("CommService.asmx/UploadFile")
    @FormUrlEncoded
    Observable<String> UploadFile(
            @Field("Json") String Json,
            @Field("UserID") long UserID,
            @Field("OwnerType") int OwnerType,
            @Field("OwnerID") long OwnerID,
            @Field("AttachType") int AttachType,
            @Field("AttachName") String AttachName,
            @Field("IsCover") boolean IsCover,
            @Field("ImgBase64") String ImgBase64

//
    );

    /* 41 获取用户基本信息:UserID
* */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/GetUserBaseInfo")
    @FormUrlEncoded
    Observable<String> GetUserBaseInfo
    (
            @Field("Json") String Json,
            @Field("UserID") int UserID
    );

    /* 42 用户信息变更:UserID
* */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/EditUserInfo")
    @FormUrlEncoded
    Observable<String> EditUserInfo
    (
            @Field("Json") String Json,
            @Field("UserID") int UserID,
            @Field("InfoType") int InfoType,
            @Field("Content") String Content
    );

    /* 43 获取当前用户的用户组织
* */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/GetCurrUserOrg")
    @FormUrlEncoded
    Observable<String> GetCurrUserOrg
    (
            @Field("Json") String Json,
            @Field("UserID") int UserID
    );

    /* 44 获取用户相册列表
* */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/GetUserAttachList")
    @FormUrlEncoded
    Observable<String> GetUserAttachList
    (
            @Field("Json") String Json,
            @Field("UserID") long UserID,
            @Field("AttachType") int AttachType,
            @Field("RowsPerPage") int RowsPerPage,
            @Field("PageNum") int PageNum
    );

    /*
* 45 加好友申请
* */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/DoMakeFriend")
    @FormUrlEncoded
    Observable<String> DoMakeFriend(
            @Field("Json") String Json,
            @Field("UserID") long UserID,
            @Field("FriendUserID") long FriendUserID,
            @Field("UserGroupID") long UserGroupID,
            @Field("Remark") String Remark
//
    );

    /*
* 46 获取我(及好友)的动态列表
* */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/GetUserPubMovementList")
    @FormUrlEncoded
    Observable<String> GetUserPubMovementList(
            @Field("Json") String Json,
            @Field("UserID") long UserID,
            @Field("ShopID") long ShopID,
            @Field("RowsPerPage") int RowsPerPage,
            @Field("PageNum") int PageNum
//
    );

    /*
 * 47 修改用户密码
 * */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/ChangeUserPwd")
    @FormUrlEncoded
    Observable<String> ChangeUserPwd(
            @Field("Json") String Json,
            @Field("UserID") long UserID,
            @Field("OldEnPwd") String OldEnPwd,
            @Field("NewEnPwd") String NewEnPwd
    );

    /*
* 48 商家.获取订单列表(倒序)
* */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("BusinessService.asmx/GetShopOrders")
    @FormUrlEncoded
    Observable<String> GetShopOrders(
            @Field("Json") String Json,
            @Field("ShopID") long ShopID,
            @Field("OrderType") int OrderType,
            @Field("LatestMonth") int LatestMonth,
            @Field("OrderStatusLst") String OrderStatusLst,
            @Field("RowsPerPage") int RowsPerPage,
            @Field("PageNum") int PageNum
    );

           /*
    * 49.手机号码验证验证码
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("CommService.asmx/VerifyIDNum")
    @FormUrlEncoded
    Observable<String> VerifyIDNum(
            @Field("Json") String Json,
            @Field("IDNum") String IDNum

    );

         /*
    * 50.发布动态
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/PublishMovement")
    @FormUrlEncoded
    Observable<String> PublishMovement(
            @Field("Json") String Json,
            @Field("UserID") Long UserID,
            @Field("ShopID") Long ShopID,
            @Field("Remark") String Remark

    );


    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/EditMovement")
    @FormUrlEncoded
    Observable<String> EditMovement(
            @Field("Json") String Json,
            @Field("UserID") Long UserID,
            @Field("MovementID") Long MovementID,
            @Field("Remark") String Remark

    );

        /*
    * 55.评价
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/DoCommentMovement")
    @FormUrlEncoded
    Observable<String> DoCommentMovement(
            @Field("Json") String Json,
            @Field("UserID") Long UserID,
            @Field("MovementID") Long MovementID,
            @Field("RefCommentID") Long RefCommentID,
            @Field("Remark") String Remark

    );

         /*
    * 56.获取动态评价列表
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/GetMovementCommentList")
    @FormUrlEncoded
    Observable<String> GetMovementCommentList(
            @Field("Json") String Json,
            @Field("MovementID") Long MovementID

    );

       /*
    * 57.动态评价(赞、踩)
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/DoMovementAppraise")
    @FormUrlEncoded
    Observable<String> DoMovementAppraise(
            @Field("Json") String Json,
            @Field("UserID") Long UserID,
            @Field("MovementID") Long MovementID,
            @Field("MovementAppraiseType") int MovementAppraiseType

    );

      /*
    * 58.取消动态评价(赞、踩)
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/DoCancelMovementAppraise")
    @FormUrlEncoded
    Observable<String> DoCancelMovementAppraise(
            @Field("Json") String Json,
            @Field("UserID") Long UserID,
            @Field("MovementID") Long MovementID,
            @Field("MovementAppraiseType") int MovementAppraiseType

    );

    /*
    * 59. 收藏动态
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/DoCollectMovement")
    @FormUrlEncoded
    Observable<String> DoCollectMovement(
            @Field("Json") String Json,
            @Field("UserID") Long UserID,
            @Field("MovementID") Long MovementID,
            @Field("Remark") String Remark

    );
      /*
    * 60. 取消收藏动态
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/DoCancelCollectMovement")
    @FormUrlEncoded
    Observable<String> DoCancelCollectMovement(
            @Field("Json") String Json,
            @Field("UserID") Long UserID,
            @Field("MovementID") Long MovementID

    );

    /*
    * 61. 获取用户钱包
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("FinService.asmx/GetUserWallet")
    @FormUrlEncoded
    Observable<String> GetUserWallet(
            @Field("Json") String Json,
            @Field("UserID") Long UserID

    );
     /*
    * 62. 用户结算汇总(包含全部、待结、已结 与10配套使用)
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("FinService.asmx/GetUserSettleSum")
    @FormUrlEncoded
    Observable<String> GetUserSettleSum(
            @Field("Json") String Json,
            @Field("WalletID") Long WalletID

    );

      /*
    * 63. 用户已结算明细(已结算)
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("FinService.asmx/GetUserSettledDetial")
    @FormUrlEncoded
    Observable<String> GetUserSettledDetial(
            @Field("Json") String Json,
            @Field("WalletID") Long WalletID,
            @Field("YearMonth") String YearMonth,
            @Field("RowsPerPage") int RowsPerPage,
            @Field("PageNum") int PageNum

    );

      /*
    * 64. 获取用户钱包积分转账列表
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("FinService.asmx/GetBonusTransferList")
    @FormUrlEncoded
    Observable<String> GetBonusTransferList(
            @Field("Json") String Json,
            @Field("WalletID") Long WalletID,
            @Field("YearMonth") String YearMonth,
            @Field("InOutType") int InOutType,
            @Field("RowsPerPage") int RowsPerPage,
            @Field("PageNum") int PageNum

    );

       /*
    * 65. 获取用户积分兑换列表
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("FinService.asmx/GetUserIntegralExchList")
    @FormUrlEncoded
    Observable<String> GetUserIntegralExchList(
            @Field("Json") String Json,
            @Field("WalletID") Long WalletID,
            @Field("YearMonth") String YearMonth,
            @Field("RowsPerPage") int RowsPerPage,
            @Field("PageNum") int PageNum

    );

      /*
    * 66. 获取结算货款明细
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("FinService.asmx/GetSettlePaymentDetailForGoods")
    @FormUrlEncoded
    Observable<String> GetSettlePaymentDetailForGoods(
            @Field("Json") String Json,
            @Field("WalletID") Long WalletID,
            @Field("aSettleType") int aSettleType,
            @Field("StartDate") String StartDate,
            @Field("EndDate") String EndDate,
            @Field("RowsPerPage") int RowsPerPage,
            @Field("PageNum") int PageNum

    );

      /*
    * 67. 获取用户银行卡列表
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/GetUserBankCardLst")
    @FormUrlEncoded
    Observable<String> GetUserBankCardLst(
            @Field("Json") String Json,
            @Field("UserID") Long UserID

    );

     /*
    * 68. 保存用户银行卡
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/DoSaveUserBankCard")
    @FormUrlEncoded
    Observable<String> DoSaveUserBankCard(
            @Field("Json") String Json,
            @Field("BankCardID") Long BankCardID,
            @Field("UserID") Long UserID,
            @Field("AccType") int AccType,
            @Field("CardHolder") String CardHolder,
            @Field("BankAccCode") String BankAccCode,
            @Field("AliasName") String AliasName,
            @Field("CardIdx") int CardIdx,
            @Field("DefaultWithdraw") int DefaultWithdraw,
            @Field("Remark") String Remark

    );
      /*
    * 68. 获取发卡行信息
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("SystemService.asmx/GetIssuerCardInfo")
    @FormUrlEncoded
    Observable<String> GetIssuerCardInfo(
            @Field("Json") String Json,
            @Field("CardNo") String CardNo

    );

    /*
    * 69. 设置/修改用户支付密码(必须通过验证才能调用)
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/SetUserPayPwd")
    @FormUrlEncoded
    Observable<String> SetUserPayPwd(
            @Field("Json") String Json,
            @Field("UserID") long UserID,
            @Field("OldPayPwd") String OldPayPwd,
            @Field("NewPayPwd") String NewPayPwd

    );

     /*
    * 70. 验证码验证
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("CommService.asmx/VerifyVerCode")
    @FormUrlEncoded
    Observable<String> VerifyVerCode(
            @Field("Json") String Json,
            @Field("UserID") long UserID,
            @Field("VerifyMode") int VerifyMode,
            @Field("VerifyType") int VerifyType,
            @Field("VerifyCode") String VerifyCode

    );


         /*
    * 71. 判定pasward
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/GetUserBaseInfo")
    @FormUrlEncoded
    Observable<String> GetUserBaseInfo(
            @Field("Json") String Json,
            @Field("UserID") long UserID);
  /*
    * 72. 重置用户支付密码(必须通过验证才能调用)
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/ResetUserPayPwd")
    @FormUrlEncoded
    Observable<String> ResetUserPayPwd(
            @Field("Json") String Json,
            @Field("UserID") long UserID,
            @Field("VerifyMode") int VerifyMode,
            @Field("VerCode") String VerCode
    );

    /*
    * 74. 获取用户钱包积分明细
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("FinService.asmx/GetUserBonusInoutList")
    @FormUrlEncoded
    Observable<String> GetUserBonusInoutList(
            @Field("Json") String Json,
            @Field("WalletID") long WalletID,
            @Field("YearMonth") String YearMonth,
            @Field("RowsPerPage") int RowsPerPage,
            @Field("PageNum") int PageNum
    );

    /*
    * 75. 加密字符串
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("CommService.asmx/EncryptStr")
    @FormUrlEncoded
    Observable<String> EncryptStr(
            @Field("Json") String Json,
            @Field("SrcStr") long SrcStr

    );

     /*
    * 76. 删除用户银行卡
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/DeleteUserBankCard")
    @FormUrlEncoded
    Observable<String> DeleteUserBankCard(
            @Field("Json") String Json,
            @Field("UserID") long UserID,
            @Field("BankCardID") long BankCardID

    );

       /*
    * 77. 积分兑换
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("FinService.asmx/DoIntegralExch")
    @FormUrlEncoded
    Observable<String> DoIntegralExch(
            @Field("Json") String Json,
            @Field("UserID") long UserID,
            @Field("ExchAmt") int ExchAmt,
            @Field("PayPwd") String PayPwd,
            @Field("Remark") String Remark

    );

      /*
    * 78. 积分转账
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("FinService.asmx/BonusTransfer")
    @FormUrlEncoded
    Observable<String> BonusTransfer(
            @Field("Json") String Json,
            @Field("ToUserID") long ToUserID,
            @Field("FromUserID") long FromUserID,
            @Field("TransferBonus") int TransferBonus,
            @Field("PayPwd") String PayPwd,
            @Field("Remark") String Remark

    );

      /*
    * 79. 获取用户钱包余额明细
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("FinService.asmx/GetUserBalanceInoutList")
    @FormUrlEncoded
    Observable<String> GetUserBalanceInoutList(
            @Field("Json") String Json,
            @Field("WalletID") long WalletID,
            @Field("YearMonth") String YearMonth,
            @Field("RowsPerPage") int RowsPerPage,
            @Field("PageNum") int PageNum

    );

     /*
    * 80. 获取用户钱包余额转账列表
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("FinService.asmx/GetBalanceTransferList")
    @FormUrlEncoded
    Observable<String> GetBalanceTransferList(
            @Field("Json") String Json,
            @Field("WalletID") long WalletID,
            @Field("YearMonth") String YearMonth,
            @Field("InOutType") int InOutType,
            @Field("RowsPerPage") int RowsPerPage,
            @Field("PageNum") int PageNum

    );

    /*
    * 81. 余额转账
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("FinService.asmx/BalanceTransfer")
    @FormUrlEncoded
    Observable<String> BalanceTransfer(
            @Field("Json") String Json,
            @Field("ToUserID") long ToUserID,
            @Field("FromUserID") long FromUserID,
            @Field("TransferAmt") BigDecimal TransferAmt,
            @Field("PayPwd") String PayPwd,
            @Field("Remark") String Remark

    );

      /*
    * 82. 获取最近月份的提现单列表
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("FinService.asmx/GetWithDrawBillList")
    @FormUrlEncoded
    Observable<String> GetWithDrawBillList(
            @Field("Json") String Json,
            @Field("WalletID") long WalletID,
            @Field("YearMonth") String YearMonth,
            @Field("RowsPerPage") int RowsPerPage,
            @Field("PageNum") int PageNum
    );

       /*
    * 83. 余额提现
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("FinService.asmx/DoWithDraw")
    @FormUrlEncoded
    Observable<String> DoWithDraw(
            @Field("Json") String Json,
            @Field("UserID") long UserID,
            @Field("BankCardID") long BankCardID,
            @Field("WithDrawAmt") BigDecimal WithDrawAmt,
            @Field("PayPwd") String PayPwd,
            @Field("Remark") String Remark
    );

       /*
    * 84. 用户结算明细(全部)
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("FinService.asmx/GetUserSettleDetial")
    @FormUrlEncoded
    Observable<String> GetUserSettleDetial(
            @Field("Json") String Json,
            @Field("WalletID") long WalletID,
            @Field("YearMonth") String YearMonth,
            @Field("RowsPerPage") int RowsPerPage,
            @Field("PageNum") int PageNum
    );

    /*
    * 85. 用户待结算明细(待结算)
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("FinService.asmx/GetUserSettlingDetial")
    @FormUrlEncoded
    Observable<String> GetUserSettlingDetial(
            @Field("Json") String Json,
            @Field("WalletID") long WalletID,
            @Field("RowsPerPage") int RowsPerPage,
            @Field("PageNum") int PageNum
    );

    /*
    * 86. 获取结算货款明细
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("FinService.asmx/GetSettlePaymentDetailForGoods")
    @FormUrlEncoded
    Observable<String> GetSettlePaymentDetailForGoods(
            @Field("Json") String Json,
            @Field("WalletID") long WalletID,
            @Field("aSettleType") int aSettleType,
            @Field("StartDate") String StartDate,
            @Field("EndDate") String EndDate,
            @Field("RowsPerPage") int RowsPerPage,
            @Field("PageNum") int PageNum

            );

    /*
    * 87. 获取加好友的新申请
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/GetMakeFriendApplication")
    @FormUrlEncoded
    Observable<String> GetMakeFriendApplication(
            @Field("Json") String Json,
            @Field("UserID") long UserID

    );

     /*
    * 88. 回复加好友申请
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/ResponseToMakingFriend")
    @FormUrlEncoded
    Observable<String> ResponseToMakingFriend(
            @Field("Json") String Json,
            @Field("UserID") long UserID,
            @Field("FriendID") long FriendID,
            @Field("ResAction") int ResAction,
            @Field("Remark") String Remark,
            @Field("Appending") int Appending

    );

    /*
    * 89. 获取好友列表(正式好友)
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/GetUserFriends")
    @FormUrlEncoded
    Observable<String> GetUserFriends(
            @Field("Json") String Json,
            @Field("UserID") long UserID

    );
     /*
    * 90. 获取用户银行卡列表
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/GetUserBankCardLst")
    @FormUrlEncoded
    Observable<String> GetUserBankCardLst(
            @Field("Json") String Json,
            @Field("UserID") long UserID

    );


    /*
    * 91. 获取动态相册列表
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("UserService.asmx/GetMovementAttachList")
    @FormUrlEncoded
    Observable<String> GetMovementAttachList(
            @Field("Json") String Json,
            @Field("MovementID") long MovementID
    );


    /*
    * 92. 获取店铺退款单列表
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("BusinessService.asmx/GetShopRefundBills")
    @FormUrlEncoded
    Observable<String> GetShopRefundBills(
            @Field("Json") String Json,
            @Field("ShopID") long ShopID,
            @Field("RefundStatus") int RefundStatus,
            @Field("RowsPerPage") int RowsPerPage,
            @Field("PageNum") int PageNum
    );

      /*
    * 92. 订单.商家调整订单金额
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("BusinessService.asmx/SellerAdjustAmt")
    @FormUrlEncoded
    Observable<String> SellerAdjustAmt(
            @Field("Json") String Json,
            @Field("UserID") long UserID,
            @Field("OrderID") long OrderID,
            @Field("SellerAdjustAmt") BigDecimal SellerAdjustAmt
    );

      /*
    * 92. 延长订单收货/到店期限
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("BusinessService.asmx/DoDelayOrderSuccessTime")
    @FormUrlEncoded
    Observable<String> DoDelayOrderSuccessTime(
            @Field("Json") String Json,
            @Field("UserID") long UserID,
            @Field("OrderID") long OrderID,
            @Field("DelayDays") int DelayDays
    );


    /*
    * 93. 商家.确认到店消费(相当于收件)
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("BusinessService.asmx/ConfirmArrivalConsumption")
    @FormUrlEncoded
    Observable<String> ConfirmArrivalConsumption(
            @Field("Json") String Json,
            @Field("UserID") long UserID,
            @Field("ShopID") long ShopID,
            @Field("OrderID") long OrderID,
            @Field("VerifyCode") String VerifyCode,
            @Field("Remark") String Remark
    );
/*
    * 94. 商家.同意退款(店铺设置默认退货地址)
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("BusinessService.asmx/AgreeRefundBill")
    @FormUrlEncoded
    Observable<String> AgreeRefundBill(
            @Field("Json") String Json,
            @Field("UserID") long UserID,
            @Field("RefundBillID") long RefundBillID,
            @Field("Remark") String Remark
    );

    /*
    * 95. 商家.拒绝退款(拒绝退款凭证上传参照 16)
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("BusinessService.asmx/RefuseRefundBill")
    @FormUrlEncoded
    Observable<String> RefuseRefundBill(
            @Field("Json") String Json,
            @Field("UserID") long UserID,
            @Field("RefundBillID") long RefundBillID,
            @Field("Remark") String Remark
    );
/*
    * 96.商家.确认退款()
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("BusinessService.asmx/ConfirmRefundBill")
    @FormUrlEncoded
    Observable<String> ConfirmRefundBill(
            @Field("Json") String Json,
            @Field("UserID") long UserID,
            @Field("RefundBillID") long RefundBillID,
            @Field("Remark") String Remark
    );

    /*
    * 97.保存发运单(新增、编辑)
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("BusinessService.asmx/SaveDeliveryBill")
    @FormUrlEncoded
    Observable<String> SaveDeliveryBill(
            @Field("Json") String Json,
            @Field("UserID") long UserID,
            @Field("DeliveryID") long DeliveryID,
            @Field("OrderID") long OrderID,
            @Field("ExpCorpID") long ExpCorpID,
            @Field("DeliveryCode") String DeliveryCode,
            @Field("Remark") String Remark
    );

     /*
    * 98. 获取订单的运单
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("BusinessService.asmx/GetDeliveryBillEx")
    @FormUrlEncoded
    Observable<String> GetDeliveryBillEx(
            @Field("Json") String Json,
            @Field("OrderID") long OrderID
    );

    /*
    * 99. 获取快递公司信息
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("SystemService.asmx/GetExpressCorp")
    @FormUrlEncoded
    Observable<String> GetExpressCorp(
            @Field("Json") String Json,
            @Field("ExpCorpID") long ExpCorpID
    );

    /*
    * 100. 获取快递公司列表
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("SystemService.asmx/GetExpressCorpList")
    @FormUrlEncoded
    Observable<String> GetExpressCorpList(
            @Field("Json") String Json,
            @Field("ExpCorpType") int ExpCorpType,
            @Field("IsFrequently") int IsFrequently,
            @Field("IsOrderOnline") int IsOrderOnline,
            @Field("IsWayBill") int IsWayBill,
            @Field("RowsPerPage") int RowsPerPage,
            @Field("PageNum") int PageNum
    );

      /*
    * 101. 关于
    * */

    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @POST("SystemService.asmx/GetAboutInfo")
    @FormUrlEncoded
    Observable<String> GetAboutInfo(
            @Field("Json") String Json
    );

}
