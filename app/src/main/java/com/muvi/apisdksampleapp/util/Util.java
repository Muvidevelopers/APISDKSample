package com.muvi.apisdksampleapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import com.muvi.apisdksampleapp.R;
import com.muvi.apisdksampleapp.model.LanguageModel;
import com.muvi.apisdksampleapp.preferences.LanguagePreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.release.muvisdk.player.utils.Util.BUTTON_OK;
import static com.release.muvisdk.player.utils.Util.DEFAULT_BUTTON_OK;
import static com.release.muvisdk.player.utils.Util.DEFAULT_NO_VIDEO_AVAILABLE;
import static com.release.muvisdk.player.utils.Util.DEFAULT_SORRY;
import static com.release.muvisdk.player.utils.Util.NO_VIDEO_AVAILABLE;
import static com.release.muvisdk.player.utils.Util.SORRY;


/**
 * Created by User on 24-07-2015.
 */
public class Util {


    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {

            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            e.printStackTrace();
            outputDate = "";
        }
        return outputDate;

    }

    public static int isDouble(String str) {
        Double d = Double.parseDouble(str);
        int i = d.intValue();
        return i;
    }

    public static void showNoDataAlert(Context mContext) {
        LanguagePreference languagePreference = LanguagePreference.getLanguagePreference(mContext);
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle);
        dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_VIDEO_AVAILABLE, DEFAULT_NO_VIDEO_AVAILABLE));
        dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
        dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        dlgAlert.create().show();
    }


   public static boolean drawer_line_visibility = true;
   public static boolean itemclicked = false;
   public static ArrayList<LanguageModel> languageModel = null;

   public static boolean my_library_visibility = false;

   public static ArrayList<Integer> image_orentiation = new ArrayList<>();

   /**
    * Method to check portrait orientation.
    * @param context
    * @return
    */
   public static boolean isOrientationPortrait(Context context) {
      return context.getResources().getConfiguration().orientation
              == Configuration.ORIENTATION_PORTRAIT;
   }

   public static void printMD5Key(Context mContext){

      try {
         PackageInfo info = mContext.getPackageManager().getPackageInfo(
                 "com.release.cmaxtv",  // replace with your unique package name
                 PackageManager.GET_SIGNATURES);
         for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            //  Log.v("MUVI:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

         }
      } catch (PackageManager.NameNotFoundException e) {

      } catch (NoSuchAlgorithmException e) {

      }
   }

   /**
    * Parse language key and store in prefernces.
    * @param languagePreference
    * @param jsonResponse
    * @param default_Language
    * @throws JSONException
    */

   public static void parseLanguage(LanguagePreference languagePreference, String jsonResponse, String default_Language) throws JSONException {
         JSONObject json = new JSONObject(jsonResponse);


         languagePreference.setLanguageSharedPrefernce( LanguagePreference.ALREADY_MEMBER, json.optString("already_member").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.ACTIAVTE_PLAN_TITLE, json.optString("activate_plan_title").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.TRANSACTION_STATUS_ACTIVE, json.optString("transaction_status_active").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.ADD_TO_FAV, json.optString("add_to_fav").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.ADDED_TO_FAV, json.optString("added_to_fav").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.ENTER_EMPTY_FIELD, json.optString("enter_register_fields_data").trim());

         languagePreference.setLanguageSharedPrefernce( LanguagePreference.ADVANCE_PURCHASE, json.optString("advance_purchase").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.ALERT, json.optString("alert").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.EPISODE_TITLE, json.optString("episodes_title").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.SORT_ALPHA_A_Z, json.optString("sort_alpha_a_z").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.SORT_ALPHA_Z_A, json.optString("sort_alpha_z_a").trim());

         languagePreference.setLanguageSharedPrefernce( LanguagePreference.AMOUNT, json.optString("amount").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.COUPON_CANCELLED, json.optString("coupon_cancelled").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.BUTTON_APPLY, json.optString("btn_apply").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.SIGN_OUT_WARNING, json.optString("sign_out_warning").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.DISCOUNT_ON_COUPON, json.optString("discount_on_coupon").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.MY_LIBRARY, json.optString("my_library").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.CREDIT_CARD_CVV_HINT, json.optString("credit_card_cvv_hint").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.CAST, json.optString("cast").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.CAST_CREW_BUTTON_TITLE, json.optString("cast_crew_button_title").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.CENSOR_RATING, json.optString("censor_rating").trim());
         if (json.optString("change_password").trim() == null || json.optString("change_password").trim().equals("")) {
            languagePreference.setLanguageSharedPrefernce( LanguagePreference.CHANGE_PASSWORD, LanguagePreference.DEFAULT_CHANGE_PASSWORD);
         } else {
            languagePreference.setLanguageSharedPrefernce( LanguagePreference.CHANGE_PASSWORD, json.optString("change_password").trim());
         }
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.CANCEL_BUTTON, json.optString("btn_cancel").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.RESUME_MESSAGE, json.optString("resume_watching").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.CONTINUE_BUTTON, json.optString("continue").trim());


         languagePreference.setLanguageSharedPrefernce( LanguagePreference.CONFIRM_PASSWORD, json.optString("confirm_password").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.CREDIT_CARD_DETAILS, json.optString("credit_card_detail").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.DIRECTOR, json.optString("director").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.DOWNLOAD_BUTTON_TITLE, json.optString("download_button_title").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.DESCRIPTION, json.optString("description").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.HOME, json.optString("home").trim());

         languagePreference.setLanguageSharedPrefernce( LanguagePreference.EMAIL_EXISTS, json.optString("email_exists").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.EMAIL_DOESNOT_EXISTS, json.optString("email_does_not_exist").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.EMAIL_PASSWORD_INVALID, json.optString("email_password_invalid").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.COUPON_CODE_HINT, json.optString("coupon_code_hint").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.SEARCH_ALERT, json.optString("search_alert").trim());

         languagePreference.setLanguageSharedPrefernce( LanguagePreference.CREDIT_CARD_NUMBER_HINT, json.optString("credit_card_number_hint").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.TEXT_EMIAL, json.optString("text_email").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.NAME_HINT, json.optString("name_hint").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.CREDIT_CARD_NAME_HINT, json.optString("credit_card_name_hint").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.TEXT_PASSWORD, json.optString("text_password").trim());

         languagePreference.setLanguageSharedPrefernce( LanguagePreference.ERROR_IN_PAYMENT_VALIDATION, json.optString("error_in_payment_validation").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.ERROR_IN_REGISTRATION, json.optString("error_in_registration").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.TRANSACTION_STATUS_EXPIRED, json.optString("transaction_status_expired").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.DETAILS_NOT_FOUND_ALERT, json.optString("details_not_found_alert").trim());

         languagePreference.setLanguageSharedPrefernce( LanguagePreference.FAILURE, json.optString("failure").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.FILTER_BY, json.optString("filter_by").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.FORGOT_PASSWORD, json.optString("forgot_password").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.GENRE, json.optString("genre").trim());

         languagePreference.setLanguageSharedPrefernce( LanguagePreference.AGREE_TERMS, json.optString("agree_terms").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.INVALID_COUPON, json.optString("invalid_coupon").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.INVOICE, json.optString("invoice").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.LANGUAGE_POPUP_LANGUAGE, json.optString("language_popup_language").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.SORT_LAST_UPLOADED, json.optString("sort_last_uploaded").trim());

         languagePreference.setLanguageSharedPrefernce( LanguagePreference.LANGUAGE_POPUP_LOGIN, json.optString("language_popup_login").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.LOGIN, json.optString("login").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.LOGOUT, json.optString("logout").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.LOGOUT_SUCCESS, json.optString("logout_success").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.MY_FAVOURITE, json.optString("my_favourite").trim());

         languagePreference.setLanguageSharedPrefernce( LanguagePreference.NEW_PASSWORD, json.optString("new_password").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.NEW_HERE_TITLE, json.optString("new_here_title").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.NO, json.optString("no").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.NO_DATA, json.optString("no_data").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.NO_INTERNET_CONNECTION, json.optString("no_internet_connection").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.ENTER_REGISTER_FIELDS_DATA, json.optString("enter_register_fields_data").trim());

         languagePreference.setLanguageSharedPrefernce( LanguagePreference.NO_INTERNET_NO_DATA, json.optString("no_internet_no_data").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.NO_DETAILS_AVAILABLE, json.optString("no_details_available").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.BUTTON_OK, json.optString("btn_ok").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.OLD_PASSWORD, json.optString("old_password").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.OOPS_INVALID_EMAIL, json.optString("oops_invalid_email").trim());

         languagePreference.setLanguageSharedPrefernce( LanguagePreference.ORDER, json.optString("order").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.TRANSACTION_DETAILS_ORDER_ID, json.optString("transaction_detail_order_id").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.PASSWORD_RESET_LINK, json.optString("password_reset_link").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.PASSWORDS_DO_NOT_MATCH, json.optString("password_donot_match").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.PAY_BY_PAYPAL, json.optString("pay_by_paypal").trim());

         languagePreference.setLanguageSharedPrefernce( LanguagePreference.BTN_PAYNOW, json.optString("btn_paynow").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.PAY_WITH_CREDIT_CARD, json.optString("pay_with_credit_card").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.PAYMENT_OPTIONS_TITLE, json.optString("payment_options_title").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.PLAN_NAME, json.optString("plan_name").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, json.optString("activate_subscription_watch_video").trim());

         languagePreference.setLanguageSharedPrefernce( LanguagePreference.COUPON_ALERT, json.optString("coupon_alert").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.VALID_CONFIRM_PASSWORD, json.optString("valid_confirm_password").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.PROFILE, json.optString("profile").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.PROFILE_UPDATED, json.optString("profile_updated").trim());

         languagePreference.setLanguageSharedPrefernce( LanguagePreference.PURCHASE, json.optString("purchase").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.TRANSACTION_DETAIL_PURCHASE_DATE, json.optString("transaction_detail_purchase_date").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.PURCHASE_HISTORY, json.optString("purchase_history").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.BTN_REGISTER, json.optString("btn_register").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.SORT_RELEASE_DATE, json.optString("sort_release_date").trim());

         languagePreference.setLanguageSharedPrefernce( LanguagePreference.SAVE_THIS_CARD, json.optString("save_this_card").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.TEXT_SEARCH_PLACEHOLDER, json.optString("text_search_placeholder").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.SEASON, json.optString("season").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.SELECT_OPTION_TITLE, json.optString("select_option_title").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.SELECT_PLAN, json.optString("select_plan").trim());

         languagePreference.setLanguageSharedPrefernce( LanguagePreference.SIGN_UP_TITLE, json.optString("signup_title").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.SLOW_INTERNET_CONNECTION, json.optString("slow_internet_connection").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.SLOW_ISSUE_INTERNET_CONNECTION, json.optString("slow_issue_internet_connection").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.SORRY, json.optString("sorry").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.GEO_BLOCKED_ALERT, json.optString("geo_blocked_alert").trim());

         languagePreference.setLanguageSharedPrefernce( LanguagePreference.SIGN_OUT_ERROR, json.optString("sign_out_error").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.ALREADY_PURCHASE_THIS_CONTENT, json.optString("already_purchase_this_content").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.CROSSED_MAXIMUM_LIMIT, json.optString("crossed_max_limit_of_watching").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.SORT_BY, json.optString("sort_by").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.STORY_TITLE, json.optString("story_title").trim());

         languagePreference.setLanguageSharedPrefernce( LanguagePreference.BTN_SUBMIT, json.optString("btn_submit").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.TRANSACTION_STATUS, json.optString("transaction_success").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.VIDEO_ISSUE, json.optString("video_issue").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.NO_CONTENT, json.optString("no_content").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.NO_VIDEO_AVAILABLE, json.optString("no_video_available").trim());

         languagePreference.setLanguageSharedPrefernce( LanguagePreference.CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY, json.optString("content_not_available_in_your_country").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.TRANSACTION_DATE, json.optString("transaction_date").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.TRANASCTION_DETAIL, json.optString("transaction_detail").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.TRANSACTION_STATUS, json.optString("transaction_status").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.TRANSACTION, json.optString("transaction").trim());

         languagePreference.setLanguageSharedPrefernce( LanguagePreference.TRY_AGAIN, json.optString("try_again").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.UNPAID, json.optString("unpaid").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.USE_NEW_CARD, json.optString("use_new_card").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.VIEW_MORE, json.optString("view_more").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.VIEW_TRAILER, json.optString("view_trailer").trim());

         languagePreference.setLanguageSharedPrefernce( LanguagePreference.WATCH, json.optString("watch").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.WATCH_NOW, json.optString("watch_now").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.SIGN_OUT_ALERT, json.optString("sign_out_alert").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.UPDATE_PROFILE_ALERT, json.optString("update_profile_alert").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.YES, json.optString("yes").trim());

         languagePreference.setLanguageSharedPrefernce( LanguagePreference.PURCHASE_SUCCESS_ALERT, json.optString("purchase_success_alert").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.CARD_WILL_CHARGE, json.optString("card_will_charge").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.SEARCH_HINT, json.optString("search_hint").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.TERMS, json.optString("terms").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.UPDATE_PROFILE, json.optString("btn_update_profile").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.APP_ON, json.optString("app_on").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.APP_SELECT_LANGUAGE, json.optString("app_select_language").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.FILL_FORM_BELOW, json.optString("Fill_form_below").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.MESSAGE, json.optString("text_message").trim());


         languagePreference.setLanguageSharedPrefernce( LanguagePreference.SIMULTANEOUS_LOGOUT_SUCCESS_MESSAGE, json.optString("simultaneous_logout_message").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.LOGIN_STATUS_MESSAGE, json.optString("login_status_message").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.FILL_FORM_BELOW, json.optString("fill_form_below").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.MESSAGE, json.optString("text_message").trim());

         languagePreference.setLanguageSharedPrefernce( LanguagePreference.SELECTED_LANGUAGE_CODE, default_Language);

   }


    public static void getDPI(Context _context) {

        int density = _context.getResources().getDisplayMetrics().densityDpi;
        float density1 = _context.getResources().getDisplayMetrics().density;

        Activity act = (Activity) _context;
        Display display = act.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float dpHeight = outMetrics.heightPixels / density1;
        float dpWidth = outMetrics.widthPixels / density1;
        Log.d("Login", "height-" + dpHeight + ",Width:-" + dpWidth);
        switch (density) {
            case DisplayMetrics.DENSITY_LOW: {
                Log.d("Login", "LDPI height-" + dpHeight + ",Width:-" + dpWidth);
            }
            break;
            case DisplayMetrics.DENSITY_MEDIUM: {
                Log.d("Login", "MDPI height-" + dpHeight + ",Width:-" + dpWidth);

            }
            break;
            case DisplayMetrics.DENSITY_HIGH: {
                Log.d("Login", "HDPI height-" + dpHeight + ",Width:-" + dpWidth);

            }
            break;
            case DisplayMetrics.DENSITY_XHIGH: {
                Log.d("Login", "XHDPI height-" + dpHeight + ",Width:-" + dpWidth);

            }
            break;
            case DisplayMetrics.DENSITY_XXHIGH: {
                Log.d("Login", "XXHDPI height-" + dpHeight + ",Width:-" + dpWidth);

            }
            break;
            case DisplayMetrics.DENSITY_XXXHIGH: {
                Log.d("Login", "XXXHDPI height-" + dpHeight + ",Width:-" + dpWidth);

            }
            break;
            case DisplayMetrics.DENSITY_TV: {
                Log.d("Login", "TVDPI height-" + dpHeight + ",Width:-" + dpWidth);

            }
            break;
        }
    }
}
