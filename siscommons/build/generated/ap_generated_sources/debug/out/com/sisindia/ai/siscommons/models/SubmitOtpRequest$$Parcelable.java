
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
public class SubmitOtpRequest$$Parcelable
    implements Parcelable, ParcelWrapper<com.sisindia.ai.siscommons.models.SubmitOtpRequest>
{

    private com.sisindia.ai.siscommons.models.SubmitOtpRequest submitOtpRequest$$0;
    @SuppressWarnings("UnusedDeclaration")
    public final static Creator<SubmitOtpRequest$$Parcelable>CREATOR = new Creator<SubmitOtpRequest$$Parcelable>() {


        @Override
        public SubmitOtpRequest$$Parcelable createFromParcel(android.os.Parcel parcel$$2) {
            return new SubmitOtpRequest$$Parcelable(read(parcel$$2, new IdentityCollection()));
        }

        @Override
        public SubmitOtpRequest$$Parcelable[] newArray(int size) {
            return new SubmitOtpRequest$$Parcelable[size] ;
        }

    }
    ;

    public SubmitOtpRequest$$Parcelable(com.sisindia.ai.siscommons.models.SubmitOtpRequest submitOtpRequest$$2) {
        submitOtpRequest$$0 = submitOtpRequest$$2;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel$$0, int flags) {
        write(submitOtpRequest$$0, parcel$$0, flags, new IdentityCollection());
    }

    public static void write(com.sisindia.ai.siscommons.models.SubmitOtpRequest submitOtpRequest$$1, android.os.Parcel parcel$$1, int flags$$0, IdentityCollection identityMap$$0) {
        int identity$$0 = identityMap$$0 .getKey(submitOtpRequest$$1);
        if (identity$$0 != -1) {
            parcel$$1 .writeInt(identity$$0);
        } else {
            parcel$$1 .writeInt(identityMap$$0 .put(submitOtpRequest$$1));
            parcel$$1 .writeString(InjectionUtil.getField(java.lang.String.class, com.sisindia.ai.siscommons.models.SubmitOtpRequest.class, submitOtpRequest$$1, "countryCode"));
            parcel$$1 .writeString(InjectionUtil.getField(java.lang.String.class, com.sisindia.ai.siscommons.models.SubmitOtpRequest.class, submitOtpRequest$$1, "mobileNumber"));
            parcel$$1 .writeString(InjectionUtil.getField(java.lang.String.class, com.sisindia.ai.siscommons.models.SubmitOtpRequest.class, submitOtpRequest$$1, "otp"));
        }
    }

    @Override
    public int describeContents() {
        return  0;
    }

    @Override
    public com.sisindia.ai.siscommons.models.SubmitOtpRequest getParcel() {
        return submitOtpRequest$$0;
    }

    public static com.sisindia.ai.siscommons.models.SubmitOtpRequest read(android.os.Parcel parcel$$3, IdentityCollection identityMap$$1) {
        int identity$$1 = parcel$$3 .readInt();
        if (identityMap$$1 .containsKey(identity$$1)) {
            if (identityMap$$1 .isReserved(identity$$1)) {
                throw new ParcelerRuntimeException("An instance loop was detected whild building Parcelable and deseralization cannot continue.  This error is most likely due to using @ParcelConstructor or @ParcelFactory.");
            }
            return identityMap$$1 .get(identity$$1);
        } else {
            com.sisindia.ai.siscommons.models.SubmitOtpRequest submitOtpRequest$$4;
            int reservation$$0 = identityMap$$1 .reserve();
            submitOtpRequest$$4 = new com.sisindia.ai.siscommons.models.SubmitOtpRequest();
            identityMap$$1 .put(reservation$$0, submitOtpRequest$$4);
            InjectionUtil.setField(com.sisindia.ai.siscommons.models.SubmitOtpRequest.class, submitOtpRequest$$4, "countryCode", parcel$$3 .readString());
            InjectionUtil.setField(com.sisindia.ai.siscommons.models.SubmitOtpRequest.class, submitOtpRequest$$4, "mobileNumber", parcel$$3 .readString());
            InjectionUtil.setField(com.sisindia.ai.siscommons.models.SubmitOtpRequest.class, submitOtpRequest$$4, "otp", parcel$$3 .readString());
            com.sisindia.ai.siscommons.models.SubmitOtpRequest submitOtpRequest$$3 = submitOtpRequest$$4;
            identityMap$$1 .put(identity$$1, submitOtpRequest$$3);
            return submitOtpRequest$$3;
        }
    }

}
