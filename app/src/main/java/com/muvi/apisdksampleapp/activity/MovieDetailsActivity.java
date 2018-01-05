package com.muvi.apisdksampleapp.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;

import com.muvi.apisdksampleapp.R;
import com.muvi.apisdksampleapp.network.NetworkStatus;
import com.muvi.apisdksampleapp.preferences.LanguagePreference;
import com.muvi.apisdksampleapp.preferences.PreferenceManager;
import com.muvi.apisdksampleapp.util.Util;
import com.release.muvisdk.api.apiController.AddToFavAsync;
import com.release.muvisdk.api.apiController.GetContentDetailsAsynTask;
import com.release.muvisdk.api.apiController.GetIpAddressAsynTask;
import com.release.muvisdk.api.apiController.GetValidateUserAsynTask;
import com.release.muvisdk.api.apiController.VideoDetailsAsynctask;
import com.release.muvisdk.api.apiController.ViewContentRatingAsynTask;
import com.release.muvisdk.api.apiModel.APVModel;
import com.release.muvisdk.api.apiModel.ContentDetailsInput;
import com.release.muvisdk.api.apiModel.ContentDetailsOutput;
import com.release.muvisdk.api.apiModel.CurrencyModel;
import com.release.muvisdk.api.apiModel.GetVideoDetailsInput;
import com.release.muvisdk.api.apiModel.PPVModel;
import com.release.muvisdk.api.apiModel.Video_Details_Output;
import com.release.muvisdk.player.activity.Player;
import com.release.muvisdk.player.activity.PlayerActivity;
import com.release.muvisdk.player.model.DataModel;
import com.release.muvisdk.player.utils.ProgressBarHandler;
import com.release.muvisdk.player.utils.ResizableCustomView;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


import static com.muvi.apisdksampleapp.preferences.LanguagePreference.ADVANCE_PURCHASE;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_ADVANCE_PURCHASE;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_PLAN_ID;
import static com.muvi.apisdksampleapp.util.Constant.PERMALINK_INTENT_KEY;
import static com.muvi.apisdksampleapp.util.Constant.authTokenStr;
import static com.release.muvisdk.api.apiModel.CommonConstants.PLAN_ID;
import static com.release.muvisdk.player.utils.Util.DEFAULT_IS_CHROMECAST;
import static com.release.muvisdk.player.utils.Util.DEFAULT_NO_CONTENT;
import static com.release.muvisdk.player.utils.Util.DEFAULT_NO_DATA;
import static com.release.muvisdk.player.utils.Util.DEFAULT_NO_INTERNET_CONNECTION;
import static com.release.muvisdk.player.utils.Util.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.release.muvisdk.player.utils.Util.DEFAULT_VIEW_MORE;
import static com.release.muvisdk.player.utils.Util.IS_CHROMECAST;
import static com.release.muvisdk.player.utils.Util.NO_CONTENT;
import static com.release.muvisdk.player.utils.Util.NO_DATA;
import static com.release.muvisdk.player.utils.Util.NO_INTERNET_CONNECTION;
import static com.release.muvisdk.player.utils.Util.SELECTED_LANGUAGE_CODE;
import static com.release.muvisdk.player.utils.Util.VIEW_MORE;


