
package com.sisindia.ai.siscommons.models;

import android.os.Parcelable;
import android.os.Parcelable.Creator;
import org.parceler.Generated;
import org.parceler.IdentityCollection;
import org.parceler.InjectionUtil;
import org.parceler.ParcelWrapper;
import org.parceler.ParcelerRuntimeException;

@Generated("org.parceler.ParcelAnnotationProcessor")
@SuppressWarnings({
    "unchecked",
    "deprecation"
})
public class SisCountry$$Parcelable
    implements Parcelable, ParcelWrapper<com.sisindia.ai.siscommons.models.SisCountry>
{

    private com.sisindia.ai.siscommons.models.SisCountry sisCountry$$0;
    @SuppressWarnings("UnusedDeclaration")
    public final static Creator<SisCountry$$Parcelable>CREATOR = new Creator<SisCountry$$Parcelable>() {


        @Override
        public SisCountry$$Parcelable createFromParcel(android.os.Parcel parcel$$2) {
            return new SisCountry$$Parcelable(read(parcel$$2, new IdentityCollection()));
        }

        @Override
        public SisCountry$$Parcelable[] newArray(int size) {
            return new SisCountry$$Parcelable[size] ;
        }

    }
    ;

    public SisCountry$$Parcelable(com.sisindia.ai.siscommons.models.SisCountry sisCountry$$2) {
        sisCountry$$0 = sisCountry$$2;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel$$0, int flags) {
        write(sisCountry$$0, parcel$$0, flags, new IdentityCollection());
    }

    public static void write(com.sisindia.ai.siscommons.models.SisCountry sisCountry$$1, android.os.Parcel parcel$$1, int flags$$0, IdentityCollection identityMap$$0) {
        int identity$$0 = identityMap$$0 .getKey(sisCountry$$1);
        if (identity$$0 != -1) {
            parcel$$1 .writeInt(identity$$0);
        } else {
            parcel$$1 .writeInt(identityMap$$0 .put(sisCountry$$1));
            parcel$$1 .writeString(InjectionUtil.getField(java.lang.String.class, com.sisindia.ai.siscommons.models.SisCountry.class, sisCountry$$1, "countryUrl"));
            parcel$$1 .writeInt(InjectionUtil.getField(int.class, com.sisindia.ai.siscommons.models.SisCountry.class, sisCountry$$1, "countryFlag"));
            parcel$$1 .writeString(InjectionUtil.getField(java.lang.String.class, com.sisindia.ai.siscommons.models.SisCountry.class, sisCountry$$1, "countryCode"));
            parcel$$1 .writeInt(InjectionUtil.getField(int.class, com.sisindia.ai.siscommons.models.SisCountry.class, sisCountry$$1, "minLength"));
            parcel$$1 .writeString(InjectionUtil.getField(java.lang.String.class, com.sisindia.ai.siscommons.models.SisCountry.class, sisCountry$$1, "countryName"));
            parcel$$1 .writeInt(InjectionUtil.getField(int.class, com.sisindia.ai.siscommons.models.SisCountry.class, sisCountry$$1, "maxLength"));
        }
    }

    @Override
    public int describeContents() {
        return  0;
    }

    @Override
    public com.sisindia.ai.siscommons.models.SisCountry getParcel() {
        return sisCountry$$0;
    }

    public static com.sisindia.ai.siscommons.models.SisCountry read(android.os.Parcel parcel$$3, IdentityCollection identityMap$$1) {
        int identity$$1 = parcel$$3 .readInt();
        if (identityMap$$1 .containsKey(identity$$1)) {
            if (identityMap$$1 .isReserved(identity$$1)) {
                throw new ParcelerRuntimeException("An instance loop was detected whild building Parcelable and deseralization cannot continue.  This error is most likely due to using @ParcelConstructor or @ParcelFactory.");
            }
            return identityMap$$1 .get(identity$$1);
        } else {
            com.sisindia.ai.siscommons.models.SisCountry sisCountry$$4;
            int reservation$$0 = identityMap$$1 .reserve();
            sisCountry$$4 = new com.sisindia.ai.siscommons.models.SisCountry();
            identityMap$$1 .put(reservation$$0, sisCountry$$4);
            InjectionUtil.setField(com.sisindia.ai.siscommons.models.SisCountry.class, sisCountry$$4, "countryUrl", parcel$$3 .readString());
            InjectionUtil.setField(com.sisindia.ai.siscommons.models.SisCountry.class, sisCountry$$4, "countryFlag", parcel$$3 .readInt());
            InjectionUtil.setField(com.sisindia.ai.siscommons.models.SisCountry.class, sisCountry$$4, "countryCode", parcel$$3 .readString());
            InjectionUtil.setField(com.sisindia.ai.siscommons.models.SisCountry.class, sisCountry$$4, "minLength", parcel$$3 .readInt());
            InjectionUtil.setField(com.sisindia.ai.siscommons.models.SisCountry.class, sisCountry$$4, "countryName", parcel$$3 .readString());
            InjectionUtil.setField(com.sisindia.ai.siscommons.models.SisCountry.class, sisCountry$$4, "maxLength", parcel$$3 .readInt());
            com.sisindia.ai.siscommons.models.SisCountry sisCountry$$3 = sisCountry$$4;
            identityMap$$1 .put(identity$$1, sisCountry$$3);
            return sisCountry$$3;
        }
    }

}
