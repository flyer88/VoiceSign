package me.shyboy.mengma;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import me.shyboy.mengma.Common.Sign;
import me.shyboy.mengma.Common.SignConfig;
import me.shyboy.mengma.Common.SignList;
import me.shyboy.mengma.database.SignHelper;
import me.shyboy.mengma.methods.OkHttpUtil;


public class SignListActivity extends Activity {

    private List<Sign> list;
    private ImageButton btn_out;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_list);
        btn_out = (ImageButton)findViewById(R.id.signlist_out);
        listView = (ListView)findViewById(R.id.signlist_listview);

        list = new SignHelper(SignListActivity.this).getSigns();
        listView.setAdapter(new ListAdapter(SignListActivity.this,R.layout.item_sign,list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sign sign = list.get(position);
                if(SignConfig.isNetworkConnected(SignListActivity.this) == false)
                {
                    Toast.makeText(SignListActivity.this,"凑 ~ ~ 没联网",Toast.LENGTH_SHORT).show();
                    return ;
                }
                new OkHttpUtil(SignListActivity.this).details(sign);
            }
        });
        btn_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private class ListAdapter extends ArrayAdapter<Sign>
    {
        private int resourceId;
        public ListAdapter(Context context,int resourceId,List<Sign> objects)
        {
            super(context,resourceId,objects);
            this.resourceId = resourceId;
        }
        public View getView(int position,View convertView,ViewGroup parent)
        {
            Sign sign = getItem(position);
            View view;
            ViewHolder viewHolder;

            if(convertView == null)
            {
                view = LayoutInflater.from(getContext()).inflate(resourceId,null);
                viewHolder = new ViewHolder();
                viewHolder.date = (TextView)view.findViewById(R.id.item_sign_date);
                viewHolder.description = (TextView)view.findViewById(R.id.item_sign_description);
                viewHolder.time = (TextView)view.findViewById(R.id.item_sign_time);
                view.setTag(viewHolder);
            }
            else
            {
                view = convertView;
                viewHolder = (ViewHolder)view.getTag();
            }
            viewHolder.date.setText(sign.getDate()+"   "+sign.getWeek());
            viewHolder.description.setText(sign.getDescription());
            viewHolder.time.setText(sign.getTime());
            return  view;
        }
        class ViewHolder
        {
            TextView date;
            TextView description;
            TextView time;
        }
    }
}
