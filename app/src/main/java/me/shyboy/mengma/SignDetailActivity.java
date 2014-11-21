package me.shyboy.mengma;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import me.shyboy.mengma.Common.Sign;
import me.shyboy.mengma.Common.SignDetail;
import me.shyboy.mengma.database.SignDetailHelper;


public class SignDetailActivity extends Activity {

    private ImageButton bt_out;
    private TextView title;
    private List<SignDetail> list;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_detail);

        listView = (ListView)findViewById(R.id.detail_list);
        bt_out = (ImageButton)findViewById(R.id.detail_out);
        title = (TextView)findViewById(R.id.detail_title);
        list = new SignDetailHelper(SignDetailActivity.this).getDetails();
        if(null != list)
        {
            title.setText(list.get(0).getDescription());
        }
        listView.setAdapter(new ListAdapter(SignDetailActivity.this,R.layout.item_details,list));
        bt_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class ListAdapter extends ArrayAdapter<SignDetail>
    {
        private int resourceId;
        public ListAdapter(Context context,int resourceId,List<SignDetail> objects)
        {
            super(context,resourceId,objects);
            this.resourceId = resourceId;
        }
        public View getView(int position,View convertView,ViewGroup parent)
        {
            SignDetail signDetail = getItem(position);
            View view;
            ViewHolder viewHolder;

            if(convertView == null)
            {
                view = LayoutInflater.from(getContext()).inflate(resourceId,null);
                viewHolder = new ViewHolder();
                viewHolder.sno = (TextView)view.findViewById(R.id.item_detail_sno);
                viewHolder.name = (TextView)view.findViewById(R.id.item_detail_name);
                viewHolder.time = (TextView)view.findViewById(R.id.item_detail_time);
                view.setTag(viewHolder);
            }
            else
            {
                view = convertView;
                viewHolder = (ViewHolder)view.getTag();
            }
            viewHolder.sno.setText(signDetail.getSno());
            viewHolder.name.setText(signDetail.getName());
            viewHolder.time.setText(signDetail.getTime());
            return  view;
        }
        class ViewHolder
        {
            TextView sno;
            TextView name;
            TextView time;
        }
    }

}
