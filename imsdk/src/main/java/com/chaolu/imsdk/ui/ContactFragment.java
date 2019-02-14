package com.chaolu.imsdk.ui;


import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaolu.imsdk.R;
import com.chaolu.imsdk.adapter.ExpandGroupListAdapter;
import com.chaolu.imsdk.mvp.model.FriendProfile;
import com.chaolu.imsdk.mvp.model.FriendshipInfo;
import com.chaolu.imsdk.mvp.model.GroupInfo;
import com.chaolu.imsdk.widget.TemplateTitle;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * 联系人界面
 */
public class ContactFragment extends Fragment implements  View.OnClickListener, Observer {

    private View view;
    private ExpandGroupListAdapter mGroupListAdapter;
    private ExpandableListView mGroupListView;
    private LinearLayout mNewFriBtn, mPublicGroupBtn, mChatRoomBtn,mPrivateGroupBtn;
    Map<String, List<FriendProfile>> friends;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view == null){
            view = inflater.inflate(R.layout.fragment_contact, container, false);
            mGroupListView = (ExpandableListView) view.findViewById(R.id.groupList);
            mNewFriBtn = (LinearLayout) view.findViewById(R.id.btnNewFriend);
            mNewFriBtn.setOnClickListener(this);
            mPublicGroupBtn = (LinearLayout) view.findViewById(R.id.btnPublicGroup);
            mPublicGroupBtn.setOnClickListener(this);
            mChatRoomBtn = (LinearLayout) view.findViewById(R.id.btnChatroom);
            mChatRoomBtn.setOnClickListener(this);
            mPrivateGroupBtn = (LinearLayout) view.findViewById(R.id.btnPrivateGroup);
            mPrivateGroupBtn.setOnClickListener(this);
            TemplateTitle title = (TemplateTitle) view.findViewById(R.id.contact_antionbar);
            title.setMoreImgAction(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMoveDialog();
                }
            });

            friends = FriendshipInfo.getInstance().getFriends();
            mGroupListAdapter = new ExpandGroupListAdapter(getActivity(), FriendshipInfo.getInstance().getGroups(), friends);
            mGroupListView.setAdapter(mGroupListAdapter);
            mGroupListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                    friends.get(FriendshipInfo.getInstance().getGroups().get(groupPosition)).get(childPosition).onClick(getActivity());
                    return false;
                }
            });
            FriendshipInfo.getInstance().addObserver(this);
            mGroupListAdapter.notifyDataSetChanged();
        }
        return view;
    }


    @Override
    public void onResume(){
        super.onResume();
        if(mGroupListAdapter!=null) {
            mGroupListAdapter.notifyDataSetChanged();
        }
    }



    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnNewFriend) {
            //新朋友列表
            Intent intent = new Intent(getActivity(), FriendshipManageMessageActivity.class);
            getActivity().startActivity(intent);
        }
        if (view.getId() == R.id.btnPublicGroup) {
            showGroups(GroupInfo.publicGroup);
        }
        if (view.getId() == R.id.btnChatroom) {
            showGroups(GroupInfo.chatRoom);
        }
        if (view.getId() == R.id.btnPrivateGroup) {
            showGroups(GroupInfo.privateGroup);
        }
    }

    private Dialog inviteDialog;
    private TextView addFriend, managerGroup,addGroup;

    private void showMoveDialog() {
        inviteDialog = new Dialog(getActivity(), R.style.dialog_imsdk);
        inviteDialog.setContentView(R.layout.contact_more);
        addFriend = (TextView) inviteDialog.findViewById(R.id.add_friend);
        managerGroup = (TextView) inviteDialog.findViewById(R.id.manager_group);
        addGroup = (TextView) inviteDialog.findViewById(R.id.add_group);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchFriendActivity.class);
                getActivity().startActivity(intent);
                inviteDialog.dismiss();
            }
        });
        managerGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ManageFriendGroupActivity.class);
                getActivity().startActivity(intent);
                inviteDialog.dismiss();
            }
        });
        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchGroupActivity.class);
                getActivity().startActivity(intent);
                inviteDialog.dismiss();
            }
        });
        Window window = inviteDialog.getWindow();
        window.setGravity(Gravity.TOP | Gravity.RIGHT);
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setAttributes(lp);
        inviteDialog.show();
    }


    /**
     * 群组列表
     * @param type
     */
    private void showGroups(String type){
        Intent intent = new Intent(getActivity(), GroupListActivity.class);
        intent.putExtra("type", type);
        getActivity().startActivity(intent);
    }

    /**
     * This method is called if the specified {@code Observable} object's
     * {@code notifyObservers} method is called (because the {@code Observable}
     * object has been updated.
     *
     * @param observable the {@link Observable} object.
     * @param data       the data passed to {@link Observable#notifyObservers(Object)}.
     */
    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof FriendshipInfo){
            mGroupListAdapter.notifyDataSetChanged();
        }
    }
}
