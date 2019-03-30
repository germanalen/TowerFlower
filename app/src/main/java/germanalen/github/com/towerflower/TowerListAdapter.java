package germanalen.github.com.towerflower;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import germanalen.github.com.towerflower.database.Tower;

public class TowerListAdapter extends RecyclerView.Adapter<TowerListAdapter.TowerViewHolder> {

    class TowerViewHolder extends RecyclerView.ViewHolder {
        private final TextView towerItemView;

        private TowerViewHolder(View itemView) {
            super(itemView);
            towerItemView = itemView.findViewById(R.id.textView);
        }
    }

    private final LayoutInflater mInflater;
    private List<Tower> mTowers; // Cached copy of towers

    TowerListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public TowerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new TowerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TowerViewHolder holder, int position) {
        if (mTowers != null) {
            Tower current = mTowers.get(position);
            holder.towerItemView.setText(current.id + " " + current.creatorName);
        } else {
            // Covers the case of data not being ready yet.
            holder.towerItemView.setText("No Tower");
        }
    }

    void setTowers(List<Tower> towers){
        mTowers = towers;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mTowers has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mTowers != null)
            return mTowers.size();
        else return 0;
    }
}