package htc.cloud.intern.hungrytest;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by intern on 7/31/15.
 */
public class PlaceState implements Comparable<PlaceState>{

    private String mId;
    private String mName;
    private String mAddr;
    private int mRank;
    private double mRating;
    private double mDist;
    private LatLng mLatLng;
    private String mImgSrc;
    private String mCategory;
    private String mPhoneNum;
    private String mSnippet;
    private ArrayList<String> mImgList;
    private ArrayList<String> mReviewList;

    public PlaceState(String id, String name, String address, int rank, double rating, double dist, LatLng latlng, String imgSrc,
                      String category, String phoneNum, String snippet, ArrayList<String> imgList) {
        setId(id);
        setName(name);
        setAddr(address);
        setRank(rank);
        setRating(rating);
        setDist(dist);
        setLatLng(latlng);
        setImgSrc(imgSrc);
        setCategory(category);
        setPhoneNum(phoneNum);
        setSnippet(snippet);
        setImgList(imgList);
        setReviewList(new ArrayList<String>());

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

    public void setRank(int rank) {
        mRank = rank;
    }

    public int getRank() {
        return mRank;
    }

    public void setRating(double rating) {
        mRating = rating;
    }

    public double getRating() {
        return mRating;
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

    public void setReviewList(ArrayList<String> reviewList) {
        mReviewList = new ArrayList<String>(reviewList);
    }
//
//    public ArrayList<String> getReviewList() {
//        return mReviewList;
//    }

    @Override
    public String toString(){
        return String.format("(%s, %s)", mName, mPhoneNum);
    }

    @Override
    public int compareTo(PlaceState another) {
        return ((Integer)getRank()).compareTo((Integer)another.getRank());
    }
}
