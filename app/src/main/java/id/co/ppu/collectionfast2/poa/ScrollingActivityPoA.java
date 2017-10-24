package id.co.ppu.collectionfast2.poa;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import id.co.ppu.collectionfast2.R;
import id.co.ppu.collectionfast2.component.BasicActivity;
import id.co.ppu.collectionfast2.pojo.trn.TrnCollectAddr;
import id.co.ppu.collectionfast2.util.DataUtil;
import id.co.ppu.collectionfast2.util.NetUtil;
import id.co.ppu.collectionfast2.util.PoAUtil;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class ScrollingActivityPoA extends BasicActivity {
    public static final String PARAM_LKP_DETAIL = "lkp.detail";
    public static final String PARAM_COLLECTOR_ID = "collector.id";
    public static final String PARAM_CUSTOMER_NAME = "customer.name";
    //    public static final String PARAM_CUSTOMER_ADDR = "customer.address";
    public static final String PARAM_CONTRACT_NO = "customer.contractNo";
    public static final String PARAM_LDV_NO = "ldvNo";

    private String lkpDetail = null;
    private String collCode = null;
    private String contractNo = null;
    //    private String custAddr = null;
    private String custName = null;

    private String ldvNo = null;

    private int seqAddressCounter = 0;

    @BindView(R.id.ivCamera)
    ImageView ivCamera;
    @BindView(R.id.pulsator)
    PulsatorLayout pulsator;

    @BindView(R.id.tvAddressDetail)
    TextView tvAddressDetail;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Animation animZoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        ivCamera.startAnimation(animZoomIn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_poa);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleAdress();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showMap();
                return true;
            }
        });

        tvAddressDetail.setAutoLinkMask(Linkify.PHONE_NUMBERS);
        tvAddressDetail.setLinksClickable(true);

//        Drawable drawableTaskLog = menu.findItem(R.id.action_clear_chats).getIcon();
        Drawable drawableTaskLog = DrawableCompat.wrap(fab.getDrawable());
        DrawableCompat.setTint(drawableTaskLog, ContextCompat.getColor(this, android.R.color.white));
        fab.setImageDrawable(drawableTaskLog);

//        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
//        params.setBehavior(new AppBarLayout.Behavior());
//        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
//        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
//            @Override
//            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
//                return false;
//            }
//        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.lkpDetail = extras.getString(PARAM_LKP_DETAIL);        // bisa kosong di paymententri
            this.collCode = extras.getString(PARAM_COLLECTOR_ID);
            this.contractNo = extras.getString(PARAM_CONTRACT_NO);  // bisa kosong di paymententry
            this.custName = extras.getString(PARAM_CUSTOMER_NAME);
//            this.custAddr = extras.getString(PARAM_CUSTOMER_ADDR);
            this.ldvNo = extras.getString(PARAM_LDV_NO);

//            setTitle(null);
            setTitle(custName);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setSubtitle(this.contractNo);
            getSupportActionBar().setElevation(0);

        }

//        Typeface font = Typeface.createFromAsset(getAssets(), Utility.FONT_SAMSUNG);
//        tvAddressDetail.setTypeface(font);

//        tvAddressDetail.performLongClick();
        fab.performClick();

        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        fadeInAnimation.setDuration(1000);
        tvAddressDetail.startAnimation(fadeInAnimation);

        pulsator.start();
    }

    private void showMap() {
        if (!NetUtil.isConnected(this)) {
            Toast.makeText(this, getString(R.string.error_offline), Toast.LENGTH_SHORT).show();
            return;
        }

        TrnCollectAddr trnCollectAddr = this.realm.where(TrnCollectAddr.class)
                .equalTo("pk.contractNo", this.contractNo)
                .equalTo("pk.seqNo", seqAddressCounter)
                .findFirst();

        if (trnCollectAddr == null)
            return;

        String parseAddr = trnCollectAddr.getCollAddr().toUpperCase()
                .replace("NOMOR ", "")
                .replace("NO ", "")
                .replace("JALAN ", "")
                .replace("JLN ", "")
                .replace("JL ", "")
                .replace("BLOK ", "")
                .replaceAll("[0-9()?:!.,;{}]+", "")
                ;
        // hilangkan nomor, jalan, jln, no, no.

        String map = "http://maps.google.co.in/maps?q=" + parseAddr + " " + trnCollectAddr.getCollKec();
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
        startActivity(i);
    }

    private void toggleAdress() {
        seqAddressCounter += 1;

        if (seqAddressCounter > 2)
            seqAddressCounter = 1;

        TrnCollectAddr trnCollectAddr = this.realm.where(TrnCollectAddr.class)
                .equalTo("pk.contractNo", this.contractNo)
                .equalTo("pk.seqNo", seqAddressCounter)
                .findFirst();

        if (trnCollectAddr != null) {

            String added = "";
            if (seqAddressCounter == 1)
                added = getString(R.string.label_home);
            else if (seqAddressCounter == 2)
                added = getString(R.string.label_office);


            tvAddressDetail.setText("<" + added + ">\n" + DataUtil.prettyAddress(trnCollectAddr));
        }
    }

//    @OnClick(R.id.tvAddressDetail)
//    public void onClickAddress() {
////        toggleAdress();
//        showMap();
//    }

    @OnLongClick(R.id.tvAddressDetail)
    public boolean onLongClickAddress() {
        showMap();

        return true;
    }

    @OnClick(R.id.llTakePhoto)
    public void onTakePoA() {

        try {
            PoAUtil.callCameraIntent(this, collCode, ldvNo, contractNo);
        } catch (Exception e) {
            e.printStackTrace();

            StringBuffer message2 = new StringBuffer("ldvNo=");
            message2.append(ldvNo);
            message2.append("contractNo=").append(contractNo);

            NetUtil.syncLogError(getBaseContext(), realm, collCode, "Pra PoA", e.getMessage(), message2.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        pulsator.stop();

        Intent dataReturn = new Intent();
        dataReturn.putExtra(PARAM_LKP_DETAIL, lkpDetail);

        try {
            PoAUtil.postCameraIntoCache(this, collCode, contractNo);// /storage/emulated/0/RadanaCache/cache/poaDefault_demo_71000000008115.jpg

            setResult(RESULT_OK, dataReturn);

        } catch (Exception e) {
            e.printStackTrace();

            StringBuffer message2 = new StringBuffer("ldvNo=");
            message2.append(ldvNo);
            message2.append("contractNo=").append(contractNo);

            NetUtil.syncLogError(getBaseContext(), realm, collCode, "Post PoA", e.getMessage(), message2.toString());
        }

        finish();
    }

}
