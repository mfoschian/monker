package it.mfx.monker.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.mfx.monker.MyApp;
import it.mfx.monker.R;
import it.mfx.monker.models.Tag;
import it.mfx.monker.ui.HolderFactory;
import it.mfx.monker.ui.ListRecyclerViewAdapter;
import it.mfx.monker.ui.Utils;

public class TagChooserFragment extends Fragment {

    private static final String ARG_PARENT_TAG_ID = "parent_id";
    //private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private String mParentTagId;
    public String getParentTagId() {
        return mParentTagId;
    }

    public interface Listener {
        void onTagSelected(final Tag tag);
    }

    private class TagViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTagLabelView;
        public Tag mItem;

        public TagViewHolder(View view) {
            super(view);
            mView = view;
            mTagLabelView = view.findViewById(R.id.tag_label);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTagLabelView.getText() + "'";
        }
    }

    private ListRecyclerViewAdapter<Tag, TagViewHolder, Listener> adapter;
    private List<Tag> mTags;
    private Listener mListener;


    public TagChooserFragment() {
    }

    public static TagChooserFragment newInstance(String parent_tag_id) {
        TagChooserFragment fragment = new TagChooserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARENT_TAG_ID, parent_tag_id);
        fragment.setArguments(args);
        return fragment;
    }


    private void onChoosedTag( Tag tag ) {

        if( mListener != null && tag != null )
            mListener.onTagSelected(tag);
    }

    private void loadTags() {

        MyApp app = (MyApp)getActivity().getApplication();
        app.getTagsAsync(mParentTagId, new MyApp.Callback<List<Tag>>() {
            @Override
            public void onSuccess(List<Tag> result) {
                mTags.clear();
                mTags.addAll(result);
                Utils.runOnUIthread(new Utils.UICallback() {
                    @Override
                    public void onUIReady() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParentTagId = getArguments().getString(ARG_PARENT_TAG_ID);
        }
        else
            mParentTagId = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tag_chooser, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            mTags = new ArrayList<>();
            adapter = new ListRecyclerViewAdapter<>(mTags, R.layout.tag_chooser_item,
                    new HolderFactory<TagViewHolder>() {
                        @Override
                        public TagViewHolder createHolder(View view) {
                            return new TagViewHolder(view);
                        }
                    },
                    new ListRecyclerViewAdapter.Binder<Tag, TagViewHolder, Listener>() {
                        @Override
                        public void bind(Tag item, final TagViewHolder holder, final Listener listener) {
                            holder.mItem = item;
                            holder.mTagLabelView.setText(item.label);

                            holder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onChoosedTag(holder.mItem);
                                }
                            });
                        }
                    });

            recyclerView.setAdapter(adapter);

            loadTags();
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            mListener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
