package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.mapper.SpendEntityRowMapper;
import guru.qa.niffler.data.repository.SpendRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class SpendRepositoryJdbc implements SpendRepository {
    private static final Config CFG = Config.getInstance();
    private final SpendDao spendDaoJdbc = new SpendDaoJdbc();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    @Override
    public SpendEntity create(SpendEntity spend) {
        CategoryEntity categoryEntity = categoryDao.create(spend.getCategory());
        spend.setCategory(categoryEntity);
        return spendDaoJdbc.create(spend);
    }

    @Override
    public SpendEntity update(SpendEntity spend) {
        categoryDao.update(spend.getCategory());
        return spendDaoJdbc.update(spend);
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        return categoryDao.create(category);
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return categoryDao.findCategoryById(id);
    }

    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String name) {
        return categoryDao.findCategoryByUsernameAndCategoryName(username, name);
    }

    @Override
    public Optional<SpendEntity> findById(UUID id) {
        return spendDaoJdbc.findSpendById(id);
    }

    @Override
    public Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "select * from category c join spend s on c.id = s.category_id where s.description = ? " +
                        "and s.username=?"
        )) {
            ps.setObject(1, description);
            ps.setObject(2, username);

            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                SpendEntity spendEntity = null;
                while (rs.next()) {
                    spendEntity = SpendEntityRowMapper.instance.mapRow(rs, 1);
                }

                if (spendEntity == null) {
                    return Optional.empty();
                } else {
                    return Optional.of(spendEntity);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(SpendEntity spend) {
        spendDaoJdbc.deleteSpend(spend);
        categoryDao.deleteCategory(spend.getCategory());
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        spendDaoJdbc.deleteSpendByCategoryId(category.getId());
        categoryDao.deleteCategory(category);
    }
}
