package germanalen.github.com.towerflower;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import germanalen.github.com.towerflower.database.Tower;
import germanalen.github.com.towerflower.graphics.MyGLSurfaceView;
import germanalen.github.com.towerflower.graphics.TowerDrawer;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Tower> towers;
    private static int condition;
    public MyAdapter(int condition) {
        this.condition = condition;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private MyGLSurfaceView view;
        private Tower tower;
        public ViewHolder(MyGLSurfaceView v, final Context context) {
            super(v);
            view = v;
            if (condition == 0) {
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, TowerEditorActivity.class);
                        intent.putExtra("tower", tower);
                        context.startActivity(intent);
                    }
                });
            } else {
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, SearchCoupleActivity.class);
                        intent.putExtra("tower", tower);
                        context.startActivity(intent);
                    }
                });
            }

        }

        public void setTower(Tower tower) {
            this.tower = tower;
            view.getRenderer().getTowerDrawer().setDna(tower.decodeDna());
        }
    }



    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                   int viewType) {
        // create a new view
        MyGLSurfaceView v = new MyGLSurfaceView(parent.getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                500,
                600);
        v.setLayoutParams(layoutParams);

        return new ViewHolder(v, parent.getContext());
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.setTower(towers.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (towers == null)
            return 0;
        return towers.size();
    }

    public void setTowers(List<Tower> towers) {
        this.towers = towers;
        notifyDataSetChanged();
    }
}
