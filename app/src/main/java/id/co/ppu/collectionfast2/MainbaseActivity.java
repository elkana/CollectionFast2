package id.co.ppu.collectionfast2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ppu.collectionfast2.component.BasicActivity;
import id.co.ppu.collectionfast2.listener.OnApproveListener;
import id.co.ppu.collectionfast2.util.Storage;
import id.co.ppu.collectionfast2.util.Utility;

/**
 * Created by Eric on 30-Dec-16.
 */

public abstract class MainbaseActivity extends BasicActivity {

    @BindView(R.id.fab)
    public FloatingActionButton fab;

    @BindView(R.id.coordinatorLayout)
    public View coordinatorLayout;

    @BindView(R.id.nav_view)
    public NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    public DrawerLayout drawer;

    protected abstract void changeFabIcon(int resId);
    protected abstract void displayView(int viewId);


    protected void confirmPassword(final OnApproveListener listener) {
        View promptsView = LayoutInflater.from(this).inflate(R.layout.dialog_pwd, null);
        final EditText input = ButterKnife.findById(promptsView, R.id.password);

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_cancel))
                .setMessage(getString(R.string.prompt_your_password))
                .setView(promptsView)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Utility.hideKeyboard(input);

                        String value = input.getText().toString();
                        final String userPwd = Storage.getPref(Storage.KEY_PASSWORD, null);
                        if (!value.equals(userPwd)) {
                            Snackbar.make(coordinatorLayout, getString(R.string.error_incorrect_password), Snackbar.LENGTH_LONG).show();
                            return;
                        }

                        if (listener != null)
                            listener.onApprove();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Utility.hideKeyboard(input);
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Utility.hideKeyboard(input);
                    }
                })
                .show();

    }


}
