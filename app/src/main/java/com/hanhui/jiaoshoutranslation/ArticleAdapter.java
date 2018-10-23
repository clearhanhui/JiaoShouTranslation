package com.hanhui.jiaoshoutranslation;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hanhui.jiaoshoutranslation.news.Articles;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticleAdapter extends BaseAdapter {

    Context context;
    List<Articles> list ;

    public ArticleAdapter(Context context, List<Articles> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_news,null);
            holder = new ViewHolder();
            holder.iv = view.findViewById(R.id.iv_item_news);
            holder.tv_source = view.findViewById(R.id.tv_sources_item_news);
            holder.tv_time = view.findViewById(R.id.tv_date_item_news);
            holder.tv_title = view.findViewById(R.id.tv_title_item_news);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Articles articles = list.get(i);
        holder.tv_title.setText(articles.getTitle());
        holder.tv_time.setText(articles.getPublishedAt().substring(0,19).replace("T"," "));
        holder.tv_source.setText(articles.getSource().getName());
        Picasso.with(context).load(articles.getUrlToImage()).into(holder.iv);
        Log.i("+++++++", "getView: "+articles.getUrlToImage());


        return view;
    }

    class ViewHolder{
        ImageView iv;
        TextView tv_title;
        TextView tv_source;
        TextView tv_time;
    }
}
