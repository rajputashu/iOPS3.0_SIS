package com.sisindia.ai.android.uimodels;

import androidx.room.Ignore;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class KitItemModel {


    public int id;

    public String itemName;

    public String description;

    public String itemCode;

    public int companyId;

    public boolean isActive;

    @Ignore
    public boolean isSelected = false;

    @Ignore
    public KitItemSizeModel selectedSize;

    @Ignore
    public List<KitItemSizeModel> sizes;

    public KitItemModel() {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof KitItemModel)) return false;
        KitItemModel it = (KitItemModel) obj;
        return id == it.id;
    }

    @Parcel
    public static class KitItemSizeModel {


        public int id;

        public int kitItemId;

        public String itemSizeCode;

        public String itemSizeName;

        public float itemSize;

        public boolean isActive;

        public KitItemSizeModel() {
        }
    }
}
