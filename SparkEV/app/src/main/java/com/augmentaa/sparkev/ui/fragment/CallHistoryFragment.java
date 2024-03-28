package com.augmentaa.sparkev.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.model.signup.question_list.Data;
import com.augmentaa.sparkev.retrofit.APIClient;
import com.augmentaa.sparkev.retrofit.APIInterface;
import com.augmentaa.sparkev.utils.Utils;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.List;

public class CallHistoryFragment extends Fragment {
    ListView listView;
    List<Data> list;
    View view;
    APIInterface apiInterface;
    ProgressDialog progress;

    SupportMapFragment mapFragment;
    TextView textView;
    String error_message;

    public CallHistoryFragment() {
        // Required empty public constructor  
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_upcoming_call, container, false);
        listView = (ListView) view.findViewById(R.id.listview);
        textView = (TextView) view.findViewById(R.id.text);
        this.progress = new ProgressDialog(getActivity());
        this.apiInterface = (APIInterface) APIClient.getClientNew().create(APIInterface.class);
        list = new ArrayList<>();
        if (Utils.isNetworkConnected(getActivity())) {
            progress.setMessage(getResources().getString(R.string.loading));
            progress.show();
//            getCallHistory();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }
      /*  //You need to add the following line for this solution to work; thanks skayred
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Intent intent=new Intent(getActivity(),RequestToCallbackActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });*/
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

  /*  private void getCallHistory() {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("Authorization", Pref.getValue(Pref.TYPE.TOKEN.toString(), null));
        this.apiInterface.getCallHistory(hashMap1, new CallRequest("C", Pref.getIntValue(Pref.TYPE.USER_ID.toString(), 0), Pref.getValue(Pref.TYPE.MOBILE.toString(), null))).enqueue(new Callback<Question>() {
            public void onResponse(Call<Question> call, Response<Question> response) {
                progress.dismiss();
                Logger.e("URL: " + call.request().toString());
                Logger.e("Response Code: " + response.code());
                AppUtils.bodyToString(call.request().body());
                if (response.code() == 200) {
                    try {
                        Question loginResponse = (Question) response.body();
                        Logger.e("Response:111111 " + loginResponse.data.size());
                        if (loginResponse.status) {
                            if (loginResponse.data.size() > 0) {
                                for (int i = 0; i < loginResponse.data.size(); i++) {
                                    if (loginResponse.data.get(i).status.equalsIgnoreCase("C")) {
                                        list.add(loginResponse.data.get(i));
                                        CallHistoryAdapter adapter = new CallHistoryAdapter(getActivity(), list);
                                        listView.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                    }

                                }
                                if (list.size() > 0) {
                                    textView.setVisibility(View.GONE);
                                    listView.setVisibility(View.VISIBLE);
                                } else {
                                    textView.setVisibility(View.VISIBLE);
                                    listView.setVisibility(View.GONE);
                                }
                            } else {
                                textView.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "No call request found", Toast.LENGTH_LONG).show();

                            }

                        } else {
                            try {
                                Toast.makeText(getActivity(), loginResponse.message, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {

                            }

                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                        error_message = e.getMessage();

                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();

                }
            }

            public void onFailure(Call<Question> call, Throwable t) {
                Toast.makeText(getActivity(), getResources().getString(R.string.server_not_found_please_try_again), Toast.LENGTH_LONG).show();
                progress.dismiss();
                call.cancel();
            }
        });


    }
*/

}  