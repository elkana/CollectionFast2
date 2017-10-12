package id.co.ppu.collectionfast2.test;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ppu.collectionfast2.R;
import id.co.ppu.collectionfast2.component.BasicActivity;
import id.co.ppu.collectionfast2.util.Utility;
import io.realm.Realm;
import io.realm.RealmModel;

public class ActivityDeveloper extends BasicActivity {

    @BindView(R.id.table)
    TableLayout tableLayout;

    private StringBuilder allStrings = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);

        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        // clear table
        tableLayout.removeAllViews();
        // create header
        TableRow row_header = (TableRow) LayoutInflater.from(this).inflate(R.layout.row_developer, null);

        setTableHeader((TextView) ButterKnife.findById(row_header, R.id.attrib_no), "No.");
        setTableHeader((TextView) ButterKnife.findById(row_header, R.id.attrib_key), "Key");
        setTableHeader((TextView) ButterKnife.findById(row_header, R.id.attrib_value), "Value");

        tableLayout.addView(row_header);

        way2();
/*
        new AsyncTask<Void, Void, Void>() {
            final ProgressDialog mProgressDialog = new ProgressDialog(ActivityDeveloper.this);

            @Override
            protected void onPreExecute() {
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);
                mProgressDialog.setMessage("Please wait...");
                mProgressDialog.show();

                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                Realm r = Realm.getDefaultInstance();
                try{

                    int rowCount = 0;

                    List<Pair<String, String>> listMaster = new ArrayList<>();
                    List<Pair<String, String>> listTrn = new ArrayList<>();
                    List<Pair<String, String>> listSync = new ArrayList<>();
                    List<Pair<String, String>> listOthers = new ArrayList<>();

                    Set<Class<? extends RealmModel>> tables = r.getConfiguration().getRealmObjectClasses();
                    for (Class<? extends RealmModel> table : tables) {
                        String key = table.getSimpleName();
                        long count = r.where(table).count();

                        Pair<String, String> p = Pair.create(key, "" + count);

                        if (key.toLowerCase().startsWith("mst")) {
                            listMaster.add(p);
                        }else
                        if (key.toLowerCase().startsWith("trn")) {
                            listTrn.add(p);
                        }else
                        if (key.toLowerCase().startsWith("sync")) {
                            listSync.add(p);
                        }else
                            listOthers.add(p);
                    }

                    displayRows(listMaster);
                    displayRows(listTrn);
                    displayRows(listSync);
                    displayRows(listOthers);

                }finally {
                    if (r != null)
                        r.close();

                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }

                }
            }
        }.execute();
        */
    }

    private void way2() {

        final ProgressDialog mProgressDialog = Utility.createAndShowProgressDialog(this, getString(R.string.message_please_wait));

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {

                List<Pair<String, String>> listMaster = new ArrayList<>();
                List<Pair<String, String>> listTrn = new ArrayList<>();
                List<Pair<String, String>> listSync = new ArrayList<>();
                List<Pair<String, String>> listOthers = new ArrayList<>();

                Set<Class<? extends RealmModel>> tables = realm.getConfiguration().getRealmObjectClasses();
                for (Class<? extends RealmModel> table : tables) {
                    String key = table.getSimpleName();
                    long count = realm.where(table).count();

                    Pair<String, String> p = Pair.create(key, "" + count);

                    if (key.toLowerCase().startsWith("mst")) {
                        listMaster.add(p);
                    }else
                    if (key.toLowerCase().startsWith("trn")) {
                        listTrn.add(p);
                    }else
                    if (key.toLowerCase().startsWith("sync")) {
                        listSync.add(p);
                    }else
                        listOthers.add(p);
                }

                int rowCount = tables.size();

                displayRows(listMaster);
                displayRows(listTrn);
                displayRows(listSync);
                displayRows(listOthers);

                List<Pair<String, String>> listPref = new ArrayList<>();

                /*
                Map<String, ?> allEntries = Storage.getSharedPreferences(getApplicationContext()).getAll();

                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    Pair<String, String> p = Pair.create(entry.getKey(), "" + entry.getValue().toString());

                    listPref.add(p);
                }
                */

                Pair<String, String> pScreenRes = Pair.create("screen.density", Utility.getDeviceResolution(ActivityDeveloper.this));
                listPref.add(pScreenRes);

                displayRows(listPref);

                List<Pair<String, String>> listKeyVal = new ArrayList<>();
                String sysInfoCsv = Utility.buildSysInfoAsCsv(ActivityDeveloper.this);
                String[] split = sysInfoCsv.split(",", -1);
                for (String s : split) {
                    String[] _s = s.split("=", -1);
                    Pair<String, String> p = Pair.create(_s[0], _s[1]);

                    listKeyVal.add(p);
                }

                displayRows(listKeyVal);

                Utility.dismissDialog(mProgressDialog);

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Utility.dismissDialog(mProgressDialog);
            }
        });

    }

    private Pair<String, String> createPair(String key, String value) {
        Pair<String, String> p = Pair.create(key, value);
        return p;
    }

    private void setTableHeader(TextView textView, String text) {
        textView.setText(text);
        if (Build.VERSION.SDK_INT < 23) {
            textView.setTextAppearance(this, android.R.style.TextAppearance_Small);
        } else {
            textView.setTextAppearance(android.R.style.TextAppearance_Small);
        }
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setBackgroundColor(Color.LTGRAY);

    }

    private void displayRows(List<Pair<String, String>> list) {
        for (int i = 0; i < list.size(); i++) {
            TableRow row = (TableRow) LayoutInflater.from(ActivityDeveloper.this).inflate(R.layout.row_developer, null);

            ((TextView) row.findViewById(R.id.attrib_key)).setText( list.get(i).first);
            ((TextView) row.findViewById(R.id.attrib_value)).setText( list.get(i).second);

            ((TextView) row.findViewById(R.id.attrib_no)).setText("" + (i+1));

            tableLayout.addView(row);

            allStrings.append(list.get(i).first).append(": ").append(list.get(i).second).append("\n");

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_dev, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_copy_clipboard) {
            setClipboard(this, allStrings.toString());
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }
}
