
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
public class ReviewInformationResponse$TempReviewInformation$$Parcelable
    implements Parcelable, ParcelWrapper<com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation>
{

    private com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation tempReviewInformation$$0;
    @SuppressWarnings("UnusedDeclaration")
    public final static Creator<ReviewInformationResponse$TempReviewInformation$$Parcelable>CREATOR = new Creator<ReviewInformationResponse$TempReviewInformation$$Parcelable>() {


        @Override
        public ReviewInformationResponse$TempReviewInformation$$Parcelable createFromParcel(android.os.Parcel parcel$$2) {
            return new ReviewInformationResponse$TempReviewInformation$$Parcelable(read(parcel$$2, new IdentityCollection()));
        }

        @Override
        public ReviewInformationResponse$TempReviewInformation$$Parcelable[] newArray(int size) {
            return new ReviewInformationResponse$TempReviewInformation$$Parcelable[size] ;
        }

    }
    ;

    public ReviewInformationResponse$TempReviewInformation$$Parcelable(com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation tempReviewInformation$$2) {
        tempReviewInformation$$0 = tempReviewInformation$$2;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel$$0, int flags) {
        write(tempReviewInformation$$0, parcel$$0, flags, new IdentityCollection());
    }

    public static void write(com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation tempReviewInformation$$1, android.os.Parcel parcel$$1, int flags$$0, IdentityCollection identityMap$$0) {
        int identity$$0 = identityMap$$0 .getKey(tempReviewInformation$$1);
        if (identity$$0 != -1) {
            parcel$$1 .writeInt(identity$$0);
        } else {
            parcel$$1 .writeInt(identityMap$$0 .put(tempReviewInformation$$1));
            parcel$$1 .writeInt(InjectionUtil.getField(int.class, com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation.class, tempReviewInformation$$1, "id"));
            parcel$$1 .writeInt(InjectionUtil.getField(int.class, com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation.class, tempReviewInformation$$1, "status"));
        }
    }

    @Override
    public int describeContents() {
        return  0;
    }

    @Override
    public com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation getParcel() {
        return tempReviewInformation$$0;
    }

    public static com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation read(android.os.Parcel parcel$$3, IdentityCollection identityMap$$1) {
        int identity$$1 = parcel$$3 .readInt();
        if (identityMap$$1 .containsKey(identity$$1)) {
            if (identityMap$$1 .isReserved(identity$$1)) {
                throw new ParcelerRuntimeException("An instance loop was detected whild building Parcelable and deseralization cannot continue.  This error is most likely due to using @ParcelConstructor or @ParcelFactory.");
            }
            return identityMap$$1 .get(identity$$1);
        } else {
            com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation tempReviewInformation$$4;
            int reservation$$0 = identityMap$$1 .reserve();
            tempReviewInformation$$4 = new com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation();
            identityMap$$1 .put(reservation$$0, tempReviewInformation$$4);
            InjectionUtil.setField(com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation.class, tempReviewInformation$$4, "id", parcel$$3 .readInt());
            InjectionUtil.setField(com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation.class, tempReviewInformation$$4, "status", parcel$$3 .readInt());
            com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation tempReviewInformation$$3 = tempReviewInformation$$4;
            identityMap$$1 .put(identity$$1, tempReviewInformation$$3);
            return tempReviewInformation$$3;
        }
    }

}
