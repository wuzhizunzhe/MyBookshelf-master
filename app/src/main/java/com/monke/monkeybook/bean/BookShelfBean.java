//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.monke.monkeybook.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

/**
 * 书架item Bean
 */

@Entity
public class BookShelfBean implements Parcelable {
    @Transient
    public static final String LOCAL_TAG = "loc_book";
    @Transient
    private String errorMsg;
    @Transient
    private boolean isLoading;

    @Id
    private String noteUrl; //对应BookInfoBean noteUrl;
    private Integer durChapter = 0;   //当前章节 （包括番外）
    private Integer durChapterPage = 0;  // 当前章节位置   用页码
    private Long finalDate = System.currentTimeMillis();  //最后阅读时间
    private Boolean hasUpdate = false;  //是否有更新
    private Integer newChapters = 0;  //更新章节数
    private String tag;
    private Integer serialNumber = 0; //手动排序
    private Long finalRefreshData = System.currentTimeMillis();  //章节最后更新时间
    private Integer group = 0;
    private String durChapterName;
    private String lastChapterName;
    private Integer chapterListSize = 0;
    private String customCoverPath;

    @Transient
    private BookInfoBean bookInfoBean = new BookInfoBean();

    public BookShelfBean() {

    }

    @Transient
    public static final Creator<BookShelfBean> CREATOR = new Creator<BookShelfBean>() {
        @Override
        public BookShelfBean createFromParcel(Parcel in) {
            return new BookShelfBean(in);
        }

        @Override
        public BookShelfBean[] newArray(int size) {
            return new BookShelfBean[size];
        }
    };

    protected BookShelfBean(Parcel in) {
        noteUrl = in.readString();
        durChapter = in.readInt();
        durChapterPage = in.readInt();
        finalDate = in.readLong();
        tag = in.readString();
        bookInfoBean = in.readParcelable(BookInfoBean.class.getClassLoader());
        serialNumber = in.readInt();
        finalRefreshData = in.readLong();
        group = in.readInt();
        durChapterName = in.readString();
        lastChapterName = in.readString();
        chapterListSize = in.readInt();
        customCoverPath = in.readString();
    }

