package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.SpendRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.SpendRepositoryJdbc;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.Optional;

public class SpendDbClient implements SpendClient {

    private static final Config CFG = Config.getInstance();
    private final SpendRepositoryHibernate spendRepositoryHibernate = new SpendRepositoryHibernate();
    private final SpendRepository spendRepository = new SpendRepositoryJdbc();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl()
    );


    @Override
    public SpendJson createSpend(SpendJson spend) {
        return xaTransactionTemplate.execute(() -> SpendJson.fromEntity(
                        spendRepository.create(SpendEntity.fromJson(spend))
                )
        );
    }

    @Override
    public CategoryJson createCategory(CategoryJson category) {
        return xaTransactionTemplate.execute(() -> CategoryJson.fromEntity(
                        spendRepository.createCategory(CategoryEntity.fromJson(category))
                )
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
