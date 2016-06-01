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
import android.widget.EditText;
import android.widget.TextView;
import com.brainbeats.MixerActivity;
import com.brainbeats.R;

import java.util.ArrayList;
import java.util.List;
import adapters.MixItemAdapter;
import data.MixContract;
import model.Mix;
import model.MixItem;
import utils.Constants;

public class MixerDetailFragment extends Fragment {

    List<MixItem> mixItemList = new ArrayList<>();
    private RecyclerView mMixerItemList;
    private MixItemAdapter mMixerItemAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public Bundle mUserSelections;
    public EditText mMixTitle;
    public Mix mSelectedMix;

    public MixerDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mixer_detail, container, false);
        mMixerItemList = (RecyclerView) v.findViewById(R.id.beat_mix_item_list);
        mMixTitle = (EditText) v.findViewById(R.id.track_title);
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
                String[] args = {
                        String.valueOf(mSelectedMix.getMixId()),
                };
                mSelectedMix.setMixTitle(mMixTitle.getText().toString());
                ((MixerActivity) getActivity()).getContentResolver().update(MixContract.MixEntry.CONTENT_URI,Constants.buildMixRecord(mSelectedMix),getResources().getString(R.string.db_id_field) + mSelectedMix.getMixId(), null);
                for (int i = 0; i < mSelectedMix.getMixItems().size(); i++) {
                    MixItem item = mSelectedMix.getMixItems().get(i);
                    ((MixerActivity) getActivity()).getContentResolver().update(MixContract.MixItemsEntry.CONTENT_URI,
                            Constants.buildMixItemsRecord(mSelectedMix.getMixId(),item),
                            getResources().getString(R.string.db_id_field) + item.getMixItemId(),
                            null);
                }
                ((MixerActivity) getActivity()).navigateUpOrBack(getActivity(), fm);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUserSelections = getArguments();
        if(mUserSelections != null) {
            mSelectedMix = (Mix) mUserSelections.get(Constants.KEY_EXTRA_SELECTED_MIX);
            mMixTitle.setText(mSelectedMix.getBeatTitle());
            mixItemList = mSelectedMix.getMixItems();
            MixItem addNewMix = new MixItem();
            addNewMix.setMixItemTitle("Add New");
            mixItemList.add(addNewMix);
        }

        mMixerItemAdapter = new MixItemAdapter(getContext(), mixItemList);
        mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        mMixerItemList.setLayoutManager(mLayoutManager);
        mMixerItemList.setAdapter(mMixerItemAdapter);
        mMixerItemAdapter.notifyDataSetChanged();
    }
}
