package ydh.recyclerview.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @description:天气
 * @author:袁东华 created at 2016/8/22 0022 下午 5:48
 */
public class Weather implements Parcelable {
    private String title = "";
    private String descr = "";
    private String timeShow = "";
    private int thumb ;
    public Weather() {
    }

    protected Weather(Parcel in) {
        title = in.readString();
        descr = in.readString();
        thumb = in.readInt();
        timeShow = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(descr);
        dest.writeInt(thumb);
        dest.writeString(timeShow);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Weather> CREATOR = new Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }


    public int getThumb() {
        return thumb;
    }

    public void setThumb(int thumb) {
        this.thumb = thumb;
    }


    public String getTimeShow() {
        return timeShow;
    }

    public void setTimeShow(String timeShow) {
        this.timeShow = timeShow;
    }

}
