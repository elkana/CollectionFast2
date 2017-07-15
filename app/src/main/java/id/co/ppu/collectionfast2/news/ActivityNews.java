package id.co.ppu.collectionfast2.news;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import id.co.ppu.collectionfast2.R;
import id.co.ppu.collectionfast2.component.BasicActivity;
import id.co.ppu.collectionfast2.pojo.news.TrnNews;
import id.co.ppu.collectionfast2.util.NewsUtil;
import id.co.ppu.collectionfast2.util.Storage;
import id.co.ppu.collectionfast2.util.Utility;
import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;
import io.realm.Sort;

public class ActivityNews extends BasicActivity {

    @BindView(R.id.news)
    public RealmRecyclerView news;

    public NewsAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ButterKnife.bind(this);

        setTitle("News");

        // generate dummy news for testing
        if (Utility.DEVELOPER_MODE) {

            if (this.realm.where(TrnNews.class).count() < 1) {

                this.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        for (int i = 0; i < 10; i++) {
                            TrnNews e = realm.createObject(TrnNews.class);

                            e.setUid(java.util.UUID.randomUUID().toString());
                            e.setSender("HQ");
                            e.setTo("you");
                            e.setExcerpt("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
                            e.setMessage("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
                            e.setTitle("News #" + i);
                            e.setMessageType(NewsUtil.MESSAGE_TYPE_TEXT);

                            e.setMessageStatus(NewsUtil.MESSAGE_STATUS_UNOPENED_OR_FIRSTTIME);
                            if (i == 0) {
                                e.setMessageStatus(NewsUtil.MESSAGE_STATUS_OPENED);
                            }

                            e.setCreatedTimestamp(new Date());

                            realm.copyToRealm(e);
                        }

                    }
                });

//                this.realm.where(TrnNews.class).findAll().deleteAllFromRealm();

            }

        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
//            getSupportActionBar().setSubtitle(this.contractNo);
            getSupportActionBar().setElevation(0);

        }

        if (listAdapter == null) {
            RealmResults<TrnNews> realmResults = this.realm.where(TrnNews.class)
                    /*
                    .beginGroup()
                    .equalTo("fromCollCode", user1)
                    .equalTo("toCollCode", user2)
                    .endGroup()
                    .or()
                    .beginGroup()
                    .equalTo("fromCollCode", user2)
                    .equalTo("toCollCode", user1)
                    .endGroup();
                    */
                    .findAllSorted("createdTimestamp", Sort.DESCENDING);

            listAdapter = new NewsAdapter(this, realmResults, true, true);
        }

        news.setAdapter(listAdapter);
    }

    public class NewsAdapter extends RealmBasedRecyclerViewAdapter<TrnNews, RealmViewHolder> {

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;

        public NewsAdapter(@NonNull Context context, RealmResults<TrnNews> realmResults, boolean automaticUpdate,
                           boolean animateResults) {
            super(context, realmResults, automaticUpdate, animateResults);
        }

        @Override
        public int getItemViewType(int position) {
            final TrnNews detail = realmResults.get(position);

            if (detail.getMessageType() == null || detail.getMessageType().equals("0"))
                return TYPE_ITEM;
            else if (detail.getMessageType().equals("1"))
                return TYPE_HEADER;

            return TYPE_ITEM;
        }

        @Override
        public RealmViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
            if (viewType == TYPE_HEADER) {
                View v = inflater.inflate(R.layout.row_news_header, viewGroup, false);
                return new VHHeader(v);

            } else if (viewType == TYPE_ITEM) {
                View v = inflater.inflate(R.layout.row_news_list, viewGroup, false);
                return new DataViewHolder((FrameLayout) v);

            }
            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
        }

        @Override
        public void onBindRealmViewHolder(RealmViewHolder rvHolder, int position) {
            final TrnNews detail = realmResults.get(position);

            if (!detail.isValid()) {
                return;
            }

            if (rvHolder instanceof VHHeader) {
                VHHeader holder = (VHHeader) rvHolder;
//                holder.txtTitle.setText(Utility.convertDateToString(detail.getCreatedTimestamp(), "EEE, d MMM yyyy"));
                holder.txtTitle.setText(detail.getMessage());

            } else if (rvHolder instanceof DataViewHolder) {
                DataViewHolder dataViewHolder = (DataViewHolder) rvHolder;

                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) dataViewHolder.llRowMsg.getLayoutParams();

                layoutParams.gravity = Gravity.START;
                dataViewHolder.llRowMsg.setLayoutParams(layoutParams);

                dataViewHolder.llRowMsg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // update as read
                        Realm r1 = Realm.getDefaultInstance();
                        try {
                            TrnNews _trnNews = r1.where(TrnNews.class)
                                    .equalTo("uid", detail.getUid())
                                    .findFirst();

                            if (_trnNews != null) {
                                r1.beginTransaction();

                                _trnNews.setMessageStatus(NewsUtil.MESSAGE_STATUS_OPENED);

                                r1.copyToRealmOrUpdate(_trnNews);
                                r1.commitTransaction();
                            }
                        } finally {
                            if (r1 != null) {
                                r1.close();
                            }

                            listAdapter.notifyDataSetChanged();
                        }

//                        Toast.makeText(getApplicationContext(), detail.getMessage(), Toast.LENGTH_LONG).show();
                        FragmentNewsDtl dialog = FragmentNewsDtl.newInstance(detail.getUid());
                        dialog.show(getSupportFragmentManager(), "News Fragment");
                    }
                });

                /*
                if (detail.getFromCollCode().equals(userCode1)) {
                    layoutParams.gravity = Gravity.END;

                    dataViewHolder.llRowMsg.setBackground(ContextCompat.getDrawable(getContext(), R.mipmap.bubble2));
                } else {
                    dataViewHolder.llRowMsgtr.setLayoutParams(layoutParams);

                    dataViewHolder.llRowMsg.setBackground(ContextCompat.getDrawable(getContext(), R.mipmap.bubble1));
                }*/

                TextView tvTitle = dataViewHolder.tvTitle;
                tvTitle.setText(detail.getTitle());

                TextView tvMsg = dataViewHolder.tvMsg;
                tvMsg.setText(detail.getExcerpt());

                if (detail.getMessageStatus().equals(NewsUtil.MESSAGE_STATUS_OPENED)) {
                    tvMsg.setTypeface(Typeface.DEFAULT);
                } else {
                    tvMsg.setTypeface(Typeface.DEFAULT_BOLD);

                }

                TextView tvTime = dataViewHolder.tvTime;

                PrettyTime pt = new PrettyTime(new Locale(Storage.getLanguageId(getBaseContext())));

                String fixIndonesia = pt.format(detail.getCreatedTimestamp());
                if (fixIndonesia.toLowerCase().equals("yang lalu"))
                    fixIndonesia = "baru";

                tvTime.setText(fixIndonesia);
