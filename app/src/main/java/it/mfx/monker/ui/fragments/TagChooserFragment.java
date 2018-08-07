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

import it.mfx.monker.R;
import it.mfx.monker.models.Tag;
import it.mfx.monker.ui.HolderFactory;
import it.mfx.monker.ui.ListRecyclerViewAdapter;

public class TagChooserFragment extends Fragment {

    private static final String ARG_PARENT_TAG_ID = "parent_id";
    //private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    String mParentTagId;

    public interface Listener {
        void onTagSelected(String tag_id);
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


    private void onChoosedTag( String tag_id ) {
        if( mListener != null )
            mListener.onTagSelected(tag_id);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParentTagId = getArguments().getString(ARG_PARENT_TAG_ID);
        }
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
            adapter = new ListRecyclerViewAdapter<Tag, TagViewHolder, Listener>(mTags, R.layout.tag_chooser_item,
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

                            if( listener != null ) {
                                holder.mView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String tag_id = holder.mItem.id;
                                        listener.onTagSelected(tag_id);
                                    }
                                });
                            }
                        }
                    },
                    new Listener() {
                        @Override
                        public void onTagSelected(String tag_id) {
                            onChoosedTag(tag_id);
                        }
                    });

            recyclerView.setAdapter(adapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            mListener = (Listener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
