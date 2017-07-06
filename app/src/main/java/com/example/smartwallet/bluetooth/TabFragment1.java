package com.example.smartwallet.bluetooth;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 主界面的第一个部分
 * 搜索蓝牙设备
 */
public class TabFragment1 extends Fragment{
    public static BluetoothSocket bluetoothSocket;
    public static UUID SPP_UUID = UUID.fromString("00000000-0000-1000-8000-00805F9B34FB");
    //启用蓝牙
    public static final int BLUE_TOOTH_OPEN = 1000;
    //禁用蓝牙
    public static final int BLUE_TOOTH_CLOSE = BLUE_TOOTH_OPEN + 1;
    //搜索蓝牙
    public static final int BLUE_TOOTH_SEARTH = BLUE_TOOTH_CLOSE + 1;
    //被搜索蓝牙
    public static final int BLUE_TOOTH_MY_SEARTH = BLUE_TOOTH_SEARTH + 1;
    //关闭蓝牙连接
    public static final int BLUE_TOOTH_CLEAR = BLUE_TOOTH_MY_SEARTH + 1;

    private Button searth_switch, searth_my_switch, create_service;
    private Switch blue_switch;
    private ListView blue_list;
    private List<BluetoothDevice> bltList;
    private MyAdapter myAdapter;
    private ProgressBar btl_bar;
    private TextView blt_status_text;
    private LinearLayout content_ly;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://搜索蓝牙
                    break;
                case 2://蓝牙可以被搜索
                    break;
                case 3://设备已经接入
                    btl_bar.setVisibility(View.GONE);
                    BluetoothDevice device = (BluetoothDevice) msg.obj;
                    blt_status_text.setText("设备" + device.getName() + "已经接入");
                    Toast.makeText(getActivity(), "设备" + device.getName() + "已经接入", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), BltSocketActivity.class);
                    startActivity(intent);
                    break;
                case 4://已连接某个设备
                    btl_bar.setVisibility(View.GONE);
                    BluetoothDevice device1 = (BluetoothDevice) msg.obj;
                    blt_status_text.setText("已连接" + device1.getName() + "设备");
                    Toast.makeText(getActivity(), "已连接" + device1.getName() + "设备", Toast.LENGTH_LONG).show();
                    Intent intent1 = new Intent(getActivity(), BltSocketActivity.class);
                    startActivity(intent1);
                    break;
                case 5:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view1,container,false);
