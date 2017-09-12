package com.muvi.apisdksampleapp.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.home.apisdk.apiController.GetImageForDownloadAsynTask;
import com.home.apisdk.apiController.GetLanguageListAsynTask;
import com.home.apisdk.apiController.GetMenuListAsynctask;
import com.home.apisdk.apiController.GetTranslateLanguageAsync;
import com.home.apisdk.apiController.LogoutAsynctask;
import com.home.apisdk.apiController.SDKInitializer;
import com.home.apisdk.apiModel.LanguageListInputModel;
import com.home.apisdk.apiModel.LanguageListOutputModel;
import com.home.apisdk.apiModel.LogoutInput;
import com.home.apisdk.apiModel.MenuListInput;
import com.home.apisdk.apiModel.MenuListOutput;
import com.muvi.apisdksampleapp.R;
import com.muvi.apisdksampleapp.adapter.LanguageCustomAdapter;
import com.muvi.apisdksampleapp.fragment.FragmentDrawer;
import com.muvi.apisdksampleapp.fragment.HomeFragment;
import com.muvi.apisdksampleapp.model.LanguageModel;
import com.muvi.apisdksampleapp.model.NavDrawerItem;
import com.muvi.apisdksampleapp.network.NetworkStatus;
import com.muvi.apisdksampleapp.preferences.LanguagePreference;
import com.muvi.apisdksampleapp.preferences.PreferenceManager;
import com.muvi.apisdksampleapp.util.LogUtil;
import com.muvi.apisdksampleapp.util.ProgressBarHandler;
import com.muvi.apisdksampleapp.util.Util;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.muvi.apisdksampleapp.preferences.LanguagePreference.APP_SELECT_LANGUAGE;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.BTN_REGISTER;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.BUTTON_APPLY;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_APP_SELECT_LANGUAGE;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_BTN_REGISTER;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_BUTTON_APPLY;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_HOME;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_IS_ONE_STEP_REGISTRATION;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_LANGUAGE_POPUP_LANGUAGE;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_LANGUAGE_POPUP_LOGIN;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_LOGOUT;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_LOGOUT_SUCCESS;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_MY_LIBRARY;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_NO;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_NO_INTERNET_NO_DATA;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_PROFILE;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_PURCHASE_HISTORY;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_SIGN_OUT_ERROR;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_SIGN_OUT_WARNING;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_YES;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.HOME;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.IS_ONE_STEP_REGISTRATION;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.LANGUAGE_POPUP_LANGUAGE;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.LANGUAGE_POPUP_LOGIN;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.LOGOUT;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.LOGOUT_SUCCESS;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.MY_LIBRARY;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.NO;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.NO_INTERNET_NO_DATA;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.PROFILE;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.PURCHASE_HISTORY;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.SIGN_OUT_ERROR;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.SIGN_OUT_WARNING;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.YES;
import static com.muvi.apisdksampleapp.util.Constant.authTokenStr;
import static com.muvi.apisdksampleapp.util.Util.languageModel;


