package weike.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import weike.data.BookItem;
import weike.shutuier.R;
import weike.util.Mysingleton;

/**
 * Created by Rth on 2015/2/23.
 */
public class BookListAdapter extends BaseAdapter {

    private List<BookItem> list = new ArrayList<>();
    private Context context;
    private ImageLoader loader = null;
    private int w,h;

    public BookListAdapter(List<BookItem> list,Context con) {
        this.list.clear();
        this.list.addAll(list);
        this.context = con;
        loader = Mysingleton.getInstance(con).getImageLoader();
        w = con.getResources().getDimensionPixelSize(R.dimen.imgwidth_listitem);
        h = con.getResources().getDimensionPixelSize(R.dimen.imgheight_listitem);
    }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.list_item,null);
                holder = new ViewHolder();
                holder.imgView = (ImageView) convertView.findViewById(R.id.img_item);
                holder.tvName = (TextView) convertView.findViewById(R.id.textview_item_book_name);
                holder.tvHowOld = (TextView) convertView.findViewById(R.id.textview_item_book_degree);
                holder.tvAuthor = (TextView) convertView.findViewById(R.id.textview_item_book_author);
                holder.tvPublisher = (TextView) convertView.findViewById(R.id.textview_item_book_press);
                holder.tvDetail = (TextView) convertView.findViewById(R.id.textview_item_book_description);
                holder.tvStatue = (TextView) convertView.findViewById(R.id.textview_item_goal);
                holder.tvOPrice = (TextView) convertView.findViewById(R.id.textview_item_origin_price);
                holder.tvSPrice = (TextView) convertView.findViewById(R.id.textview_item_sell_price);
                RelativeLayout rlShare = (RelativeLayout) convertView.findViewById(R.id.rl_list_item_share);
                RelativeLayout rlLiuyan = (RelativeLayout) convertView.findViewById(R.id.rl_list_item_liuyan);
                holder.tvSNum = (TextView) rlShare.findViewById(R.id.tv_share_number);
                holder.tvMNum = (TextView) rlLiuyan.findViewById(R.id.tv_liuyan_number);
                convertView.setTag(holder);
            }else {
                holder =(ViewHolder) convertView.getTag();
            }
            try {
                BookItem item = list.get(position);
                holder.tvName.setText(item.getBookName());
                String howOld = "全新";
                switch (item.getHowOld()) {
                    case "10":
                        howOld = "全新";
                        break;
                    case "9":
                        howOld = "九成新";
                        break;
                    case "8":
                        howOld = "八成新";
                        break;
                    case "7":
                        howOld = "七成新";
                        break;
                    case "6":
                        howOld = "六成新";
                        break;
                    case "5":
                        howOld = "五成新";
                        break;
                    case "4":
                        howOld = "四成新";
                        break;
                    case "3":
                        howOld = "三成以下";
                        break;
                }
                holder.tvHowOld.setText(howOld);
                holder.tvAuthor.setText(item.getAuthorName());
                holder.tvPublisher.setText(item.getPublisher());
                holder.tvDetail.setText(item.getDetail());
                String status = "出售";
                switch (item.getStatue()) {
                    case "0":
                        status = "出售";
                        break;
                    case "1":
                        status = "求购";
                       break;
                    case "2":
                        status = "赠送";
                        break;
                    case "3":
                        status = "求送";
                        break;
                    case "4":
                        status = "出售/赠送";
                        break;
                    case "5":
                        status = "求购/赠送";
                        break;
                }
                holder.tvStatue.setText(status);
                holder.tvOPrice.setText("￥"+item.getOriginPrice());
                holder.tvSPrice.setText("￥"+item.getSellPrice());
                holder.tvSNum.setText(item.getShareNumber()+"");
                holder.tvMNum.setText(item.getMessageNumber()+"");
                loader.get(list.get(position).getImgUrl(),ImageLoader.getImageListener(holder.imgView,R.drawable.def,R.drawable.def),w,h);
            } catch (Exception e) {
                Log.e("BookListAdapter","error in setTextView",e);
            }
            return convertView;
        }

    static class ViewHolder {
        ImageView imgView;
        TextView tvName,tvHowOld,tvAuthor,tvPublisher, tvDetail,tvStatue,tvOPrice,tvSPrice,tvSNum,tvMNum;
    }

    public void updateData(List<BookItem> data){
        this.list.clear();
        this.list.addAll(data);
        notifyDataSetChanged();
    }


}