//                tvTime.setText(Utility.convertDateToString(detail.getCreatedTimestamp(), "HH:mm"));

                TextView tvFrom = dataViewHolder.tvFrom;
                tvFrom.setText(getString(R.string.news_from) + ": " + detail.getSender().toUpperCase());
//                tvStatus.setText(detail.getMessageStatus());
//                tvFrom.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

                /*
                if (Utility.DEVELOPER_MODE && detail.getFromCollCode().equals(userCode1)) {

                    int idIcon;
                    if (detail.getMessageStatus().equals(ConstChat.MESSAGE_STATUS_UNOPENED_OR_FIRSTTIME)
                            || detail.getMessageStatus().equals(ConstChat.MESSAGE_STATUS_TRANSMITTING))
                        idIcon = R.mipmap.chat0;
                    else if (detail.getMessageStatus().equals(ConstChat.MESSAGE_STATUS_SERVER_RECEIVED))
                        idIcon = R.mipmap.chat1;
                    else if (detail.getMessageStatus().equals(ConstChat.MESSAGE_STATUS_DELIVERED))
                        idIcon = R.mipmap.chat2;
                    else if (detail.getMessageStatus().equals(ConstChat.MESSAGE_STATUS_FAILED))
                        idIcon = R.mipmap.chatfailed;
                    else // if (detail.getMessageStatus().equals(ConstChat.MESSAGE_STATUS_READ_AND_OPENED))
                        idIcon = R.mipmap.chat3;

//                    Drawable cekIcon = ContextCompat.getDrawable(getContext(), idIcon);
//                    tvStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, cekIcon, null);
                }
*/
            }
        }

        public class DataViewHolder extends RealmViewHolder {

            public FrameLayout container;

            @BindView(R.id.llRowMsg)
            LinearLayout llRowMsg;

            @BindView(R.id.tvMsg)
            TextView tvMsg;

            @BindView(R.id.tvTitle)
            TextView tvTitle;

            @BindView(R.id.tvTime)
            TextView tvTime;

            @BindView(R.id.tvFrom)
            TextView tvFrom;

            public DataViewHolder(FrameLayout container) {
                super(container);

                this.container = container;
                ButterKnife.bind(this, container);

                Typeface font = Typeface.createFromAsset(getAssets(), Utility.FONT_SAMSUNG_BOLD);
                tvTitle.setTypeface(font);

            }

        }

        class VHHeader extends RealmViewHolder {
            TextView txtTitle;

            public VHHeader(View itemView) {
                super(itemView);
                this.txtTitle = (TextView) itemView.findViewById(R.id.txtHeader);
            }
        }

    }

}