public class MainActivity extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener,
        LogoutAsynctask.LogoutListener, GetMenuListAsynctask.GetMenuListListener,
        GetLanguageListAsynTask.GetLanguageListListener,
        GetTranslateLanguageAsync.GetTranslateLanguageInfoListener {


    public MainActivity() {
    }

    LanguagePreference languagePreference;

    //*** chromecast**************//*

    public enum PlaybackLocation {
        LOCAL,
        REMOTE
    }


    public enum PlaybackState {
        PLAYING, PAUSED, BUFFERING, IDLE
    }


    private Timer mControllersTimer;
    private PlaybackLocation mLocation;
    private PlaybackState mPlaybackState;
    private final float mAspectRatio = 72f / 128;
    private MenuItem mediaRouteMenuItem;
    public static int vertical = 0;
    private String lang_code = "";
    int check = 0;
    public static int isNavigated = 0;
    String Default_Language = "";
    public ArrayList<NavDrawerItem> menuList = new ArrayList<>();
    public ArrayList<LanguageModel> languageModels = new ArrayList<>();
    private String imageUrlStr;
    int state = 0;
    LanguageCustomAdapter languageCustomAdapter;
    public static ProgressBarHandler internetSpeedDialog;
    //Load on background thread
    /*Asynctask on background thread*/
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    //Toolbar
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private RelativeLayout noInternetLayout;
    public static String internetSpeed = "0";
    Fragment fragment = null;
    private ProgressBarHandler pDialog = null;
    String loggedInStr, loginHistoryIdStr, email, id;

    GetImageForDownloadAsynTask as = null;
    GetMenuListAsynctask asynLoadMenuItems = null;
    int isLogin = 0;

    public static int planIdOfStudios = 3;
    int prevPosition = 0;


    AlertDialog alert;
    String Previous_Selected_Language = "";
    TextView noInternetTextView;
    // SharedPreferences isLoginPref;
    public static ProgressBarHandler progressBarHandler;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogUtil.showLog("BKS", "packagenameMAINactivity1===" + SDKInitializer.user_Package_Name_At_Api);
        if (menuList != null && menuList.size() > 0) {
            menuList.clear();
        }

        languagePreference = LanguagePreference.getLanguagePreference(this);

        /*Set Toolbar*/
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        LogUtil.showLog("Abhishek", "Toolbar");

        preferenceManager = PreferenceManager.getPreferenceManager(this);
        isLogin = preferenceManager.getLoginFeatureFromPref();
        noInternetLayout = (RelativeLayout) findViewById(R.id.noInternet);
        noInternetTextView = (TextView) findViewById(R.id.noInternetTextView);
        noInternetTextView.setText(languagePreference.getTextofLanguage(NO_INTERNET_NO_DATA, DEFAULT_NO_INTERNET_NO_DATA));
        noInternetLayout.setVisibility(View.GONE);


        if (NetworkStatus.getInstance().isConnected(MainActivity.this)) {
            if (asynLoadMenuItems != null) {
                asynLoadMenuItems = null;
            }
            MenuListInput menuListInput = new MenuListInput();
            menuListInput.setAuthToken(authTokenStr);
            String countryCodeStr = preferenceManager.getCountryCodeFromPref();
            if (countryCodeStr == null) {
                menuListInput.setCountry("IN");
            }
            menuListInput.setCountry(countryCodeStr);
            menuListInput.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
            asynLoadMenuItems = new GetMenuListAsynctask(menuListInput, MainActivity.this, this);
            asynLoadMenuItems.executeOnExecutor(threadPoolExecutor);

        } else {
            noInternetLayout.setVisibility(View.VISIBLE);
            DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
            dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem itemFillterMenu,
                itemProfileMenu,
                itemPurchaseMenu,
                itemLogoutMenu,
                itemLoginMenu,
                itemRegisterMenu,
                itemLanguageMenu,
                itemDownload,
                itemFavMenu;

        itemFillterMenu = menu.findItem(R.id.action_filter);
        itemFavMenu = menu.findItem(R.id.menu_item_favorite);
        itemLanguageMenu = menu.findItem(R.id.menu_item_language);
        itemLoginMenu = menu.findItem(R.id.action_login);
        itemRegisterMenu = menu.findItem(R.id.action_register);
        itemProfileMenu = menu.findItem(R.id.menu_item_profile);
        itemPurchaseMenu = menu.findItem(R.id.action_purchage);
        itemLogoutMenu = menu.findItem(R.id.action_logout);

        itemFillterMenu.setVisible(false);


        loggedInStr = preferenceManager.getLoginStatusFromPref();
        id = preferenceManager.getUseridFromPref();
        email = preferenceManager.getEmailIdFromPref();

        (itemLanguageMenu).setTitle(languagePreference.getTextofLanguage(LANGUAGE_POPUP_LANGUAGE, DEFAULT_LANGUAGE_POPUP_LANGUAGE));


        if (preferenceManager.getLanguageListFromPref().equals("1"))
            (itemLanguageMenu).setVisible(false);

        if (loggedInStr != null) {

            itemLoginMenu.setTitle(languagePreference.getTextofLanguage(LANGUAGE_POPUP_LOGIN, DEFAULT_LANGUAGE_POPUP_LOGIN));
            itemLoginMenu.setVisible(false);

            itemRegisterMenu.setTitle(languagePreference.getTextofLanguage(BTN_REGISTER, DEFAULT_BTN_REGISTER));
            itemRegisterMenu.setVisible(false);
            itemProfileMenu.setTitle(languagePreference.getTextofLanguage(PROFILE, DEFAULT_PROFILE));
            itemProfileMenu.setVisible(true);
            itemPurchaseMenu.setTitle(languagePreference.getTextofLanguage(PURCHASE_HISTORY, DEFAULT_PURCHASE_HISTORY));
            itemPurchaseMenu.setVisible(true);
            itemLogoutMenu.setTitle(languagePreference.getTextofLanguage(LOGOUT, DEFAULT_LOGOUT));
            itemLogoutMenu.setVisible(true);

        } else if (loggedInStr == null) {
            itemLoginMenu.setTitle(languagePreference.getTextofLanguage(LANGUAGE_POPUP_LOGIN, DEFAULT_LANGUAGE_POPUP_LOGIN));
            itemRegisterMenu.setTitle(languagePreference.getTextofLanguage(BTN_REGISTER, DEFAULT_BTN_REGISTER));
            if (isLogin == 1) {
                itemLoginMenu.setVisible(true);
                itemRegisterMenu.setVisible(true);

            } else {
                itemLoginMenu.setVisible(false);
                itemRegisterMenu.setVisible(false);
            }
            itemProfileMenu.setTitle(languagePreference.getTextofLanguage(PROFILE, DEFAULT_PROFILE));
            itemProfileMenu.setVisible(false);

            itemPurchaseMenu.setTitle(languagePreference.getTextofLanguage(PURCHASE_HISTORY, DEFAULT_PURCHASE_HISTORY));
            itemPurchaseMenu.setVisible(false);

            itemLogoutMenu.setTitle(languagePreference.getTextofLanguage(LOGOUT, DEFAULT_LOGOUT));
            itemLogoutMenu.setVisible(false);
            itemFavMenu.setVisible(false);
        }
        return true;
    }

  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                final Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(searchIntent);
                // Not implemented here
                return false;
            case R.id.action_filter:

                // Not implemented here
                return false;
            case R.id.action_login:

                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                Util.check_for_subscription = 0;
                startActivity(loginIntent);
                // Not implemented here
                return false;
            case R.id.action_register:

                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                Util.check_for_subscription = 0;
                startActivity(registerIntent);
                // Not implemented here
                return false;
            case R.id.menu_item_language:

                Default_Language = languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE);
                Previous_Selected_Language = languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE);

                if (languageModel != null && languageModel.size() > 0) {
                    ShowLanguagePopup();

                } else {
                    LanguageListInputModel languageListInputModel = new LanguageListInputModel();
                    languageListInputModel.setAuthToken(authTokenStr);
                    GetLanguageListAsynTask asynGetLanguageList = new GetLanguageListAsynTask(languageListInputModel, this, this);
                    asynGetLanguageList.executeOnExecutor(threadPoolExecutor);
                }
                return false;
            case R.id.menu_item_profile:

                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                profileIntent.putExtra("EMAIL", email);
                profileIntent.putExtra("LOGID", id);
                startActivity(profileIntent);
                // Not implemented here
                return false;
            case R.id.action_purchage:

                Intent purchaseintent = new Intent(MainActivity.this, PurchaseHistoryActivity.class);
                startActivity(purchaseintent);
                // Not implemented here
                return false;
            case R.id.action_logout:

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MainActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(languagePreference.getTextofLanguage(SIGN_OUT_WARNING, DEFAULT_SIGN_OUT_WARNING));
                dlgAlert.setTitle("");

                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(YES, DEFAULT_YES), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog

                        // dialog.cancel();
                        LogoutInput logoutInput = new LogoutInput();
                        logoutInput.setAuthToken(authTokenStr);
                        LogUtil.showLog("Abhi", authTokenStr);
                        String loginHistoryIdStr = preferenceManager.getLoginHistIdFromPref();
                        logoutInput.setLogin_history_id(loginHistoryIdStr);
                        logoutInput.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                        LogUtil.showLog("Abhi", languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                        LogoutAsynctask asynLogoutDetails = new LogoutAsynctask(logoutInput, MainActivity.this, MainActivity.this);
                        asynLogoutDetails.executeOnExecutor(threadPoolExecutor);


                        dialog.dismiss();
                    }
                });

                dlgAlert.setNegativeButton(languagePreference.getTextofLanguage(NO, DEFAULT_NO), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });
                dlgAlert.setCancelable(false);
                dlgAlert.create().show();
                return false;
            default:
                break;
        }

        return false;
    }*/

    @Override
    public void onDrawerItemSelected(View view, int position) {
        check = position;
        displayView(position);
    }

    //Display View based on selection of menu item

    private void displayView(int position) {

        isNavigated = 1;

        String title = getString(R.string.app_name);
     /*   SharedPreferences.Editor dataEditor = dataPref.edit();*/
        Bundle bundle = new Bundle();
        String str = menuList.get(position).getPermalink();
        String titleStr = menuList.get(position).getTitle();
        // state = position;

        if (internetSpeedDialog != null && internetSpeedDialog.isShowing()) {
            internetSpeedDialog.hide();
            internetSpeedDialog = null;

        }
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.hide();
            pDialog = null;

        }
        if (str != null && !str.equalsIgnoreCase("") && !str.isEmpty() && menuList.get(position).getLinkType().equalsIgnoreCase("-101")) {

            fragment = new HomeFragment();
            bundle.putString("item", str);


        } else if (menuList.get(position).getLinkType().equalsIgnoreCase("102")) {

            //fragment = new MyLibraryFragment();
            bundle.putString("title", titleStr);

        } else if (menuList.get(position).getIsEnabled() == false) {

            if (str.equals("contactus")) {

                /*fragment = new ContactUsFragment();
                bundle.putString("title", titleStr);*/
                Toast.makeText(MainActivity.this, str,Toast.LENGTH_LONG).show();

            } else {


                /*fragment = new AboutUsFragment();
                bundle.putString("item", str);
                bundle.putString("title", titleStr);*/
                Toast.makeText(MainActivity.this, str,Toast.LENGTH_LONG).show();

            }


        } else if (menuList.get(position).getIsEnabled() == true) {

            /*fragment = new VideosListFragment();
            bundle.putString("item", str);
            bundle.putString("title", titleStr);*/

            Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();
        }
       // fragment.setArguments(bundle);

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {

        if (asynLoadMenuItems != null) {
            asynLoadMenuItems.cancel(true);
        }


        if (as != null) {
            as.cancel(true);
        }
        super.onBackPressed();


    }


    @Override
    public void onLogoutPreExecuteStarted() {
        pDialog = new ProgressBarHandler(MainActivity.this);
        pDialog.show();
    }

    @Override
    public void onLogoutPostExecuteCompleted(int code, String status, String message) {
        if (code != 200) {
            Toast.makeText(MainActivity.this, languagePreference.getTextofLanguage(SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

        }
        if (code == 0) {
            Toast.makeText(MainActivity.this, languagePreference.getTextofLanguage(SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

        }
        if (code > 0) {
            if (code == 200) {
                preferenceManager.clearLoginPref();

                if ((languagePreference.getTextofLanguage(IS_ONE_STEP_REGISTRATION, DEFAULT_IS_ONE_STEP_REGISTRATION)
                        .trim()).equals("1")) {
                    final Intent startIntent = new Intent(MainActivity.this, SplashScreen.class);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(startIntent);
                            Toast.makeText(MainActivity.this, languagePreference.getTextofLanguage(LOGOUT_SUCCESS, DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();

                            finish();

                        }
                    });
                } else {
                    final Intent startIntent = new Intent(MainActivity.this, MainActivity.class);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(startIntent);
                            Toast.makeText(MainActivity.this, languagePreference.getTextofLanguage(LOGOUT_SUCCESS, DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();

                            finish();

                        }
                    });
                }


            } else {
                Toast.makeText(MainActivity.this, languagePreference.getTextofLanguage(SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

            }
        }

    }

    @Override
    public void onGetMenuListPreExecuteStarted() {
        try {
            internetSpeedDialog = new ProgressBarHandler(MainActivity.this);
            internetSpeedDialog.show();
            LogUtil.showLog("Alok", "onGetMenuListPreExecuteStarted");


        } catch (IllegalArgumentException ex) {

            noInternetLayout.setVisibility(View.VISIBLE);
            DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
            dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            LogUtil.showLog("Alok", "onGetMenuListPreExecuteStarted IllegalArgumentException");
        }
    }

    @Override
    public void onGetMenuListPostExecuteCompleted(ArrayList<MenuListOutput> menuListOutputList, ArrayList<MenuListOutput> footermenuListOutputList, int status, String message) {
        LogUtil.showLog("BKS", "packagenameMAINactivity===" + SDKInitializer.user_Package_Name_At_Api);

        LogUtil.showLog("Alok", "onGetMenuListPostExecuteCompleted");
        if (status == 0) {
            noInternetLayout.setVisibility(View.VISIBLE);
            DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
            dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


        } else {
            menuList.add(new NavDrawerItem(languagePreference.getTextofLanguage(HOME, DEFAULT_HOME), "-101", true, "-101"));
            for (MenuListOutput menuListOutput : menuListOutputList) {
                LogUtil.showLog("Alok", "menuListOutputList ::" + menuListOutput.getPermalink());
                if (menuListOutput.getLink_type() != null && !menuListOutput.getLink_type().equalsIgnoreCase("") && menuListOutput.getLink_type().equalsIgnoreCase("0")) {
                    menuList.add(new NavDrawerItem(menuListOutput.getDisplay_name(), menuListOutput.getPermalink(), menuListOutput.isEnable(), menuListOutput.getLink_type()));
                }
            }

            menuList.add(new NavDrawerItem(languagePreference.getTextofLanguage(MY_LIBRARY, DEFAULT_MY_LIBRARY), "102", true, "102"));
            LogUtil.showLog("Alok", "getTextofLanguage MY_LIBRARY");

            for (MenuListOutput menuListOutput : footermenuListOutputList) {
                LogUtil.showLog("Alok", "footermenuListOutputList ::" + menuListOutput.getPermalink());
                if (menuListOutput.getUrl() != null && !menuListOutput.getUrl().equalsIgnoreCase("")) {
                    menuList.add(new NavDrawerItem(menuListOutput.getDisplay_name(), menuListOutput.getPermalink(), menuListOutput.isEnable(), menuListOutput.getUrl()));
                }
            }


            imageUrlStr = "https://dadc-muvi.s3-eu-west-1.amazonaws.com/check-download-speed.jpg";
            if (NetworkStatus.getInstance().isConnected(MainActivity.this)) {

                //new Thread(mWorker).start();
            } else {
                internetSpeed = "0";
            }

            drawerFragment = (FragmentDrawer)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
            drawerFragment.setData(menuList);
            drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
            drawerFragment.setDrawerListener(MainActivity.this);
            displayView(0);
        }

        if (internetSpeedDialog != null && internetSpeedDialog.isShowing()) {
            internetSpeedDialog.hide();
            internetSpeedDialog = null;

        }

    }

    public void ShowLanguagePopup() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.MyAlertDialogStyle);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View convertView = inflater.inflate(R.layout.language_pop_up, null);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.languagePopupTitle);
        titleTextView.setText(languagePreference.getTextofLanguage(APP_SELECT_LANGUAGE, DEFAULT_APP_SELECT_LANGUAGE));

        alertDialog.setView(convertView);
        alertDialog.setTitle("");

        RecyclerView recyclerView = (RecyclerView) convertView.findViewById(R.id.language_recycler_view);
        Button apply = (Button) convertView.findViewById(R.id.apply_btn);
        apply.setText(languagePreference.getTextofLanguage(BUTTON_APPLY, DEFAULT_BUTTON_APPLY));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        languageCustomAdapter = new LanguageCustomAdapter(MainActivity.this, languageModel);
        recyclerView.setAdapter(languageCustomAdapter);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener1(MainActivity.this, recyclerView, new ClickListener1() {
            @Override
            public void onClick(View view, int position) {
                Util.itemclicked = true;

                languageModel.get(position).setSelected(true);


                if (prevPosition != position) {
                    languageModel.get(prevPosition).setSelected(false);
                    prevPosition = position;

                }

                Default_Language = languageModel.get(position).getLanguageId();


                languagePreference.setLanguageSharedPrefernce(SELECTED_LANGUAGE_CODE, languageModel.get(position).getLanguageId());
                languageCustomAdapter.notifyDataSetChanged();


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();


                if (!Previous_Selected_Language.equals(Default_Language)) {


                    LanguageListInputModel languageListInputModel = new LanguageListInputModel();
                    languageListInputModel.setLangCode(Default_Language);
                    languageListInputModel.setAuthToken(authTokenStr);
                    GetTranslateLanguageAsync asynGetTransalatedLanguage = new GetTranslateLanguageAsync(languageListInputModel, MainActivity.this, MainActivity.this);
                    asynGetTransalatedLanguage.executeOnExecutor(threadPoolExecutor);
                }

            }
        });


        alert = alertDialog.show();


        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                languagePreference.setLanguageSharedPrefernce(SELECTED_LANGUAGE_CODE, Previous_Selected_Language);
            }
        });

    }

    @Override
    public void onGetLanguageListPreExecuteStarted() {
        progressBarHandler = new ProgressBarHandler(MainActivity.this);
        progressBarHandler.show();
    }

    @Override
    public void onGetLanguageListPostExecuteCompleted(ArrayList<LanguageListOutputModel> languageListOutputArray, int status, String message, String defaultLanguage) {

        ArrayList<LanguageModel> languageModels = new ArrayList<LanguageModel>();

        for (int i = 0; i < languageListOutputArray.size(); i++) {
            String language_id = languageListOutputArray.get(i).getLanguageCode();
            String language_name = languageListOutputArray.get(i).getLanguageName();


            LanguageModel languageModel = new LanguageModel();
            languageModel.setLanguageId(language_id);
            languageModel.setLanguageName(language_name);

            if (Default_Language.equalsIgnoreCase(language_id)) {
                languageModel.setIsSelected(true);
            } else {
                languageModel.setIsSelected(false);
            }
            languageModels.add(languageModel);
        }

        languageModel = languageModels;
        ShowLanguagePopup();
    }


    public static class RecyclerTouchListener1 implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener1 clickListener;

        public RecyclerTouchListener1(Context context, final RecyclerView recyclerView, final ClickListener1 clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface ClickListener1 {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }



    @Override
    public void onGetTranslateLanguagePreExecuteStarted() {
        progressBarHandler = new ProgressBarHandler(MainActivity.this);
        progressBarHandler.show();
    }

    @Override
    public void onGetTranslateLanguagePostExecuteCompleted(String jsonResponse, int status) {
        if (status > 0 && status == 200) {

            try {

                Util.parseLanguage(languagePreference, jsonResponse, Default_Language);

                languageCustomAdapter.notifyDataSetChanged();

                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);


            } catch (JSONException e) {
                e.printStackTrace();
                noInternetLayout.setVisibility(View.GONE);
            }
            // Call For Other Methods.


        } else {
            noInternetLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public void onUserLeaveHint() {
        super.onUserLeaveHint();
        /*List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null) {
            //TODO: Perform your logic to pass back press here
            for(Fragment fragment : fragmentList){
                if(fragment instanceof HomeFragment){
                    ((HomeFragment)fragment).myOnKeyDown();
                    ActivityCompat.finishAffinity(this);
                    finish();
                    System.exit(0);
                }
                else if(fragment instanceof VideosListFragment){
                    ((VideosListFragment)fragment).myOnKeyDown();
                    ActivityCompat.finishAffinity(this);
                    finish();
                    System.exit(0);
                }
            }
        }*/
        if (asynLoadMenuItems != null) {
            asynLoadMenuItems.cancel(true);
        }
        if (as != null) {
            as.cancel(true);
        }
        if (isNavigated == 0) {
            if (internetSpeedDialog != null && internetSpeedDialog.isShowing()) {
                internetSpeedDialog.hide();
                internetSpeedDialog = null;

            }

            ActivityCompat.finishAffinity(this);
            finish();
            System.exit(0);
        }

    }
    private int networkType(final double kbps) {
        int type = 1;//3G
        //Check if its EDGE
        if (kbps < EDGE_THRESHOLD) {
            type = 0;
        }
        return type;
    }

    /**
     * 1 byte = 0.0078125 kilobits
     * 1 kilobits = 0.0009765625 megabit
     *
     * @param downloadTime in miliseconds
     * @param bytesIn      number of bytes downloaded
     * @return SpeedInfo containing current speed
     */
    private SpeedInfo calculate(final long downloadTime, final long bytesIn) {
        SpeedInfo info = new SpeedInfo();
        //from mil to sec
        long bytespersecond = (bytesIn / downloadTime) * 1000;
        double kilobits = bytespersecond * BYTE_TO_KILOBIT;
        double megabits = kilobits * KILOBIT_TO_MEGABIT;
        info.downspeed = bytespersecond;
        info.kilobits = kilobits;
        info.megabits = megabits;

        return info;
    }

    /**
     * Transfer Object
     *
     * @author devil
     */
    private static class SpeedInfo {
        public double kilobits = 0;
        public double megabits = 0;
        public double downspeed = 0;
    }


    //Private fields
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int EXPECTED_SIZE_IN_BYTES = 1048576;//1MB 1024*1024

    private static final double EDGE_THRESHOLD = 176.0;
    private static final double BYTE_TO_KILOBIT = 0.0078125;
    private static final double KILOBIT_TO_MEGABIT = 0.0009765625;

    private final int MSG_UPDATE_STATUS = 0;
    private final int MSG_UPDATE_CONNECTION_TIME = 1;
    private final int MSG_COMPLETE_STATUS = 2;

    private final static int UPDATE_THRESHOLD = 300;


    private DecimalFormat mDecimalFormater;

    @Override
    protected void onPause() {
     /*   mCastContext.removeCastStateListener(mCastStateListener);
        mCastContext.getSessionManager().removeSessionManagerListener(
                mSessionManagerListener, CastSession.class);*/
        super.onPause();
    }


    private void updatePlaybackLocation(PlaybackLocation location) {
        mLocation = location;
        if (location == PlaybackLocation.LOCAL) {
            if (mPlaybackState == PlaybackState.PLAYING
                    || mPlaybackState == PlaybackState.BUFFERING) {
                //setCoverArtStatus(null);
                startControllersTimer();
            } else {
                stopControllersTimer();
                //setCoverArtStatus(MediaUtils.getImageUrl(mSelectedMedia, 0));
            }
        } else {
            stopControllersTimer();
            //setCoverArtStatus(MediaUtils.getImageUrl(mSelectedMedia, 0));
            updateControllersVisibility(false);
        }
    }

    private void stopTrickplayTimer() {
     /*   if (mSeekbarTimer != null) {
            mSeekbarTimer.cancel();
        }*/
    }


    private void stopControllersTimer() {
        if (mControllersTimer != null) {
            mControllersTimer.cancel();
        }
    }

    private void startControllersTimer() {
        if (mControllersTimer != null) {
            mControllersTimer.cancel();
        }
        if (mLocation == PlaybackLocation.REMOTE) {
            return;
        }
        mControllersTimer = new Timer();
        mControllersTimer.schedule(new HideControllersTask(), 5000);
    }

    // should be called from the main thread
    private void updateControllersVisibility(boolean show) {
        if (show) {
            getSupportActionBar().show();
            // mControllers.setVisibility(View.VISIBLE);
        } else {
            if (!Util.isOrientationPortrait(this)) {
                getSupportActionBar().hide();
            }
            //  mControllers.setVisibility(View.INVISIBLE);
        }
    }

    private class HideControllersTask extends TimerTask {

        @Override
        public void run() {
           /* mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // updateControllersVisibility(false);
                    //  mControllersVisible = false;
                }
            });*/

        }
    }

}


