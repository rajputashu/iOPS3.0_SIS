
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
public class ReviewInformationResponse$$Parcelable
    implements Parcelable, ParcelWrapper<com.sisindia.ai.siscommons.models.ReviewInformationResponse>
{

    private com.sisindia.ai.siscommons.models.ReviewInformationResponse reviewInformationResponse$$0;
    @SuppressWarnings("UnusedDeclaration")
    public final static Creator<ReviewInformationResponse$$Parcelable>CREATOR = new Creator<ReviewInformationResponse$$Parcelable>() {


        @Override
        public ReviewInformationResponse$$Parcelable createFromParcel(android.os.Parcel parcel$$2) {
            return new ReviewInformationResponse$$Parcelable(read(parcel$$2, new IdentityCollection()));
        }

        @Override
        public ReviewInformationResponse$$Parcelable[] newArray(int size) {
            return new ReviewInformationResponse$$Parcelable[size] ;
        }

    }
    ;

    public ReviewInformationResponse$$Parcelable(com.sisindia.ai.siscommons.models.ReviewInformationResponse reviewInformationResponse$$2) {
        reviewInformationResponse$$0 = reviewInformationResponse$$2;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel$$0, int flags) {
        write(reviewInformationResponse$$0, parcel$$0, flags, new IdentityCollection());
    }

    public static void write(com.sisindia.ai.siscommons.models.ReviewInformationResponse reviewInformationResponse$$1, android.os.Parcel parcel$$1, int flags$$0, IdentityCollection identityMap$$0) {
        int identity$$0 = identityMap$$0 .getKey(reviewInformationResponse$$1);
        if (identity$$0 != -1) {
            parcel$$1 .writeInt(identity$$0);
        } else {
            parcel$$1 .writeInt(identityMap$$0 .put(reviewInformationResponse$$1));
            if (InjectionUtil.getField(new InjectionUtil.GenericType<java.util.ArrayList<com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation>>(), com.sisindia.ai.siscommons.models.ReviewInformationResponse.class, reviewInformationResponse$$1, "poas") == null) {
                parcel$$1 .writeInt(-1);
            } else {
                parcel$$1 .writeInt(InjectionUtil.getField(new InjectionUtil.GenericType<java.util.ArrayList<com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation>>(), com.sisindia.ai.siscommons.models.ReviewInformationResponse.class, reviewInformationResponse$$1, "poas").size());
                for (com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation tempReviewInformation$$0 : InjectionUtil.getField(new InjectionUtil.GenericType<java.util.ArrayList<com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation>>(), com.sisindia.ai.siscommons.models.ReviewInformationResponse.class, reviewInformationResponse$$1, "poas")) {
                    com.sisindia.ai.siscommons.models.ReviewInformationResponse$TempReviewInformation$$Parcelable.write(tempReviewInformation$$0, parcel$$1, flags$$0, identityMap$$0);
                }
            }
            if (InjectionUtil.getField(new InjectionUtil.GenericType<java.util.ArrayList<com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation>>(), com.sisindia.ai.siscommons.models.ReviewInformationResponse.class, reviewInformationResponse$$1, "complaints") == null) {
                parcel$$1 .writeInt(-1);
            } else {
                parcel$$1 .writeInt(InjectionUtil.getField(new InjectionUtil.GenericType<java.util.ArrayList<com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation>>(), com.sisindia.ai.siscommons.models.ReviewInformationResponse.class, reviewInformationResponse$$1, "complaints").size());
                for (com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation tempReviewInformation$$1 : InjectionUtil.getField(new InjectionUtil.GenericType<java.util.ArrayList<com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation>>(), com.sisindia.ai.siscommons.models.ReviewInformationResponse.class, reviewInformationResponse$$1, "complaints")) {
                    com.sisindia.ai.siscommons.models.ReviewInformationResponse$TempReviewInformation$$Parcelable.write(tempReviewInformation$$1, parcel$$1, flags$$0, identityMap$$0);
                }
            }
            if (InjectionUtil.getField(new InjectionUtil.GenericType<java.util.ArrayList<com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation>>(), com.sisindia.ai.siscommons.models.ReviewInformationResponse.class, reviewInformationResponse$$1, "grievances") == null) {
                parcel$$1 .writeInt(-1);
            } else {
                parcel$$1 .writeInt(InjectionUtil.getField(new InjectionUtil.GenericType<java.util.ArrayList<com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation>>(), com.sisindia.ai.siscommons.models.ReviewInformationResponse.class, reviewInformationResponse$$1, "grievances").size());
                for (com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation tempReviewInformation$$2 : InjectionUtil.getField(new InjectionUtil.GenericType<java.util.ArrayList<com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation>>(), com.sisindia.ai.siscommons.models.ReviewInformationResponse.class, reviewInformationResponse$$1, "grievances")) {
                    com.sisindia.ai.siscommons.models.ReviewInformationResponse$TempReviewInformation$$Parcelable.write(tempReviewInformation$$2, parcel$$1, flags$$0, identityMap$$0);
                }
            }
        }
    }

    @Override
    public int describeContents() {
        return  0;
    }

    @Override
    public com.sisindia.ai.siscommons.models.ReviewInformationResponse getParcel() {
        return reviewInformationResponse$$0;
    }

    public static com.sisindia.ai.siscommons.models.ReviewInformationResponse read(android.os.Parcel parcel$$3, IdentityCollection identityMap$$1) {
        int identity$$1 = parcel$$3 .readInt();
        if (identityMap$$1 .containsKey(identity$$1)) {
            if (identityMap$$1 .isReserved(identity$$1)) {
                throw new ParcelerRuntimeException("An instance loop was detected whild building Parcelable and deseralization cannot continue.  This error is most likely due to using @ParcelConstructor or @ParcelFactory.");
            }
            return identityMap$$1 .get(identity$$1);
        } else {
            com.sisindia.ai.siscommons.models.ReviewInformationResponse reviewInformationResponse$$4;
            int reservation$$0 = identityMap$$1 .reserve();
            reviewInformationResponse$$4 = new com.sisindia.ai.siscommons.models.ReviewInformationResponse();
            identityMap$$1 .put(reservation$$0, reviewInformationResponse$$4);
            int int$$0 = parcel$$3 .readInt();
            java.util.ArrayList<com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation> list$$0;
            if (int$$0 < 0) {
                list$$0 = null;
            } else {
                list$$0 = new java.util.ArrayList<com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation>(int$$0);
                for (int int$$1 = 0; (int$$1 <int$$0); int$$1 ++) {
                    com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation tempReviewInformation$$3 = com.sisindia.ai.siscommons.models.ReviewInformationResponse$TempReviewInformation$$Parcelable.read(parcel$$3, identityMap$$1);
                    list$$0 .add(tempReviewInformation$$3);
                }
            }
            InjectionUtil.setField(com.sisindia.ai.siscommons.models.ReviewInformationResponse.class, reviewInformationResponse$$4, "poas", list$$0);
            int int$$2 = parcel$$3 .readInt();
            java.util.ArrayList<com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation> list$$1;
            if (int$$2 < 0) {
                list$$1 = null;
            } else {
                list$$1 = new java.util.ArrayList<com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation>(int$$2);
                for (int int$$3 = 0; (int$$3 <int$$2); int$$3 ++) {
                    com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation tempReviewInformation$$4 = com.sisindia.ai.siscommons.models.ReviewInformationResponse$TempReviewInformation$$Parcelable.read(parcel$$3, identityMap$$1);
                    list$$1 .add(tempReviewInformation$$4);
                }
            }
            InjectionUtil.setField(com.sisindia.ai.siscommons.models.ReviewInformationResponse.class, reviewInformationResponse$$4, "complaints", list$$1);
            int int$$4 = parcel$$3 .readInt();
            java.util.ArrayList<com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation> list$$2;
            if (int$$4 < 0) {
                list$$2 = null;
            } else {
                list$$2 = new java.util.ArrayList<com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation>(int$$4);
                for (int int$$5 = 0; (int$$5 <int$$4); int$$5 ++) {
                    com.sisindia.ai.siscommons.models.ReviewInformationResponse.TempReviewInformation tempReviewInformation$$5 = com.sisindia.ai.siscommons.models.ReviewInformationResponse$TempReviewInformation$$Parcelable.read(parcel$$3, identityMap$$1);
                    list$$2 .add(tempReviewInformation$$5);
                }
            }
            InjectionUtil.setField(com.sisindia.ai.siscommons.models.ReviewInformationResponse.class, reviewInformationResponse$$4, "grievances", list$$2);
            com.sisindia.ai.siscommons.models.ReviewInformationResponse reviewInformationResponse$$3 = reviewInformationResponse$$4;
            identityMap$$1 .put(identity$$1, reviewInformationResponse$$3);
            return reviewInformationResponse$$3;
        }
    }

}
