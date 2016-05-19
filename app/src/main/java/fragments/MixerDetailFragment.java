package fragments;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.brainbeats.MixerActivity;
import com.brainbeats.R;
import java.util.ArrayList;
import java.util.List;
import adapters.MixItemAdapter;
import model.MixerItem;

public class MixerDetailFragment extends Fragment {

    List<MixerItem> mixerItemList = new ArrayList<>();
    private RecyclerView mMixerItemList;
    private MixItemAdapter mMixerItemAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public MixerDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mixer_detail, container, false);
        mMixerItemList = (RecyclerView) v.findViewById(R.id.beat_mix_item_list);
        ((TextView)v.findViewById(R.id.separator_title)).setText(R.string.beat_levels);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        Drawable up = DrawableCompat.wrap(ContextCompat.getDrawable(getContext(), R.drawable.ic_up));
        DrawableCompat.setTint(up, getResources().getColor(R.color.theme_primary_text_color));
        toolbar.setNavigationIcon(up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                ((MixerActivity) getActivity()).navigateUpOrBack(getActivity(), fm);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMixerItemAdapter = new MixItemAdapter(getContext(),mixerItemList);
        mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        mMixerItemList.setLayoutManager(mLayoutManager);
        mMixerItemList.setAdapter(mMixerItemAdapter);
        getMixerItemData();
    }

    //TODO: Replace dummy data with real data from sound cloud
    public void getMixerItemData(){
        mixerItemList.add(new MixerItem());
        mixerItemList.get(0).setMixItemTitle("Alpha");
        mixerItemList.add(new MixerItem());
        mixerItemList.get(1).setMixItemTitle("Beta");
        mixerItemList.add(new MixerItem());
        mixerItemList.get(2).setMixItemTitle("Gamma");
        mixerItemList.add(new MixerItem());
        mixerItemList.get(3).setMixItemTitle("Theta");
        mixerItemList.add(new MixerItem());
        mixerItemList.get(4).setMixItemTitle("Add new");
        mMixerItemAdapter.notifyDataSetChanged();
    }
}
