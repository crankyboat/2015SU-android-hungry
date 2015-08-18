package htc.cloud.intern.hungrytest.business;

/**
 * Created by intern on 8/16/15.
 */
public class ReviewItem {

    private float mRating;
    private String mDate;
    private String mContent;
    private String mUserName;
    private String mUserImgUrl;

    public ReviewItem(float rating, String date, String content,
                      String userName, String userImgUrl) {

        setRating(rating);
        setDate(date);
        setContent(content);
        setUserName(userName);
        setUserImgUrl(userImgUrl);

    }

    public void setRating(float rating) {
        mRating = rating;
    }

    public float getRating() {
        return mRating;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getDate() {
        return mDate;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getContent() {
        return mContent;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserImgUrl(String userImg) {
        mUserImgUrl = userImg;
    }

    public String getUserImgUrl() {
        return mUserImgUrl;
    }

}
