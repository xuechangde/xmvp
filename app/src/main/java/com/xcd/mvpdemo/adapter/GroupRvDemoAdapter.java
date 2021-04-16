package com.xcd.mvpdemo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.xcd.mvpdemo.R;
import com.xcd.mvpdemo.bean.AttrGroupsM;


import java.util.List;

public class GroupRvDemoAdapter extends GroupedRecyclerViewAdapter {
    private List<AttrGroupsM.AttrGroupsDTO> mGroups;

    public GroupRvDemoAdapter(Context context,List<AttrGroupsM.AttrGroupsDTO> mGroups) {
        super(context);
        this.mGroups = mGroups;
    }

    @Override
    public int getGroupCount() {
        return mGroups == null ? 0 : mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //如果当前组收起，就直接返回0，否则才返回子项数。这是实现列表展开和收起的关键。
        if (!isExpand(groupPosition)) {
            return 0;
        }
        List<AttrGroupsM.AttrGroupsDTO.AttrsDTO> children = mGroups.get(groupPosition).getAttrs();
        return children == null ? 0 : children.size();
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        return true;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return false;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return R.layout.grouprv_item_header;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return 0;
    }

    @Override
    public int getChildLayout(int viewType) {
        return R.layout.grouprv_item;
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        AttrGroupsM.AttrGroupsDTO entity = mGroups.get(groupPosition);
        holder.setText(R.id.tv_buzhou, entity.getAttrGroupName());
        //id=0没有选择子项，如果选择了子项，显示子项内容
        if(entity.getAttrId()!=0){
            holder.get(R.id.tv_neirong).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_neirong,entity.getValue());
        }else{
            holder.get(R.id.tv_neirong).setVisibility(View.GONE);
        }
        ImageView ivState = holder.get(R.id.iv_state);
        if(entity.isExpand()){//判断子项是否已展开，如果有箭头可以可以根据这个判断来旋转箭头或者显示隐藏
//            ivState.setRotation(180);
            ivState.setVisibility(View.GONE);
        } else {
//            ivState.setRotation(0);
            ivState.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {
    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        AttrGroupsM.AttrGroupsDTO.AttrsDTO entity = mGroups.get(groupPosition).getAttrs().get(childPosition);
        if(mGroups.get(groupPosition).getAttrId()==entity.getAttrId())
        {//如果改子项的id==父级选中的子项id，则改变背景色
            holder.get(R.id.ll_item).setBackgroundColor(Color.parseColor("#F44336"));
        }else{
            holder.get(R.id.ll_item).setBackgroundColor(Color.parseColor("#B9CBDF"));
        }
        holder.setText(R.id.tv_neirong,entity.getValue());
        //这里可以用自己使用的图片加载显示网络图片
        ImageView iv_img = holder.get(R.id.iv_img);
    }

    /**
     * 判断当前组是否展开
     *
     * @param groupPosition
     * @return
     */
    public boolean isExpand(int groupPosition) {
        AttrGroupsM.AttrGroupsDTO entity = mGroups.get(groupPosition);
        return entity.isExpand();
    }

    /**
     * 展开一个组
     *
     * @param groupPosition
     */
    public void expandGroup(int groupPosition) {
        expandGroup(groupPosition, true);
    }

    /**
     * 展开一个组
     *
     * @param groupPosition
     * @param animate
     */
    public void expandGroup(int groupPosition, boolean animate) {
        AttrGroupsM.AttrGroupsDTO entity = mGroups.get(groupPosition);
        entity.setExpand(true);
        if (animate) {
            notifyChildrenInserted(groupPosition);
            notifyGroupChanged(groupPosition);
        } else {
            notifyDataChanged();
        }
    }

    /**
     * 收起一个组
     *
     * @param groupPosition
     */
    public void collapseGroup(int groupPosition) {
        collapseGroup(groupPosition, true);
    }

    /**
     * 收起一个组
     *
     * @param groupPosition
     * @param animate
     */
    public void collapseGroup(int groupPosition, boolean animate) {
        AttrGroupsM.AttrGroupsDTO entity = mGroups.get(groupPosition);
        entity.setExpand(false);
        if (animate) {
            notifyChildrenRemoved(groupPosition);
            notifyGroupChanged(groupPosition);
        } else {
            notifyDataChanged();
        }
    }
}
