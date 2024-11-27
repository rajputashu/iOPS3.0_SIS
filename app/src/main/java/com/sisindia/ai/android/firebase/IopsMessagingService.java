package com.sisindia.ai.android.firebase;

import static com.sisindia.ai.android.constants.PrefConstants.FCM_TOKEN;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.droidcommons.preference.Prefs;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.sisindia.ai.android.R;
import com.sisindia.ai.android.commons.NotificationMode;
import com.sisindia.ai.android.features.billsubmit.BillSubmissionCardsActivity;
import com.sisindia.ai.android.features.dashboard.DashBoardActivity;
import com.sisindia.ai.android.features.notification.CustomWebPage;
import com.sisindia.ai.android.features.splash.SplashFragment;
import com.sisindia.ai.android.room.dao.NotificationsDao;
import com.sisindia.ai.android.room.entities.NotificationDataEntity;

import java.util.Objects;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.AndroidInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class IopsMessagingService extends FirebaseMessagingService {

    private static final String channelId = "com.sisindia.ai.android.iops2.0";

    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Inject
    @Named("@NotificationHandler")
    NotificationHandler notificationHandler;

    @Inject
    NotificationsDao notificationsDao;

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }

    @Override
    public void handleIntent(@NonNull Intent intent) {
        super.handleIntent(intent);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
            String notificationData = new Gson().toJson(remoteMessage.getData());
            NotificationDataEntity model = new Gson().fromJson(notificationData, NotificationDataEntity.class);

            if (model.getMode() != null && !model.getMode().isEmpty()) {
                if (Integer.parseInt(model.getMode()) == NotificationMode.SILENT.getMode()) {
                    //USE BELOW NOTIFICATION TO TRIGGER NOTIFICATION BUT NOT TO SHOW NOTIFICATION ON TRAY
                    silentNotificationOrReadOnly(model, true);
                    handleSilentNotification(model);
                } else if (Integer.parseInt(model.getMode()) == NotificationMode.READ_ACTION.getMode()) {
//                    handleNotificationRequest(model);
                    handleSilentNotification(model);
                    actionableNotification(model);
                } else if (Integer.parseInt(model.getMode()) == NotificationMode.NO_ACTION.getMode()) {
                    //USE BELOW NOTIFICATION if want to show only message or information with no action
                    silentNotificationOrReadOnly(model, false);
//                    customNotification(model);
                } else if (Integer.parseInt(model.getMode()) == NotificationMode.WEB_VIEW.getMode()) {
                    actionableNotification(model);
                } else if (Integer.parseInt(model.getMode()) == NotificationMode.SQL_DELETE.getMode() ||
                        Integer.parseInt(model.getMode()) == NotificationMode.SQL_UPDATE.getMode()) {
                    silentNotificationOrReadOnly(model, true);
                    handleSilentNotification(model);
                }
                insertNotificationToDB(model);
            }
            Timber.d("Notification DATA %s", notificationData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
//        Timber.d("On New token %s", s);
        Prefs.putString(FCM_TOKEN, s);
    }

    private void silentNotificationOrReadOnly(NotificationDataEntity notificationDataEntity, Boolean autoRemove) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_app_notification)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.logo_sis_splash))
                .setContentTitle(notificationDataEntity.getMessage())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationDataEntity.getPayload()))
                .setAutoCancel(true)
                .setContentText(notificationDataEntity.getPayload())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .setColor(getResources().getColor(R.color.colorChatBG))
                .setColor(getNotificationColor())
                .setColorized(true)
                .setSound(uri);
                /*.setSmallIcon(R.drawable.ic_app_notification)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.logo_sis_splash))
                .setContentTitle(notificationDataEntity.getMessage())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationDataEntity.getPayload()))
                .setAutoCancel(true)
                .setSound(uri);*/

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Channel_iOPS2.0", NotificationManager.IMPORTANCE_DEFAULT);
            if (manager != null)
                manager.createNotificationChannel(channel);
        }
        if (manager != null) {
            int id = Integer.parseInt(Objects.requireNonNull(notificationDataEntity.getNotificationId()));
            manager.notify(id, builder.build());
            if (autoRemove)
                manager.cancel(id); // Using to auto cancel notification : without displaying it
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private void actionableNotification(NotificationDataEntity notificationMO) {
        Intent intent = null;
        if (notificationMO.getModuleName() != null) {
            switch (notificationMO.getModuleName()) {
                case "ROTA_STATUS":
                case "NEW_SITE_RISK_STATUS":
                case "GRIEVANCE_STATUS":
                case "UNITS_STATUS":
                case "DUTY_ON_OFF_STATUS":
                case "AdhocApproved":
                case "AKR_SYNC":
                    intent = new Intent(this, DashBoardActivity.class)
                            .putExtra("NOTIFICATION_MODULE", notificationMO.getModuleName());
                    break;
                case "NEW_BILL_SYNC_STATUS":
                    intent = new Intent(this, BillSubmissionCardsActivity.class);
                    break;
                case "WEB_VIEW":
                    intent = new Intent(this, CustomWebPage.class)
                            .putExtra("NOTIFICATION_URL", notificationMO.getCallBackUrl())
                            .putExtra("NOTIFICATION_TITLE", notificationMO.getMessage());
                    break;
                default:
                    intent = new Intent(this, SplashFragment.class);
                    break;
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this,
                    0, intent, PendingIntent.FLAG_IMMUTABLE);

        } else {
            pendingIntent = PendingIntent.getActivity(this,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_app_notification)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.logo_sis_splash))
                .setContentTitle(notificationMO.getMessage())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationMO.getPayload()))
                .setAutoCancel(true)
                .setSound(uri)
                .setContentText(notificationMO.getPayload())
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .setColor(getResources().getColor(R.color.colorChatBG))
                .setColor(getNotificationColor())
                .setColorized(true)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Channel_iOPS2.0", NotificationManager.IMPORTANCE_DEFAULT);
            if (manager != null)
                manager.createNotificationChannel(channel);
        }
        if (manager != null) {
            int id = Integer.parseInt(Objects.requireNonNull(notificationMO.getNotificationId()));
            manager.notify(id, builder.build());
        }
    }

    /*private void customNotification(NotificationDataEntity notificationDataEntity) {
        if (notificationDataEntity.getModuleName().equals("1")) {
            RemoteViews collapsedView = new RemoteViews(getPackageName(), R.layout.collapse_notification);
            collapsedView.setTextViewText(R.id.notificationTitleTV, notificationDataEntity.getMessage());
            collapsedView.setTextViewText(R.id.notificationInfoTV, notificationDataEntity.getPayload());
            RemoteViews expandableView = new RemoteViews(getPackageName(), R.layout.expandable_notification);

            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_app_notification)
//                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.logo_sis_splash))
//                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(collapsedView)
                    .setCustomBigContentView(expandableView)
//                .setContentTitle(notificationDataEntity.getModuleName())
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationDataEntity.getMessage()))
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setSound(uri);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, "Channel_iOPS2.0", NotificationManager.IMPORTANCE_DEFAULT);
                if (manager != null)
                    manager.createNotificationChannel(channel);
            }
            if (manager != null) {
                int id = Integer.parseInt(Objects.requireNonNull(notificationDataEntity.getNotificationId()));
                manager.notify(id, builder.build());
//                manager.cancel(id);
            }

        } else if (notificationDataEntity.getModuleName().equals("2")) {

//            RemoteViews customView = new RemoteViews(getPackageName(), R.layout.expandable_notification);
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_app_notification)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.logo_sis_splash))
                    .setContentTitle(notificationDataEntity.getMessage())
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationDataEntity.getPayload()))
//                    .setStyle(new androidx.media.app.NotificationCompat.DecoratedMediaCustomViewStyle())
                    .setAutoCancel(true)
                    .setContentText(notificationDataEntity.getPayload())
//                    .setCustomContentView(customView)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setColor(getResources().getColor(R.color.colorStatusPending))
                    .setColorized(true)
                    .setSound(uri);

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, "Channel_iOPS2.0", NotificationManager.IMPORTANCE_DEFAULT);
                if (manager != null)
                    manager.createNotificationChannel(channel);
            }
            if (manager != null) {
                int id = Integer.parseInt(Objects.requireNonNull(notificationDataEntity.getNotificationId()));
                manager.notify(id, builder.build());
//                manager.cancel(id);
            }
        }
    }*/

    private void handleSilentNotification(NotificationDataEntity notification) {
        if (notification.getModuleName() != null) {
            switch (notification.getModuleName()) {
                case "ROTA_SYNC":
                    notificationHandler.triggerGetRota();
                    break;
                case "NEW_SITE_RISK_SYNC":
                    notificationHandler.triggerGetNewSiteAtRisk();
                    break;
                case "NEW_BILL_SYNC":
                    notificationHandler.triggerGetNewBill();
                    break;
                case "UNITS_SYNC":
                    notificationHandler.triggerRefreshMySitesData();
                    break;
                case "RESTART_GEOLOCATION_SERVICE":
                    notificationHandler.triggerToStartLocationService(this);
                    break;
                case "AKR_SYNC":
                    notificationHandler.triggerGetNewAKRs();
                    break;
                case "AdhocApproved":
                    notificationHandler.triggerToUpdateAdHocTask(notification.getJsonData());
                    break;
                case "Nudges":
                    Timber.e("Notification Coming to trigger Nudges");
                    break;
            }
        }
    }

    private void insertNotificationToDB(NotificationDataEntity notificationMO) {
        addDisposable(notificationsDao.insert(notificationMO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> Timber.e("Notification is inserted successfully"), Timber::e));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
            compositeDisposable.clear();
        }
    }

    private int getNotificationColor() {
        try {
            int[] colors = {getResources().getColor(R.color.colorChatBG),
                    getResources().getColor(R.color.colorStatusPending),
                    getResources().getColor(R.color.colorRed_30opct)};
            return colors[new Random().nextInt(3) - 1];
        } catch (Exception ignored) {
        }
        return 0;
    }
}