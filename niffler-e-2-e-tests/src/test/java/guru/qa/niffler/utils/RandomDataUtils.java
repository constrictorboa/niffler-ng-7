package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

public class RandomDataUtils {
    private static final Faker faker = new Faker();

    public static String randomUsername() {
        return faker.name().username() + faker.number().numberBetween(0, 9999999);
    }

    public static String randomPassword() {
        return faker.crypto().md5();
    }

    public static String randomName() {
        return faker.name().firstName();
    }

    public static String randomSurname() {
        return faker.name().lastName();
    }

    public static String randomCategoryName() {
        return faker.cat().breed().replaceAll("\\s", "") + faker.number().randomNumber();
    }

    public static String randomSentence(int wordsCount) {
        return faker.lorem().sentence(wordsCount);
    }
}
