package shop.fly.com.shop.bean;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/6.
 */

public class OrderBean {

    /**
     * OrderNo : 25b65e79-efba-47
     * OrderIndex :
     * DeskNO :
     * TotalAmount : 100
     * PayAmount : 100
     * LessAmount : 0
     * Status : 1
     * CouponAmount : 0
     * ActivityId : 0
     * ActivityLessAmount : 0
     * Details : [{"OrderNo":"25b65e79-efba-47","ProductId":1,"ProductName":"测试产品8","Price":100,"Number":1,"PayAmount":100,"ProductProId":1,"ProductProName":"规格1"}]
     * MemberName : null
     * TakeOut : false
     * Mobile :
     * Address :
     * OrderType: 订单类型 店内用餐 = 1,外卖派送 = 2,预约订餐 = 3,店内自提 = 4 [Int32]
     * BookingTime  当类型是外卖 BookingTime表示送达时间 当类型是预约,BookingTime表示到店用餐时间
     * AddTime : 2017-06-14T17:04:21.56
     */

    private String OrderNo;
    private String OrderIndex;
    private String DeskNO;
    private String TotalAmount;
    private String PayAmount;
    private String LessAmount;
    private String Status;
    private String CouponAmount;
    private String ActivityId;
    private String ActivityLessAmount;
    private String MemberName;
    private String BookingTime;
    private int OrderType;
    private String PackageBoxNum;
    private String PackageBoxTotalPrice;
    private int TablewareNum;


    private boolean TakeOut;
    private boolean IsBooking;
    private boolean IsTakeSelf;
    private String Mobile;
    private String Address;
    private String AddTime;
    private String Remarks;
    private String CancelReason;

    private List<DetailsBean> Details;

    public OrderBean() {
    }

    public OrderBean(String orderNo, String orderIndex, String deskNO, String totalAmount,
                     String payAmount, String lessAmount, String status, String couponAmount,
                     String activityId, String activityLessAmount, String memberName, boolean takeOut,
                     boolean isBooking, boolean isTakeSelf, String mobile, String address,
                     String addTime, String BookingTime, String remarks, int orderType, List<DetailsBean> details) {
        OrderNo = orderNo;
        OrderIndex = orderIndex;
        DeskNO = deskNO;
        TotalAmount = totalAmount;
        PayAmount = payAmount;
        LessAmount = lessAmount;
        Status = status;
        CouponAmount = couponAmount;
        ActivityId = activityId;
        ActivityLessAmount = activityLessAmount;
        MemberName = memberName;
        TakeOut = takeOut;
        IsBooking = isBooking;
        IsTakeSelf = isTakeSelf;
        Mobile = mobile;
        Address = address;
        AddTime = addTime;
        this.Remarks = remarks;
        this.OrderType = orderType;
        this.BookingTime = BookingTime;
        Details = details;
    }

    public String getPackageBoxNum() {
        return PackageBoxNum;
    }

    public void setPackageBoxNum(String packageBoxNum) {
        PackageBoxNum = packageBoxNum;
    }

    public String getPackageBoxTotalPrice() {
        return PackageBoxTotalPrice;
    }

    public void setPackageBoxTotalPrice(String packageBoxTotalPrice) {
        PackageBoxTotalPrice = packageBoxTotalPrice;
    }

    public int getTablewareNum() {
        return TablewareNum;
    }

    public void setTablewareNum(int tablewareNum) {
        TablewareNum = tablewareNum;
    }

    public String getBookingTime() {
        return BookingTime;
    }

    public void setBookingTime(String bookingTime) {
        BookingTime = bookingTime;
    }


    public int getOrderType() {
        return OrderType;
    }

    public void setOrderType(int orderType) {
        this.OrderType = orderType;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String OrderNo) {
        this.OrderNo = OrderNo;
    }

    public String getOrderIndex() {
        return OrderIndex;
    }

    public void setOrderIndex(String OrderIndex) {
        this.OrderIndex = OrderIndex;
    }

    public String getDeskNO() {
        return DeskNO;
    }

