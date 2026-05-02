package br.pucrs.estudoorganizado;

public class Utils {

//    private static final String[] rgbs = {
//            "#71AD47", "#4F7A32", "#93E410",
//            "#FEC000", "#D2A000", "#FFDE75",
//            "#EE7D31", "#E36713", "#F5B183",
//            "#BF0001", "#920000", "#FF6D6D",
//            "#9E9E9E", "#777777", "#C2C2C2"
//    };

    private static final String VERDE = "#71AD47";
    private static final String VERDE_ESCURO = "#4F7A32";
    private static final String AMARELO = "#FEC000";
    private static final String LARANJA = "#EE7D31";
    private static final String VERMELHO = "#BF0001";
    private static final String VERMELHO_ESCURO = "#920000";
    private static final String CINZA = "#9E9E9E";


    public static String getColorByScore(Integer score) {

        if (score == null) {
            return CINZA;
        }

        if (score >= 90) {
            return VERMELHO_ESCURO;
        }

        if (score >= 70) {
            return VERMELHO;
        }

        if (score >= 50) {
            return LARANJA;
        }

        if (score >= 30) {
            return AMARELO;
        }

        if (score >= 10) {
            return VERDE_ESCURO;
        }

        if (score >= 0) {
            return VERDE;
        }

        return CINZA;
    }

}
