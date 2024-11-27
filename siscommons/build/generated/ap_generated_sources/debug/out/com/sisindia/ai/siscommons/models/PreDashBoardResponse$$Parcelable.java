
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
public class PreDashBoardResponse$$Parcelable
    implements Parcelable, ParcelWrapper<com.sisindia.ai.siscommons.models.PreDashBoardResponse>
{

    private com.sisindia.ai.siscommons.models.PreDashBoardResponse preDashBoardResponse$$0;
    @SuppressWarnings("UnusedDeclaration")
    public final static Creator<PreDashBoardResponse$$Parcelable>CREATOR = new Creator<PreDashBoardResponse$$Parcelable>() {


        @Override
        public PreDashBoardResponse$$Parcelable createFromParcel(android.os.Parcel parcel$$2) {
            return new PreDashBoardResponse$$Parcelable(read(parcel$$2, new IdentityCollection()));
        }

        @Override
        public PreDashBoardResponse$$Parcelable[] newArray(int size) {
            return new PreDashBoardResponse$$Parcelable[size] ;
        }

    }
    ;

    public PreDashBoardResponse$$Parcelable(com.sisindia.ai.siscommons.models.PreDashBoardResponse preDashBoardResponse$$2) {
        preDashBoardResponse$$0 = preDashBoardResponse$$2;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel$$0, int flags) {
        write(preDashBoardResponse$$0, parcel$$0, flags, new IdentityCollection());
    }

    public static void write(com.sisindia.ai.siscommons.models.PreDashBoardResponse preDashBoardResponse$$1, android.os.Parcel parcel$$1, int flags$$0, IdentityCollection identityMap$$0) {
        int identity$$0 = identityMap$$0 .getKey(preDashBoardResponse$$1);
        if (identity$$0 != -1) {
            parcel$$1 .writeInt(identity$$0);
        } else {
            parcel$$1 .writeInt(identityMap$$0 .put(preDashBoardResponse$$1));
            parcel$$1 .writeSerializable(InjectionUtil.getField(new InjectionUtil.GenericType<java.util.ArrayList<java.lang.Object>>(), com.sisindia.ai.siscommons.models.PreDashBoardResponse.class, preDashBoardResponse$$1, "efforts"));
            parcel$$1 .writeSerializable(InjectionUtil.getField(new InjectionUtil.GenericType<java.util.ArrayList<java.lang.Object>>(), com.sisindia.ai.siscommons.models.PreDashBoardResponse.class, preDashBoardResponse$$1, "results"));
        }
    }

    @Override
    public int describeContents() {
        return  0;
    }

    @Override
    public com.sisindia.ai.siscommons.models.PreDashBoardResponse getParcel() {
        return preDashBoardResponse$$0;
    }

    public static com.sisindia.ai.siscommons.models.PreDashBoardResponse read(android.os.Parcel parcel$$3, IdentityCollection identityMap$$1) {
        int identity$$1 = parcel$$3 .readInt();
        if (identityMap$$1 .containsKey(identity$$1)) {
            if (identityMap$$1 .isReserved(identity$$1)) {
                throw new ParcelerRuntimeException("An instance loop was detected whild building Parcelable and deseralization cannot continue.  This error is most likely due to using @ParcelConstructor or @ParcelFactory.");
            }
            return identityMap$$1 .get(identity$$1);
        } else {
            com.sisindia.ai.siscommons.models.PreDashBoardResponse preDashBoardResponse$$4;
            int reservation$$0 = identityMap$$1 .reserve();
            preDashBoardResponse$$4 = new com.sisindia.ai.siscommons.models.PreDashBoardResponse();
            identityMap$$1 .put(reservation$$0, preDashBoardResponse$$4);
            InjectionUtil.setField(com.sisindia.ai.siscommons.models.PreDashBoardResponse.class, preDashBoardResponse$$4, "efforts", ((java.util.ArrayList) parcel$$3 .readSerializable()));
            InjectionUtil.setField(com.sisindia.ai.siscommons.models.PreDashBoardResponse.class, preDashBoardResponse$$4, "results", ((java.util.ArrayList) parcel$$3 .readSerializable()));
            com.sisindia.ai.siscommons.models.PreDashBoardResponse preDashBoardResponse$$3 = preDashBoardResponse$$4;
            identityMap$$1 .put(identity$$1, preDashBoardResponse$$3);
            return preDashBoardResponse$$3;
        }
    }

}
