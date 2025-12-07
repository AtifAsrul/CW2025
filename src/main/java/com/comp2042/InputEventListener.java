package com.comp2042;

/**
 * Interface for handling game input events.
 * Implemented by controllers that respond to user actions.
 */
public interface InputEventListener {

    /**
     * Called when a move down event occurs (key press or tick).
     *
     * @param event the move event.
     * @return the result of the down movement.
     */
    DownData onDownEvent(MoveEvent event);

    /**
     * Called when a move left event occurs.
     *
     * @param event the move event.
     * @return the updated view data.
     */
    ViewData onLeftEvent(MoveEvent event);

    /**
     * Called when a move right event occurs.
     *
     * @param event the move event.
     * @return the updated view data.
     */
    ViewData onRightEvent(MoveEvent event);

    /**
     * Called when a rotate event occurs.
     *
     * @param event the move event.
     * @return the updated view data.
     */
    ViewData onRotateEvent(MoveEvent event);

    /**
     * Called when a drop event occurs.
     *
     * @param event the move event.
     * @return the result of the drop movement.
     */
    DownData onDropEvent(MoveEvent event);

    /**
     * Called when a hold event occurs.
     *
     * @param event the move event.
     * @return the updated view data.
     */
    ViewData onHoldEvent(MoveEvent event);

    /**
     * Called to start a new game.
     */
    void createNewGame();

    /**
     * Called to stop the current game.
     */
    void stopGame();
}
