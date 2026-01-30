package br.pucrs.estudoorganizado;

import java.util.Random;

public class Utils {

    private static final String[] rgbs = {
            "#71AD47", "#4F7A32", "#93E410",
            "#FEC000", "#D2A000", "#FFDE75",
            "#EE7D31", "#E36713", "#F5B183",
            "#BF0001", "#920000", "#FF6D6D",
            "#9E9E9E", "#777777", "#C2C2C2"
    };
    public static String getRandomRGB() {
        Random random = new Random();
        int indice = random.nextInt(rgbs.length);
        return rgbs[indice];
    }

}
