package com.augmentaa.sparkev.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.country.Data;
import com.augmentaa.sparkev.ui.ForgotPasswordActivity;
import com.augmentaa.sparkev.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class GetCountryForgotPasswordListAdapter extends BaseAdapter implements Filterable {
    Context context;
    private List<Data> itemsModelsl;
    private List<Data> itemsModelListFiltered;
    ProgressDialog progress;
    ViewHolder viewHolder;
    private List<Data> list_search;
//    ResponseCountry country=new ResponseCountry();

    public GetCountryForgotPasswordListAdapter(Context context, List<Data> itemsModelsl) {
        this.context = context;
        this.itemsModelListFiltered = itemsModelsl;
        this.itemsModelsl = itemsModelsl;
        this.progress = new ProgressDialog(context);
//        this.list_search=list_search;
    }

    @Override
    public int getCount() {
        return itemsModelListFiltered.size();

    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.country_itemlist, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        ForgotPasswordActivity.country.setData(itemsModelListFiltered);
        Logger.e("======Data list=inner="+itemsModelListFiltered.size()+"   "+ForgotPasswordActivity.country.getData());
        viewHolder.txtName.setText("" + itemsModelListFiltered.get(position).name);

        return convertView;
    }


    public class ViewHolder {
        TextView txtName;


    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                try {

                    if (constraint == null || constraint.length() == 0) {
                        filterResults.count = itemsModelsl.size();
                        filterResults.values = itemsModelsl;


                    } else {
                        List<Data> resultsModel = new ArrayList<>();
                        String searchStr = constraint.toString().toLowerCase();
                        for (Data itemsModel : itemsModelsl) {
                            if (itemsModel.name == null) {
                                itemsModel.name = "Not Applicable";
                            }
                            if (itemsModel.name.toLowerCase().contains(searchStr)) {
                                resultsModel.add(itemsModel);
                            }
                            filterResults.count = resultsModel.size();
                            filterResults.values = resultsModel;


                        }
                    }


//                }
                } catch (Exception e) {

                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemsModelListFiltered = (List<Data>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }
}