public class MovieDetailsActivity extends AppCompatActivity implements VideoDetailsAsynctask.VideoDetailsListener,
        GetContentDetailsAsynTask.GetContentDetailsListener,
        GetIpAddressAsynTask.IpAddressListener {
    int prevPosition = 0;
    PreferenceManager preferenceManager;
    private static final int MAX_LINES = 3;
    ProgressBarHandler pDialog;
    int ratingAddedByUser = 1;
    String ipAddressStr = "";
    String filename = "";
    static File mediaStorageDir;
    ArrayList<String> SubTitleName = new ArrayList<>();
    ArrayList<String> SubTitlePath = new ArrayList<>();
    ArrayList<String> FakeSubTitlePath = new ArrayList<>();
    ArrayList<String> ResolutionFormat = new ArrayList<>();
    ArrayList<String> ResolutionUrl = new ArrayList<>();
    ArrayList<String> SubTitleLanguage = new ArrayList<>();


    VideoDetailsAsynctask asynLoadVideoUrls;
    GetValidateUserAsynTask asynValidateUserDetails;
    GetContentDetailsAsynTask asynLoadMovieDetails;
    ViewContentRatingAsynTask asynGetReviewDetails;
    String loggedInIdStr;
    AddToFavAsync asynFavoriteAdd;
    Toolbar mActionBarToolbar;
    ImageView moviePoster;
    PPVModel ppvmodel;
    APVModel advmodel;
    CurrencyModel currencymodel;
    ImageView playButton, favorite_view;
    String PlanId = "";
    ImageButton offlineImageButton;
    Button watchTrailerButton;
    Button preorderButton;
    int loginresultcode = 0;
    //for resume play_new
    String seek_status = "";
    int Played_Length = 0;
    String watch_status_String = "start";

    String default_Language = "";
    private boolean isThirdPartyTrailer = false;
    String Default_Language = "";
    String Previous_Selected_Language = "";

    RelativeLayout viewStoryLayout;

    //Add By Bibhu Later.
    TextView videoStoryTextView;
    Button storyViewMoreButton;

    boolean isExpanded = false;

    TextView videoTitle, videoGenreTextView, videoDurationTextView, videoCensorRatingTextView, videoCensorRatingTextView1,
            videoReleaseDateTextView, videoCastCrewTitleTextView;
    String movieNameStr;
    String movieTypeStr = "";
    boolean castStr = false;
    String censorRatingStr = "";
    String videoduration = "";
    String movieDetailsStr = "";
    String Video_Url = "";
    String movieThirdPartyUrl = "";


//     ///****rating****///

    RatingBar ratingBar;
    TextView viewRatingTextView;
    String movieIdStr;

    /*** rating***///
    String rating;
    String reviews;


    Intent DataIntent;
    String permalinkStr;
    String movieTrailerUrlStr, movieReleaseDateStr = "";
    String movieStreamUniqueId, bannerImageId, posterImageId, priceForUnsubscribedStr, priceFosubscribedStr, currencyIdStr, currencyCountryCodeStr,
            currencySymbolStr;
    String movieUniqueId = "", isEpisode = "";
    int isFreeContent, isPPV, isConverted, contentTypesId, isAPV;

    RelativeLayout noInternetConnectionLayout, noDataLayout, iconImageRelativeLayout, bannerImageRelativeLayout;
    LinearLayout story_layout;
    String sucessMsg;
    int isFavorite;
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    int isLogin = 0;
    TextView noDataTextView;
    TextView noInternetTextView;
    String email, id;
    AlertDialog alert;
    String isMemberSubscribed, loggedInStr;

    // Added For The Voucher

    int isVoucher = 0;
    String VoucherCode = "";

    TextView content_label, content_name, voucher_success;
    EditText voucher_code;
    Button apply, watch_now;
    boolean watch_status = false;
    String ContentName = "";
    AlertDialog voucher_alert;
    Player playerModel;
    // Video_Details_Output _video_details_output;
    LanguagePreference languagePreference;


    // voucher ends here //

    @Override
    protected void onResume() {

        super.onResume();

        GetIpAddressAsynTask asynGetIpAddress = new GetIpAddressAsynTask(this, this);
        asynGetIpAddress.executeOnExecutor(threadPoolExecutor);


        invalidateOptionsMenu();

    }







    /*chromecast-------------------------------------*/

    View view;


    public enum PlaybackLocation {
        LOCAL,
        REMOTE
    }

    /**
     * List of various states that we can be in
     */
    public enum PlaybackState {
        PLAYING, PAUSED, BUFFERING, IDLE
    }

    private PlaybackLocation mLocation;
    private PlaybackState mPlaybackState;
    private final float mAspectRatio = 72f / 128;
    private AQuery mAquery;
    private MediaInfo mSelectedMedia;

    private MenuItem mediaRouteMenuItem;
    private CastContext mCastContext;
    private SessionManagerListener<CastSession> mSessionManagerListener =
            new MySessionManagerListener();
    private CastSession mCastSession;

    private class MySessionManagerListener implements SessionManagerListener<CastSession> {

        @Override
        public void onSessionEnded(CastSession session, int error) {
            if (session == mCastSession) {
                mCastSession = null;
            }
            invalidateOptionsMenu();
        }

        @Override
        public void onSessionResumed(CastSession session, boolean wasSuspended) {
            mCastSession = session;
            invalidateOptionsMenu();
        }

        @Override
        public void onSessionStarted(CastSession session, String sessionId) {
            mCastSession = session;
            invalidateOptionsMenu();
        }

        @Override
        public void onSessionStarting(CastSession session) {
        }

        @Override
        public void onSessionStartFailed(CastSession session, int error) {
        }

        @Override
        public void onSessionEnding(CastSession session) {
        }

        @Override
        public void onSessionResuming(CastSession session, String sessionId) {
        }

        @Override
        public void onSessionResumeFailed(CastSession session, int error) {
        }

        @Override
        public void onSessionSuspended(CastSession session, int reason) {
        }
    }


    MediaInfo mediaInfo;

    /*chromecast-------------------------------------*/

    RelativeLayout relativeOverlayLayout;
    private BroadcastReceiver DELETE_ACTION = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String movieUniqId = intent.getStringExtra("movie_uniq_id").trim();
            if (movieUniqId.equals(movieUniqueId.trim())) {
                isFavorite = 0;
                favorite_view.setImageResource(R.drawable.favorite_unselected);
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.details_layout);
        playerModel = new Player();
        pDialog = new ProgressBarHandler(MovieDetailsActivity.this);

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(DELETE_ACTION, new IntentFilter("ITEM_STATUS"));
        // _video_details_output = new Video_Details_Output();
        languagePreference = LanguagePreference.getLanguagePreference(this);
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        moviePoster = (ImageView) findViewById(R.id.bannerImageView);
        playButton = (ImageView) findViewById(R.id.playButton);
        watchTrailerButton = (Button) findViewById(R.id.viewTrailerButton);
        preorderButton = (Button) findViewById(R.id.preOrderButton);
        favorite_view = (ImageView) findViewById(R.id.favorite_view);
        Typeface submitButtonTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.regular_fonts));
        watchTrailerButton.setTypeface(submitButtonTypeface);
        Typeface preorderButtonTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.regular_fonts));
        preorderButton.setTypeface(preorderButtonTypeface);
        preorderButton.setVisibility(View.GONE);

        offlineImageButton = (ImageButton) findViewById(R.id.offlineImageButton);
        videoTitle = (TextView) findViewById(R.id.videoTitle);
        videoGenreTextView = (TextView) findViewById(R.id.videoGenreTextView);
        videoDurationTextView = (TextView) findViewById(R.id.videoDurationTextView);
        videoCensorRatingTextView = (TextView) findViewById(R.id.videoCensorRatingTextView);
        videoCensorRatingTextView1 = (TextView) findViewById(R.id.videoCensorRatingTextView1);
        videoReleaseDateTextView = (TextView) findViewById(R.id.videoReleaseDateTextView);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        viewStoryLayout = (RelativeLayout) findViewById(R.id.viewStoryLayout);

        videoStoryTextView = (TextView) findViewById(R.id.videoStoryTextView);
        storyViewMoreButton = (Button) findViewById(R.id.storyViewMoreButton);


        videoCastCrewTitleTextView = (TextView) findViewById(R.id.videoCastCrewTitleTextView);
        videoCastCrewTitleTextView.setVisibility(View.GONE);


        // *** rating***////
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setFocusable(false);
        ratingBar.setVisibility(View.GONE);

        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        viewRatingTextView = (TextView) findViewById(R.id.viewRatingTextView);
        // loggedInStr = preferenceManager.getLoginStatusFromPref();
        //pref = getSharedPreferences(Util.LOGIN_PREF, 0);

        relativeOverlayLayout = (RelativeLayout) findViewById(R.id.relativeOverlayLayout);

        noInternetConnectionLayout = (RelativeLayout) findViewById(R.id.noInternet);
        noDataLayout = (RelativeLayout) findViewById(R.id.noData);
        noInternetTextView = (TextView) findViewById(R.id.noInternetTextView);
        noDataTextView = (TextView) findViewById(R.id.noDataTextView);
        noInternetTextView.setText(languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));
        noDataTextView.setText(languagePreference.getTextofLanguage(NO_CONTENT, DEFAULT_NO_CONTENT));

        iconImageRelativeLayout = (RelativeLayout) findViewById(R.id.iconImageRelativeLayout);
        bannerImageRelativeLayout = (RelativeLayout) findViewById(R.id.bannerImageRelativeLayout);
        story_layout = (LinearLayout) findViewById(R.id.story_layout);

        preferenceManager = PreferenceManager.getPreferenceManager(this);

        permalinkStr = getIntent().getStringExtra(PERMALINK_INTENT_KEY);
        // isLogin = ((Global) getApplicationContext()).getIsLogin();

        isLogin = preferenceManager.getLoginFeatureFromPref();

        ppvmodel = new PPVModel();
        advmodel = new APVModel();
        currencymodel = new CurrencyModel();
        PlanId = (languagePreference.getTextofLanguage(PLAN_ID, DEFAULT_PLAN_ID)).trim();


        /***favorite *****/

        favorite_view.setVisibility(View.GONE);


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                VodApplication.getCastCrewDetails("77488");
//
                //playermodel set data
