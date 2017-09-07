package fup.prototype.domain.di;

import dagger.Subcomponent;
import fup.prototype.domain.github.provider.GitHubRepoProvider;

@Subcomponent(modules = DomainModule.class)
public interface DomainComponent {

    GitHubRepoProvider gitHubRepoProvider();

    @Subcomponent.Builder
    interface Builder {
        Builder requestModule(DomainModule module);

        DomainComponent build();
    }
}
