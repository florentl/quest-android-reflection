package fr.wildcodeschool.gson;

import java.util.Locale;

@SuppressWarnings("unused")
class Database {
  // TAG
  private static final String TAG = Database.class.getName();
  // Static data
  private static final String ABOUT_DATA = "UNE TABLE GASTRONOMIQUE CHALEUREUSE";
  private static final Boolean OPEN_DATA = true;
  private static final Integer START_DATA = 19;
  private static final Double TEMP_DATA = 22.5;

  // Private parameters to populate
  private String about;
  private boolean open;
  private int start;
  private double temp;
  private Menus[] menus;

  /**
   * Check Database content.
   * @return boolean: true if data checked are correct otherwise false.
   */
  boolean checkData() {
    return
      ABOUT_DATA.equals(about) & OPEN_DATA.equals(open) &
      START_DATA.equals(start) & TEMP_DATA.equals(temp) & checkMenus();
  }

  /**
   * Check Database Menus[] content
   * @return boolean: true if data checked are correct otherwise false
   */
  private boolean checkMenus() {
    if (null == menus) return false;

    boolean status = true;
    for (int i = 0; i < menus.length; i++)
      status &= null != menus[i] && menus[i].checkData(i);
    return status;
  }
}

@SuppressWarnings("unused")
class Menus {
  // TAG
  private static final String TAG = Menus.class.getName();
  // Static data
  private static final String NAME_DATA = "MENU%d";
  private static final String PRICE_DATA = "%d€";

  // Private parameters to populate
  private String name;
  private String price;
  private Menu menu;

  /**
   * Check Menus instance content.
   * @return boolean: true if data checked are correct otherwise false.
   */
  boolean checkData(int index) {
    return
      String.format(Locale.getDefault(), NAME_DATA, index).equals(name) &
        String.format(Locale.getDefault(), PRICE_DATA, index).equals(price) &
        checkMenu(index);
  }

  /**
   * Check Menu instance content.
   * @param index int: The position of Menus object in the array.
   * @return boolean: true if data checked are correct otherwise false.
   */
  private boolean checkMenu(int index) {
    return (null != menu) && menu.checkData(index);
  }
}

@SuppressWarnings("unused")
class Menu {
  // TAG
  private static final String TAG = Menu.class.getName();
  // Static data
  private static final String STARTER_DATA = "ENTREE %d%d";
  private static final String DISH_DATA    = "PLAT %d%d";
  private static final String DESSERT_DATA = "DESSERT %d%d";

  // Private parameters to populate
  private String[] starter;
  private String[] dish;
  private String[] dessert;

  /**
   * Check Menu instance content.
   * @return boolean: true if data checked are correct otherwise false.
   */
  boolean checkData(int index) {
    return
      checkArray(index, STARTER_DATA, starter) &
        checkArray(index, DISH_DATA, dish)  &
        checkArray(index, DESSERT_DATA, dessert);
  }

  /**
   * Check array content.
   * @param index int: The position of Menus object in the array.
   * @param format String: The string to format to check content.
   * @param array String[]: The array used to compare with format parameter.
   * @return boolean: true if data checked are correct otherwise false.
   */
  private boolean checkArray(int index, String format, String[] array) {
    if (null == array) return false;

    boolean status = true;
    for (int i = 0; i < array.length; i++)
      status &= String.format(Locale.getDefault(), format, index, i+1).equals(array[i]);
    return status;
  }
}