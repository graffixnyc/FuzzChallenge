package nyc.patrickhill.fuzzchallenge;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick Hill on 2/8/16.
 */
public class ListAdapter extends BaseAdapter {
    private static final String KEY_ADAPTER_STATE = "ListAdapter.KEY_ADAPTER_STATE";
    public enum ListMode {
        IMAGES_AND_TEXT,
        IMAGES_ONLY,
        TEXT_ONLY
    }

    private ListMode mListMode = ListMode.IMAGES_AND_TEXT;

    private ArrayList<Item> mItems= new ArrayList<>();

    private ArrayList<Item> mImages= new ArrayList<>();

    private ArrayList<Item> mTexts= new ArrayList<>();

    @Override
    public int getCount() {
        switch (mListMode) {
            case IMAGES_AND_TEXT:
                return mItems == null ? 0 : mItems.size();
            case IMAGES_ONLY:
                return mImages == null ? 0 : mImages.size();
            case TEXT_ONLY:
                return mTexts == null ? 0 : mTexts.size();
        }
        return 0;
    }

    @Override
    public Item getItem(int position) {
        switch (mListMode) {
            case IMAGES_AND_TEXT:
                return mItems == null ? null : mItems.get(position);
            case IMAGES_ONLY:
                return mImages == null ? null : mImages.get(position);
            case TEXT_ONLY:
                return mTexts == null ? null : mTexts.get(position);

        }
        return null;
    }
    public ArrayList getAllItems() {
        switch (mListMode) {
            case IMAGES_AND_TEXT:
                return mItems;
            case IMAGES_ONLY:
                return mImages;
            case TEXT_ONLY:
                return  mTexts;

        }
        return null;
    }
    @Override
    public long getItemId(int position) {
        return position;    // not really used
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = null;
        TextView tn = null;
        ImageView img = null;

        if (convertView == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(parent.getContext());
            v = vi.inflate(R.layout.list, null);
        } else {
            v=convertView;
        }

        Item p = getItem(position);
        tn = (TextView) v.findViewById(R.id.tvText);
        img = (ImageView) v.findViewById(R.id.thumbnail);
        if (p.getmType().equals("image")) {
            img.setVisibility(View.VISIBLE);
            Picasso.with(parent.getContext()).load(p.getmData()).error((R.drawable.placeholder_error)).placeholder(R.drawable.placeholder).resize(90,0).into(img);
            tn.setText("ID: " + p.getmID()+"\nTYPE: " + p.getmType() +"\nDate: " + p.getmDate()+ "\nImage URL: " +  p.getmData());
        } else {
            img.setVisibility(View.GONE);
            tn.setText("ID: " + p.getmID()+"\nTYPE: " + p.getmType() +"\nDate: " + p.getmDate()+ "\nText Data: " +  p.getmData());
        }
        return v;
    }

    public void setListMode(ListMode listMode) {
        mListMode = listMode;
        notifyDataSetChanged();
    }

    public void setItems(JSONArray jsonArray) throws JSONException {

        List<Item> items = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            items.add(new Item((JSONObject) jsonArray.get(i)));
        }
        setItems(items);
    }

    private void setItems(List<Item> items) {
        for (Item item : items) {
            mItems.add(item);
            if (item.getmType().equals("image")) {
                mImages.add(item);
            }
            if (item.getmType().equals("text")) {
                mTexts.add(item);
            }
            notifyDataSetChanged();
        }
    }
    public ListAdapter(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            int ordinal = savedInstanceState.getInt("adapter_mode", 0);
            mListMode = ListMode.values()[ordinal];

            ArrayList<Item> items =
                    savedInstanceState.getParcelableArrayList(KEY_ADAPTER_STATE);
            if (items != null) {
                setItems(items);
            }
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("adapter_mode", mListMode.ordinal());
        outState.putParcelableArrayList(KEY_ADAPTER_STATE, mItems);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(KEY_ADAPTER_STATE)) {
            ArrayList<Item> objects = savedInstanceState.getParcelableArrayList(KEY_ADAPTER_STATE);
            setItems(objects);
        }
    }

}