    public void setDeskNO(String DeskNO) {
        this.DeskNO = DeskNO;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getPayAmount() {
        return PayAmount;
    }

    public void setPayAmount(String payAmount) {
        PayAmount = payAmount;
    }

    public String getLessAmount() {
        return LessAmount;
    }

    public void setLessAmount(String lessAmount) {
        LessAmount = lessAmount;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCouponAmount() {
        return CouponAmount;
    }
    public double getDouble(String data){
        if(TextUtils.isEmpty(data))
            return 0;
        double d = 0;
        try {
            d = Double.parseDouble(data);
        }catch (Exception e){
        }

        return d;
    }

    public void setCouponAmount(String couponAmount) {
        CouponAmount = couponAmount;
    }

    public String getActivityId() {
        return ActivityId;
    }

    public void setActivityId(String activityId) {
        ActivityId = activityId;
    }

    public String getActivityLessAmount() {
        return ActivityLessAmount;
    }

    public void setActivityLessAmount(String activityLessAmount) {
        ActivityLessAmount = activityLessAmount;
    }

    public String getAddTime() {
        return dataFormate(AddTime);
    }

    public void setAddTime(String addTime) {
        AddTime = addTime;
    }

    public String getMemberName() {
        return MemberName;
    }

    public void setMemberName(String MemberName) {
        this.MemberName = MemberName;
    }

    public boolean isTakeOut() {
        return TakeOut;
    }

    public void setTakeOut(boolean TakeOut) {
        this.TakeOut = TakeOut;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public List<DetailsBean> getDetails() {
        return Details;
    }

    public void setDetails(List<DetailsBean> Details) {
        this.Details = Details;
    }

    public boolean isTakeSelf() {
        return IsTakeSelf;
    }

    public void setTakeSelf(boolean takeSelf) {
        IsTakeSelf = takeSelf;
    }

    public boolean isBooking() {
        return IsBooking;
    }

    public void setBooking(boolean booking) {
        IsBooking = booking;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getCancelReason() {
        return CancelReason;
    }

    public void setCancelReason(String cancelReason) {
        CancelReason = cancelReason;
    }

    public static class DetailsBean {
        public DetailsBean() {
        }

        public DetailsBean(String orderNo, int productId, String productName, String price, int number, String payAmount, int productProId, String productProName) {
            OrderNo = orderNo;
            ProductId = productId;
            ProductName = productName;
            Price = price;
            Number = number;
            PayAmount = payAmount;
            ProductProId = productProId;
            ProductProName = productProName;
        }

        /**
         * OrderNo : 25b65e79-efba-47
         * ProductId : 1
         * ProductName : 测试产品8
         * Price : 100
         * Number : 1
         * PayAmount : 100
         * ProductProId : 1
         * ProductProName : 规格1
         */


        private String OrderNo;
        private int ProductId;
        private String ProductName;
        private String Price;
        private int Number;
        private String PayAmount;
        private int ProductProId;
        private String ProductProName;

        public String getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(String OrderNo) {
            this.OrderNo = OrderNo;
        }

        public int getProductId() {
            return ProductId;
        }

        public void setProductId(int ProductId) {
            this.ProductId = ProductId;
        }

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String ProductName) {
            this.ProductName = ProductName;
        }

        public String getPrice() {
            return Price;
        }

        public void setPrice(String Price) {
            this.Price = Price;
        }

        public int getNumber() {
            return Number;
        }

        public void setNumber(int Number) {
            this.Number = Number;
        }

        public String getPayAmount() {
            return PayAmount;
        }

        public void setPayAmount(String PayAmount) {
            this.PayAmount = PayAmount;
        }

        public int getProductProId() {
            return ProductProId;
        }

        public void setProductProId(int ProductProId) {
            this.ProductProId = ProductProId;
        }

        public String getProductProName() {
            return ProductProName;
        }

        public void setProductProName(String ProductProName) {
            this.ProductProName = ProductProName;
        }
    }

    private String dataFormate(String sTime){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date parse = sdf.parse(sTime);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            sTime = sdf2.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sTime;
    }

}
