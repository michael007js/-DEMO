package com.sss.framework.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * POI兴趣点模型
 * Created by leilei on 2017/4/20.
 */

public class TargetInfoModel implements Parcelable {
     private int LocationType ;//获取当前定位结果来源，如网络定位结果，详见定位类型表
     private double Latitude ;//获取纬度
     private double Longitude ;//获取经度
     private float Accuracy ;//获取精度信息
     private String Address ;//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
     private String Country ;//国家信息
     private String Province ;//省信息
     private String City ;//城市信息
     private String District ;//城区信息
     private String Street ;//街道信息
     private String StreetNum ;//街道门牌号信息
     private String CityCode ;//城市编码
     private String AdCode ;//地区编码
     private String AoiName ;//获取当前定位点的AOI信息
     private String BuildingId ;//获取当前室内定位的建筑物Id
     private String Floor ;//获取当前室内定位的楼层
     private int getGpsAccuracyStatus ;//获取GPS的精度状态
     private double getAltitude ;//获得高度
     private float getBearing ;//获得方位
     private String getLocationDetail ;//获得具体位置
     private String getRoad ;//获得道路
     private float getSpeed ;//获得当前速度、
     private String time;//时间

     public TargetInfoModel() {
     }

     public String getTime() {
          return time;
     }

     public void setTime(String time) {
          this.time = time;
     }

     public String getDistrict() {
          return District;
     }

     public void setDistrict(String district) {
          District = district;
     }

     public int getLocationType() {
          return LocationType;
     }

     public void setLocationType(int locationType) {
          LocationType = locationType;
     }

     public double getLatitude() {
          return Latitude;
     }

     public void setLatitude(double latitude) {
          Latitude = latitude;
     }

     public double getLongitude() {
          return Longitude;
     }

     public void setLongitude(double longitude) {
          Longitude = longitude;
     }

     public float getAccuracy() {
          return Accuracy;
     }

     public void setAccuracy(float accuracy) {
          Accuracy = accuracy;
     }

     public String getAddress() {
          return Address;
     }

     public void setAddress(String address) {
          Address = address;
     }

     public String getCountry() {
          return Country;
     }

     public void setCountry(String country) {
          Country = country;
     }

     public String getProvince() {
          return Province;
     }

     public void setProvince(String province) {
          Province = province;
     }

     public String getCity() {
          return City;
     }

     public void setCity(String city) {
          City = city;
     }

     public String getStreet() {
          return Street;
     }

     public void setStreet(String street) {
          Street = street;
     }

     public String getStreetNum() {
          return StreetNum;
     }

     public void setStreetNum(String streetNum) {
          StreetNum = streetNum;
     }

     public String getCityCode() {
          return CityCode;
     }

     public void setCityCode(String cityCode) {
          CityCode = cityCode;
     }

     public String getAdCode() {
          return AdCode;
     }

     public void setAdCode(String adCode) {
          AdCode = adCode;
     }

     public String getAoiName() {
          return AoiName;
     }

     public void setAoiName(String aoiName) {
          AoiName = aoiName;
     }

     public String getBuildingId() {
          return BuildingId;
     }

     public void setBuildingId(String buildingId) {
          BuildingId = buildingId;
     }

     public String getFloor() {
          return Floor;
     }

     public void setFloor(String floor) {
          Floor = floor;
     }

     public int getGetGpsAccuracyStatus() {
          return getGpsAccuracyStatus;
     }

     public void setGetGpsAccuracyStatus(int getGpsAccuracyStatus) {
          this.getGpsAccuracyStatus = getGpsAccuracyStatus;
     }

     public double getGetAltitude() {
          return getAltitude;
     }

     public void setGetAltitude(double getAltitude) {
          this.getAltitude = getAltitude;
     }

     public float getGetBearing() {
          return getBearing;
     }

     public void setGetBearing(float getBearing) {
          this.getBearing = getBearing;
     }

     public String getGetLocationDetail() {
          return getLocationDetail;
     }

