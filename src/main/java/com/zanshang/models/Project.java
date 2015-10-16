package com.zanshang.models;

import com.zanshang.constants.BookType;
import com.zanshang.constants.ProjectState;
import com.zanshang.framework.Price;
import com.zanshang.framework.PriceUnit;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Lookis on 5/24/15.
 */
@Document(collection = "projects")
public class Project {

    @Id
    @Field("id")
    private ObjectId id;

    @Field("uid")
    private ObjectId uid; //owner，版权所属人

    @Field("first_author_description")
    private String firstAuthorDescription; //owner 简介

    @Field("authors")
    private List<Author> authors; //作者

    @Field("book_name")
    private String bookName;

    @Field("book_type")
    private BookType type;

    @Field("book_types")
    private List<BookType> types;

    @Field("description")
    private String description;

    @Field("tags")
    private Collection<String> tags;

    @Field("cover")
    private String cover;

    @Field("images")
    private Collection<String> images;

    @Field("outline")
    private String outline;

    @Field("draft")
    private String draft;

    @Field("color_mode")
    private int colorMode;

    @Field("publish_mode")
    private int publishMode;

    @Field("word_count")
    private int wordCount;

    @Field("image_count")
    private int imageCount;

    @Field("deadline")
    private Date deadline;

    @Field("create_time")
    private Date createTime;

    @Field("update_time")
    private Date updateTime;

    @Field("current_balance")
    private Price currentBalance;

    @Field("goal")
    private Price goal;

    //锁定的出版人，由后台审核后填入
    @Field("publisher")
    private ObjectId publisher;

    @Field("rewards")
    private Collection<ObjectId> rewards;

    @Field("state")
    private ProjectState state;

    @Field("progress_code")
    private int progressCode;

    private Project() {
    }

    /**
     * without publishMode
     * @param uid
     * @param firstAuthorDescription
     * @param authors
     * @param bookName
     * @param types
     * @param description
     * @param tags
     * @param cover
     * @param images
     * @param outline
     * @param draft
     * @param colorMode
     * @param wordCount
     * @param imageCount
     */
    public Project(ObjectId uid, String firstAuthorDescription, List<Author> authors, String bookName, List<BookType> types, String description,
                   Collection<String> tags, String cover, Collection<String> images, String outline, String draft,
                   int colorMode, int wordCount, int imageCount) {
        this.uid = uid;
        this.authors = authors;
        this.firstAuthorDescription = firstAuthorDescription;
        this.bookName = bookName;
        this.types = types;
        this.description = description;
        this.tags = tags;
        this.cover = cover;
        this.images = images;
        this.outline = outline;
        this.draft = draft;
        this.colorMode = colorMode;
        this.wordCount = wordCount;
        this.imageCount = imageCount;
        this.id = ObjectId.get();
        this.state = ProjectState.REVIEWING;
        this.createTime = new Date();
        this.rewards = new ArrayList<>();
        this.currentBalance = new Price(0, PriceUnit.CENT);
        this.updateTime = new Date();
        this.progressCode = 0;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getFirstAuthorDescription() {
        return firstAuthorDescription;
    }

    public void setFirstAuthorDescription(String firstAuthorDescription) {
        this.firstAuthorDescription = firstAuthorDescription;
    }

    public Price getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Price currentBalance) {
        this.currentBalance = currentBalance;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getUid() {
        return uid;
    }

    public void setUid(ObjectId uid) {
        this.uid = uid;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public BookType getType() {
        return type;
    }

    public void setType(BookType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<String> getTags() {
        return tags;
    }

    public void setTags(Collection<String> tags) {
        this.tags = tags;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Collection<String> getImages() {
        return images;
    }

    public void setImages(Collection<String> images) {
        this.images = images;
    }

    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }

    public String getDraft() {
        return draft;
    }

    public void setDraft(String draft) {
        this.draft = draft;
    }

    public int getColorMode() {
        return colorMode;
    }

    public void setColorMode(int colorMode) {
        this.colorMode = colorMode;
    }

    public int getPublishMode() {
        return publishMode;
    }

    public void setPublishMode(int publishMode) {
        this.publishMode = publishMode;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Price getGoal() {
        return goal;
    }

    public void setGoal(Price goal) {
        this.goal = goal;
    }

    public Collection<ObjectId> getRewards() {
        return rewards;
    }

    public void setRewards(Collection<ObjectId> rewards) {
        this.rewards = rewards;
    }

    public ProjectState getState() {
        if(state == ProjectState.FUNDING) {
            Date now = new Date();
            if(getDeadline() != null) {
                if(now.after(getDeadline())) {
                    if(getPublisher() == null) {
                        return ProjectState.FAILURE;
                    }else {
                        return ProjectState.SUCCESS;
                    }
                }
            }
        }
        return state;
    }

    public ObjectId getPublisher() {
        return publisher;
    }

    public void setPublisher(ObjectId publisher) {
        this.publisher = publisher;
    }

    public void setState(ProjectState state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<BookType> getTypes() {
        return types;
    }

    public void setTypes(List<BookType> types) {
        this.types = types;
    }

    public int getProgressCode() {
        return progressCode;
    }

    public void setProgressCode(int progressCode) {
        this.progressCode = progressCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Project project = (Project) o;

        return id.equals(project.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public static class Author {

        @Field("identity")
        private String identity;

        @Field("name")
        private String name;

        @Field("description")
        private String description;

        public Author() {
        }

        public Author(String id, String name, String description) {
            this.identity = id;
            this.name = name;
            this.description = description;
        }

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
