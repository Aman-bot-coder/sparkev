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

import java.util.ArrayList;
import java.util.List;

public class GetAllCountryListAdapter extends BaseAdapter implements Filterable {
    Context context;
    private List<Data> itemsModelsl;
    private List<Data> itemsModelListFiltered;
    ProgressDialog progress;
    ViewHolder viewHolder;
   ClickButton clickButton;
    public GetAllCountryListAdapter(Context context, List<Data> itemsModelsl, ClickButton clickButton) {
        this.context = context;
        this.itemsModelListFiltered = itemsModelsl;
        this.itemsModelsl = itemsModelsl;
        this.clickButton=clickButton;
        this.progress = new ProgressDialog(context);
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

        try {
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.country_itemlist, parent, false);
                viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();

            }
            viewHolder.txtName.setText("" + itemsModelListFiltered.get(position).name);
            viewHolder.txtName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickButton.search_country(position, itemsModelListFiltered);

                }
            });

        } catch (Exception e) {

        }


        return convertView;
    }


    public class ViewHolder {
        TextView txtName;


    }


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

    public interface ClickButton {
        void search_country(int position,List<Data> list);

    }
}
