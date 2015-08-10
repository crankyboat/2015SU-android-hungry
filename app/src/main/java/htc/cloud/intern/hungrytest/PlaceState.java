package htc.cloud.intern.hungrytest;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by intern on 7/31/15.
 */
public class PlaceState {

    private String mId;
    private String mName;
    private String mAddr;
    private LatLng mLatLng;
    private String mImgSrc;
    private String mCategory;
    private String mPhoneNum;
    private String mSnippet;

    public PlaceState(String id, String name, String address, LatLng latlng, String imgSrc,
                      String category, String phoneNum, String snippet) {
        setId(id);
        setName(name);
        setAddr(address);
        setLatLng(latlng);
        setImgSrc(imgSrc);
        setCategory(category);
        setPhoneNum(phoneNum);
        setSnippet(snippet);

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

    @Override
    public String toString(){
        return String.format("(%s, %s)", mName, mPhoneNum);
    }

}
