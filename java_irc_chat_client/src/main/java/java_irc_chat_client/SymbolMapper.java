package java_irc_chat_client;

public class SymbolMapper {

    public SymbolMapper() {
        // No hace nada en el constructor
    }

    /**
     * Devuelve el carácter tal cual.
     * Se puede extender más adelante si se desea mapping.
     */
    public String mapChar(char c) {
        return String.valueOf(c);
    }

    /**
     * Devuelve el String tal cual, usando mapChar para cada carácter.
     */
    public String mapString(String input) {
        if (input == null || input.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            int codePoint = input.codePointAt(i);
            sb.append(mapChar((char) codePoint));
            if (Character.isSupplementaryCodePoint(codePoint)) i++; // Avanza extra para Unicode
        }
        return sb.toString();
    }
}
