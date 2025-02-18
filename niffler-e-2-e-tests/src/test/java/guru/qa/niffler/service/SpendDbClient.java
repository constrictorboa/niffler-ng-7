package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.Optional;

import static guru.qa.niffler.data.Databases.dataSource;
import static guru.qa.niffler.data.Databases.transaction;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();

    public SpendJson createSpend(SpendJson spend) {
        return transaction(connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = new CategoryDaoJdbc(connection)
                                .create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            new SpendDaoJdbc(connection).create(spendEntity)
                    );
                },
                CFG.spendJdbcUrl(), 1
        );
    }

    public CategoryJson createCategory(CategoryJson categoryJson) {
        return transaction(connection -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
                    return CategoryJson.fromEntity(
                            new CategoryDaoJdbc(connection).create(categoryEntity)
                    );
                },
                CFG.spendJdbcUrl(), 1
        );
    }

    public void deleteCategory(CategoryJson categoryJson) {
        transaction(connection -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
                    new CategoryDaoJdbc(connection).deleteCategory(categoryEntity);
                },
                CFG.spendJdbcUrl(), 1
        );

    }

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return transaction(connection -> {
                    return new CategoryDaoJdbc(connection).findCategoryByUsernameAndCategoryName(username, categoryName)
                            .map(CategoryJson::fromEntity);
                },
                CFG.spendJdbcUrl(), 1
        );
    }

    public SpendJson createSpendSpringJdbc(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);
        if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = new CategoryDaoSpringJdbc(dataSource(CFG.spendJdbcUrl()))
                    .create(spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
        }
        return SpendJson.fromEntity(new SpendDaoSpringJdbc(dataSource(CFG.spendJdbcUrl()))
                .create(spendEntity));
    }

    public CategoryJson createCategorySpringJdbc(CategoryJson categoryJson) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
        return CategoryJson.fromEntity(
                new CategoryDaoSpringJdbc(dataSource(CFG.spendJdbcUrl())).create(categoryEntity)
        );
    }

    public void deleteCategorySpringJdbc(CategoryJson categoryJson) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
        new CategoryDaoSpringJdbc(dataSource(CFG.spendJdbcUrl())).deleteCategory(categoryEntity);

    }

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryNameSpringJdbc(String username, String categoryName) {
        return new CategoryDaoSpringJdbc(dataSource(CFG.spendJdbcUrl()))
                .findCategoryByUsernameAndCategoryName(username, categoryName)
                .map(CategoryJson::fromEntity);
    }
}
