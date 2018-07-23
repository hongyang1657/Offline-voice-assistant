package fitme.ai.zotyeautoassistant.bean;

/**
 * speech 类型类型body中特有的字段
 * Created by zzy on 2017/11/3.
 */

public class Rows {
    private String title;//标题
    private String h1;//h1
    private String h2;//h2
    private String url;//url
    private String image_url;//图片url

    public Rows() {

    }

    public Rows(String title, String h1, String h2, String url, String image_url) {
        this.title = title;
        this.h1 = h1;
        this.h2 = h2;
        this.url = url;
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getH1() {
        return h1;
    }

    public void setH1(String h1) {
        this.h1 = h1;
    }

    public String getH2() {
        return h2;
    }

    public void setH2(String h2) {
        this.h2 = h2;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @Override
    public String toString() {
        return "Rows{" +
                "title='" + title + '\'' +
                ", h1='" + h1 + '\'' +
                ", h2='" + h2 + '\'' +
                ", url='" + url + '\'' +
                ", image_url='" + image_url + '\'' +
                '}';
    }
}
