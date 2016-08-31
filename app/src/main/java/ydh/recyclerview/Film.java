package ydh.recyclerview;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @description:电影信息
 * @author:袁东华 created at 2016/8/22 0022 下午 5:48
 */
public class Film implements Parcelable {
    //电影id
    private String id = "";
    //电影标题
    private String title = "";
    //电影描述
    private String descr = "";
    //电影链接
    private String url = "";
    //电影上线时间
    private String addTimeShow = "";
    //电影时长
    private String timeShow = "";
    //电影分类
    private String sort = "";
    //电影图片
    private String thumb = "";
    private String sfrom = "";
    private String duration = "";
    private String auth = "";
    //电影个数
    private String count = "";
    private String canShow="";
    private String path="";
    private String hasFavourite="";
    private String download="false";
    private String suffix="";
    private String fileName="";
    private String type="";
    //播放历史记录id
    private String historyId="";
    public Film() {
    }

    protected Film(Parcel in) {
        id = in.readString();
        title = in.readString();
        descr = in.readString();
        url = in.readString();
        addTimeShow = in.readString();
        sort = in.readString();
        thumb = in.readString();
        sfrom = in.readString();
        duration = in.readString();
        auth = in.readString();
        count = in.readString();
        timeShow = in.readString();
        canShow = in.readString();
        path= in.readString();
        hasFavourite= in.readString();
        download= in.readString();
        suffix= in.readString();
        fileName=in.readString();
        type=in.readString();
        historyId=in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(descr);
        dest.writeString(url);
        dest.writeString(addTimeShow);
        dest.writeString(sort);
        dest.writeString(thumb);
        dest.writeString(sfrom);
        dest.writeString(duration);
        dest.writeString(auth);
        dest.writeString(count);
        dest.writeString(timeShow);
        dest.writeString(canShow);
        dest.writeString(path);
        dest.writeString(hasFavourite);
        dest.writeString(download);
        dest.writeString(suffix);
        dest.writeString(fileName);
        dest.writeString(type);
        dest.writeString(historyId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Film> CREATOR = new Creator<Film>() {
        @Override
        public Film createFromParcel(Parcel in) {
            return new Film(in);
        }

        @Override
        public Film[] newArray(int size) {
            return new Film[size];
        }
    };

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAddTimeShow() {
        return addTimeShow;
    }

    public void setAddTimeShow(String addTimeShow) {
        this.addTimeShow = addTimeShow;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getSfrom() {
        return sfrom;
    }

    public void setSfrom(String sfrom) {
        this.sfrom = sfrom;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTimeShow() {
        return timeShow;
    }

    public void setTimeShow(String timeShow) {
        this.timeShow = timeShow;
    }

    public String getCanShow() {
        return canShow;
    }

    public void setCanShow(String canShow) {
        this.canShow = canShow;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHasFavourite() {
        return hasFavourite;
    }

    public void setHasFavourite(String hasFavourite) {
        this.hasFavourite = hasFavourite;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
