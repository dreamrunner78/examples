package com.bas.tools;

public class ToolsConvert {

    public static Double convertStringToDouble(String input) {
        Double value = -1.0;
        
        if (input != null && !"".equals(input)) {
            try {
                value = Double.valueOf(input);
            } catch (NumberFormatException e) {
                value = -1.0;
            }
        }

        return value;
    }
}
