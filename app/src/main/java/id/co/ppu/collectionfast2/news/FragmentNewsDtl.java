package id.co.ppu.collectionfast2.news;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.ppu.collectionfast2.R;
import id.co.ppu.collectionfast2.pojo.news.TrnNews;
import id.co.ppu.collectionfast2.util.NewsUtil;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentNewsDtl#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentNewsDtl extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "news.uid";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String uid;
//    private String mParam2;

    private Realm realm;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.wvNews)
    WebView wvNews;

    public FragmentNewsDtl() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment FragmentNewsDtl.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentNewsDtl newInstance(String param1) {
        FragmentNewsDtl fragment = new FragmentNewsDtl();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uid = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_dtl, container, false);

        ButterKnife.bind(this, view);

//        wvNews.getSettings().setJavaScriptEnabled(true);
//        wvNews.getSettings().setUseWideViewPort(true);
//        wvNews.getSettings().setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            wvNews.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        }

        return view;
    }

    @OnClick(R.id.dismiss)
    public void OnClickDismiss(View v) {
        dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        this.realm = Realm.getDefaultInstance();

        TrnNews first = this.realm.where(TrnNews.class).equalTo("uid", this.uid).findFirst();

        getDialog().setTitle(first.getSender());
        tvTitle.setText(first.getTitle());



        if (first.getMessageType().equals(NewsUtil.MESSAGE_TYPE_TEXT))
            wvNews.loadDataWithBaseURL("", first.getMessage(), "text/html; charset=utf-8", "UTF-8", "");
        else if (first.getMessageType().equals(NewsUtil.MESSAGE_TYPE_WEB))
            wvNews.loadData(first.getMessage(), "text/html", null);

    }

    @Override
    public void onStop() {
        super.onStop();

        if (this.realm != null) {
            this.realm.close();
            this.realm = null;
        }

    }
}

