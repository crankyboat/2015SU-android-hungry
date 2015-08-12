package htc.cloud.intern.hungrytest;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by intern on 7/31/15.
 */
public class PlaceState {

    private String mId;
    private String mName;
    private String mAddr;
    private double mDist;
    private LatLng mLatLng;
    private String mImgSrc;
    private String mCategory;
    private String mPhoneNum;
    private String mSnippet;
    private ArrayList<String> mImgList;

    public PlaceState(String id, String name, String address, double dist, LatLng latlng, String imgSrc,
                      String category, String phoneNum, String snippet, ArrayList<String> imgList) {
        setId(id);
        setName(name);
        setAddr(address);
        setDist(dist);
        setLatLng(latlng);
        setImgSrc(imgSrc);
        setCategory(category);
        setPhoneNum(phoneNum);
        setSnippet(snippet);
        setImgList(imgList);

    }

    public void setId(String id) {
        mId = id;
    }

    public String getId() {
        return mId;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setAddr(String address) {
        mAddr = address;
    }

    public String getAddr() {
        return mAddr;
    }

    public void setDist(double dist) {
        mDist = dist;
    }

    public double getDist() {
        return mDist;
    }

    public void setLatLng(LatLng latLng) {
        mLatLng = latLng;
    }

    public LatLng getLatLng() {
        return mLatLng;
    }

    public void setImgSrc(String imgSrc) {
        mImgSrc = imgSrc;
    }

    public String getImgSrc() {
        return mImgSrc;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setPhoneNum(String phoneNum) {
        mPhoneNum = phoneNum;
    }

    public String getPhoneNum() {
        return mPhoneNum;
    }

    public void setSnippet(String snippet) {
        mSnippet = snippet;
    }

    public String getSnippet() {
        return mSnippet;
    }

    public void setImgList(ArrayList<String> imgList) {
        mImgList = new ArrayList<String>(imgList);
    }

    public ArrayList<String> getImgList() {
        return mImgList;
    }

    @Override
    public String toString(){
        return String.format("(%s, %s)", mName, mPhoneNum);
    }

}