//        listView = (ListView) getActivity().findViewById(R.id.list_view);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BltManager.getInstance().initBltManager(getActivity());
        initView();
        initData();
    }

    private void initView() {
        searth_switch = (Button) getActivity().findViewById(R.id.searth_switch);
        blue_switch = (Switch) getActivity().findViewById(R.id.blue_switch);
        blue_list = (ListView) getActivity().findViewById(R.id.blue_list);
        btl_bar = (ProgressBar) getActivity().findViewById(R.id.btl_bar);
        blt_status_text = (TextView) getActivity().findViewById(R.id.blt_status_text);
        searth_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btl_bar.setVisibility(View.VISIBLE);
                blt_status_text.setText("正在搜索设备");
                BltManager.getInstance().clickBlt(getActivity(), BLUE_TOOTH_SEARTH);

            }
        });
    }

    private void initData() {
        btl_bar.setVisibility(View.GONE);
        bltList = new ArrayList<>();
        myAdapter = new MyAdapter();
        blue_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final BluetoothDevice bluetoothDevice = bltList.get(i);
                btl_bar.setVisibility(View.VISIBLE);
                blt_status_text.setText("正在连接" + bluetoothDevice.getName());
                //链接的操作应该在子线程
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BltManager.getInstance().createBond(bluetoothDevice, handler);
                    }
                }).start();
            }
        });

        blue_list.setAdapter(myAdapter);
        //检查蓝牙是否开启
        BltManager.getInstance().checkBleDevice(getActivity());
        //注册蓝牙扫描广播
        blueToothRegister();
        //更新蓝牙开关状态
        checkBlueTooth();
        //第一次进来搜索设备
        BltManager.getInstance().clickBlt(getActivity(), BLUE_TOOTH_SEARTH);
    }

    /**
     * 注册蓝牙回调广播
     */
    private void blueToothRegister() {
        BltManager.getInstance().registerBltReceiver(getActivity(), new BltManager.OnRegisterBltReceiver() {

            /**搜索到新设备
             * @param device
             */
            @Override
            public void onBluetoothDevice(BluetoothDevice device) {
                if (bltList != null && !bltList.contains(device)) {
                    bltList.add(device);
                }
                if (myAdapter != null)
                    myAdapter.notifyDataSetChanged();
            }

            /**连接中
             * @param device
             */
            @Override
            public void onBltIng(BluetoothDevice device) {
                btl_bar.setVisibility(View.VISIBLE);
                blt_status_text.setText("配对" + device.getName() + "中……");
            }

            /**连接完成
             * @param device
             */
            @Override
            public void onBltEnd(BluetoothDevice device) {
                btl_bar.setVisibility(View.GONE);
                blt_status_text.setText("配对" + device.getName() + "完成");
            }

            /**取消链接
             * @param device
             */
            @Override
            public void onBltNone(BluetoothDevice device) {
                btl_bar.setVisibility(View.GONE);
                blt_status_text.setText("取消了配对" + device.getName());
            }
        });
    }

    /**
     * 检查蓝牙的开关状态
     */
    private void checkBlueTooth() {
        if (BltManager.getInstance().getmBluetoothAdapter() == null || !BltManager.getInstance().getmBluetoothAdapter().isEnabled()) {
            blue_switch.setChecked(false);
        } else
            blue_switch.setChecked(true);
        blue_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    //启用蓝牙
                    BltManager.getInstance().clickBlt(getActivity(), BLUE_TOOTH_OPEN);
                else
                    //禁用蓝牙
                    BltManager.getInstance().clickBlt(getActivity(), BLUE_TOOTH_CLOSE);
            }
        });
    }

//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
//    @Override
//    public void onClick(View v) {
//
//        btl_bar.setVisibility(View.VISIBLE);
//        blt_status_text.setText("正在搜索设备");
//        BltManager.getInstance().clickBlt(getActivity(), BLUE_TOOTH_SEARTH);
//
//    }

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        final BluetoothDevice bluetoothDevice = bltList.get(position);
//        btl_bar.setVisibility(View.VISIBLE);
//        blt_status_text.setText("正在连接" + bluetoothDevice.getName());
//        //链接的操作应该在子线程
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                BltManager.getInstance().createBond(bluetoothDevice, handler);
//            }
//        }).start();
//
//    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return bltList.size();
        }

        @Override
        public Object getItem(int position) {
            return bltList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            ViewHolder vh;
            BluetoothDevice device = bltList.get(position);// 从集合中获取当前行的数据
            if (convertView == null) {
                // 说明当前这一行不是重用的
                // 加载行布局文件，产生具体的一行
//                v = Inflater.inflate(R.layout.bluetooth_item_blt, null);
                v = View.inflate(getActivity(),R.layout.bluetooth_item_blt,null);
                // 创建存储一行控件的对象
                vh = new ViewHolder();
                // 将该行的控件全部存储到vh中
                vh.blt_name = (TextView) v.findViewById(R.id.blt_name);
                vh.blt_address = (TextView) v.findViewById(R.id.blt_address);
                vh.blt_bond_state = (TextView) v.findViewById(R.id.blt_bond_state);
                v.setTag(vh);// 将vh存储到行的Tag中
            } else {
                v = convertView;
                // 取出隐藏在行中的Tag--取出隐藏在这一行中的vh控件缓存对象
                vh = (ViewHolder) convertView.getTag();
            }

            // 从ViewHolder缓存的控件中改变控件的值
            // 这里主要是避免多次强制转化目标对象而造成的资源浪费
            vh.blt_name.setText("蓝牙名称：" + device.getName());
            vh.blt_address.setText("蓝牙地址:" + device.getAddress());
            vh.blt_bond_state.setText("蓝牙状态:" + BltManager.getInstance().bltStatus(device.getBondState()));
            return v;
        }

        private class ViewHolder {
            TextView blt_name, blt_address,blt_bond_state;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //页面关闭的时候要断开蓝牙
        BltManager.getInstance().unregisterReceiver(getActivity());
    }
}

