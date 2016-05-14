package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.brainbeats.R;
import java.util.List;
import model.User;

/**
 * Created by douglas on 5/13/2016.
 */
public class SocialAdapter extends RecyclerView.Adapter<SocialAdapter.ViewHolder> {

    Context mAdapterContext;
    List<User> mUsers;

    public SocialAdapter(Context context, List<User> data) {
        mAdapterContext = context;
        mUsers = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mUserTitle;

        public ViewHolder(View v) {
            super(v);
            mUserTitle = (TextView) v.findViewById(R.id.user_name);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mAdapterContext).inflate(R.layout.user_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mUserTitle.setText(mUsers.get(position).getUserName());
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}
