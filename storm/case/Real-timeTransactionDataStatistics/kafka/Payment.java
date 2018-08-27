package cn.itcast.realtime.kafka;

/**
 * 订单支付的信息
 * 100多个字段，用户信息，用户收货地址，商家信息，订单信息
 */

import com.google.gson.Gson;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class Payment implements Serializable {
    private static final long serialVersionUID = -7958315778386204397L;
    private String orderId;//订单编号
    private Date createOrderTime;//订单创建时间
    private String paymentId;//支付编号
    private Date paymentTime;//支付时间
    private String productId;//商品编号
    private String productName;//商品名称
    private long productPrice;//商品价格
    private long promotionPrice;//促销价格
    private String shopId;//商铺编号
    private String shopName;//商铺名称
    private String shopMobile;//商品电话
    private long payPrice;//订单支付价格
    private int num;//订单数量

    /**
     * <Province>19</Province>
     * <City>1657</City>
     * <County>4076</County>
     */
    private String province; //省
    private String city; //市
    private String county;//县

    //102,144,114
    private String catagorys;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCatagorys() {
        return catagorys;
    }

    public void setCatagorys(String catagorys) {
        this.catagorys = catagorys;
    }

    public Payment() {
    }

    public Payment(String orderId, Date createOrderTime, String paymentId, Date paymentTime, String productId, String productName, long productPrice, long promotionPrice, String shopId, String shopName, String shopMobile, long payPrice, int num) {
        this.orderId = orderId;
        this.createOrderTime = createOrderTime;
        this.paymentId = paymentId;
        this.paymentTime = paymentTime;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.promotionPrice = promotionPrice;
        this.shopId = shopId;
        this.shopName = shopName;
        this.shopMobile = shopMobile;
        this.payPrice = payPrice;
        this.num = num;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getCreateOrderTime() {
        return createOrderTime;
    }

    public void setCreateOrderTime(Date createOrderTime) {
        this.createOrderTime = createOrderTime;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(long productPrice) {
        this.productPrice = productPrice;
    }

    public long getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(long promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopMobile() {
        return shopMobile;
    }

    public void setShopMobile(String shopMobile) {
        this.shopMobile = shopMobile;
    }

    public long getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(long payPrice) {
        this.payPrice = payPrice;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "PaymentInfo{" +
                "orderId='" + orderId + '\'' +
                ", createOrderTime=" + createOrderTime +
                ", paymentId='" + paymentId + '\'' +
                ", paymentTime=" + paymentTime +
                ", productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", promotionPrice=" + promotionPrice +
                ", shopId='" + shopId + '\'' +
                ", shopName='" + shopName + '\'' +
                ", shopMobile='" + shopMobile + '\'' +
                ", payPrice=" + payPrice +
                ", num=" + num +
                '}';
    }
     // 随机产生订单信息
    public String random() {
        this.orderId = UUID.randomUUID().toString().replaceAll("-", "");
        this.paymentId = UUID.randomUUID().toString().replaceAll("-", "");
        this.productPrice = new Random().nextInt(1000);
        this.promotionPrice = new Random().nextInt(500);
        this.payPrice = new Random().nextInt(480);
        this.shopId = new Random().nextInt(200000)+"";

        this.catagorys = new Random().nextInt(10000)+","+new Random().nextInt(10000)+","+new Random().nextInt(10000);
        this.province = new Random().nextInt(23)+"";
        this.city = new Random().nextInt(265)+"";
        this.county = new Random().nextInt(1489)+"";

        String date = "2018-08-21 12:22:12";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.createOrderTime = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //转换为JSON格式的字符串
        return new Gson().toJson(this);
    }
}