     public void setGetLocationDetail(String getLocationDetail) {
          this.getLocationDetail = getLocationDetail;
     }

     public String getGetRoad() {
          return getRoad;
     }

     public void setGetRoad(String getRoad) {
          this.getRoad = getRoad;
     }

     public float getGetSpeed() {
          return getSpeed;
     }

     public void setGetSpeed(float getSpeed) {
          this.getSpeed = getSpeed;
     }


     @Override
     public int describeContents() {
          return 0;
     }

     @Override
     public void writeToParcel(Parcel dest, int flags) {
          dest.writeInt(this.LocationType);
          dest.writeDouble(this.Latitude);
          dest.writeDouble(this.Longitude);
          dest.writeFloat(this.Accuracy);
          dest.writeString(this.Address);
          dest.writeString(this.Country);
          dest.writeString(this.Province);
          dest.writeString(this.City);
          dest.writeString(this.District);
          dest.writeString(this.Street);
          dest.writeString(this.StreetNum);
          dest.writeString(this.CityCode);
          dest.writeString(this.AdCode);
          dest.writeString(this.AoiName);
          dest.writeString(this.BuildingId);
          dest.writeString(this.Floor);
          dest.writeInt(this.getGpsAccuracyStatus);
          dest.writeDouble(this.getAltitude);
          dest.writeFloat(this.getBearing);
          dest.writeString(this.getLocationDetail);
          dest.writeString(this.getRoad);
          dest.writeFloat(this.getSpeed);
          dest.writeString(this.time);
     }

     protected TargetInfoModel(Parcel in) {
          this.LocationType = in.readInt();
          this.Latitude = in.readDouble();
          this.Longitude = in.readDouble();
          this.Accuracy = in.readFloat();
          this.Address = in.readString();
          this.Country = in.readString();
          this.Province = in.readString();
          this.City = in.readString();
          this.District = in.readString();
          this.Street = in.readString();
          this.StreetNum = in.readString();
          this.CityCode = in.readString();
          this.AdCode = in.readString();
          this.AoiName = in.readString();
          this.BuildingId = in.readString();
          this.Floor = in.readString();
          this.getGpsAccuracyStatus = in.readInt();
          this.getAltitude = in.readDouble();
          this.getBearing = in.readFloat();
          this.getLocationDetail = in.readString();
          this.getRoad = in.readString();
          this.getSpeed = in.readFloat();
          this.time = in.readString();
     }

     public static final Creator<TargetInfoModel> CREATOR = new Creator<TargetInfoModel>() {
          @Override
          public TargetInfoModel createFromParcel(Parcel source) {
               return new TargetInfoModel(source);
          }

          @Override
          public TargetInfoModel[] newArray(int size) {
               return new TargetInfoModel[size];
          }
     };

     @Override
     public String toString() {
          return "TargetInfoModel{" +
                  "LocationType=" + LocationType +
                  ", Latitude=" + Latitude +
                  ", Longitude=" + Longitude +
                  ", Accuracy=" + Accuracy +
                  ", Address='" + Address + '\'' +
                  ", Country='" + Country + '\'' +
                  ", Province='" + Province + '\'' +
                  ", City='" + City + '\'' +
                  ", District='" + District + '\'' +
                  ", Street='" + Street + '\'' +
                  ", StreetNum='" + StreetNum + '\'' +
                  ", CityCode='" + CityCode + '\'' +
                  ", AdCode='" + AdCode + '\'' +
                  ", AoiName='" + AoiName + '\'' +
                  ", BuildingId='" + BuildingId + '\'' +
                  ", Floor='" + Floor + '\'' +
                  ", getGpsAccuracyStatus=" + getGpsAccuracyStatus +
                  ", getAltitude=" + getAltitude +
                  ", getBearing=" + getBearing +
                  ", getLocationDetail='" + getLocationDetail + '\'' +
                  ", getRoad='" + getRoad + '\'' +
                  ", getSpeed=" + getSpeed +
                  ", time='" + time + '\'' +
                  '}';
     }
}
