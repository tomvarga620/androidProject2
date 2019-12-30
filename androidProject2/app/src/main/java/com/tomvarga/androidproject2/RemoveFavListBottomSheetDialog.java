package com.tomvarga.androidproject2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONObject;

public class RemoveFavListBottomSheetDialog extends BottomSheetDialogFragment {

    private RequestQueue myQueue;
    SharedPrefs modSharedPrefs;

    View v;
    Long id;
    String token;
    int position;
    String title;

    public RemoveFavListBottomSheetDialog(Long idList, String token, int position,String title) {
        this.id = idList;
        this.token = token;
        this.position = position;
        this.title = title;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.options_favorit_list_dialog,container,false);

        modSharedPrefs = new SharedPrefs(getActivity());
        myQueue = Volley.newRequestQueue(v.getContext());

        LinearLayout remove = v.findViewById(R.id.removeList);
        LinearLayout cancel = v.findViewById(R.id.cancelAction);
        TextView title = v.findViewById(R.id.currentTitleList);
        title.setText(this.title);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              deleteFavoritList();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return v;
    }

    private void deleteFavoritList() {

        String URL = modSharedPrefs.getIP()+"/deleteFavoriteList?token="+token+"&id="+id;

        JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.DELETE, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(v.getContext(), "Response:  " + response.toString(), Toast.LENGTH_SHORT).show();
                ((FavoriteActivity)getActivity()).removeViewFromList(position);
                dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(v.getContext(), "Error: something wrong " + error.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("RemoveFavListDialog",error.getMessage());
            }
        }) {
        };
        myQueue.add(jsonOblect);
    }
}
