package com.sisindia.ai.android.commons;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.sisindia.ai.android.R;

import org.parceler.Parcel;

@Parcel
public class MenuNavigationItem {

    public @DrawableRes
    int menuIconId;

    public @StringRes
    int menuTextId;

    public MenuItemType itemType;

    public boolean isChecked;

    public MenuNavigationItem() {
    }

    public static MenuNavigationItem postItem() {
        MenuNavigationItem item = new MenuNavigationItem();
        item.menuIconId = R.drawable.ic_post_check;
        item.menuTextId = R.string.post_check_text;
        item.itemType = MenuItemType.POST_CHECK;
        return item;
    }

    public static MenuNavigationItem strengthItem() {
        MenuNavigationItem item = new MenuNavigationItem();
        item.menuIconId = R.drawable.ic_strength_check;
        item.menuTextId = R.string.strength_check_text;
        item.itemType = MenuItemType.STRENGTH_CHECK;
        return item;
    }

    public static MenuNavigationItem clientHandShakeItem() {
        MenuNavigationItem item = new MenuNavigationItem();
        item.menuIconId = R.drawable.ic_client_handshake;
        item.menuTextId = R.string.client_check_text;
        item.itemType = MenuItemType.CLIENT_HAND_SHAKE;
        return item;
    }

    public static MenuNavigationItem checkGuard() {
        MenuNavigationItem item = new MenuNavigationItem();
        item.menuIconId = R.drawable.ic_check_guard;
        item.menuTextId = R.string.post_check_guards_text;
        item.itemType = MenuItemType.CHECK_GUARD;
        return item;
    }

    public static MenuNavigationItem checkRegister() {
        MenuNavigationItem item = new MenuNavigationItem();
        item.menuIconId = R.drawable.ic_register_check;
        item.menuTextId = R.string.post_check_registers_text;
        item.itemType = MenuItemType.CHECK_REGISTER;
        return item;
    }

    public static MenuNavigationItem addSecurity() {
        MenuNavigationItem item = new MenuNavigationItem();
        item.menuIconId = R.drawable.ic_add_security;
        item.menuTextId = R.string.post_check_add_security_risk_text;
        item.itemType = MenuItemType.ADD_SECURITY;
        return item;
    }

    public static MenuNavigationItem addGrievance() {
        MenuNavigationItem item = new MenuNavigationItem();
        item.menuIconId = R.drawable.ic_add_grievance;
        item.menuTextId = R.string.post_check_add_grievance_text;
        item.itemType = MenuItemType.ADD_GRIEVANCE;
        return item;
    }

    public static MenuNavigationItem addKitRequest() {
        MenuNavigationItem item = new MenuNavigationItem();
        item.menuIconId = R.drawable.ic_add_kit_request;
        item.menuTextId = R.string.post_check_add_kit_request_text;
        item.itemType = MenuItemType.ADD_KIT_REQUEST;
        return item;
    }

    public enum MenuItemType {
        POST_CHECK, STRENGTH_CHECK, CLIENT_HAND_SHAKE,
        CHECK_GUARD, CHECK_REGISTER, ADD_SECURITY, ADD_GRIEVANCE, ADD_KIT_REQUEST
    }
}
