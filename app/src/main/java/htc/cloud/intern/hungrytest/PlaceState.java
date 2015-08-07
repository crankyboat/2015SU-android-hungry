package htc.cloud.intern.hungrytest;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by intern on 7/31/15.
 */
public class PlaceState {

    private String mName;
    private LatLng mLatLng;
    private String mImgSrc;
    private String mCategory;
    private String mPhoneNum;

    public PlaceState(String name, LatLng latlng, String imgSrc,
                      String category, String phoneNum) {
        setName(name);
        setLatLng(latlng);
        setImgSrc(imgSrc);
        setCategory(category);
        setPhoneNum(phoneNum);

    }

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
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

    @Override
    public String toString(){
        return String.format("(%s, %s)", mName, mPhoneNum);
    }

}
