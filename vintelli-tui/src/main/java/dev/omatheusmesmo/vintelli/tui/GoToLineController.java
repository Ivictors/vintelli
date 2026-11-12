package dev.omatheusmesmo.vintelli.tui;

import java.util.Optional;

/** State and input rules for the editor's Go to Line prompt. */
final class GoToLineController {

    record Position(int line, int column) {
    }

    private boolean open;
    private String input = "";
    private String error = "";

    void open() {
        open = true;
        input = "";
        error = "";
    }

    void close() { open = false; }
    boolean isOpen() { return open; }
    String input() { return input; }
    String error() { return error; }

    Optional<Position> press(OverlayKey key, int codePoint) {
        if (!open) return Optional.empty();
        switch (key) {
            case ESCAPE -> close();
            case BACKSPACE -> {
                if (!input.isEmpty()) input = input.substring(0, input.length() - 1);
                error = "";
            }
            case TEXT -> {
                if (Character.isDigit(codePoint) || codePoint == ':') {
                    input += new String(Character.toChars(codePoint));
                    error = "";
                }
            }
            case ENTER -> {
                Optional<Position> parsed = parse(input);
                if (parsed.isPresent()) {
                    close();
                    return parsed;
                }
                error = "Use line or line:column with positive numbers";
            }
            default -> { }
        }
        return Optional.empty();
    }

    static Optional<Position> parse(String value) {
        if (value == null || value.isBlank()) return Optional.empty();
        String[] parts = value.trim().split(":", -1);
        if (parts.length > 2) return Optional.empty();
        try {
            int line = Integer.parseInt(parts[0]);
            int column = parts.length == 2 ? Integer.parseInt(parts[1]) : 1;
            if (line < 1 || column < 1) return Optional.empty();
            return Optional.of(new Position(line - 1, column - 1));
        } catch (NumberFormatException invalid) {
            return Optional.empty();
        }
    }
}
