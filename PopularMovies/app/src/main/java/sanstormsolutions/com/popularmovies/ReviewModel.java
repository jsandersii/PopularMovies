package sanstormsolutions.com.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jsandersii on 1/11/16.
 */
public class ReviewModel implements Parcelable{
    //Vars
    private String id = null;
    private String author = null;
    private String content = null;

    //Default Constructor
    public ReviewModel(String id, String author, String content){
        this.id = id;
        this.author = author;
        this.content = content;
    }

    protected ReviewModel(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
    }

    public static final Creator<ReviewModel> CREATOR = new Creator<ReviewModel>() {
        @Override
        public ReviewModel createFromParcel(Parcel in) {
            return new ReviewModel(in);
        }

        @Override
        public ReviewModel[] newArray(int size) {
            return new ReviewModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
    }
}
