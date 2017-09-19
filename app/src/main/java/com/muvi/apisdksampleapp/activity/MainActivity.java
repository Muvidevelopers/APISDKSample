package com.muvi.apisdksampleapp.activity;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.home.apisdk.apiController.GetImageForDownloadAsynTask;
import com.home.apisdk.apiController.GetMenuListAsynctask;
import com.home.apisdk.apiController.SDKInitializer;
import com.home.apisdk.apiModel.MenuListInput;
import com.home.apisdk.apiModel.MenuListOutput;
import com.muvi.apisdksampleapp.R;
import com.muvi.apisdksampleapp.fragment.FragmentDrawer;
import com.muvi.apisdksampleapp.fragment.HomeFragment;
import com.muvi.apisdksampleapp.model.NavDrawerItem;
import com.muvi.apisdksampleapp.network.NetworkStatus;
import com.muvi.apisdksampleapp.preferences.LanguagePreference;
import com.muvi.apisdksampleapp.preferences.PreferenceManager;
import com.muvi.apisdksampleapp.util.LogUtil;
import com.muvi.apisdksampleapp.util.ProgressBarHandler;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.muvi.apisdksampleapp.preferences.LanguagePreference.BTN_REGISTER;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_BTN_REGISTER;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_HOME;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_LANGUAGE_POPUP_LANGUAGE;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_LANGUAGE_POPUP_LOGIN;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_LOGOUT;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_MY_LIBRARY;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_NO_INTERNET_NO_DATA;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_PROFILE;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_PURCHASE_HISTORY;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.HOME;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.LANGUAGE_POPUP_LANGUAGE;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.LANGUAGE_POPUP_LOGIN;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.LOGOUT;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.MY_LIBRARY;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.NO_INTERNET_NO_DATA;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.PROFILE;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.PURCHASE_HISTORY;
import static com.muvi.apisdksampleapp.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.muvi.apisdksampleapp.util.Constant.authTokenStr;


public class MainActivity extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener,
       GetMenuListAsynctask.GetMenuListListener
         {

    public MainActivity() {
    }

    LanguagePreference languagePreference;
    public static int vertical = 0;
    int check = 0;
    public static int isNavigated = 0;
    public ArrayList<NavDrawerItem> menuList = new ArrayList<>();
    private String imageUrlStr;
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

    TextView noInternetTextView;
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
        LogUtil.showLog("Muvi", "Toolbar");

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

    @Override
    protected void onPause() {
     /*   mCastContext.removeCastStateListener(mCastStateListener);
        mCastContext.getSessionManager().removeSessionManagerListener(
                mSessionManagerListener, CastSession.class);*/
        super.onPause();
    }


}


