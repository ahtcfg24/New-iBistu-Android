package org.iflab.newbistu.models;

import android.os.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 从DreamFactory得到的json数据包含resource对象，存放了json数组,因此抽象出一个Resource对象，用于存放资源，
 * 可通过get方法获取资源，返回的是包含资源的list
 */
public class Resource<T> implements Serializable {

    protected List<T> resource = new ArrayList<>();

    public Resource() {
    }

    public List<T> getResource() {
        return resource;
    }

    public void setResource(List<T> resource) {
        this.resource = resource;
    }

    public void addResource(T value) {
        resource.add(value);
    }


    public static class Parcelable<T extends BaseRecord> extends Resource<T> implements android.os.Parcelable {

        public Parcelable() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(resource.size());

            if (resource.size() > 0) {
                dest.writeValue(resource.get(0).getClass());
            }

            for (T record : resource) {
                dest.writeParcelable((android.os.Parcelable) record, flags);
            }
        }

        public static final Creator<Parcelable> CREATOR = new Creator<Parcelable>() {
            public Parcelable createFromParcel(Parcel in) {
                return new Parcelable(in);
            }

            @Override
            public Parcelable[] newArray(int size) {
                return new Parcelable[size];
            }
        };

        private Parcelable(Parcel in) {
            int size = in.readInt();

            resource = new ArrayList<>();

            if (size > 0) {
                Class c = (Class) in.readValue(null);

                for (int i = 0; i < size; i++) {
                    T value = in.readParcelable(c.getClassLoader());

                    resource.add(value);
                }
            }
        }
    }
}
