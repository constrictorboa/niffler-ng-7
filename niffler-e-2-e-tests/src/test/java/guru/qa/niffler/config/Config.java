package guru.qa.niffler.config;

public interface Config {

  static Config getInstance() {
    return LocalConfig.INSTANCE;
  }

  String frontUrl();

  String spendUrl();
  
  String spendJdbcUrl();

  String ghUrl();
  
  String gatewayUrl();
  
  String userdataUrl();
  
  String userdataJdbcUrl();
  
  String authUrl();
  
  String authJdbcUrl();
  
  String currencyJdbcUrl();

}
