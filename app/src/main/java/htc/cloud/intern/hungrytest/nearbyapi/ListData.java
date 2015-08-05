package htc.cloud.intern.hungrytest.nearbyapi;

/**
 * Created by intern on 7/28/15.
 */

public class ListData {

    String title;
    String phone;
    String category;
    String imgUrl;

    public ListData(String t, String p, String c, String url) {
        setTitle(t);
        setPhone(p);
        setCategory(c);
        setImgResUrl(url);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String p) {
        phone = p;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String t) {
        title = t;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String c) {
        category = c;
    }

    public String getImgResUrl() {
        return imgUrl;
    }

    public void setImgResUrl(String url) {
        imgUrl = url.replace("ms.jpg", "ls.jpg");
    }

    @Override
    public String toString(){
        return String.format("(%s, %s)", title, phone);
    }
}
