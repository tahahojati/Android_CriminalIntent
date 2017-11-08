package com.bignerdranch.android.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class CrimePhotoDetail extends DialogFragment {
    private static final String PATH_ARG = "path";
    private Uri mPhotoUri;
    private ImageView mImageView ;
    public static CrimePhotoDetail newInstance(String photoPath) {
        CrimePhotoDetail fragment = new CrimePhotoDetail();
        Bundle args = new Bundle();
        args.putString(PATH_ARG, photoPath);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View v = li.inflate(R.layout.fragment_crime_photo_detail, null);
        mImageView = v.findViewById(R.id.zoomed_crime_photo);
        String path = getArguments().getString(PATH_ARG);
        Bitmap bm = PictureUtils.getScaledBitmap(path, getActivity());
        mImageView.setImageBitmap(bm);
        AlertDialog ad =new  AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.detail_crime_photo_title)
                .create();
        return ad;
    }
}
