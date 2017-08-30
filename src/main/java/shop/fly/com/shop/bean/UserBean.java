package shop.fly.com.shop.bean;

/**
 * Created by fly12 on 2017/6/3.
 */

public class UserBean {

    /**
     * Id : 1
     * Name : store1
     * Key : 54c0c159-8eb5-4380-8261-bc50ea1e71bf
     * Voucher : aef47f9f-9241-4b6b-a244-16fc45061c01
     * No : 3c153f61-24e5-4f5f-b974-e4d8f9c05984
     * Notic :
     * Telephone :
     * Address :
     * EnableOrder : true
     * Logo :
     * OrderingNeedTableNo : false
     * IsCacher : false
     */

    private int Id;
    private String Name;
    private String Key;
    private String Voucher;
    private String No;
    private String Notic;
    private String Telephone;
    private String Address;
    private boolean EnableOrder;
    private String Logo;
    private boolean OrderingNeedTableNo;
    private boolean IsCacher;
    private String ManageName;
    private String ManageMobile;
    private String StoreUrl;
    private String QrcCodeImg;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String Key) {
        this.Key = Key;
    }

    public String getVoucher() {
        return Voucher;
    }

    public void setVoucher(String Voucher) {
        this.Voucher = Voucher;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String No) {
        this.No = No;
    }

    public String getNotic() {
        return Notic;
    }

    public void setNotic(String Notic) {
        this.Notic = Notic;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String Telephone) {
        this.Telephone = Telephone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public boolean isEnableOrder() {
        return EnableOrder;
    }

    public void setEnableOrder(boolean EnableOrder) {
        this.EnableOrder = EnableOrder;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String Logo) {
        this.Logo = Logo;
    }

    public boolean isOrderingNeedTableNo() {
        return OrderingNeedTableNo;
    }

    public void setOrderingNeedTableNo(boolean OrderingNeedTableNo) {
        this.OrderingNeedTableNo = OrderingNeedTableNo;
    }

    public boolean isIsCacher() {
        return IsCacher;
    }

    public void setIsCacher(boolean IsCacher) {
        this.IsCacher = IsCacher;
    }

    public boolean isCacher() {
        return IsCacher;
    }

    public void setCacher(boolean cacher) {
        IsCacher = cacher;
    }

    public String getManageName() {
        return ManageName;
    }

    public void setManageName(String manageName) {
        ManageName = manageName;
    }

    public String getManageMobile() {
        return ManageMobile;
    }

    public void setManageMobile(String manageMobile) {
        ManageMobile = manageMobile;
    }

    public String getStoreUrl() {
        return StoreUrl;
    }

    public void setStoreUrl(String storeUrl) {
        StoreUrl = storeUrl;
    }

    public String getQrcCodeImg() {
        return QrcCodeImg;
    }

    public void setQrcCodeImg(String qrcCodeImg) {
        QrcCodeImg = qrcCodeImg;
    }
}
