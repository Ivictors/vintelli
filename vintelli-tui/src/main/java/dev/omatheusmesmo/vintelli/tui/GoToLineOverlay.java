package dev.omatheusmesmo.vintelli.tui;

import dev.tamboui.layout.Rect;
import dev.tamboui.style.Style;
import dev.tamboui.terminal.Frame;
import dev.tamboui.text.Line;
import dev.tamboui.text.Span;
import dev.tamboui.text.Text;
import dev.tamboui.tui.event.KeyEvent;
import dev.tamboui.widgets.Clear;
import dev.tamboui.widgets.block.Block;
import dev.tamboui.widgets.block.BorderType;
import dev.tamboui.widgets.block.Borders;
import dev.tamboui.widgets.paragraph.Paragraph;

import java.util.Optional;

/** Immediate-mode renderer and keyboard handler for Go to Line. */
final class GoToLineOverlay {
    private final GoToLineController controller = new GoToLineController();

    void open() { controller.open(); }
    boolean isOpen() { return controller.isOpen(); }
    void close() { controller.close(); }
    Optional<GoToLineController.Position> handleKey(KeyEvent key) {
        return controller.press(OverlayInput.key(key), key.codePoint());
    }

    void render(Frame frame, Rect area) {
        if (!controller.isOpen() || area.width() < 10 || area.height() < 5) return;
        int width = Math.min(44, Math.max(24, area.width() - 4));
        int height = 5;
        Rect panel = new Rect(area.x() + (area.width() - width) / 2,
                area.y() + (area.height() - height) / 2, width, height);
        frame.renderWidget(Clear.INSTANCE, panel);
        frame.renderWidget(Block.builder().title("Go to Line").borders(Borders.ALL)
                .borderType(BorderType.ROUNDED).build(), panel);
        Rect inner = new Rect(panel.x() + 1, panel.y() + 1, panel.width() - 2, panel.height() - 2);
        String hint = controller.error().isBlank() ? "Enter: go  Escape: cancel" : controller.error();
        frame.renderWidget(Paragraph.builder().text(Text.from(
                Line.from(Span.styled("> " + controller.input(), Style.create().bold())),
                Line.from(Span.styled(hint, controller.error().isBlank() ? Style.create().dim() : Style.create()))))
                .build(), inner);
    }
}
