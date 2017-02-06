package com.dhome.crazywinner.appdeneme.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.dhome.crazywinner.appdeneme.R;
import com.dhome.crazywinner.appdeneme.SettingApplier;
import com.dhome.crazywinner.appdeneme.Utils;
import com.dhome.crazywinner.appdeneme.activity_parameters;
import com.dhome.crazywinner.appdeneme.fragmentTasker;
import com.dhome.crazywinner.appdeneme.kernelTask;
import com.dhome.crazywinner.appdeneme.sqLite;
import com.pkmmte.view.CircularImageView;

import java.util.List;


public class RecylerAdapter extends RecyclerView.Adapter<RecylerAdapter.ViewHolder> implements View.OnClickListener
        ,View.OnLongClickListener{
    sqLite SQL;
    List<Integer> ids;
    RecyclerView vievim;
    Fragment mContext;
    SharedPreferences kayitlar;

    public RecylerAdapter(Fragment context,List<Integer> ids,RecyclerView vieww){

        this.mContext=context;
        this.vievim=vieww;

        this.ids=ids;
        SQL=new sqLite(mContext.getActivity());
        kayitlar=context.getActivity().getSharedPreferences("com.dhome.crazywinner.appdeneme", Context.MODE_PRIVATE);

    }
    public void satirKaldir(int position){

        ids.remove(position);
        notifyItemRemoved(position);
    }



    public String getItemName(int position){
        return SQL.getTask(position).getAd();
    }
    public void satirEkle(int id){

        ids.add(id);
        notifyItemInserted(ids.size());


    }

    @Override
    public void onClick(View v) {

        ViewHolder holder=(ViewHolder)v.getTag();

        if(v instanceof CheckBox){
            CheckBox cb=(CheckBox)v;
                   kernelTask taskim=SQL.getTask(Integer.parseInt(holder.ID.getText().toString()));
                    taskim.setActive(cb.isChecked() ? 1 : 0);

                    if(taskim.getAction().equals("onActivate")){
                        taskim.setActive(0);
                        cb.setChecked(false);
                        SettingApplier.Apply(mContext.getActivity(), taskim.id, false);

                    }

            SQL.upgradeTask(Integer.parseInt(holder.ID.getText().toString()),taskim);
            if(kayitlar.getBoolean("mysettings.forceset",false) && cb.isChecked()){
                Toast.makeText(mContext.getActivity(),"Profile can't be applied, if you want to apply,please select profile 'none' in left drawer",Toast.LENGTH_SHORT).show();
            }
        }else{
            Intent i = new Intent(mContext.getActivity(), activity_parameters.class);
            kernelTask taskim=SQL.getTask(Integer.parseInt(holder.ID.getText().toString()));

            i.putExtra("editmi", true);
            i.putExtra("num", Integer.parseInt(holder.ID.getText().toString()));
            i.putExtra("name",taskim.getAd());
            i.putExtra("action", taskim.getAction());

            mContext.startActivity(i);
        }

    }

    @Override
    public boolean onLongClick(View v) {
        fragmentTasker tasker=(fragmentTasker)mContext;
        ViewHolder holder=(ViewHolder)v.getTag();
        tasker.setNumber(Integer.parseInt(holder.ID.getText().toString()),holder.getPosition());
       tasker.openContextMenu(vievim);

        return true;
    }




    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtViewTitle;
        public TextView txtViewSummary;
        public CircularImageView imageView;
        public CheckBox chbView;
        public CardView cardView;
        public TextView ID;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.textViewadi);
            txtViewSummary = (TextView) itemLayoutView.findViewById(R.id.textViewAcik);
            chbView=(CheckBox)itemLayoutView.findViewById(R.id.checkbox2);
            cardView=(CardView)itemLayoutView.findViewById(R.id.cardView);
            ID=(TextView)itemLayoutView.findViewById(R.id.textViewID);
            imageView=(CircularImageView)itemLayoutView.findViewById(R.id.simge);
        }

    }

    @Override
    public RecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext.getActivity()).inflate(R.layout.satir_layout, parent, false);
        ViewHolder holder=new ViewHolder(view);
        holder.cardView.setOnClickListener(this);
        holder.cardView.setOnLongClickListener(this);
        holder.cardView.setTag(holder);
        holder.chbView.setOnClickListener(this);
        holder.chbView.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        kernelTask takim=SQL.getTask(ids.get(position));
        holder.ID.setText(""+ids.get(position));
        holder.txtViewTitle.setText(takim.getAd());
        holder.txtViewSummary.setText(takim.getAction());
        holder.chbView.setChecked(takim.isActive());
        holder.imageView.setImageDrawable(Utils.getBitmapFromPath(mContext.getActivity(), takim.getImagePath(), 48));

    }



    @Override
    public int getItemCount() {
        return ids.size();
    }
}
