package htc.cloud.intern.hungrytest.nearby;

/**
 * Created by intern on 7/28/15.
 */

public class ListData {

    String description;
    String title;
    int imgId;

    public ListData(String t, String d, int id) {
        setTitle(t);
        setDescription(d);
        setImgResId(id);
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

    public int getImgResId() {
        return imgId;
    }

    public void setImgResId(int id) {
        imgId = id;
    }

    @Override
    public String toString(){
        return String.format("(%s, %s)", title, description);
    }
}
