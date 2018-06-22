package com.glowingsoft.photographist.Fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.glowingsoft.photographist.Activities.AppActivity;
import com.glowingsoft.photographist.Activities.MenuActivity;
import com.glowingsoft.photographist.Activities.SelectionActivity;
import com.glowingsoft.photographist.Adapters.PortfolioRecyclerView;
import com.glowingsoft.photographist.Help.ApiEndPoints;
import com.glowingsoft.photographist.Help.Utils;
import com.glowingsoft.photographist.Model.CommentsModel;
import com.glowingsoft.photographist.Model.FeedModel;
import com.glowingsoft.photographist.R;
import com.glowingsoft.photographist.webRequest.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends ParentFragment {
    private static final int SELECT_FILE = 2;
    ImageView profileImageView, coverImageView, coverChangeIV;
    TextView followTV;
    Button walletBT, portfolioBT,aboutBT;
    ImageView addPostFAB;
    LinearLayout walletLL , aboutLL;
    RelativeLayout portfolioLL;
    ArrayList<TextView> textViews;
    ArrayList<String> paramsUpdate;
    ImageView menuIV;
    AlertDialog.Builder alert;
    ArrayList<FeedModel> feedModels;
    ArrayList<CommentsModel> commentsModels;
    int VISITOR;   ////IS ITSELF LOGGED IN USER OR ANY OTHER USER. 0 ITSELF 1 OTHER;
    private int UPDATEPHOTOORCOVER; /// 1 profile 2 cover
    private String user_id;
    private Bitmap bm;
    private boolean uploadLogo;
    private String finalPath;
    private String current_id;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();

                try {
                    finalPath = getPath(selectedImageUri, getActivity());

                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                    bm = BitmapFactory.decodeFile(finalPath, btmapOptions);
                    if (UPDATEPHOTOORCOVER == 1) {
                        profileImageView.setImageBitmap(bm);
                        uploadLogo = true;
                        bm=getResizedBitmap(bm,200);
                    } else if (UPDATEPHOTOORCOVER == 2) {
                        coverImageView.setImageBitmap(bm);
                        uploadLogo = true;
                        bm=getResizedBitmap(bm,400);
                    }
                    requestUpdatePhoto();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_profile, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        context = getActivity();
        textViews = new ArrayList<>();
        paramsUpdate = new ArrayList<>();
        alert = new AlertDialog.Builder(context);
        profileImageView = view.findViewById(R.id.profileImageView);
        coverImageView = view.findViewById(R.id.coverImageView);
        coverChangeIV = view.findViewById(R.id.coverChangeIV);
        aboutBT = view.findViewById(R.id.aboutBT);
        walletBT = view.findViewById(R.id.walletBT);
        portfolioBT = view.findViewById(R.id.portfolioBT);
        aboutLL = view.findViewById(R.id.about_layout);
        walletLL = view.findViewById(R.id.wallet_layout);
        portfolioLL = view.findViewById(R.id.portfolio_layout);
        followTV = view.findViewById(R.id.followTV);
        menuIV = view.findViewById(R.id.menuIV);
        addPostFAB = view.findViewById(R.id.addPostFAB);

        textViews.add(0, ((TextView) view.findViewById(R.id.descTV)));
        textViews.add(1, ((TextView) view.findViewById(R.id.occupationTV)));
        textViews.add(2, ((TextView) view.findViewById(R.id.cityTV)));
        textViews.add(3, ((TextView) view.findViewById(R.id.facebookTV)));
        textViews.add(4, ((TextView) view.findViewById(R.id.twitterTV)));
        textViews.add(5, ((TextView) view.findViewById(R.id.emailTV)));
        textViews.add(6, ((TextView) view.findViewById(R.id.onlineTV)));

        paramsUpdate.add(0, "description");
        paramsUpdate.add(1, "occupation");
        paramsUpdate.add(2, "city");
        paramsUpdate.add(3, "facebook");
        paramsUpdate.add(4, "twitter");

        if (getArguments() == null) {
            try {
                user_id = AppActivity.getInstance().getUserID(context);
            } catch (JSONException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            VISITOR = 0;
        } else {
            user_id = getArguments().getString("user_id");
            VISITOR = 1;
        }
        addPostFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        if (VISITOR==0){
            menuIV.setVisibility(View.VISIBLE);
            menuIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(context, MenuActivity.class));

                }
            });
        }
        getProfile();
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (VISITOR == 0) {
                    UPDATEPHOTOORCOVER = 1;
                    chooseImage();
                }
            }
        });
        coverChangeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (VISITOR == 0) {
                    UPDATEPHOTOORCOVER = 2;
                    chooseImage();
                }
            }
        });

        aboutBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aboutLL.setVisibility(View.VISIBLE);
                walletLL.setVisibility(View.GONE);
                portfolioLL.setVisibility(View.GONE);
            }
        });
        portfolioBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                portfolioLL.setVisibility(View.VISIBLE);
                walletLL.setVisibility(View.GONE);
                aboutLL.setVisibility(View.GONE);
            }
        });
        walletBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                walletLL.setVisibility(View.VISIBLE);
                aboutLL.setVisibility(View.GONE);
                portfolioLL.setVisibility(View.GONE);
            }
        });

        textViews.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (VISITOR == 0) {
                    updateDialog(0, paramsUpdate.get(0));
                }
            }
        });
        textViews.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (VISITOR == 0) {
                    updateDialog(1, paramsUpdate.get(1));
                }
            }
        });
        textViews.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (VISITOR == 0) {
                    updateDialog(2, paramsUpdate.get(2));
                }
            }
        });
        textViews.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (VISITOR == 0) {
                    updateDialog(3, paramsUpdate.get(3));
                }
            }
        });
        textViews.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (VISITOR == 0) {
                    updateDialog(4, paramsUpdate.get(4));
                }
            }
        });


        try {
            refreshData();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void getProfile() {
        RequestParams params = new RequestParams();
        params.put("user_id", user_id);
        WebReq.post(context, ApiEndPoints.getInstance().profile, params, new ResponseHandler());
    }

    //region choose Image
    private void chooseImage() {

        final CharSequence[] items = {"Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileFragment.this.getActivity());

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                /*if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment
                            .getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else*/
                if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void updateDialog(final int position, final String param) {
        final EditText edittext = new EditText(context);
        edittext.setText(textViews.get(position).getText().toString());
        alert.setView(edittext);
        alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                String value = edittext.getText().toString();
                if (value.length() > 0) {
                    textViews.get(position).setText(value);
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("param", param);
                    requestParams.put("value", value);
                    requestParams.put("user_id", user_id);
                    WebReq.post(context, ApiEndPoints.getInstance().updateAdditionalInfo, requestParams, new ResponseHandler());
                } else {
                    Toast.makeText(context, "Not updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alert.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.

            }
        });

        alert.show();
    }
    //endregion

    private void refreshData() throws JSONException {

        url = ApiEndPoints.getInstance().getPostsById;
        RequestParams params = new RequestParams();
        params.put("user_id", user_id);
        WebReq.post(context, url, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.d(AppActivity.LOG, response.toString());
                    feedModels = new ArrayList<>();
                    commentsModels = new ArrayList<>();
                    int status = response.getInt("status");
                    if (status == Utils.STATUS) {
                        JSONArray postsArray = response.getJSONArray("posts");
                        JSONArray commentsArray;
                        JSONObject itemPost, itemComment;
                        for (int i = 0; i < postsArray.length(); i++) {


                            itemPost = postsArray.getJSONObject(i);
                            commentsArray = itemPost.getJSONArray("comments");

                            for (int j = 0; j < commentsArray.length(); j++) {

                                itemComment = commentsArray.getJSONObject(j);
                                commentsModels.add(new CommentsModel(itemComment.getString("post_id"), itemComment.getString("comment"), itemComment.getString("user_id"), itemComment.getString("name"), Utils.IMAGE + itemComment.getString("image")));
                            }
                            feedModels.add(new FeedModel(itemPost.getString("id"), itemPost.getString("user_id"), Utils.IMAGE + itemPost.getString("post_image"), itemPost.getString("created_on"), Utils.IMAGE + itemPost.getString("user_image"), itemPost.getString("uploaded_by"), itemPost.getString("islike"), itemPost.getInt("likes_count"), itemPost.getInt("comments_count"), commentsModels));
                        }
                        PortfolioRecyclerView portfolioRecyclerView = new PortfolioRecyclerView(feedModels);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(portfolioRecyclerView);
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getPath(Uri uri, Activity activity) {

        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void requestUpdatePhoto() throws JSONException {

        RequestParams params = new RequestParams();
        params.put("user_id", user_id);
        if (uploadLogo) {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 50, out);
                byte[] myByteArray = out.toByteArray();
                params.put("image", new ByteArrayInputStream(myByteArray), "newlogo.png");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (UPDATEPHOTOORCOVER == 1)
                WebReq.post(context, ApiEndPoints.getInstance().updateProfilePicture, params, new MyTextHttpResponseHandler());
            else if (UPDATEPHOTOORCOVER == 2)
                WebReq.post(context, ApiEndPoints.getInstance().updateProfileCover, params, new MyTextHttpResponseHandler());

        }
        //endregion
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    //endregion
    //region SERVER
    class MyTextHttpResponseHandler extends JsonHttpResponseHandler {
        MyTextHttpResponseHandler() {
        }

        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject mResponse) {
            getProfile();
        }

        @Override
        public void onFinish() {
            super.onFinish();
        }


        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d(AppActivity.LOG, responseString + " " + throwable);
        }


    }

    public class ResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            try {
                int status = response.getInt("status");
                if (status == Utils.STATUS) {
                    Log.d(AppActivity.LOG, response.toString());
                    JSONObject user = response.getJSONObject("object");
                    Picasso.with(context).load(Utils.IMAGE + user.getString("image")).into(profileImageView);
                    Picasso.with(context).load(Utils.IMAGE + user.getString("cover_photo")).into(coverImageView);
                    followTV.setText(response.getInt("followers") + " Followers");

                    current_id = user.getString("id");
                    if (VISITOR == 0) {
                        if (user.getString("description").length() > 0)
                            textViews.get(0).setText(user.getString("description"));
                        if (user.getString("occupation").length() > 0)
                            textViews.get(1).setText(user.getString("occupation"));
                        if (user.getString("city").length() > 0)
                            textViews.get(2).setText(user.getString("city"));
                        if (user.getString("facebook").length() > 0)
                            textViews.get(3).setText(user.getString("facebook"));
                        if (user.getString("twitter").length() > 0)
                            textViews.get(4).setText(user.getString("twitter"));
                        if (user.getString("email").length() > 0)
                            textViews.get(5).setText(user.getString("email"));
                    } else {
                        if (user.getString("description").length() > 0)
                            textViews.get(0).setText(user.getString("description"));
                        else
                            textViews.get(0).setText("");
                        if (user.getString("occupation").length() > 0)
                            textViews.get(1).setText(user.getString("occupation"));
                        else
                            textViews.get(1).setText("");
                        if (user.getString("city").length() > 0)
                            textViews.get(2).setText(user.getString("city"));
                        else
                            textViews.get(2).setText("");
                        if (user.getString("twitter").length() > 0)
                            textViews.get(4).setText(user.getString("twitter"));
                        else
                            textViews.get(4).setText("");
                        if (user.getString("facebook").length() > 0)
                            textViews.get(3).setText(user.getString("facebook"));
                        else
                            textViews.get(3).setText("");
                        if (user.getString("email").length() > 0)
                            textViews.get(5).setText(user.getString("email"));
                        else
                            textViews.get(5).setText("");
                    }

                    if (Objects.equals(user.getString("active"), "1")) {
                        textViews.get(6).setText("Online");
                    } else {
                        textViews.get(6).setText("Offline");
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onStart() {
            super.onStart();
            Toast.makeText(context, "Updating", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFinish() {
            super.onFinish();
        }
    }
}