package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Objects;
import java.util.Random;

public class CreateCategoryExtension implements BeforeEachCallback, ParameterResolver, AfterEachCallback {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateCategoryExtension.class);

    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
                .ifPresent(anno -> {
                    CategoryJson newCategory =
                            new CategoryJson(
                                    null,
                                    "category " + System.currentTimeMillis() + new Random().nextInt(1, 100),
                                    anno.username(),
                                    false
                            );
                    CategoryJson createdJson = spendApiClient.createCategory(newCategory);
                    if (anno.archived()) {
                        createdJson = new CategoryJson(
                                createdJson.id(),
                                createdJson.name(),
                                createdJson.username(),
                                true
                        );
                        createdJson = spendApiClient.updateCategory(createdJson);
                    }
                    context.getStore(NAMESPACE).put(
                            context.getUniqueId(),
                            createdJson
                    );
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(CreateCategoryExtension.NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        CategoryJson category =
                (CategoryJson) context.getStore(NAMESPACE).get(context.getUniqueId());
        if (Objects.nonNull(category)) {
            spendApiClient.updateCategory(new CategoryJson(
                    category.id(),
                    category.name(),
                    category.username(),
                    true
            ));
        }
    }
}
