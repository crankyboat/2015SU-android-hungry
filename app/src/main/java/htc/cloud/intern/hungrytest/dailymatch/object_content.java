package htc.cloud.intern.hungrytest.dailymatch;

/**
 * Created by cwhuang on 2015/8/21.
 */
public class object_content {

    String res_name,rating,distance,img;

    public object_content(String res_name,String rating,String distance, String img){
        this.res_name = res_name;
        this.rating = rating;
        this.distance = distance;
        this.img = img;
    }

    public String getName() {
        return res_name;
    }

    public void setName(String res_name) {
        this.res_name = res_name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