// *****************set data into playermdel for play_new in exoplayer************

                playerModel.setAppName(getResources().getString(R.string.app_name));
                playerModel.setStreamUniqueId(movieStreamUniqueId);
                playerModel.setMovieUniqueId(movieUniqueId);
              /*  playerModel.setUserId(preferenceManager.getUseridFromPref());
                playerModel.setEmailId(preferenceManager.getEmailIdFromPref());
                playerModel.setAuthToken(authTokenStr.trim());*/


                playerModel.setUserId("151404");
                playerModel.setEmailId("bb@gmail.com");
                playerModel.setAuthToken("25e74a5c88d19c4b57c8138bf47abdf7");

                playerModel.setRootUrl("https://www.muvi.com/rest");
                playerModel.setEpisode_id("0");
                playerModel.setIsFreeContent(isFreeContent);
                playerModel.setVideoTitle(movieNameStr);
                playerModel.setVideoStory(movieDetailsStr);
                playerModel.setVideoGenre(videoGenreTextView.getText().toString());
                playerModel.setVideoDuration(videoDurationTextView.getText().toString());
                playerModel.setVideoReleaseDate(videoReleaseDateTextView.getText().toString());
                playerModel.setCensorRating(censorRatingStr);
                playerModel.setContentTypesId(contentTypesId);
                playerModel.setPosterImageId(posterImageId);
                playerModel.setCastCrew(castStr);

                Log.v("BKS", "stramid=" + playerModel.getStreamUniqueId());
                Log.v("BKS", "movieID=" + playerModel.getMovieUniqueId());
                Log.v("BKS", "userid=" + preferenceManager.getUseridFromPref());
                Log.v("BKS", "emailid=" + playerModel.getEmailId());


                DataModel dbModel = new DataModel();
                dbModel.setIsFreeContent(isFreeContent);
                dbModel.setIsAPV(isAPV);
                dbModel.setIsPPV(isPPV);
                dbModel.setIsConverted(isConverted);
                dbModel.setMovieUniqueId(movieUniqueId);
                dbModel.setStreamUniqueId(movieStreamUniqueId);
                dbModel.setThirdPartyUrl(movieThirdPartyUrl);
                dbModel.setVideoTitle(movieNameStr);
                dbModel.setVideoStory(movieDetailsStr);
                dbModel.setVideoGenre(videoGenreTextView.getText().toString());
                dbModel.setVideoDuration(videoDurationTextView.getText().toString());
                dbModel.setVideoReleaseDate(videoReleaseDateTextView.getText().toString());
                dbModel.setCensorRating(censorRatingStr);
                dbModel.setCastCrew(castStr);
                dbModel.setEpisode_id("0");
                dbModel.setSeason_id("0");
                dbModel.setPurchase_type("show");
                dbModel.setPosterImageId(posterImageId);
                dbModel.setContentTypesId(contentTypesId);


                SubTitleName.clear();
                SubTitlePath.clear();
                ResolutionUrl.clear();
                ResolutionFormat.clear();

                GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                getVideoDetailsInput.setAuthToken(authTokenStr);
                getVideoDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                getVideoDetailsInput.setContent_uniq_id(movieUniqueId);
                getVideoDetailsInput.setStream_uniq_id(movieStreamUniqueId);
                getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                getVideoDetailsInput.setLanguage(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, MovieDetailsActivity.this, MovieDetailsActivity.this);
                asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);


            }
        });


        watchTrailerButton.setVisibility(View.GONE);
        preorderButton.setVisibility(View.GONE);
        videoCastCrewTitleTextView.setVisibility(View.GONE);

        /// Subtitle/////

        if (ContextCompat.checkSelfPermission(MovieDetailsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MovieDetailsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MovieDetailsActivity.this,
                        new String[]{Manifest.permission
                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS},
                        111);
            } else {
                ActivityCompat.requestPermissions(MovieDetailsActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        111);

            }
        } else {
            //Call whatever you want
            if (NetworkStatus.getInstance().isConnected(MovieDetailsActivity.this)) {

                ContentDetailsInput contentDetailsInput = new ContentDetailsInput();
                contentDetailsInput.setAuthToken(authTokenStr);
                contentDetailsInput.setPermalink(permalinkStr);
                contentDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                contentDetailsInput.setCountry(preferenceManager.getCountryCodeFromPref());
                contentDetailsInput.setLanguage(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));

                asynLoadMovieDetails = new GetContentDetailsAsynTask(contentDetailsInput, MovieDetailsActivity.this, MovieDetailsActivity.this);
                asynLoadMovieDetails.executeOnExecutor(threadPoolExecutor);

            } else {
                Toast.makeText(getApplicationContext(), languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                finish();
            }
        }


    }

    /******* Subtitle*****/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 111: {

                if (grantResults.length > 0) {
                    if ((grantResults.length > 0) && (grantResults[0]) == PackageManager.PERMISSION_GRANTED) {
                        //Call whatever you want

                        if (NetworkStatus.getInstance().isConnected(MovieDetailsActivity.this)) {

                            ContentDetailsInput contentDetailsInput = new ContentDetailsInput();
                            contentDetailsInput.setAuthToken(authTokenStr);
                            contentDetailsInput.setPermalink(permalinkStr);
                            contentDetailsInput.setUser_id(preferenceManager.getUseridFromPref());
                            contentDetailsInput.setCountry(preferenceManager.getCountryCodeFromPref());
                            contentDetailsInput.setLanguage(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                            asynLoadMovieDetails = new GetContentDetailsAsynTask(contentDetailsInput, MovieDetailsActivity.this, MovieDetailsActivity.this);
                            asynLoadMovieDetails.executeOnExecutor(threadPoolExecutor);
                        } else {
                            Toast.makeText(getApplicationContext(), languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                            finish();
                        }

                    } else {
                        finish();
                    }
                } else {
                    finish();
                }

                return;
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (asynValidateUserDetails != null) {
            asynValidateUserDetails.cancel(true);
        }
        if (asynLoadVideoUrls != null) {
            asynLoadVideoUrls.cancel(true);
        }
        if (asynLoadMovieDetails != null) {
            asynLoadMovieDetails.cancel(true);
        }
        if (asynGetReviewDetails != null) {
            asynGetReviewDetails.cancel(true);
        }
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.hide();

        }
        finish();
        overridePendingTransition(0, 0);
        super.onBackPressed();
    }


    public interface ClickListener1 {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


    /***********Subtitle********/

    public void Download_SubTitle(String Url) {
        new DownloadFileFromURL().execute(Url);
    }


    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pDialog != null && !pDialog.isShowing())
                pDialog.show();
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected String doInBackground(String... f_url) {
            int count;


            try {
                URL url = new URL(f_url[0]);
                String str = f_url[0];
                filename = str.substring(str.lastIndexOf("/") + 1);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                File root = Environment.getExternalStorageDirectory();
                mediaStorageDir = new File(root + "/Android/data/" + getApplicationContext().getPackageName().trim() + "/SubTitleList/", "");

                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        Log.d("App", "failed to create directory");
                    }
                }

                SubTitlePath.add(mediaStorageDir.getAbsolutePath() + "/" + System.currentTimeMillis() + ".vtt");
                OutputStream output = new FileOutputStream(mediaStorageDir.getAbsolutePath() + "/" + System.currentTimeMillis() + ".vtt");

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
        }

        @Override
        protected void onPostExecute(String file_url) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
            }
            FakeSubTitlePath.remove(0);
            if (FakeSubTitlePath.size() > 0) {
                Download_SubTitle(FakeSubTitlePath.get(0).trim());
            } else {
                playerModel.setSubTitlePath(SubTitlePath);
                final Intent playVideoIntent;
                playVideoIntent = new Intent(MovieDetailsActivity.this, PlayerActivity.class);
                playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                playVideoIntent.putExtra("PlayerModel", playerModel);
                startActivity(playVideoIntent);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //aunregisterReceiver(DELETE_ACTION);
    }


    @Override
    public void onIPAddressPreExecuteStarted() {

    }

    @Override
    public void onIPAddressPostExecuteCompleted(String message, int statusCode, String ipAddressStr) {

        this.ipAddressStr = ipAddressStr;
        return;
    }


    @Override
    public void onVideoDetailsPreExecuteStarted() {
        if (pDialog != null && !pDialog.isShowing())
            pDialog.show();
    }

    @Override
    public void onVideoDetailsPostExecuteCompleted(Video_Details_Output _video_details_output, int code, String status, String message) {

     /*check if status code 200 then set the video url before this it check it is thirdparty url or normal if third party
        then set thirdpartyurl true here and assign the url to videourl*/
        try {
            if (pDialog != null && pDialog.isShowing())
                pDialog.hide();
        } catch (IllegalArgumentException ex) {
        }
        boolean play_video = true;

        if (code == 200) {

            if ((_video_details_output.getIs_offline().trim().equals("1")) && _video_details_output.getDownload_status().trim().equals("1")) {
                playerModel.canDownload(true);
            } else {
                playerModel.canDownload(false);
            }

            if (_video_details_output.getThirdparty_url() == null || _video_details_output.getThirdparty_url().matches("")) {

                /**@bishal
                 * for drm player below condition added
                 * if studio_approved_url is there in api then set the videourl from this other wise goto 2nd one
                 */

                if (_video_details_output.getStudio_approved_url() != null &&
                        !_video_details_output.getStudio_approved_url().isEmpty() &&
                        !_video_details_output.getStudio_approved_url().equals("null") &&
                        !_video_details_output.getStudio_approved_url().matches("")) {
                    playerModel.setVideoUrl(_video_details_output.getStudio_approved_url());


                    if (_video_details_output.getLicenseUrl().trim() != null && !_video_details_output.getLicenseUrl().trim().isEmpty() && !_video_details_output.getLicenseUrl().trim().equals("null") && !_video_details_output.getLicenseUrl().trim().matches("")) {
                        playerModel.setLicenseUrl(_video_details_output.getLicenseUrl());
                    }
                    if (_video_details_output.getVideoUrl().trim() != null && !_video_details_output.getVideoUrl().isEmpty() && !_video_details_output.getVideoUrl().equals("null") && !_video_details_output.getVideoUrl().trim().matches("")) {
                        playerModel.setMpdVideoUrl(_video_details_output.getVideoUrl());

                    } else {
                        playerModel.setMpdVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));
                    }
                } else {
                    if (_video_details_output.getVideoUrl() != null || !_video_details_output.getVideoUrl().matches("")) {
                        playerModel.setVideoUrl(_video_details_output.getVideoUrl());
                        Log.v("BISHAL", "videourl===" + playerModel.getVideoUrl());
                        playerModel.setThirdPartyPlayer(false);
                    } else {
                        //  Util.dataModel.setVideoUrl(translatedLanuage.getNoData());
                        playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));

                    }
                }
            } else {
                if (_video_details_output.getThirdparty_url() != null || !_video_details_output.getThirdparty_url().matches("")) {
                    playerModel.setVideoUrl(_video_details_output.getThirdparty_url());
                    playerModel.setThirdPartyPlayer(true);

                } else {
                    //  Util.dataModel.setVideoUrl(translatedLanuage.getNoData());
                    playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));
                }
            }


            playerModel.setVideoResolution(_video_details_output.getVideoResolution());
            if (_video_details_output.getPlayed_length() != null && !_video_details_output.getPlayed_length().equals(""))
                playerModel.setPlayPos((Util.isDouble(_video_details_output.getPlayed_length())));

            SubTitleName = _video_details_output.getSubTitleName();
            SubTitleLanguage = _video_details_output.getSubTitleLanguage();


            //player model set
            playerModel.setMidRoll(_video_details_output.getMidRoll());
            playerModel.setPostRoll(_video_details_output.getPostRoll());
            playerModel.setChannel_id(_video_details_output.getChannel_id());
            playerModel.setAdNetworkId(_video_details_output.getAdNetworkId());
            playerModel.setPreRoll(_video_details_output.getPreRoll());

            /**
             * Set Data For WaterMark
             */

            if (_video_details_output.isWatermark_status()) {
                playerModel.setWaterMark(true);
                if (_video_details_output.isWatermark_email())
                    playerModel.useEmail(true);
                else
                    playerModel.useEmail(false);
                if (_video_details_output.isWatermark_ip())
                    playerModel.useIp(true);
                else
                    playerModel.useIp(false);
                if (_video_details_output.isWatermark_date())
                    playerModel.useDate(true);
                else
                    playerModel.useDate(false);
            } else {
                playerModel.setWaterMark(false);
            }

          /*  playerModel.setWaterMark(true);
            playerModel.useIp(true);
            playerModel.useEmail(true);
            playerModel.useDate(true);*/
            // for online subtitle
            playerModel.setSubTitleName(_video_details_output.getSubTitleName());
            playerModel.setSubTitlePath(_video_details_output.getSubTitlePath());


            // for offline subtitle
            playerModel.setOfflineSubtitleUrl(_video_details_output.getOfflineUrl());
            playerModel.setOfflineSubtitleLanguage(_video_details_output.getOfflineLanguage());


            //for chromecast subtitle
            playerModel.setChromecsatSubtitleUrl(_video_details_output.getSubTitlePath());
            playerModel.setChromecsatSubtitleLanguage(_video_details_output.getSubTitleName());
            playerModel.setChromecsatSubtitleLanguageCode(_video_details_output.getSubTitleLanguage());


            //for resolution change in player
            playerModel.setResolutionFormat(_video_details_output.getResolutionFormat());
            playerModel.setResolutionUrl(_video_details_output.getResolutionUrl());

            playerModel.setNonDrmDownloadFormatList(_video_details_output.getResolutionFormat());
            playerModel.setNonDrmDownloadUrlList(_video_details_output.getResolutionUrl());


            if (languagePreference.getTextofLanguage(IS_CHROMECAST, DEFAULT_IS_CHROMECAST).equals("1")) {
                playerModel.setChromeCastEnable(true);
            } else {
                playerModel.setChromeCastEnable(false);
            }


            playerModel.setFakeSubTitlePath(_video_details_output.getFakeSubTitlePath());
            playerModel.setVideoResolution(_video_details_output.getVideoResolution());
            FakeSubTitlePath = _video_details_output.getFakeSubTitlePath();
            playerModel.setSubTitleLanguage(_video_details_output.getSubTitleLanguage());


            if (playerModel.getVideoUrl() == null || playerModel.getVideoUrl().matches("")) {
                Util.showNoDataAlert(MovieDetailsActivity.this);
            } else {

                // condition for checking if the response has third party url or not.
                if (_video_details_output.getThirdparty_url() == null ||
                        _video_details_output.getThirdparty_url().matches("")) {

                    playerModel.setThirdPartyPlayer(false);
                    final Intent playVideoIntent;
                    playVideoIntent = new Intent(MovieDetailsActivity.this, PlayerActivity.class);

                    if (FakeSubTitlePath.size() > 0) {
                        // This Portion Will Be changed Later.

                        File dir = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + getApplicationContext().getPackageName().trim() + "/SubTitleList/");
                        if (dir.isDirectory()) {
                            String[] children = dir.list();
                            for (int i = 0; i < children.length; i++) {
                                new File(dir, children[i]).delete();
                            }
                        }

                                   /* pDialog = new ProgressBarHandler(MovieDetailsActivity.this);
                                    pDialog.show();*/
                        Download_SubTitle(FakeSubTitlePath.get(0).trim());
                    } else {
                        playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        playVideoIntent.putExtra("PlayerModel", playerModel);
                        startActivity(playVideoIntent);
                    }


                } else {
                    final Intent playVideoIntent = new Intent(MovieDetailsActivity.this, PlayerActivity.class);
                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    playVideoIntent.putExtra("PlayerModel", playerModel);
                    startActivity(playVideoIntent);
                }
            }

        } else {

            playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));
            Util.showNoDataAlert(MovieDetailsActivity.this);
        }


    }

    @Override
    public void onGetContentDetailsPreExecuteStarted() {
        if (pDialog != null && !pDialog.isShowing())
            pDialog.show();
    }

    @Override
    public void onGetContentDetailsPostExecuteCompleted(ContentDetailsOutput contentDetailsOutput, int status, String message) {

        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();

            }
        } catch (IllegalArgumentException ex) {
            noInternetConnectionLayout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }


        if (status == 200) {
            castStr = contentDetailsOutput.getCastStr();
            noInternetConnectionLayout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.GONE);
            isPPV = contentDetailsOutput.getIsPpv();
            isAPV = contentDetailsOutput.getIsApv();
            movieUniqueId = contentDetailsOutput.getMuviUniqId();
            isEpisode = contentDetailsOutput.getIsEpisode();
            movieStreamUniqueId = contentDetailsOutput.getMovieStreamUniqId();
            movieNameStr = contentDetailsOutput.getName();
            movieTrailerUrlStr = contentDetailsOutput.getTrailerUrl();
            videoduration = contentDetailsOutput.getVideoDuration();
            censorRatingStr = contentDetailsOutput.getCensorRating();
            movieDetailsStr = contentDetailsOutput.getStory();
            movieTypeStr = contentDetailsOutput.getGenre();
            isFavorite = contentDetailsOutput.getIs_favorite();
            reviews = contentDetailsOutput.getReview();
            rating = contentDetailsOutput.getRating();
            movieIdStr = contentDetailsOutput.getId();
            posterImageId = contentDetailsOutput.getPoster();
            contentTypesId = contentDetailsOutput.getContentTypesId();

            /***play_new button visibility condition *****/

            if (contentDetailsOutput.getIsApv() == 1) {
                playButton.setVisibility(View.INVISIBLE);
                preorderButton.setText(languagePreference.getTextofLanguage(ADVANCE_PURCHASE, DEFAULT_ADVANCE_PURCHASE));
                preorderButton.setVisibility(View.VISIBLE);
            } else if (contentDetailsOutput.getContentTypesId() == 4) {
                playButton.setVisibility(View.VISIBLE);
                preorderButton.setVisibility(View.GONE);

            } else if ((contentDetailsOutput.getIsFreeContent().equals("1") || contentDetailsOutput.getIsPpv() == 1)
                    && contentDetailsOutput.getIsConverted() == 1) {

                playButton.setVisibility(View.VISIBLE);
                preorderButton.setVisibility(View.GONE);

            } else if (contentDetailsOutput.getIsApv() == 0 && contentDetailsOutput.getIsPpv() == 0 &&
                    contentDetailsOutput.getIsConverted() == 1) {
                playButton.setVisibility(View.VISIBLE);
                preorderButton.setVisibility(View.GONE);

            }


            videoTitle.setVisibility(View.VISIBLE);
            Typeface castDescriptionTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.regular_fonts));
            videoTitle.setTypeface(castDescriptionTypeface);
            videoTitle.setText(contentDetailsOutput.getName());


            if (contentDetailsOutput.getGenre() != null && contentDetailsOutput.getGenre().matches("") || contentDetailsOutput.getGenre().matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                videoGenreTextView.setVisibility(View.GONE);

            } else {
                videoGenreTextView.setVisibility(View.VISIBLE);
                Typeface videoGenreTextViewTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
                videoGenreTextView.setTypeface(videoGenreTextViewTypeface);
                videoGenreTextView.setText(contentDetailsOutput.getGenre());

            }
            if (contentDetailsOutput.getVideoDuration().matches("") || contentDetailsOutput.getVideoDuration().matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                videoDurationTextView.setVisibility(View.GONE);

            } else {

                videoDurationTextView.setVisibility(View.VISIBLE);
                Typeface videoGenreTextViewTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
                videoDurationTextView.setTypeface(videoGenreTextViewTypeface);
                videoDurationTextView.setText(contentDetailsOutput.getVideoDuration());
            }


            if (contentDetailsOutput.getReleaseDate().matches("") || contentDetailsOutput.getReleaseDate().matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                videoReleaseDateTextView.setVisibility(View.GONE);
            } else {
                videoReleaseDateTextView.setVisibility(View.VISIBLE);
                Typeface videoGenreTextViewTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
                videoReleaseDateTextView.setTypeface(videoGenreTextViewTypeface);
                movieReleaseDateStr = Util.formateDateFromstring("yyyy-mm-dd", "yyyy", contentDetailsOutput.getReleaseDate());
                videoReleaseDateTextView.setText(movieReleaseDateStr);

            }

            if (contentDetailsOutput.getStory().matches("") || contentDetailsOutput.getStory().matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                videoStoryTextView.setVisibility(View.GONE);

            } else {
                videoStoryTextView.setVisibility(View.VISIBLE);
                Typeface videoGenreTextViewTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
                videoStoryTextView.setTypeface(videoGenreTextViewTypeface);
                videoStoryTextView.setText(contentDetailsOutput.getStory());
                ResizableCustomView.doResizeTextView(MovieDetailsActivity.this, videoStoryTextView, MAX_LINES, languagePreference.getTextofLanguage(VIEW_MORE, DEFAULT_VIEW_MORE), true);

            }

            if (contentDetailsOutput.getCensorRating().matches("") || contentDetailsOutput.getCensorRating().matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {
                videoCensorRatingTextView.setVisibility(View.GONE);
                videoCensorRatingTextView1.setVisibility(View.GONE);

            } else {

                if (contentDetailsOutput.getCensorRating().contains("-")) {
                    String Data[] = contentDetailsOutput.getCensorRating().split("-");
                    videoCensorRatingTextView.setVisibility(View.VISIBLE);
                    videoCensorRatingTextView1.setVisibility(View.VISIBLE);
                    Typeface videoGenreTextViewTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
                    videoCensorRatingTextView.setTypeface(videoGenreTextViewTypeface);
                    videoCensorRatingTextView1.setTypeface(videoGenreTextViewTypeface);

                    videoCensorRatingTextView.setText(Data[0]);
                    videoCensorRatingTextView1.setText(Data[1]);

                } else {
                    videoCensorRatingTextView.setVisibility(View.VISIBLE);
                    videoCensorRatingTextView1.setVisibility(View.GONE);
                    Typeface videoGenreTextViewTypeface = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
                    videoCensorRatingTextView.setTypeface(videoGenreTextViewTypeface);
                    videoCensorRatingTextView.setText(contentDetailsOutput.getCensorRating());
                }
            }


            if (contentDetailsOutput.getBanner().trim().matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {

                if (contentDetailsOutput.getPoster().trim().matches(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA))) {

                    moviePoster.setImageResource(R.drawable.new_logo);
                } else {

                    Picasso.with(MovieDetailsActivity.this)
                            .load(contentDetailsOutput.getPoster().trim())
                            .error(R.drawable.new_logo)
                            .placeholder(R.drawable.new_logo)
                            .into(moviePoster);

                }

            } else {
                Picasso.with(MovieDetailsActivity.this)
                        .load(contentDetailsOutput.getPoster().trim())
                        .error(R.drawable.new_logo)
                        .placeholder(R.drawable.new_logo)
                        .into(moviePoster);
            }

        } else {
            noInternetConnectionLayout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }
    }

}
