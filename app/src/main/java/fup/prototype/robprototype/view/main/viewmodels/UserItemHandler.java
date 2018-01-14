package fup.prototype.robprototype.view.main.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import com.rxdroid.repository.model.User;
import fup.prototype.robprototype.view.details.DetailsActivity;
public class UserItemHandler {

    public void onClick(@NonNull final UserItemViewModel viewModel, @NonNull final ConstraintLayout constraintLayout) {
        final Context context = constraintLayout.getContext();
        final User user = viewModel.getUser();
        final Intent intent = DetailsActivity.createIntent(context, user);
        context.startActivity(intent);
    }
}
