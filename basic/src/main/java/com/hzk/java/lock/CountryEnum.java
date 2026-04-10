package com.hzk.java.lock;

public enum CountryEnum {

    ONE(1,"齐"),TWO(2,"楚"),THREE(3,"燕"),FOUR(4,"赵"),FIVE(5,"魏"),SIX(6,"韩");

    CountryEnum(int code,String country){
        this.code = code;
        this.country = country;
    }

    public static CountryEnum getCountry(int code){
        CountryEnum result=null;
        CountryEnum[] values = CountryEnum.values();
        for(CountryEnum countryEnum:values){
            if(countryEnum.getCode() == code){
                result = countryEnum;
            }
        }
        return result;
    }

    private int code;
    private String country;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
