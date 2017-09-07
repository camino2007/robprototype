package fup.prototype.robprototype.view.bindings;

import android.support.v4.app.Fragment;

import javax.inject.Inject;

/**
 * Created by rxdroid on 9/7/17.
 */

public class FragmentBindingAdapters {

    final Fragment fragment;

    @Inject
    public FragmentBindingAdapters(Fragment fragment) {
        this.fragment = fragment;
    }

/*    @BindingAdapter("imageUrl")
    public void bindImage(ImageView imageView, String url) {
        Glide.with(fragment).load(url).into(imageView);
    }*/
}
