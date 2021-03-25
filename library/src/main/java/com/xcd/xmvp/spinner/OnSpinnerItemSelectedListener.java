package com.xcd.xmvp.spinner;

import android.view.View;

public interface OnSpinnerItemSelectedListener {
    void onItemSelected(NiceSpinner parent, View view, int position, long id);
}
