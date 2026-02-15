package br.pucrs.estudoorganizado.service.utils;

import java.time.format.DateTimeFormatter;

public class Utils {

    public static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String resolveTopicColor(Integer incidenceScore, Integer knowledgeScore) {

        String[] COLORS = {
                // Verde (1)
                "#93E410", "#71AD47", "#4F7A32",
                // Amarelo (2)
                "#FFDE75", "#FEC000", "#D2A000",
                // Laranja (3)
                "#F5B183", "#EE7D31", "#E36713",
                // Vermelho (4)
                "#FF6D6D", "#BF0001", "#920000",
                // Cinza (0)
                "#C2C2C2", "#9E9E9E", "#777777"
        };

        int incidence = incidenceScore != null ? incidenceScore : 0;
        int knowledge = knowledgeScore != null ? knowledgeScore : 0;

        incidence = Math.max(0, Math.min(4, incidence));
        knowledge = Math.max(0, Math.min(2, knowledge));

        int baseIndex = (incidence == 0 ? 4 : incidence - 1) * 3;

        return COLORS[baseIndex + knowledge];
    }

    public static String formatDurationMinutes(Long minutes) {
        if (minutes == null || minutes <= 0) {
            return "0 min";
        }

        long hours = minutes / 60;
        long remainingMinutes = minutes % 60;

        if (hours > 0) {
            return hours + "h " + remainingMinutes + " min";
        }
        return remainingMinutes + " min";
    }

}
