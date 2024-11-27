
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
public class GenerateOtp$$Parcelable
    implements Parcelable, ParcelWrapper<com.sisindia.ai.siscommons.models.GenerateOtp>
{

    private com.sisindia.ai.siscommons.models.GenerateOtp generateOtp$$0;
    @SuppressWarnings("UnusedDeclaration")
    public final static Creator<GenerateOtp$$Parcelable>CREATOR = new Creator<GenerateOtp$$Parcelable>() {


        @Override
        public GenerateOtp$$Parcelable createFromParcel(android.os.Parcel parcel$$2) {
            return new GenerateOtp$$Parcelable(read(parcel$$2, new IdentityCollection()));
        }

        @Override
        public GenerateOtp$$Parcelable[] newArray(int size) {
            return new GenerateOtp$$Parcelable[size] ;
        }

    }
    ;

    public GenerateOtp$$Parcelable(com.sisindia.ai.siscommons.models.GenerateOtp generateOtp$$2) {
        generateOtp$$0 = generateOtp$$2;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel$$0, int flags) {
        write(generateOtp$$0, parcel$$0, flags, new IdentityCollection());
    }

    public static void write(com.sisindia.ai.siscommons.models.GenerateOtp generateOtp$$1, android.os.Parcel parcel$$1, int flags$$0, IdentityCollection identityMap$$0) {
        int identity$$0 = identityMap$$0 .getKey(generateOtp$$1);
        if (identity$$0 != -1) {
            parcel$$1 .writeInt(identity$$0);
        } else {
            parcel$$1 .writeInt(identityMap$$0 .put(generateOtp$$1));
            parcel$$1 .writeString(InjectionUtil.getField(java.lang.String.class, com.sisindia.ai.siscommons.models.GenerateOtp.class, generateOtp$$1, "mobileNumber"));
            parcel$$1 .writeString(InjectionUtil.getField(java.lang.String.class, com.sisindia.ai.siscommons.models.GenerateOtp.class, generateOtp$$1, "countryCode"));
        }
    }

    @Override
    public int describeContents() {
        return  0;
    }

    @Override
    public com.sisindia.ai.siscommons.models.GenerateOtp getParcel() {
        return generateOtp$$0;
    }

    public static com.sisindia.ai.siscommons.models.GenerateOtp read(android.os.Parcel parcel$$3, IdentityCollection identityMap$$1) {
        int identity$$1 = parcel$$3 .readInt();
        if (identityMap$$1 .containsKey(identity$$1)) {
            if (identityMap$$1 .isReserved(identity$$1)) {
                throw new ParcelerRuntimeException("An instance loop was detected whild building Parcelable and deseralization cannot continue.  This error is most likely due to using @ParcelConstructor or @ParcelFactory.");
            }
            return identityMap$$1 .get(identity$$1);
        } else {
            com.sisindia.ai.siscommons.models.GenerateOtp generateOtp$$4;
            int reservation$$0 = identityMap$$1 .reserve();
            generateOtp$$4 = new com.sisindia.ai.siscommons.models.GenerateOtp();
            identityMap$$1 .put(reservation$$0, generateOtp$$4);
            InjectionUtil.setField(com.sisindia.ai.siscommons.models.GenerateOtp.class, generateOtp$$4, "mobileNumber", parcel$$3 .readString());
            InjectionUtil.setField(com.sisindia.ai.siscommons.models.GenerateOtp.class, generateOtp$$4, "countryCode", parcel$$3 .readString());
            com.sisindia.ai.siscommons.models.GenerateOtp generateOtp$$3 = generateOtp$$4;
            identityMap$$1 .put(identity$$1, generateOtp$$3);
            return generateOtp$$3;
        }
    }

}
