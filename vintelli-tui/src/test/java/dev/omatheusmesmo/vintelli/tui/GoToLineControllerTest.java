package dev.omatheusmesmo.vintelli.tui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GoToLineControllerTest {

    @Test
    void parsesOneBasedLineAndColumnAsZeroBasedPosition() {
        assertEquals(new GoToLineController.Position(4, 2),
                GoToLineController.parse("5:3").orElseThrow());
        assertEquals(new GoToLineController.Position(4, 0),
                GoToLineController.parse("5").orElseThrow());
    }

    @Test
    void rejectsInvalidPositions() {
        assertTrue(GoToLineController.parse("").isEmpty());
        assertTrue(GoToLineController.parse("0").isEmpty());
        assertTrue(GoToLineController.parse("4:0").isEmpty());
        assertTrue(GoToLineController.parse("4:2:1").isEmpty());
        assertTrue(GoToLineController.parse("abc").isEmpty());
    }

    @Test
    void escapeCancelsAndEnterConfirmsOnlyValidInput() {
        GoToLineController controller = new GoToLineController();
        controller.open();
        assertTrue(controller.press(OverlayKey.TEXT, '0').isEmpty());
        assertTrue(controller.press(OverlayKey.ENTER, 0).isEmpty());
        assertTrue(controller.isOpen());

        controller.press(OverlayKey.BACKSPACE, 0);
        controller.press(OverlayKey.TEXT, '3');
        assertEquals(new GoToLineController.Position(2, 0),
                controller.press(OverlayKey.ENTER, 0).orElseThrow());
        assertFalse(controller.isOpen());
    }
}
