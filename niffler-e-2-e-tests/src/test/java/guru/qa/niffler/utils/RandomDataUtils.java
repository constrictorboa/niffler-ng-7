package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

import javax.annotation.Nonnull;

public class RandomDataUtils {
    private static final Faker faker = new Faker();

    @Nonnull
    public static String randomUsername() {
        return faker.name().username() + faker.number().numberBetween(0, 9999999);
    }

    @Nonnull
    public static String randomPassword() {
        return faker.crypto().md5();
    }

    @Nonnull
    public static String randomName() {
        return faker.name().firstName();
    }

    @Nonnull
    public static String randomSurname() {
        return faker.name().lastName();
    }

    @Nonnull
    public static String randomCategoryName() {
        return faker.cat().breed().replaceAll("\\s", "") + faker.number().randomNumber();
    }

    @Nonnull
    public static String randomSentence(int wordsCount) {
        return faker.lorem().sentence(wordsCount);
    }
}
