package fup.prototype.data.di;

import dagger.Subcomponent;

@Subcomponent(modules = DataModule.class)
public interface DataComponent {

    @Subcomponent.Builder
    interface Builder {

        DataComponent build();
    }
}
