package nyc.patrickhill.fuzzchallenge;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Patrick on 2/4/2016.
 */
public class Item implements Parcelable{
    private String mID;
    private String mType;
    private String mDate;
    private String mData;

    public Item(String mID,String mType, String mDate, String mData)  {
        this.mType = mType;
        this.mID = mID;
        this.mDate = mDate;
        this.mData = mData;
    }
    public Item(JSONObject jsonItem) throws JSONException {
        String itemID=null;
        String itemType=null;
        String itemDate=null;
        String itemData=null;

        if (jsonItem.has("id")) {
            itemID=jsonItem.getString("id");
        }
        if (jsonItem.has("type")) {
            itemType=jsonItem.getString("type");
        }
        if (jsonItem.has("date")){
            itemDate=jsonItem.getString("date");
        }
        if (jsonItem.has("data")){
            itemData=jsonItem.getString("data");
        }
        this.mID=itemID;
        this.mType=itemType;
        this.mDate=itemDate;
        this.mData=itemData;
    }
    protected Item(Parcel in) {
        String[] data = new String[4];
        in.readStringArray(data);
        this.mID = data[0];
        this.mType = data[1];
        this.mDate = data[2];
        this.mData = data[3];
    }
    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getmID() {
        return mID;
    }

    public String getmType() {
        return mType;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmData() {
        return mData;
    }

    @Override
    public String toString() {
        return "{" +
                "ID='" + mID + '\'' +
                ", Type='" + mType + '\'' +
                ", Date='" + mDate + '\'' +
                ", Data='" + mData + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        String[] data = new String[4];
        data[0] = mID;
        data[1] = mType;
        data[2] = mDate;
        data[3] = mData;
        dest.writeStringArray(data);
    }
}