    @Generated(hash = 229342711)
    public BookShelfBean(String noteUrl, Integer durChapter, Integer durChapterPage, Long finalDate,
                         Boolean hasUpdate, Integer newChapters, String tag, Integer serialNumber,
                         Long finalRefreshData, Integer group, String durChapterName, String lastChapterName,
                         Integer chapterListSize, String customCoverPath) {
        this.noteUrl = noteUrl;
        this.durChapter = durChapter;
        this.durChapterPage = durChapterPage;
        this.finalDate = finalDate;
        this.hasUpdate = hasUpdate;
        this.newChapters = newChapters;
        this.tag = tag;
        this.serialNumber = serialNumber;
        this.finalRefreshData = finalRefreshData;
        this.group = group;
        this.durChapterName = durChapterName;
        this.lastChapterName = lastChapterName;
        this.chapterListSize = chapterListSize;
        this.customCoverPath = customCoverPath;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(noteUrl);
        dest.writeInt(group);
        dest.writeInt(durChapter);
        dest.writeInt(durChapterPage);
        dest.writeLong(finalDate);
        dest.writeString(tag);
        dest.writeParcelable(bookInfoBean, flags);
        dest.writeInt(serialNumber);
        dest.writeLong(finalRefreshData);
        dest.writeString(durChapterName);
        dest.writeString(lastChapterName);
        dest.writeInt(chapterListSize);
        dest.writeString(customCoverPath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public BookShelfBean copy() {
        BookShelfBean bookShelfBean = new BookShelfBean();
        bookShelfBean.noteUrl = noteUrl;
        bookShelfBean.durChapter = durChapter;
        bookShelfBean.durChapterPage = durChapterPage;
        bookShelfBean.finalDate = finalDate;
        bookShelfBean.hasUpdate = hasUpdate;
        bookShelfBean.newChapters = newChapters;
        bookShelfBean.tag = tag;
        bookShelfBean.serialNumber = serialNumber;
        bookShelfBean.finalRefreshData = finalRefreshData;
        bookShelfBean.group = group;
        bookShelfBean.durChapterName = durChapterName;
        bookShelfBean.lastChapterName = lastChapterName;
        bookShelfBean.chapterListSize = chapterListSize;
        bookShelfBean.customCoverPath = customCoverPath;
        bookShelfBean.bookInfoBean = bookInfoBean.copy();
        return bookShelfBean;
    }

    public String getNoteUrl() {
        return noteUrl;
    }

    public void setNoteUrl(String noteUrl) {
        this.noteUrl = noteUrl;
    }

    public int getDurChapter() {
        return durChapter < 0 ? 0 : durChapter;
    }

    public ChapterListBean getChapter(int index) {
        if (isChapterListEmpty() || index < 0) {
            ChapterListBean chapterListBean = new ChapterListBean();
            chapterListBean.setDurChapterName("暂无");
            chapterListBean.setDurChapterUrl("暂无");
            return chapterListBean;
        } else if (index < getChapterList().size()) {
            return getChapterList().get(index);
        } else {
            durChapter = getChapterList().size() - 1;
            return getChapterList().get(durChapter);
        }
    }

    public BookmarkBean getBookmark(int index) {
        if (isBookmarkListEmpty() || index < 0) {
            return null;
        } else if (index < getBookmarkList().size()) {
            return getBookmarkList().get(index);
        } else {
            return getBookmarkList().get(getChapterList().size() - 1);
        }
    }

    public int getDurChapterPage() {
        return durChapterPage < 0 ? 0 : durChapterPage;
    }

    public long getFinalDate() {
        return finalDate;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public BookInfoBean getBookInfoBean() {
        return bookInfoBean;
    }

    public void setBookInfoBean(BookInfoBean bookInfoBean) {
        this.bookInfoBean = bookInfoBean;
    }

    public boolean getHasUpdate() {
        return hasUpdate;
    }

    public int getNewChapters() {
        return newChapters;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getSerialNumber() {
        return this.serialNumber;
    }

    public long getFinalRefreshData() {
        return this.finalRefreshData;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public int getGroup() {
        return this.group == null ? 0 : this.group;
    }

    public void setDurChapter(Integer durChapter) {
        this.durChapter = durChapter;
    }

    public void setDurChapterPage(Integer durChapterPage) {
        this.durChapterPage = durChapterPage;
    }

    public void setFinalDate(Long finalDate) {
        this.finalDate = finalDate;
    }

    public void setHasUpdate(Boolean hasUpdate) {
        this.hasUpdate = hasUpdate;
    }

    public void setNewChapters(Integer newChapters) {
        this.newChapters = newChapters;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setFinalRefreshData(Long finalRefreshData) {
        this.finalRefreshData = finalRefreshData;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    public String getDurChapterName() {
        return this.durChapterName;
    }

    public void setDurChapterName(String durChapterName) {
        this.durChapterName = durChapterName;
    }

    public void upDurChapterName() {
        if (getChapterListSize() > 0) {
            if (durChapter < getChapterListSize()) {
                durChapterName = getChapterList().get(durChapter).getDurChapterName();
            } else {
                durChapterName = getChapterList().get(getChapterListSize() - 1).getDurChapterName();
            }
        } else {
            durChapterName = "未知";
        }
    }

    public String getLastChapterName() {
        return this.lastChapterName;
    }

    public void setLastChapterName(String lastChapterName) {
        this.lastChapterName = lastChapterName;
    }

    public void upLastChapterName() {
        if (getChapterListSize() > 0) {
            lastChapterName = getChapterList().get(getChapterListSize() - 1).getDurChapterName();
        } else {
            lastChapterName = "未知";
        }
    }

    public int getChapterListSize() {
        return chapterListSize;
    }

    public void setChapterList(List<ChapterListBean> chapterList) {
        this.bookInfoBean.setChapterList(chapterList);
        this.chapterListSize = this.bookInfoBean.getChapterList().size();
    }

    public List<ChapterListBean> getChapterList() {
        return this.bookInfoBean.getChapterList();
    }

    public void setBookmarkList(List<BookmarkBean> markList) {
        this.bookInfoBean.setBookmarkList(markList);
        this.chapterListSize = this.bookInfoBean.getChapterList().size();
    }

    public List<BookmarkBean> getBookmarkList() {
        return this.bookInfoBean.getBookmarkList();
    }

    public boolean isChapterListEmpty() {
        return getChapterList() == null || getChapterList().isEmpty();
    }

    public boolean isBookmarkListEmpty() {
        return getBookmarkList() == null || getBookmarkList().isEmpty();
    }

    public String getCustomCoverPath() {
        return this.customCoverPath;
    }

    public void setCustomCoverPath(String customCoverPath) {
        this.customCoverPath = customCoverPath;
    }

    public void setChapterListSize(Integer chapterListSize) {
        this.chapterListSize = chapterListSize;
    }
}