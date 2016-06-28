package com.afra55.baseclient.base;

import android.net.Uri;

/**
 * Created by Victor Yang on 2016/6/28.
 * This interface must be implemented by activities that contain this
 * fragment to allow an interaction in this fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 * <p/>
 * See the Android Training lesson <a href=
 * "http://developer.android.com/training/basics/fragments/communicating.html"
 * >Communicating with Other Fragments</a> for more information.
 */
public interface OnFragmentInteractionListener {
    void onFragmentInteraction(Uri uri);

    void onFragmentInteraction(String message);
}
