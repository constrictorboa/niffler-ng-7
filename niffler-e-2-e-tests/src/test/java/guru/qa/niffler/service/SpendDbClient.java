package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.impl.SpendRepositoryHibernate;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.Optional;

public class SpendDbClient implements SpendClient {

    private static final Config CFG = Config.getInstance();
    private final SpendRepositoryHibernate spendRepositoryHibernate = new SpendRepositoryHibernate();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl()
    );


    @Override
    public SpendJson createSpend(SpendJson spend) {
        return xaTransactionTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        spendEntity.setCategory(spendRepositoryHibernate.createCategory(spendEntity.getCategory()));
                    }
                    return SpendJson.fromEntity(
                            spendRepositoryHibernate.create(spendEntity));
                }
        );
    }

    @Override
    public CategoryJson createCategory(CategoryJson categoryJson) {
        return xaTransactionTemplate.execute(() -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
                    return CategoryJson.fromEntity(
                            spendRepositoryHibernate.createCategory(categoryEntity)
                    );
                }
        );
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
    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return xaTransactionTemplate.execute(() -> {
                    return spendRepositoryHibernate.findCategoryByUsernameAndCategoryName(username, categoryName)
                            .map(CategoryJson::fromEntity);
                }
        );
    }

}
