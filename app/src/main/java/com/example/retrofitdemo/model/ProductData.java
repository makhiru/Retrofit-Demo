package com.example.retrofitdemo.model;

import java.util.List;

public class ProductData {
    private Integer connection;
    private Integer result;
    private List<Productdatas> productdata;

    public Integer getConnection() {
        return connection;
    }

    public void setConnection(Integer connection) {
        this.connection = connection;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public List<Productdatas> getProductdata() {
        return productdata;
    }

    public void setProductdata(List<Productdatas> productdata) {
        this.productdata = productdata;
    }


    public class Productdatas {

        private String ID;
        private String UID;
        private String PRO_NAME;
        private String PRO_DES;
        private String PRO_PRICE;
        private String PRO_IMAGE;

        public String getId() {
            return ID;
        }

        public void setId(String id) {
            this.ID = id;
        }

        public String getUid() {
            return UID;
        }

        public void setUid(String uid) {
            this.UID = uid;
        }

        public String getProName() {
            return PRO_NAME;
        }

        public void setProName(String proName) {
            this.PRO_NAME = proName;
        }

        public String getProDes() {
            return PRO_DES;
        }

        public void setProDes(String proDes) {
            this.PRO_DES = proDes;
        }

        public String getProPrice() {
            return PRO_PRICE;
        }

        public void setProPrice(String proPrice) {
            this.PRO_PRICE = proPrice;
        }

        public String getProImage() {
            return PRO_IMAGE;
        }

        public void setProImage(String proImage) {
            this.PRO_IMAGE = proImage;
        }

    }
}
