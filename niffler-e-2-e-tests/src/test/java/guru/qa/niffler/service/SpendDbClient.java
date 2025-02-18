package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.Optional;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();
    private final SpendDao spendDao = new SpendDaoJdbc();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );


    public SpendJson createSpend(SpendJson spend) {
        return jdbcTxTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = categoryDao
                                .create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            spendDao.create(spendEntity)
                    );
                }
        );
    }

    public CategoryJson createCategory(CategoryJson categoryJson) {
        return jdbcTxTemplate.execute(() -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
                    return CategoryJson.fromEntity(
                            categoryDao.create(categoryEntity)
                    );
                }
        );
    }

    public void deleteCategory(CategoryJson categoryJson) {
        jdbcTxTemplate.execute(() -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
                    categoryDao.deleteCategory(categoryEntity);
                    return null;
                }
        );

    }

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return jdbcTxTemplate.execute(() -> {
                    return categoryDao.findCategoryByUsernameAndCategoryName(username, categoryName)
                            .map(CategoryJson::fromEntity);
                }
        );
    }

//    public SpendJson createSpendSpringJdbc(SpendJson spend) {
//        SpendEntity spendEntity = SpendEntity.fromJson(spend);
//        if (spendEntity.getCategory().getId() == null) {
//            CategoryEntity categoryEntity = new CategoryDaoSpringJdbc(dataSource(CFG.spendJdbcUrl()))
//                    .create(spendEntity.getCategory());
//            spendEntity.setCategory(categoryEntity);
//        }
//        return SpendJson.fromEntity(new SpendDaoSpringJdbc(dataSource(CFG.spendJdbcUrl()))
//                .create(spendEntity));
//    }
//
//    public CategoryJson createCategorySpringJdbc(CategoryJson categoryJson) {
//        CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
//        return CategoryJson.fromEntity(
//                new CategoryDaoSpringJdbc(dataSource(CFG.spendJdbcUrl())).create(categoryEntity)
//        );
//    }
//
//    public void deleteCategorySpringJdbc(CategoryJson categoryJson) {
//        CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
//        new CategoryDaoSpringJdbc(dataSource(CFG.spendJdbcUrl())).deleteCategory(categoryEntity);
//
//    }
//
//    public Optional<CategoryJson> findCategoryByUsernameAndCategoryNameSpringJdbc(String username, String categoryName) {
//        return new CategoryDaoSpringJdbc(dataSource(CFG.spendJdbcUrl()))
//                .findCategoryByUsernameAndCategoryName(username, categoryName)
//                .map(CategoryJson::fromEntity);
//    }
}
