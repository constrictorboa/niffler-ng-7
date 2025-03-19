package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.impl.SpendRepositoryHibernate;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Optional;


@ParametersAreNonnullByDefault
public class SpendDbClient implements SpendClient {

    private static final Config CFG = Config.getInstance();
    private final SpendRepositoryHibernate spendRepositoryHibernate = new SpendRepositoryHibernate();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl()
    );


    @Nonnull
    @Override
    public SpendJson createSpend(SpendJson spend) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(() -> SpendJson.fromEntity(
                        spendRepositoryHibernate.create(SpendEntity.fromJson(spend))
                )
        ));
    }

    @Nonnull
    @Override
    public CategoryJson createCategory(CategoryJson category) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(() -> CategoryJson.fromEntity(
                        spendRepositoryHibernate.createCategory(CategoryEntity.fromJson(category))
                )
        ));
    }


    @Override
    public void deleteCategory(CategoryJson categoryJson) {
        xaTransactionTemplate.execute(() -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
                    spendRepositoryHibernate.removeCategory(categoryEntity);
                    return null;
                }
        );

    }

    @Override
    @Nullable
    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return xaTransactionTemplate.execute(() -> {
                    return spendRepositoryHibernate.findCategoryByUsernameAndCategoryName(username, categoryName)
                            .map(CategoryJson::fromEntity);
                }
        );
    }

}
