package khoenguyen.note.module;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 5/3/2017.
 */

public class Note implements Serializable{
    String ID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    String Title;// ten cua ghi nho
    String Content;// noi dung ghi nho
    List<String >
            image ;// danh sach ghi nho
    int color;// mau cua ghi nho
    String date, time,createDate ;// ngay khoi tao

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
