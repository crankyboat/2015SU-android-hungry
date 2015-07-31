package htc.cloud.intern.hungrytest.nearbyapi;

/**
 * Created by intern on 7/28/15.
 */

public class ListData {

    String description;
    String title;
    String imgUrl;

    public ListData(String t, String d, String url) {
        setTitle(t);
        setDescription(d);
        setImgResUrl(url);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String d) {
        description = d;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String t) {
        title = t;
    }

    public String getImgResUrl() {
        return imgUrl;
    }

    public void setImgResUrl(String url) {
        imgUrl = url;
    }

    @Override
    public String toString(){
        return String.format("(%s, %s)", title, description);
    }
}
