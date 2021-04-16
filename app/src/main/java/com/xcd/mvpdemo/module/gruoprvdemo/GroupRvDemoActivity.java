package com.xcd.mvpdemo.module.gruoprvdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.google.gson.Gson;
import com.xcd.mvpdemo.R;
import com.xcd.mvpdemo.adapter.GroupRvDemoAdapter;
import com.xcd.mvpdemo.bean.AttrGroupsM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupRvDemoActivity extends AppCompatActivity {
    private TextView tv_submit;//假提交按钮，展示一下提交的数据
    private TextView tv_content;//显示提交的内容
    private RecyclerView rv_list;
    private GroupRvDemoAdapter groupRvDemoAdapter;
    private List<AttrGroupsM.AttrGroupsDTO> groupRvDemoMList = new ArrayList<>();//假设为后台返回的完整步骤数据
    public List<AttrGroupsM.AttrGroupsDTO> mGroups = new ArrayList<>();//这是用户实际已操作步骤和下一步即将操作的步骤

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_rv_demo_activity);
        tv_submit = findViewById(R.id.tv_submit);
        tv_content = findViewById(R.id.tv_content);
        rv_list = findViewById(R.id.rv_list);
        setData();//初始化假数据
        mGroups.add(groupRvDemoMList.get(0));//默认要将第一步添加进去
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        groupRvDemoAdapter = new GroupRvDemoAdapter(this, mGroups);
        //点击头部事件监听
        groupRvDemoAdapter.setOnHeaderClickListener(new GroupedRecyclerViewAdapter.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder,
                                      int groupPosition) {
                GroupRvDemoAdapter expandableAdapter = (GroupRvDemoAdapter) adapter;
                if (expandableAdapter.isExpand(groupPosition)) {
                    //收起子项
                    expandableAdapter.collapseGroup(groupPosition);
                } else {
                    //展开子项
                    expandableAdapter.expandGroup(groupPosition);
                }
            }
        });
        //点击子项事件监听
        groupRvDemoAdapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition
                    , int childPosition) {
                mGroups.get(groupPosition).setExpand(false);//选择完子项将他的父类关闭
                mGroups.get(groupPosition).setAttrId(mGroups.get(groupPosition).getAttrs()
                        .get(childPosition).getAttrId());//将选中的子项id赋给父类存储
                mGroups.get(groupPosition).setValue(mGroups.get(groupPosition).getAttrs()
                        .get(childPosition).getValue());//将选中的子项内容赋给父类存储
                if (groupPosition + 1 == groupRvDemoMList.size() || groupPosition + 1 < mGroups.size()) {
                    //如果这次更新数据的父级是最后一条步骤或者不是下一步要操作的步骤
                    groupRvDemoAdapter.notifyDataChanged();//刷新数据
                } else if (groupPosition + 1 == mGroups.size()) {
                    //否则的话插入最新一条即将操作的步骤
                    AttrGroupsM.AttrGroupsDTO attrsDTO = groupRvDemoMList.get(groupPosition + 1);
                    attrsDTO.setExpand(true);
                    mGroups.add(attrsDTO);//添加下一步操作
                    groupRvDemoAdapter.notifyDataChanged();//刷新数据
                }
            }
        });
        rv_list.setAdapter(groupRvDemoAdapter);
        //提交按钮,显示出来各步骤选择的子项内容
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Map<String, String>> mapList = new ArrayList<>();
                for (int i = 0; i < mGroups.size(); i++) {
                    Map<String, String> map = new HashMap<>();
                    map.put("分组", mGroups.get(i).getAttrGroupName());
                    map.put("子项id", mGroups.get(i).getAttrId() + "");
                    map.put("子项内容", mGroups.get(i).getValue());
                    mapList.add(map);
                }
                Gson gson = new Gson();
                tv_content.setText(gson.toJson(mapList));
            }
        });
    }

    //初始化假数据
    private void setData() {
        List<AttrGroupsM.AttrGroupsDTO.AttrsDTO> children1 = new ArrayList<>();
        //添加子项内容
        children1.add(new AttrGroupsM.AttrGroupsDTO.AttrsDTO(356, "https://res.cloudinary.com/uc-technology-inc" +
                "/image/upload/v1604477241/gizmogo3.0/gizmogoAdmin_network/verizon.png", "Version"));
        children1.add(new AttrGroupsM.AttrGroupsDTO.AttrsDTO(351, "https://res.cloudinary.com/uc-technology-inc" +
                "/image/upload/v1604477241/gizmogo3.0/gizmogoAdmin_network/att.png", "AT&T"));
        children1.add(new AttrGroupsM.AttrGroupsDTO.AttrsDTO(354, "https://res.cloudinary.com/uc-technology-inc" +
                "/image/upload/v1604477241/gizmogo3.0/gizmogoAdmin_network/t-mobile.png", "T-Mobile"));
        List<AttrGroupsM.AttrGroupsDTO.AttrsDTO> children2 = new ArrayList<>();
        //添加子项内容
        children2.add(new AttrGroupsM.AttrGroupsDTO.AttrsDTO(358, "", "64GB"));
        children2.add(new AttrGroupsM.AttrGroupsDTO.AttrsDTO(360, "", "128GB"));
        children2.add(new AttrGroupsM.AttrGroupsDTO.AttrsDTO(361, "", "256GB"));
        List<AttrGroupsM.AttrGroupsDTO.AttrsDTO> children3 = new ArrayList<>();
        //添加子项内容
        children3.add(new AttrGroupsM.AttrGroupsDTO.AttrsDTO(366, "", "Brand New"));
        children3.add(new AttrGroupsM.AttrGroupsDTO.AttrsDTO(367, "", "Like New"));
        children3.add(new AttrGroupsM.AttrGroupsDTO.AttrsDTO(365, "", "Good"));
        //添加步骤
        //第一步默认要打开
        groupRvDemoMList.add(new AttrGroupsM.AttrGroupsDTO
                (4,"Select Carrier",children1,0,"",true));
        groupRvDemoMList.add(new AttrGroupsM.AttrGroupsDTO
                (5,"Capacity",children2,0,"",false));
        groupRvDemoMList.add(new AttrGroupsM.AttrGroupsDTO
                (6,"Condition",children3,0,"",false));
    }
}